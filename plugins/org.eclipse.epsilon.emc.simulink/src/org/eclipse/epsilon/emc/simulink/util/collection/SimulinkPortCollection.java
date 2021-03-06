/*********************************************************************
* Copyright (c) 2008 The University of York.
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
**********************************************************************/
package org.eclipse.epsilon.emc.simulink.util.collection;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.epsilon.emc.simulink.engine.MatlabEngine;
import org.eclipse.epsilon.emc.simulink.model.SimulinkModel;
import org.eclipse.epsilon.emc.simulink.model.element.ISimulinkModelElement;
import org.eclipse.epsilon.emc.simulink.model.element.SimulinkPort;
import org.eclipse.epsilon.emc.simulink.operations.SimulinkCollectOperation;
import org.eclipse.epsilon.emc.simulink.operations.SimulinkSelectOperation;
import org.eclipse.epsilon.emc.simulink.util.manager.SimulinkPortManager;
import org.eclipse.epsilon.eol.execute.operations.AbstractOperation;
import org.eclipse.epsilon.eol.execute.operations.declarative.ExistsOperation;
import org.eclipse.epsilon.eol.execute.operations.declarative.FindOneOperation;
import org.eclipse.epsilon.eol.execute.operations.declarative.FindOperation;
import org.eclipse.epsilon.eol.execute.operations.declarative.ForAllOperation;
import org.eclipse.epsilon.eol.execute.operations.declarative.IAbstractOperationContributor;
import org.eclipse.epsilon.eol.execute.operations.declarative.RejectOneOperation;
import org.eclipse.epsilon.eol.execute.operations.declarative.RejectOperation;
import org.eclipse.epsilon.eol.execute.operations.declarative.SelectOneOperation;
import org.eclipse.epsilon.eol.execute.operations.declarative.SortByOperation;

public class SimulinkPortCollection extends AbstractSimulinkCollection<SimulinkPort, Double, SimulinkPortManager> implements IAbstractOperationContributor{
	
	public SimulinkPortCollection(List<Double> primitive, SimulinkModel model) {
		super(primitive, new SimulinkPortManager(model));	
	}
	
	public SimulinkPortCollection(Object primitive, SimulinkModel model) {
		super(primitive, new SimulinkPortManager(model));
	}
	
	public SimulinkPortCollection(Double[] primitive, SimulinkModel model) {
		super(primitive, new SimulinkPortManager(model));
	}
	
	@Override
	protected boolean isInstanceOf(Object object) {
		return object instanceof SimulinkPort;
	}

	@Override
	protected boolean isInstanceOfPrimitive(Object object) {
		return object instanceof Double;
	}

	@Override
	protected boolean isInstanceOfPrimitiveArray(Object object) {
		if (object instanceof Double[])
			return true;
		if (object instanceof Object[]) {
			return (Arrays.asList((Object[])object)).stream().allMatch(h -> isInstanceOfPrimitive(h));
		}
		return false;
	}
	
	@Override
	public List<ISimulinkModelElement> subList(int fromIndex, int toIndex) {
		return new SimulinkPortCollection(getPrimitive().subList(fromIndex, toIndex), getManager().getModel());
	}
	
	@Override
	public ListIterator<ISimulinkModelElement> listIterator() {
		return new SimulinkPortListIterator();
	}

	@Override
	public ListIterator<ISimulinkModelElement> listIterator(int index) {
		return new SimulinkPortListIterator(index);
	}

	@Override
	protected Iterator<ISimulinkModelElement> getInternalIterator() {
		return new SimulinkPortIterator();
	}
		
	protected class SimulinkPortIterator extends AbstractElementIterator<SimulinkPort, Double, SimulinkPortManager>{
		
		SimulinkPortIterator(){
			super(getPrimitive(), getManager());
		}
		
	}
	
	protected class SimulinkPortListIterator extends AbstractListIterator<SimulinkPort, Double, SimulinkPortManager> {
		
		SimulinkPortListIterator(){
			super(getPrimitive(), getManager());
		}
		
		SimulinkPortListIterator(int index){
			super(index, getPrimitive(), getManager());
		}
		
	}

	@Override
	public AbstractOperation getAbstractOperation(String name) {
		MatlabEngine engine = getManager().getEngine();
		switch (name) {
		
		case "select":			
			return new SimulinkSelectOperation(engine);
		case "collect":
			return new SimulinkCollectOperation(engine);

		/** Select Based */
		case "exists":
			ExistsOperation existsOperation = new ExistsOperation();
			existsOperation.setDelegateOperation(new SimulinkSelectOperation(engine));
			return existsOperation;
		case "findOne":
			FindOneOperation findOneOperation = new FindOneOperation();
			findOneOperation.setDelegateOperation(new SimulinkSelectOperation(engine));
			return findOneOperation;
		case "find":
			FindOperation findOperation = new FindOperation();
			findOperation.setDelegateOperation(new SimulinkSelectOperation(engine));
			return findOperation;
		case "forAll":
			ForAllOperation forAllOperation = new ForAllOperation();
			forAllOperation.setDelegateOperation(new SimulinkSelectOperation(engine));
			return forAllOperation;
		case "rejectOne":
			RejectOneOperation rejectOneOperation = new RejectOneOperation();
			rejectOneOperation.setDelegateOperation(new SimulinkSelectOperation(engine));
			return rejectOneOperation;
		case "reject":
			RejectOperation rejectOperation = new RejectOperation();
			rejectOperation.setDelegateOperation(new SimulinkSelectOperation(engine));
			return rejectOperation;
		case "selectOne":
			SelectOneOperation selectOneOperation = new SelectOneOperation();
			selectOneOperation.setDelegateOperation(new SimulinkSelectOperation(engine));
			return selectOneOperation;
		
		/** Collect Based */
		case "sortBy":
			SortByOperation sortByOperation = new SortByOperation();
			sortByOperation.setDelegateOperation(new SimulinkCollectOperation(engine));
			return sortByOperation;
		
		default:
			return null;
		} 
	}

}
