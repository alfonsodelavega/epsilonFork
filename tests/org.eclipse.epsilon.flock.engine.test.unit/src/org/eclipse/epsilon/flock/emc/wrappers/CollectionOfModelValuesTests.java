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
package org.eclipse.epsilon.flock.emc.wrappers;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import java.util.Arrays;
import java.util.Collection;
import org.eclipse.epsilon.flock.context.ConservativeCopyContext;
import org.eclipse.epsilon.flock.execute.exceptions.ConservativeCopyException;
import org.junit.Test;

public class CollectionOfModelValuesTests {

	private static final Model dummyModel  = createMock(Model.class);

	@SuppressWarnings("rawtypes")
	@Test
	public void unwrapShouldDelegateToUnwrapOfEachElement() {
		final BackedModelValue firstMockModelValue  = createMock(BackedModelValue.class);
		final BackedModelValue secondMockModelValue = createMock(BackedModelValue.class);

		
		// Expectations
		
		expect(firstMockModelValue.unwrap())
			.andReturn("foo");
		
		expect(secondMockModelValue.unwrap())
			.andReturn("bar");
		
		replay(firstMockModelValue, secondMockModelValue);
		
		
		// Verification
		
		assertEolCollectionsEqual(toEolCollection("foo", "bar"),
		                          new CollectionOfModelValues(dummyModel, firstMockModelValue, secondMockModelValue).unwrap());
		
		verify(firstMockModelValue, secondMockModelValue);
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void getEquivalentShouldDelegateToGetEquivalentOfEachElement() throws ConservativeCopyException {
		final Model               dummyMigratedModel = createMock(Model.class);
		final ConservativeCopyContext dummyContext       = createMock(ConservativeCopyContext.class);
		
		final BackedModelValue firstMockModelValue  = createMock(BackedModelValue.class);
		final BackedModelValue secondMockModelValue = createMock(BackedModelValue.class);

		
		// Expectations
		
		expect(firstMockModelValue.getEquivalentIn(dummyMigratedModel, dummyContext))
			.andReturn(new AttributeValue(dummyMigratedModel, "foo"));
		
		expect(secondMockModelValue.getEquivalentIn(dummyMigratedModel, dummyContext))
			.andReturn(new AttributeValue(dummyMigratedModel, "bar"));
		
		replay(dummyMigratedModel, dummyContext, firstMockModelValue, secondMockModelValue);
		
		
		// Verification
		
		assertEquals(new CollectionOfModelValues(dummyMigratedModel, new AttributeValue(dummyMigratedModel, "foo"), new AttributeValue(dummyMigratedModel, "bar")),
		             new CollectionOfModelValues(dummyModel, firstMockModelValue, secondMockModelValue).getEquivalentIn(dummyMigratedModel, dummyContext));
		
		verify(dummyMigratedModel, dummyContext, firstMockModelValue, secondMockModelValue);
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void getEquivalentShouldExcludeValuesThatUnwrapToNull() throws ConservativeCopyException {
		final Model               dummyMigratedModel = createMock(Model.class);
		final ConservativeCopyContext dummyContext       = createMock(ConservativeCopyContext.class);
		
		final BackedModelValue mockOriginalModelValue    = createMock("OriginalValue",   BackedModelValue.class);
		final BackedModelValue mockEquivalentModelValue  = createMock("EquivalentValue", BackedModelValue.class);

		
		// Expectations
		
		expect(mockOriginalModelValue.getEquivalentIn(dummyMigratedModel, dummyContext))
			.andReturn(mockEquivalentModelValue);
		
		expect(mockEquivalentModelValue.unwrap())
			.andReturn(null);
		
		replay(dummyMigratedModel, dummyContext, mockOriginalModelValue, mockEquivalentModelValue);
		
		
		// Verification
		
		assertEquals(new CollectionOfModelValues(dummyMigratedModel),
		             new CollectionOfModelValues(dummyModel, mockOriginalModelValue).getEquivalentIn(dummyMigratedModel, dummyContext));
		
		verify(dummyMigratedModel, dummyContext, mockOriginalModelValue, mockEquivalentModelValue);
	}
	
	
	private static void assertEolCollectionsEqual(Collection<?> expected, Collection<?> actual) {
		assertEquals(expected, actual);
	}
	
	private static Collection<Object> toEolCollection(Object... element) {
		return Arrays.asList(element);
	}
}
