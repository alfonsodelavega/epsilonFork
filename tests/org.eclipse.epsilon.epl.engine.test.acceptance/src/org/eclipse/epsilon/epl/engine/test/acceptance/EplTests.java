/*******************************************************************************
 * Copyright (c) 2018 The University of York.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Louis Rose - initial API and implementation
 *     Sina Madani - additional tests
 ******************************************************************************/
package org.eclipse.epsilon.epl.engine.test.acceptance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.io.File;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.eclipse.epsilon.emc.plainxml.PlainXmlModel;
import org.eclipse.epsilon.eol.exceptions.models.EolModelElementTypeNotFoundException;
import org.eclipse.epsilon.eol.exceptions.models.EolModelLoadingException;
import org.eclipse.epsilon.eol.execute.context.FrameStack;
import org.eclipse.epsilon.eol.execute.context.Variable;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.epl.EplModule;
import org.eclipse.epsilon.epl.IEplModule;
import org.eclipse.epsilon.epl.dom.NoMatch;
import org.eclipse.epsilon.epl.execute.PatternMatch;
import org.eclipse.epsilon.epl.execute.PatternMatchModel;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class EplTests {
	
	private static IModel TEST_MODEL;
	private static final File TEST_SCRIPT = new File(EplTests.class.getResource("test.epl").getFile());
	
	static {
		try {
			TEST_MODEL = setUpModel();
		}
		catch (EolModelLoadingException ex) {
			ex.printStackTrace();
		}
	}
	
	public static IModel getTestModel() {
		return TEST_MODEL;
	}
	
	@Parameter
	public Supplier<? extends IEplModule> moduleGetter;
	
	private IEplModule module;
	private PatternMatchModel patternMatchModel;
	
	@Parameters(name = "{0}")
	public static Iterable<Supplier<? extends IEplModule>> modules() {
		return Collections.singleton(EplModule::new);
	}
	
	// Using a Collector allows us to capture the errors and print more useful diagnostic info.
	@Rule
    public ErrorCollector collector = new ErrorCollector();
	
	private static IModel setUpModel() throws EolModelLoadingException {
		PlainXmlModel model = new PlainXmlModel();
		model.setFile(new File(EplTests.class.getResource("test.xml").getFile()));
		model.setName("M");
		model.setCachingEnabled(false);
		model.load();
		return model;
	}
	
	public static Path getTestScript(IEplModule module) {
		FrameStack frameStack = module.getContext().getFrameStack();
		frameStack.putGlobal(
			Variable.createReadOnlyVariable("blackboard", new HashMap<>()),
			Variable.createReadOnlyVariable("frameStack", frameStack)
		);
		return TEST_SCRIPT.toPath();
	}
	
	@SuppressWarnings("unchecked")
	Map<String, String> loadEPL() throws Exception {
		module = moduleGetter.get();
		
		Map<String, String> blackboard = new HashMap<>();
		FrameStack frameStack = module.getContext().getFrameStack();
		frameStack.putGlobal(
			Variable.createReadOnlyVariable("blackboard", blackboard),
			Variable.createReadOnlyVariable("frameStack", frameStack)
		);
		
		module.parse(TEST_SCRIPT);
		IModel model = TEST_MODEL;
		model.load();
		module.getContext().getModelRepository().addModel(model);
		
		return (Map<String, String>) frameStack.get("blackboard").getValue();
	}
	
	private void assertMatchesCollector(int expectedMatches, String name, Consumer<Collection<?>> code) throws EolModelElementTypeNotFoundException {
		Collection<?> allMatches = patternMatchModel.getAllOfType(name);
		try {
			code.accept(allMatches);
		}
		catch (AssertionError error) {
			System.err.println("Expected "+expectedMatches+" for '"+name+"', but was "+allMatches.size());
			collector.addError(error);
		}
	}
	
	void assertNumberOfMatches(int numMatches, String name) throws EolModelElementTypeNotFoundException {
		assertMatchesCollector(numMatches, name, allMatches ->
			assertEquals(numMatches, allMatches.size())
		);
	}
	
	void assertNoMatch(int numMatches, String name, boolean all) throws EolModelElementTypeNotFoundException {
		assertMatchesCollector(numMatches, name, allMatches -> {
			assertEquals(numMatches, allMatches.size());
			Predicate<? super Object> isNoMatch = v -> v instanceof NoMatch;
			
			for (Object match : allMatches) {
				assert match instanceof PatternMatch;
				Stream<Object> bindings = ((PatternMatch)match).getRoleBindings().values().stream();
				assertTrue(all ? bindings.allMatch(isNoMatch) : bindings.anyMatch(isNoMatch));
			}
		});
	}
	
	
	@Test
	public void testEpl() throws Exception {
		Map<String, String> blackboard = loadEPL();
		patternMatchModel = (PatternMatchModel) module.execute();
		
		assertNumberOfMatches(2, "B");
		assertNumberOfMatches(4, "BC");
		assertNumberOfMatches(2, "BfromAll");
		assertNumberOfMatches(2, "BfromReturnAll");
		assertNumberOfMatches(2, "Bmatch");
		assertNumberOfMatches(0, "NoB");
		assertNumberOfMatches(0, "NoBguardReturn");
		assertNumberOfMatches(2, "Bguard");
		assertNumberOfMatches(2, "BCinactive");
		assertNumberOfMatches(4, "BCactive");
		assertEquals("xx", blackboard.get("Bonmatch"));
		assertEquals("xx", blackboard.get("Bnomatch"));
		assertEquals("xxxx", blackboard.get("BCdo"));
		assertNumberOfMatches(0, "BnoC");
		assertNumberOfMatches(2, "BnonoC");
		assertNumberOfMatches(0, "Binactive");
		assertNumberOfMatches(0, "BinactiveCfromAll");
		assertNumberOfMatches(0, "BnoCinactiveB");
		assertNumberOfMatches(1, "B2Csubset");
		assertNumberOfMatches(1, "AccessSelf");
		//assertNumberOfMatches(1, "NegativeAccessPredecessor");	// FAIL
		//assertNumberOfMatches(2, "NegativeAccessSelf");			// FAIL
		assertNumberOfMatches(2, "MultipleInactive");
		assertNumberOfMatches(2, "CardinalityMany");
		//assertNumberOfMatches(2, "CardinalityRange");				// FAIL
		assertNumberOfMatches(0, "CardinalityZero");
		//assertNumberOfMatches(2, "CardinalityNegative");			// FAIL
		assertNumberOfMatches(2, "OptionalActive");
		assertNumberOfMatches(2, "OptionalInactiveMultipleDomains");
		//assertNumberOfMatches(?, "OptionalNegativeGuard");		// TODO: implement
		//assertNumberOfMatches(?, "OptionalCardinality");			// TODO: implement
		//assertNumberOfMatches(?, "KitchenSink");					// TODO: implement
	}
}