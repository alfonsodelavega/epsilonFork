/*******************************************************************************
 * Copyright (c) 2008 The University of York.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * Contributors:
 *     Louis Rose - initial API and implementation
 ******************************************************************************
 *
 * $Id$
 */
package org.eclipse.epsilon.hutn.xmi.test.integration.consistent.slots.singleValued;


public class Boolean extends AbstractSingleValuedSlotTest {
	
	@Override
	protected java.lang.String getXmi() {
		return "nuclear=\"true\"";
	}

	@Override
	protected java.lang.String getExpectedFeatureName() {
		return "nuclear";
	}
	
	@Override
	protected Object getExpectedContent() {
		return true;
	}
}
