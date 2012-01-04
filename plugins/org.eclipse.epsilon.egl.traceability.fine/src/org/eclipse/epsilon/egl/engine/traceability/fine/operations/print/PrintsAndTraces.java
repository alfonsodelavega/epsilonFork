/*******************************************************************************
 * Copyright (c) 2011 The University of York.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Louis Rose - initial API and implementation
 ******************************************************************************
 *
 * $Id$
 */
package org.eclipse.epsilon.egl.engine.traceability.fine.operations.print;


import org.eclipse.epsilon.commons.parse.AST;
import org.eclipse.epsilon.egl.engine.traceability.fine.context.IEglTraceabilityContext;
import org.eclipse.epsilon.egl.engine.traceability.fine.trace.Region;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;


public class PrintsAndTraces {
	
	private final Printer printer;
	private final IEglTraceabilityContext context;
	
	public PrintsAndTraces(Printer printer, IEglTraceabilityContext context) {
		this.printer = printer;
		this.context = context;
	}
	
	public void forAst(AST unevaluatedPrintee) throws EolRuntimeException {
		final Object printee = context.recordPropertyAccessesWhileEvaluating(unevaluatedPrintee);
		final Region destinationOfPrintee = printer.print(printee);
		
		context.addDestinationRegionForLatestPropertyAccesses(destinationOfPrintee);
	}
}