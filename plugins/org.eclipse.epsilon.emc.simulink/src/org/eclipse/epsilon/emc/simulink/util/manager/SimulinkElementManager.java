/*********************************************************************
* Copyright (c) 2008 The University of York.
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
**********************************************************************/
package org.eclipse.epsilon.emc.simulink.util.manager;

import org.eclipse.epsilon.emc.simulink.model.SimulinkModel;
import org.eclipse
.epsilon.emc.simulink.model.TypeHelper;
import org.eclipse.epsilon.emc.simulink.model.TypeHelper.Kind;
import org.eclipse.epsilon.emc.simulink.model.element.ISimulinkElement;
import org.eclipse.epsilon.emc.simulink.model.element.SimulinkBlock;
import org.eclipse.epsilon.emc.simulink.model.element.SimulinkLine;
import org.eclipse.epsilon.emc.simulink.model.element.SimulinkPort;
import org.eclipse.epsilon.emc.simulink.model.element.StateflowBlock;

public class SimulinkElementManager extends AbstractManager<ISimulinkElement, Object> {

	public SimulinkElementManager(SimulinkModel model){
		super(model);
	}
	
	@Override
	public ISimulinkElement construct(Object id) {
		Kind kind = TypeHelper.getKind(getModel(), id);
		switch (kind) {
		case BLOCK:
			return new SimulinkBlockManager( getModel()).construct((Double) id);
		case LINE:
			return new SimulinkLineManager(getModel()).construct((Double) id);
		case PORT:
			return new SimulinkPortManager(getModel()).construct((Double) id);
		case STATEFLOW:
			return (ISimulinkElement) new StateflowBlockManager(getModel()).construct((Double) id);
			//return (ISimulinkElement) new StateflowBlockManager(getModel()).construct((String) id);
		default:
			return null;
		}
	}
	
	@Override
	public Object getId(ISimulinkElement from) {
		if (from instanceof StateflowBlock) {
			return (Double) new StateflowBlockManager(getModel()).getId((StateflowBlock) from);
			//return (String) new StateflowBlockManager(getModel()).getId((StateflowBlock) from);
		} else if (from instanceof SimulinkLine) {
			return (Double) new SimulinkLineManager(getModel()).getId((SimulinkLine) from);
		} else if (from instanceof SimulinkPort) {
			return (Double) new SimulinkPortManager(getModel()).getId((SimulinkPort) from);
		} else if (from instanceof SimulinkBlock) {
			return (Double) new SimulinkBlockManager(getModel()).getId((SimulinkBlock) from);
		} 
		return null;
	}

	@Override
	public SimulinkModel getModel() {
		return (SimulinkModel)model;
	}	
	
}