/*********************************************************************
* Copyright (c) 2008 The University of York.
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
**********************************************************************/
package org.eclipse.epsilon.emc.simulink.introspection.java;

import org.eclipse.epsilon.emc.simulink.engine.MatlabEngine;
import org.eclipse.epsilon.emc.simulink.model.element.ISimulinkModelElement;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.execute.introspection.java.JavaPropertySetter;

public class SimulinkPropertySetter extends JavaPropertySetter {

	protected MatlabEngine engine;

	public SimulinkPropertySetter(MatlabEngine engine) {
		this.engine = engine;
	}

	@Override
	public void invoke(Object value) throws EolRuntimeException {
		try {
			super.invoke(value);
		} catch (Exception e) {
			if (object instanceof ISimulinkModelElement) {
				ISimulinkModelElement element = (ISimulinkModelElement) object;
				element.setProperty(property, value);
			}
				
		}
	}

}
