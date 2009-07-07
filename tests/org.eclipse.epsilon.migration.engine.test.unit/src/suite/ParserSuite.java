/*******************************************************************************
 * Copyright (c) 2009 The University of York.
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
package suite;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;

import org.eclipse.epsilon.migration.model.loader.MigrationRuleLoaderTest;
import org.eclipse.epsilon.migration.model.loader.MigrationStrategyLoaderTest;
import org.eclipse.epsilon.migration.parse.TestMigration;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({TestMigration.class,
               MigrationStrategyLoaderTest.class, MigrationRuleLoaderTest.class})
public class ParserSuite {
	public static Test suite() {
		return new JUnit4TestAdapter(ParserSuite.class);
	}
}
