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
 * $Id: UnrecognisedIdentifier.java,v 1.1 2008/08/07 14:51:11 louis Exp $
 */
package org.eclipse.epsilon.hutn.validation.model.externalrefs;

import static org.eclipse.epsilon.hutn.test.util.HutnUtil.createClassObject;
import static org.eclipse.epsilon.hutn.test.util.HutnUtil.createPackageObject;
import static org.eclipse.epsilon.hutn.test.util.HutnUtil.createReferenceSlot;
import static org.eclipse.epsilon.hutn.test.util.HutnUtil.createSpec;
import static org.junit.Assert.assertEquals;

import org.eclipse.epsilon.hutn.exceptions.HutnValidationException;
import org.eclipse.epsilon.hutn.validation.model.HutnModelValidationTest;
import org.junit.BeforeClass;
import org.junit.Test;

public class NonExistentModelElementId extends HutnModelValidationTest {

	@BeforeClass
	public static void validateModel() throws HutnValidationException {
		problems = modelValidationTest(createSpec("families", createPackageObject(createClassObject("John",
		                                                                                "Person",
		                                                                                createReferenceSlot("sharedAccounts", BANK_ACCOUNTS_MODEL_URI + "#_swAAYJX5Ed2TbbKclXXXXX")))));
	}
	
	@Test
	public void validationShouldReportNoProblems() {
		assertEquals(1, problems.size());
	}
	
	@Test
	public void reasonShouldBeUnrecognisedIdentifier() {
		assertEquals("Model element not found: _swAAYJX5Ed2TbbKclXXXXX", problems.get(0).getReason());
	}
}
