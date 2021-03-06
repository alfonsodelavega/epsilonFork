/*******************************************************************************
 * Copyright (c) 2008 The University of York.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * Contributors:
 *     Louis Rose - initial API and implementation
 ******************************************************************************/
package org.eclipse.epsilon.egl.test.acceptance.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.io.File;
import java.util.Collection;
import org.eclipse.epsilon.common.parse.problem.ParseProblem;
import static org.eclipse.epsilon.common.util.FileUtil.getFile;
import org.eclipse.epsilon.egl.exceptions.EglRuntimeException;
import org.eclipse.epsilon.egl.test.acceptance.AcceptanceTestUtil;
import org.eclipse.epsilon.egl.test.models.Model;
import org.junit.BeforeClass;
import org.junit.Test;

public class Engine {
	
	private static File OO2JavaProgram, OO2JavaImportEolProgram, OO2JavaImportEglProgram;
	private static File OO2JavaExpected;
	private static File runtimeExceptionProgram;
	private static File invalidPath;
	private static File NonExistentImport;
	private static File processTemplate;
	
	
	@BeforeClass
	public static void setUpOnce() {
		OO2JavaProgram          = getFile("OO2Java.egl",           Engine.class);
		OO2JavaImportEolProgram = getFile("OO2JavaImportEol.egl",  Engine.class);
		OO2JavaImportEglProgram = getFile("OO2JavaImportEgl.egl",  Engine.class);
		OO2JavaExpected         = getFile("OO2Java.txt",           Engine.class);
		
		NonExistentImport       = getFile("NonExistentImport.egl", Engine.class);
		
		runtimeExceptionProgram = getFile("RuntimeException.egl",  Engine.class);
		invalidPath             = getFile("Inva*lid.egl",          Engine.class);
		processTemplate         = getFile("ProcessTemplate.egl",   Engine.class);
	}
	
	@Test
	public void testValid() throws Exception {
		AcceptanceTestUtil.test(OO2JavaProgram, OO2JavaExpected, Model.OOInstance);
	}
	
	@Test
	public void testImportEol() throws Exception {
		AcceptanceTestUtil.test(OO2JavaImportEolProgram, OO2JavaExpected, Model.OOInstance);
	}
	
	@Test
	public void testImportEgl() throws Exception {
		AcceptanceTestUtil.test(OO2JavaImportEglProgram, OO2JavaExpected, Model.OOInstance);
	}

	@Test
	public void testBadImport() throws Exception {
		AcceptanceTestUtil.run(NonExistentImport);

		final Collection<ParseProblem> problems = AcceptanceTestUtil.getParseProblems();
		assertEquals(1, problems.size());
		assertTrue(problems.iterator().next().getReason().contains("NonExistent.egl"));
	}

	@Test (expected=EglRuntimeException.class)
	public void testRuntimeException() throws Exception {
		try {
			AcceptanceTestUtil.run(runtimeExceptionProgram);
		
		} catch (EglRuntimeException ex) {
			assertEquals(2,  ex.getLine());
			assertEquals(9, ex.getColumn());
			throw ex;
		}
	}
	
	@Test (expected=EglRuntimeException.class)
	public void testParseInvalid() throws Exception {
		AcceptanceTestUtil.run(invalidPath);
	}
	
	/**
	 * 
	 * @throws Exception
	 * @since 1.6
	 */
	@Test
	public void testProcessConsistency() throws Exception {
		AcceptanceTestUtil.run(processTemplate);	// TODO: fix
	}
}
