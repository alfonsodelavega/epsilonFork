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
package org.eclipse.epsilon.concordance.core.hashing.test.integration;

import static org.eclipse.epsilon.test.util.builders.emf.EClassBuilder.anEClass;
import static org.eclipse.epsilon.test.util.builders.emf.EReferenceBuilder.MANY;
import static org.eclipse.epsilon.test.util.builders.emf.EReferenceBuilder.anEReference;
import static org.eclipse.epsilon.test.util.builders.emf.EPackageBuilder.aMetamodel;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.epsilon.concordance.core.hashing.hashers.ecore.EPackageHasher;

import org.junit.Test;

public class BidirectionalReference {
	
	@Test
	public void sameHashCodeForEquivalentMetamodels() {
		assertThat(hashFor(buildMetamodelWithBidirectionalReference()), equalTo(hashFor(buildMetamodelWithBidirectionalReference())));
	}
	
	private int hashFor(EPackage pkg) {
		return EPackageHasher.getInstance().hashSafely(pkg);
	}

	private EPackage buildMetamodelWithBidirectionalReference() {
		final EClass person  = anEClass().named("Person").build();
		final EClass account = anEClass().named("Account").build();
		
		final EReference accounts = anEReference().named("accounts").references(0, MANY, account).build();
		final EReference owners   = anEReference().named("owners").references(1, MANY, person).build();
		
		accounts.setEOpposite(owners);
		owners.setEOpposite(accounts);
		
		person.getEStructuralFeatures().add(accounts);
		account.getEStructuralFeatures().add(owners);
		
		return aMetamodel().with(person).with(account).build();
	}
}
