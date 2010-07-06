/*******************************************************************************
 * Copyright (c) 2008 The University of York.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Dimitrios Kolovos - initial API and implementation
 ******************************************************************************/
package org.eclipse.epsilon.eol.emc.emf.virtual;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.epsilon.commons.util.StringProperties;
import org.eclipse.epsilon.commons.util.StringUtil;
import org.eclipse.epsilon.emc.emf.AbstractEmfModel;
import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.emc.emf.virtual.VirtualFactory;
import org.eclipse.epsilon.emc.emf.virtual.VirtualModel;
import org.eclipse.epsilon.emc.emf.virtual.VirtualObject;
import org.eclipse.epsilon.eol.exceptions.models.EolModelElementTypeNotFoundException;
import org.eclipse.epsilon.eol.exceptions.models.EolModelLoadingException;
import org.eclipse.epsilon.eol.exceptions.models.EolNotInstantiableModelElementTypeException;
import org.eclipse.epsilon.eol.execute.introspection.IPropertyGetter;
import org.eclipse.epsilon.eol.execute.introspection.IPropertySetter;


public class VirtualEmfModel extends AbstractEmfModel {

	private VirtualModel model = VirtualFactory.eINSTANCE.createVirtualModel();
	
	protected String modelFile;
	protected URI modelFileUri;
	
	@Override
	public boolean isOfKind(Object instance, String metaClass) throws EolModelElementTypeNotFoundException {
		if (instance instanceof VirtualObject) {
			return metaClass.equals(((VirtualObject)instance).getType());
		}
		
		return super.isOfKind(instance, metaClass);
	}

	@Override
	public boolean isOfType(Object instance, String metaClass) throws EolModelElementTypeNotFoundException {
		if (instance instanceof VirtualObject) {
			return metaClass.equals(((VirtualObject)instance).getType());
		}
		
		return super.isOfType(instance, metaClass);
	}
	
	protected List<String> types = new ArrayList<String>();
	
	@Override
	public boolean hasType(String type) {
		String firstChar = type.substring(0,1);
		return firstChar == firstChar.toUpperCase();

	}
	
	@Override
	public Object createInstance(String type) throws EolModelElementTypeNotFoundException, EolNotInstantiableModelElementTypeException {
		final VirtualObject instance = VirtualFactory.eINSTANCE.createVirtualObject();
		instance.setType(type);
		
		model.getObjects().add(instance);
		
		return instance;
	}
	
	@Override
	public boolean owns(Object instance) {
		return model.getObjects().contains(instance);
	}
	
	
	@Override
	public void load(StringProperties properties, String basePath) throws EolModelLoadingException {
		super.load(properties, basePath);
		
		this.modelFile = StringUtil.toString(basePath) + properties.getProperty(EmfModel.PROPERTY_MODEL_FILE);
		
		if (StringUtil.isEmpty(basePath)) {
			this.modelFileUri = URI.createFileURI(modelFile);
		
		} else {
			this.modelFileUri = URI.createPlatformResourceURI(properties.getProperty(EmfModel.PROPERTY_MODEL_FILE), true);
		}
		
		load();
	}
	
	@Override
	public void load() throws EolModelLoadingException {
		model = VirtualFactory.eINSTANCE.createVirtualModel();
		
		final ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new EcoreResourceFactoryImpl());

		modelImpl = resourceSet.createResource(modelFileUri);
		modelImpl.getContents().add(model);
	}

	public boolean store() {
		return store(modelFile);
	}

	@Override
	public IPropertyGetter getPropertyGetter() {
		return new VirtualPropertyGetter();
	}
	
	@Override
	public IPropertySetter getPropertySetter() {
		return new VirtualPropertySetter();
	}

}
