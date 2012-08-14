/*******************************************************************************
 * Copyright (c) 2012 Antonio Garcia-Dominguez.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Antonio Garcia-Dominguez - initial API and implementation
 ******************************************************************************/
package org.eclipse.epsilon.eunit.junit;

import java.net.URI;
import java.util.List;

import org.eclipse.epsilon.eol.models.IModel;

/**
 * Base interface that must be implemented by all test suites using the
 * {@link EUnitTestRunner}.
 */
public interface IEUnitSuite {

	/**
	 * Returns the URI of the main .eunit script to be run.
	 */
	URI getModuleURI() throws Exception;

	/**
	 * Returns the list of the models that should be used for the next
	 * test. These models should be reloaded in every call to this method,
	 * in order to ensure that each EUnit test is isolated from the rest.
	 */
	List<IModel> prepareModels() throws Exception;

}