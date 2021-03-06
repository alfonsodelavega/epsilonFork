/*******************************************************************************
 * Copyright (c) 2011 The University of York.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * Contributors:
 *     Louis Rose - initial API and implementation
 ******************************************************************************/
package org.eclipse.epsilon.flock.equivalences.factory;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.eclipse.epsilon.eol.execute.context.IEolContext;
import org.eclipse.epsilon.flock.context.EquivalenceEstablishmentContext.EquivalentFactory;
import org.eclipse.epsilon.flock.emc.wrappers.ModelElement;
import org.eclipse.epsilon.flock.equivalences.Equivalence;
import org.eclipse.epsilon.flock.equivalences.NoEquivalence;
import org.eclipse.epsilon.flock.equivalences.TypeBasedEquivalence;
import org.eclipse.epsilon.flock.execute.FlockExecution;
import org.eclipse.epsilon.flock.execute.exceptions.FlockRuntimeException;
import org.junit.Test;


public class DefaultEquivalenceFactoryTests {

	@Test
	public void shouldCreateATypeBasedEquivalenceWhenTypeConformsToEvolvedMetamodel() throws FlockRuntimeException {
		final EquivalentFactory equivalentFactory = mock(EquivalentFactory.class);
		
		when(equivalentFactory.typeConformsToEvolvedMetamodel(anyString())).thenReturn(true);
		
		assertThat(createEquivalence(equivalentFactory), is(instanceOf(TypeBasedEquivalence.class)));		
	}
	
	@Test
	public void shouldCreateANoEquivalenceWhenTypeDoesNotConformToEvolvedMetamodel() throws FlockRuntimeException {
		final EquivalentFactory equivalentFactory = mock(EquivalentFactory.class);
		
		when(equivalentFactory.typeConformsToEvolvedMetamodel(anyString())).thenReturn(false);
		
		assertThat(createEquivalence(equivalentFactory), is(instanceOf(NoEquivalence.class)));		
	}

	private Equivalence createEquivalence(final EquivalentFactory equivalentFactory) throws FlockRuntimeException {
		return DefaultEquivalenceFactory.getInstance().createEquivalence(mock(IEolContext.class), mock(FlockExecution.class), mock(ModelElement.class), equivalentFactory);
	}
}
