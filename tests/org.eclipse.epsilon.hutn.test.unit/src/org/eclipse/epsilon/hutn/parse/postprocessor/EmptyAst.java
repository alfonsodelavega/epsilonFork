/**
 * *******************************************************************************
 * Copyright (c) 2008 The University of York.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * Contributors:
 *     Louis Rose - initial API and implementation
 * ******************************************************************************
 *
 * $Id: EmptyAst.java,v 1.2 2008/08/07 12:44:20 louis Exp $
 */
package org.eclipse.epsilon.hutn.parse.postprocessor;

import org.eclipse.epsilon.test.util.ModelWithEolAssertions;
import org.junit.BeforeClass;
import org.junit.Test;

public class EmptyAst extends HutnPostProcessorTest {

	private static ModelWithEolAssertions model;
	
	@BeforeClass
	public static void createAstModel() {
		 model = postProcessorTest(null);
	}
	
	@Test
	public void modelShouldContainNoRoots() {
		model.assertEquals(0, "ast.roots.size()");
	}
}
