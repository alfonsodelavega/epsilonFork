/*******************************************************************************
 * Copyright (c) 2008 The University of York.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * Contributors:
 *     Dimitrios Kolovos - initial API and implementation
 ******************************************************************************/
package org.eclipse.epsilon.examples.standalone.eol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.epsilon.eol.EolModule;
import org.eclipse.epsilon.eol.IEolModule;
import org.eclipse.epsilon.eol.dom.Operation;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.examples.standalone.EpsilonStandaloneExample;

/**
 * This example demonstrates using the 
 * Epsilon Object Language, the core language
 * of Epsilon, in a stand-alone manner 
 * @author Dimitrios Kolovos
 */
public class EolReturnStandaloneExample extends EpsilonStandaloneExample {
	
	public static void main(String[] args) throws Exception {
		new EolReturnStandaloneExample().execute();
	}
	
	@Override
	public IEolModule createModule() {
		return new EolModule();
	}

	@Override
	public List<IModel> getModels() throws Exception {
		List<IModel> models = new ArrayList<IModel>();
		models.add(createEmfModel("Model", "models/Tree.xmi", "models/Tree.ecore", true, true));
		return models;
	}

	@Override
	public String getSource() throws Exception {
		return "eol/Return.eol";
	}
	
	@Override
	protected Object execute(IEolModule module)
			throws EolRuntimeException {
		Operation operation = module.getDeclaredOperations().get(0);
		return operation.execute(null, Collections.emptyList(), module.getContext());
	}
	
	@Override
	public void postProcess() {
		System.err.println(result);
	}
	
}
