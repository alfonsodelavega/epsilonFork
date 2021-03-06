/*******************************************************************************
 * Copyright (c) 2008 The University of York.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * Contributors:
 *     Louis Rose - initial API and implementation
 ******************************************************************************/
package org.eclipse.epsilon.egl.test.unit;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;

import org.eclipse.epsilon.egl.formatter.language.JavaFormatterTests;
import org.eclipse.epsilon.egl.formatter.language.PromelaIfBlockFormatterTests;
import org.eclipse.epsilon.egl.formatter.linebyline.LineByLineNavigatorTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({JavaFormatterTests.class, PromelaIfBlockFormatterTests.class,
               LineByLineNavigatorTests.class})
public class FormatterSuite {

	public static Test suite() {
		return new JUnit4TestAdapter(FormatterSuite.class);
	}
}
