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
package org.eclipse.epsilon.concordance.core.hashing.hashers.ecore;


import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.epsilon.concordance.core.hashing.hashers.java.TypeSafeHasher;


public class EAttributeHasher extends TypeSafeHasher<EAttribute> {

	private final MetamodelElementHasher hasher;
	
	private EAttributeHasher() {
		hasher = new MetamodelElementHasher("name", "lowerBound", "upperBound", "eType");
	}
	
	private static EAttributeHasher instance = new EAttributeHasher();
	
	public static EAttributeHasher getInstance() {
		return instance;
	}

	@Override
	public int hashSafely(EAttribute item) {
		return hasher.hashSafely(item);
	}
}
