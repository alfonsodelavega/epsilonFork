/*******************************************************************************
 * Copyright (c) 2008 The University of York.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * Contributors:
 *     Dimitrios Kolovos - initial API and implementation
 ******************************************************************************/
package org.eclipse.epsilon.eol.dt;

import org.eclipse.epsilon.common.dt.AbstractEpsilonUIPlugin;

public class EolPlugin extends AbstractEpsilonUIPlugin {

	public static EolPlugin getDefault() {
		return (EolPlugin) plugins.get(EolPlugin.class);
	}

}
