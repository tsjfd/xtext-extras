package org.eclipse.xtext.java.tests;

import com.google.common.base.Stopwatch;
import com.google.inject.Inject;
import com.google.inject.Provider;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.common.types.access.IJvmTypeProvider;
import org.eclipse.xtext.java.tests.JavaInjectorProvider;
import org.eclipse.xtext.resource.IResourceDescription;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.resource.impl.ChunkedResourceDescriptions;
import org.eclipse.xtext.resource.impl.ResourceDescriptionsData;
import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.XtextRunner;
import org.eclipse.xtext.testing.util.InMemoryURIHandler;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.IntegerRange;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.Pair;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(XtextRunner.class)
@InjectWith(JavaInjectorProvider.class)
@SuppressWarnings("all")
public class JavaSourceLanguagePerformanceTest {
  @Inject
  private Provider<XtextResourceSet> resourceSetProvider;
  
  @Inject
  private IResourceDescription.Manager resourceDesriptionManager;
  
  @Inject
  private IJvmTypeProvider.Factory typeProviderFactory;
  
  public void doTestPerformance(final int max) {
    final Function1<Integer, Pair<String, String>> _function = (Integer it) -> {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("package demo; public class Demo");
      _builder.append(it);
      _builder.append(" {");
      _builder.newLineIfNotEmpty();
      _builder.append("\t\t\t");
      _builder.append("private String x;");
      _builder.newLine();
      {
        if (((it).intValue() > 1)) {
          _builder.append("\t\t\t");
          _builder.append("private Demo");
          _builder.append(((it).intValue() - 1), "\t\t\t");
          _builder.append(" demo;");
          _builder.newLineIfNotEmpty();
        }
      }
      {
        if (((it).intValue() > 2)) {
          _builder.append("\t\t\t");
          _builder.append("private Demo");
          _builder.append(((it).intValue() - 2), "\t\t\t");
          _builder.append(" demo;");
          _builder.newLineIfNotEmpty();
        }
      }
      {
        if (((it).intValue() < (max - 1))) {
          _builder.append("\t\t\t");
          _builder.append("private Demo");
          _builder.append(((it).intValue() + 1), "\t\t\t");
          _builder.append(" demo;");
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.append("\t\t");
      _builder.append("}");
      return Pair.<String, String>of((("demo/Demo" + it) + ".java"), _builder.toString());
    };
    final XtextResourceSet rs = this.resourceSet(((Pair<String, String>[])Conversions.unwrapArray(IterableExtensions.<Pair<String, String>>toList(IterableExtensions.<Integer, Pair<String, String>>map(new IntegerRange(1, max), _function)), Pair.class)));
    final Stopwatch sw = Stopwatch.createStarted();
    EList<Resource> _resources = rs.getResources();
    for (final Resource r : _resources) {
      r.getContents();
    }
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(max);
    _builder.append(" file took ");
    long _elapsed = sw.elapsed(TimeUnit.MILLISECONDS);
    _builder.append(_elapsed);
    _builder.append(" ms");
    InputOutput.<String>println(_builder.toString());
  }
  
  @Test(timeout = 30000)
  public void testPerf00100() {
    this.doTestPerformance(100);
  }
  
  @Test(timeout = 30000)
  public void testPerf00200() {
    this.doTestPerformance(200);
  }
  
  @Test(timeout = 30000)
  public void testPerf00400() {
    this.doTestPerformance(400);
  }
  
  @Test(timeout = 30000)
  public void testPerf00800() {
    this.doTestPerformance(800);
  }
  
  @Test(timeout = 30000)
  public void testPerf01600() {
    this.doTestPerformance(1600);
  }
  
  @Test(timeout = 30000)
  public void testPerf03200() {
    this.doTestPerformance(3200);
  }
  
  @Test(timeout = 30000)
  public void testPerf06400() {
    this.doTestPerformance(6400);
  }
  
  @Test(timeout = 30000)
  public void testPerf12800() {
    this.doTestPerformance(12800);
  }
  
  @Test(timeout = 30000)
  public void testPerf25600() {
    this.doTestPerformance(25600);
  }
  
  @Test(timeout = 30000)
  public void testPerf51200() {
    this.doTestPerformance(51200);
  }
  
  protected XtextResourceSet resourceSet(final Pair<String, String>... files) {
    final XtextResourceSet result = this.resourceSetProvider.get();
    this.typeProviderFactory.createTypeProvider(result);
    result.setClasspathURIContext(this.getClass().getClassLoader());
    result.getURIConverter().getURIHandlers().clear();
    final InMemoryURIHandler uriHandler = new InMemoryURIHandler();
    final Function1<Pair<String, String>, URI> _function = (Pair<String, String> it) -> {
      try {
        String _key = it.getKey();
        String _plus = ((InMemoryURIHandler.SCHEME + ":/") + _key);
        final URI uri = URI.createURI(_plus);
        final OutputStream out = uriHandler.createOutputStream(uri, CollectionLiterals.<Object, Object>emptyMap());
        out.write(it.getValue().getBytes());
        out.close();
        return uri;
      } catch (Throwable _e) {
        throw Exceptions.sneakyThrow(_e);
      }
    };
    final List<URI> uris = ListExtensions.<Pair<String, String>, URI>map(((List<Pair<String, String>>)Conversions.doWrapArray(files)), _function);
    result.getURIConverter().getURIHandlers().add(uriHandler);
    final ArrayList<IResourceDescription> descriptions = CollectionLiterals.<IResourceDescription>newArrayList();
    for (final URI uri : uris) {
      {
        final Resource resource = result.getResource(uri, true);
        IResourceDescription _resourceDescription = this.resourceDesriptionManager.getResourceDescription(resource);
        descriptions.add(_resourceDescription);
      }
    }
    ResourceDescriptionsData _resourceDescriptionsData = new ResourceDescriptionsData(descriptions);
    Pair<String, ResourceDescriptionsData> _mappedTo = Pair.<String, ResourceDescriptionsData>of("default", _resourceDescriptionsData);
    final ChunkedResourceDescriptions chunkedResourceDescriptions = new ChunkedResourceDescriptions(
      Collections.<String, ResourceDescriptionsData>unmodifiableMap(CollectionLiterals.<String, ResourceDescriptionsData>newHashMap(_mappedTo)));
    chunkedResourceDescriptions.attachToEmfObject(result);
    return result;
  }
}
