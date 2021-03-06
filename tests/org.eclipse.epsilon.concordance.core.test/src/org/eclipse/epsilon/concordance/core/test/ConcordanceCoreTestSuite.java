/*******************************************************************************
 * Copyright (c) 2009 The University of York.
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
package org.eclipse.epsilon.concordance.core.test;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;

import org.eclipse.epsilon.concordance.core.hashing.test.ConcordanceHashingTestSuite;
import org.eclipse.epsilon.concordance.db.ConcordanceH2DatabaseTestSuite;
import org.eclipse.epsilon.concordance.history.ConcordanceHistoryTests;
import org.eclipse.epsilon.concordance.model.UriContentReaderTests;
import org.eclipse.epsilon.concordance.model.nsuri.ModelWalkingNsUriAnalyserTests;
import org.eclipse.epsilon.concordance.model.nsuri.NsUriIdentifyingParserTests;
import org.eclipse.epsilon.concordance.reporter.metamodel.MetamodelChangeReporterTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({NsUriIdentifyingParserTests.class, UriContentReaderTests.class, ModelWalkingNsUriAnalyserTests.class,
               MetamodelChangeReporterTests.class,
               ConcordanceH2DatabaseTestSuite.class,
               ConcordanceHistoryTests.class,
               ConcordanceHashingTestSuite.class})
public class ConcordanceCoreTestSuite {

	public static Test suite() {
		return new JUnit4TestAdapter(ConcordanceCoreTestSuite.class);
	}
}
