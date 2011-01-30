/*******************************************************************************
 * Copyright (c) 2010 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/eplv10.html
 *******************************************************************************/
package org.eclipse.xtext.common.types.util;

import java.util.HashSet;

import junit.framework.TestCase;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceImpl;
import org.eclipse.xtext.common.types.JvmFeature;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.common.types.TypesFactory;
import org.eclipse.xtext.common.types.access.impl.ClasspathTypeProvider;
import org.eclipse.xtext.common.types.memberoverrides.GenericSuperClass;
import org.eclipse.xtext.common.types.memberoverrides.SubClass;
import org.eclipse.xtext.common.types.memberoverrides.SubOfGenericClass;
import org.eclipse.xtext.common.types.memberoverrides.SuperClass;
import org.eclipse.xtext.common.types.util.TypeArgumentContext.Provider;

import com.google.common.collect.Sets;

/**
 * @author Sven Efftinge  Initial contribution and API
 */
public class JvmFeatureOverridesServiceTest extends TestCase {
    
    private ClasspathTypeProvider typeProvider;
    private JvmTypeReferences typeRefs;
    private JvmFeatureOverridesService service;
    private Provider typeArgCtxProvider;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        ResourceSet resourceSet = new ResourceSetImpl();
        Resource syntheticResource = new XMLResourceImpl(URI.createURI("http://synthetic.resource"));
        resourceSet.getResources().add(syntheticResource);
        typeProvider = new ClasspathTypeProvider(getClass().getClassLoader(), resourceSet);
        typeRefs = new JvmTypeReferences(TypesFactory.eINSTANCE, typeProvider);
        typeArgCtxProvider = new TypeArgumentContext.Provider();
        service = new JvmFeatureOverridesService(new SuperTypeCollector(TypesFactory.eINSTANCE), typeArgCtxProvider);
    }
    
    @Override
    protected void tearDown() throws Exception {
        typeProvider = null;
        typeRefs = null;
        super.tearDown();
    }
    
    public void testSimple() throws Exception {
        JvmTypeReference reference = typeRefs.typeReference("java.util.ArrayList").wildCardExtends("java.lang.CharSequence").create();
        Iterable<JvmFeature> iterable = service.getAllJvmFeatures(reference);
        HashSet<JvmFeature> set = Sets.newHashSet(iterable);
        
        assertFalse(set.contains(findOperation("java.util.AbstractList","add(int,E)")));
        assertFalse(set.contains(findOperation("java.util.List","add(int,E)")));
        assertTrue(set.contains(findOperation("java.util.ArrayList","add(int,E)")));
        assertTrue(set.contains(findOperation("java.util.AbstractList","iterator()")));
        assertFalse(set.contains(findOperation("java.util.List","iterator()")));
    }
    
    public void testContainsFields() throws Exception {
        JvmTypeReference reference = typeRefs.typeReference(SubClass.class.getName()).create();
        Iterable<JvmFeature> iterable = service.getAllJvmFeatures(reference);
        HashSet<JvmFeature> set = Sets.newHashSet(iterable);
        
        assertTrue(set.contains(findOperation(SubClass.class.getName(),"privateField")));
        assertTrue(set.contains(findOperation(SubClass.class.getName(),"protectedField")));
        assertTrue(set.contains(findOperation(SubClass.class.getName(),"publicField")));
        
        assertTrue(set.contains(findOperation(SuperClass.class.getName(),"privateField")));
        assertFalse(set.contains(findOperation(SuperClass.class.getName(),"protectedField")));
        assertFalse(set.contains(findOperation(SuperClass.class.getName(),"publicField")));
        
        assertTrue(set.contains(findOperation(SubClass.class.getName(),"publicMethod()")));
        assertFalse(set.contains(findOperation(SuperClass.class.getName(),"publicMethod()")));
        assertFalse(set.contains(findOperation(SubClass.class.getName(),"protectedMethod()")));
        assertTrue(set.contains(findOperation(SuperClass.class.getName(),"protectedMethod()")));
        assertFalse(set.contains(findOperation(SuperClass.class.getName(),"privateMethod(java.lang.Object)")));
        assertFalse(set.contains(findOperation(SuperClass.class.getName(),"privateMethod(java.lang.String)")));
        assertTrue(set.contains(findOperation(SubClass.class.getName(),"privateMethod(java.lang.Object)")));
        assertTrue(set.contains(findOperation(SubClass.class.getName(),"privateMethod(java.lang.String)")));
    }
    
    public void testGenerics_00() throws Exception {
        JvmTypeReference reference = typeRefs.typeReference(SubOfGenericClass.class.getName()).arg(String.class.getName()).create();
        Iterable<JvmFeature> iterable = service.getAllJvmFeatures(reference);
        HashSet<JvmFeature> set = Sets.newHashSet(iterable);
        
        assertTrue(set.contains(findOperation(SubOfGenericClass.class.getName(),"myMethod(java.lang.String)")));
        assertFalse(set.contains(findOperation(GenericSuperClass.class.getName(),"myMethod(java.lang.String)")));
        assertFalse(set.contains(findOperation(GenericSuperClass.class.getName(),"myMethod(T)")));
    }
    
    public void testGenerics_01() throws Exception {
        JvmTypeReference reference = typeRefs.typeReference(SubOfGenericClass.class.getName()).arg(Object.class.getName()).create();
        Iterable<JvmFeature> iterable = service.getAllJvmFeatures(reference);
        HashSet<JvmFeature> set = Sets.newHashSet(iterable);
        
        assertTrue(set.contains(findOperation(SubOfGenericClass.class.getName(),"myMethod(java.lang.String)")));
        assertFalse(set.contains(findOperation(GenericSuperClass.class.getName(),"myMethod(java.lang.String)")));
        assertTrue(set.contains(findOperation(GenericSuperClass.class.getName(),"myMethod(T)")));
    }
    
    protected JvmFeature findOperation(String typeName, String methodSignature) {
        JvmType type = typeProvider.findTypeByName(typeName);
        return (JvmFeature) type.eResource().getEObject(typeName+"."+methodSignature);
    }

    
}