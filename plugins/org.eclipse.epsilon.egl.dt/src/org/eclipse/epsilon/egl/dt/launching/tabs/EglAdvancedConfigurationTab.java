/*********************************************************************
* Copyright (c) 2008 The University of York.
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* Contributors:
*     Horacio Hoyos - initial API and implementation
**********************************************************************/
package org.eclipse.epsilon.egl.dt.launching.tabs;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.epsilon.common.dt.EpsilonPlugin;
import org.eclipse.epsilon.common.dt.launching.tabs.AbstractAdvancedConfigurationTab;
import org.eclipse.epsilon.egl.dt.EglPlugin;
import org.eclipse.epsilon.egl.dt.launching.EglLaunchConfigurationDelegate;

/**
 * The Class EglAdvancedConfigurationTab.
 * @since 1.6
 * @author Horacio Hoyos Rodriguez
 */
public class EglAdvancedConfigurationTab extends AbstractAdvancedConfigurationTab {
	
	@Override
	public EpsilonPlugin getPlugin() {
		return EglPlugin.getDefault();
	}
	
	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		super.initializeFrom(configuration);
	}
	
	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		super.performApply(configuration);
		String language = EglLaunchConfigurationDelegate.getLanguageFromSource(configuration);
		configuration.setAttribute(IMPL_NAME, language);
	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
	
	}

	@Override
	public String getLanguage(ILaunchConfiguration configuration) {
		return EglLaunchConfigurationDelegate.getLanguageFromSource(configuration);
	}
	
	
}