pipeline {
  agent any

  options {
    buildDiscarder(logRotator(numToKeepStr:'15'))
  }

  tools { 
    maven 'apache-maven-latest'
    jdk 'oracle-jdk8-latest'
  }
  
  // https://jenkins.io/doc/book/pipeline/syntax/#triggers
  triggers {
    pollSCM('H/5 * * * *')
  }
  
  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Gradle Build') {
      steps {
        sh '''
          ./gradlew clean cleanGenerateXtext build createLocalMavenRepo \
            -PuseJenkinsSnapshots=true \
            -PJENKINS_URL=$JENKINS_URL \
            -PcompileXtend=true \
            -PignoreTestFailures=true \
            --refresh-dependencies \
            --continue
        '''
        step([$class: 'JUnitResultArchiver', testResults: '**/build/test-results/test/*.xml'])
      }
    }
    
    stage('Gradle Longrunning Tests') {
      steps {
        sh '''
          ./gradlew longrunningTest \
            -PuseJenkinsSnapshots=true \
            -PJENKINS_URL=$JENKINS_URL \
            -PignoreTestFailures=true \
            --continue
        '''
        step([$class: 'JUnitResultArchiver', testResults: '**/build/test-results/longrunningTest/*.xml'])
      }
    }
    
    stage('Maven Build') {
      steps {
        dir('.m2/repository/org/eclipse/xtext') { deleteDir() }
        configFileProvider(
          [configFile(fileId: '7a78c736-d3f8-45e0-8e69-bf07c27b97ff', variable: 'MAVEN_SETTINGS')]) {
          sh '''
            mvn \
              -s $MAVEN_SETTINGS \
              -f releng \
              --batch-mode \
              --update-snapshots \
              -Dmaven.repo.local=.m2/repository \
              -DJENKINS_URL=$JENKINS_URL \
              -DupstreamBranch=kth_issue1309_cbi \
              -Dorg.slf4j.simpleLogger.log.org.apache.maven.cli.transfer.Slf4jMavenTransferListener=warn \
              clean install
          '''
        }
      }
    }
  }

  post {
    success {
      archiveArtifacts artifacts: 'build/**'
    }
  }
}