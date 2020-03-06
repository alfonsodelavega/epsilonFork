/*********************************************************************
 * Copyright (c) 2018 The University of York.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
**********************************************************************/
package org.eclipse.epsilon.egx.engine.test.acceptance;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import org.eclipse.epsilon.common.util.FileUtil;
import org.eclipse.epsilon.egl.EgxModule;
import org.eclipse.epsilon.egl.IEgxModule;
import org.eclipse.epsilon.egl.concurrent.*;
import org.eclipse.epsilon.egl.concurrent.atomic.*;
import org.eclipse.epsilon.egl.execute.context.concurrent.EgxContextParallel;
import org.eclipse.epsilon.egl.launch.EgxRunConfiguration;
import org.eclipse.epsilon.eol.engine.test.acceptance.util.EolAcceptanceTestUtil;

public class EgxAcceptanceTestUtil extends EolAcceptanceTestUtil {
	private EgxAcceptanceTestUtil() {}
	
	public static final String
		testsBase = getTestBaseDir(EgxAcceptanceTestSuite.class),
		ecoreBase = testsBase+"ecore2dot/",
		ecoreMetamodel = "Ecore.ecore",
		ecoreModels[] = {"java.ecore"},
		ecoreScripts[] = {"ecore"},
		thriftBase = testsBase+"thrift/",
		thriftMetamodel = "thrift.ecore",
		thriftModels[] = {"ThriftTest.xmi", "fb303.xmi", "SimpleService.xmi"},
		thriftScripts[] = {"thrift-rb", "thrift-java"};
		
	public static final List<String[]>
		ecoreInputs = new ArrayList<>(),
		thriftInputs = new ArrayList<>(),
		allInputs = new ArrayList<>();
	
	static {
		ecoreInputs.addAll(addAllInputs(
			ecoreScripts,
			ecoreModels, ecoreMetamodel, "egx",
			ecoreBase, ecoreBase, ecoreBase
		));
		thriftInputs.addAll(addAllInputs(
			new String[]{thriftScripts[0]},
			thriftModels, thriftMetamodel, "egx",
			thriftBase+"ruby/", thriftBase, thriftBase
		));
		thriftInputs.addAll(addAllInputs(
			new String[]{thriftScripts[1]},
			thriftModels, thriftMetamodel, "egx",
			thriftBase+"java/", thriftBase, thriftBase
		));
		allInputs.addAll(ecoreInputs);
		//allInputs.addAll(thriftInputs);
	}
	
	public static Collection<Supplier<? extends IEgxModule>> modules(boolean includeStandard) {
		return parallelModules(THREADS,
			includeStandard ? EgxModule::new : null,
			p -> new EgxModuleParallelAnnotation(new EgxContextParallel(p)),
			p -> new EgxModuleParallelElements(new EgxContextParallel(p)),
			p -> new EgxModuleParallelGenerationRuleAtoms(new EgxContextParallel(p))
		);
	}
	
	public static Collection<EgxRunConfiguration> getScenarios(
		List<String[]> testInputs,
		Collection<Supplier<? extends IEgxModule>> moduleGetters) {
			return getScenarios(EgxRunConfiguration.class, testInputs, moduleGetters, null);
	}
	
	public static void deleteOutputDirectories() throws IOException {
		try {
			FileUtil.deleteDirectory(ecoreBase+"output");
			FileUtil.deleteDirectory(thriftBase+"ruby/output");
			FileUtil.deleteDirectory(thriftBase+"java/output");
		}
		catch (java.nio.file.NoSuchFileException ignore) {}
	}
	
	public static Map<Path, byte[]> getOutputFiles() throws IOException {
		Map<Path, byte[]> outputs = new java.util.HashMap<>();
		try {
			outputs.putAll(FileUtil.readDirectory(ecoreBase+"output"));
			outputs.putAll(FileUtil.readDirectory(thriftBase+"ruby/output"));
			outputs.putAll(FileUtil.readDirectory(thriftBase+"java/output"));
		}
		catch (java.nio.file.NoSuchFileException ignore) {}
		return outputs;
	}
	
	public static Map<Path, byte[]> getResult(EgxRunConfiguration scenario) throws IOException {
		return FileUtil.readDirectory(Paths.get(scenario.outputRoot));
	}
}
