/*********************************************************************
* Copyright (c) 2008 The University of York.
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
**********************************************************************/
package org.eclipse.epsilon.emc.spreadsheets.propertysetter;

import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.eclipse.epsilon.emc.spreadsheets.SpreadsheetColumn;
import org.eclipse.epsilon.emc.spreadsheets.SpreadsheetModel;
import org.eclipse.epsilon.emc.spreadsheets.SpreadsheetPropertySetter;
import org.eclipse.epsilon.emc.spreadsheets.SpreadsheetRow;
import org.eclipse.epsilon.emc.spreadsheets.test.SharedTestMethods;
import org.eclipse.epsilon.emc.spreadsheets.test.TestModelFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class CascadeUpdatesCellsManyToNotManyPropertySetterTest
{
	private SpreadsheetModel model = null;

	public CascadeUpdatesCellsManyToNotManyPropertySetterTest(SpreadsheetModel model)
	{
		this.model = model;
	}

	@Parameterized.Parameters
	public static Collection<Object[]> models() throws Exception
	{
		String PATH_TO_FILE = "resources/propertysetter/PropertySetterTest.xlsx";
		String CONFIG = "resources/propertysetter/CascadeUpdatesCellsManyToNotManyPropertySetterTest.xml";
		return TestModelFactory.getModelsToTest("", PATH_TO_FILE, CONFIG, "PropertySetterTest");
	}

	@Test
	public void testCellsManyToNotMany() throws Exception
	{
		SharedTestMethods.clearWorksheet(model, "Sheet1");
		SharedTestMethods.clearWorksheet(model, "Sheet2");
		SharedTestMethods.clearWorksheet(model, "Sheet3");

		String col = "c_2";

		SpreadsheetColumn columnSheet1 = model.getWorksheetByType("Sheet1").getColumn(col);
		SpreadsheetColumn columnSheet2 = model.getWorksheetByType("Sheet2").getColumn(col);
		SpreadsheetColumn columnSheet3 = model.getWorksheetByType("Sheet3").getColumn(col);

		SpreadsheetRow rowSheet3 = SharedTestMethods.writeRow(model, "Sheet3", "c_1", "1", col, "REQ1, REQ2");
		assertTrue(rowSheet3.getVisibleCellValue(columnSheet3).equals("REQ1, REQ2"));
		assertTrue(rowSheet3.getAllVisibleCellValuesAsIs(columnSheet3).size() == 1);
		assertTrue(rowSheet3.getVisibleCellValue(model.getWorksheetByType("Sheet3").getColumn("c_1")).equals("1"));

		SpreadsheetRow rowSheet3_req1 = SharedTestMethods.writeRow(model, "Sheet3", "c_1", "2", col, "REQ1");
		assertTrue(rowSheet3_req1.getVisibleCellValue(columnSheet3).equals("REQ1"));
		assertTrue(rowSheet3_req1.getAllVisibleCellValuesAsIs(columnSheet3).size() == 1);
		assertTrue(rowSheet3_req1.getVisibleCellValue(model.getWorksheetByType("Sheet3").getColumn("c_1")).equals("2"));

		SpreadsheetRow rowSheet3_req2 = SharedTestMethods.writeRow(model, "Sheet3", "c_1", "3", col, "REQ2");
		assertTrue(rowSheet3_req2.getVisibleCellValue(columnSheet3).equals("REQ2"));
		assertTrue(rowSheet3_req2.getAllVisibleCellValuesAsIs(columnSheet3).size() == 1);
		assertTrue(rowSheet3_req2.getVisibleCellValue(model.getWorksheetByType("Sheet3").getColumn("c_1")).equals("3"));

		SpreadsheetRow rowSheet3_req3 = SharedTestMethods.writeRow(model, "Sheet3", "c_1", "4", col, "REQ3");
		assertTrue(rowSheet3_req3.getVisibleCellValue(columnSheet3).equals("REQ3"));
		assertTrue(rowSheet3_req3.getAllVisibleCellValuesAsIs(columnSheet3).size() == 1);
		assertTrue(rowSheet3_req3.getVisibleCellValue(model.getWorksheetByType("Sheet3").getColumn("c_1")).equals("4"));

		SpreadsheetRow rowSheet2 = SharedTestMethods.writeRow(model, "Sheet2", "c_1", "1", col, rowSheet3);
		assertTrue(rowSheet2.getVisibleCellValue(columnSheet2).equals("REQ1, REQ2"));
		assertTrue(rowSheet2.getAllVisibleCellValuesAsIs(columnSheet2).size() == 2);
		assertTrue(rowSheet2.getVisibleCellValue(model.getWorksheetByType("Sheet2").getColumn("c_1")).equals("1"));

		assertTrue(model.getAllOfType("Sheet3").size() == 4);
		model.getWorksheetByType("Sheet3").removeRow(rowSheet3);
		assertTrue(model.getAllOfType("Sheet3").size() == 3);

		SpreadsheetRow rowSheet1 = SharedTestMethods.writeRow(model, "Sheet1", "c_1", "1", col, rowSheet2);
		assertTrue(rowSheet1.getVisibleCellValue(columnSheet1).equals("REQ1, REQ2"));
		assertTrue(rowSheet1.getAllVisibleCellValuesAsIs(columnSheet1).size() == 2);
		assertTrue(rowSheet1.getVisibleCellValue(model.getWorksheetByType("Sheet1").getColumn("c_1")).equals("1"));

		final SpreadsheetPropertySetter setter = new SpreadsheetPropertySetter(model);
		setter.setObject(rowSheet3_req2);
		setter.setProperty(columnSheet3.getPrefixedIndex());
		setter.invoke("REQ4");

		assertTrue(rowSheet3_req1.getVisibleCellValue(columnSheet3).equals("REQ1"));
		assertTrue(rowSheet3_req2.getVisibleCellValue(columnSheet3).equals("REQ4"));
		assertTrue(rowSheet3_req3.getVisibleCellValue(columnSheet3).equals("REQ3"));
		assertTrue(rowSheet2.getAllVisibleCellValuesAsIs(columnSheet2).size() == 2);
		assertTrue(rowSheet2.getAllVisibleCellValuesAsIs(columnSheet2).contains("REQ1"));
		assertTrue(rowSheet2.getAllVisibleCellValuesAsIs(columnSheet2).contains("REQ4"));
		assertTrue(rowSheet2.getVisibleCellValue(columnSheet2).equals("REQ1,REQ4"));
		assertTrue(rowSheet1.getVisibleCellValue(columnSheet1).equals("REQ1, REQ2"));
	}

	@Test
	public void testCellsManyToNotMany_CascadeToSecondLevel() throws Exception
	{
		SharedTestMethods.clearWorksheet(model, "Sheet1");
		SharedTestMethods.clearWorksheet(model, "Sheet2");
		SharedTestMethods.clearWorksheet(model, "Sheet3");

		String col = "c_4";

		SpreadsheetColumn columnSheet1 = model.getWorksheetByType("Sheet1").getColumn(col);
		SpreadsheetColumn columnSheet2 = model.getWorksheetByType("Sheet2").getColumn(col);
		SpreadsheetColumn columnSheet3 = model.getWorksheetByType("Sheet3").getColumn(col);

		SpreadsheetRow rowSheet3 = SharedTestMethods.writeRow(model, "Sheet3", "c_1", "1", col, "REQ1, REQ2");
		assertTrue(rowSheet3.getVisibleCellValue(columnSheet3).equals("REQ1, REQ2"));
		assertTrue(rowSheet3.getAllVisibleCellValuesAsIs(columnSheet3).size() == 1);
		assertTrue(rowSheet3.getVisibleCellValue(model.getWorksheetByType("Sheet3").getColumn("c_1")).equals("1"));

		SpreadsheetRow rowSheet3_req1 = SharedTestMethods.writeRow(model, "Sheet3", "c_1", "2", col, "REQ1");
		assertTrue(rowSheet3_req1.getVisibleCellValue(columnSheet3).equals("REQ1"));
		assertTrue(rowSheet3_req1.getAllVisibleCellValuesAsIs(columnSheet3).size() == 1);
		assertTrue(rowSheet3_req1.getVisibleCellValue(model.getWorksheetByType("Sheet3").getColumn("c_1")).equals("2"));

		SpreadsheetRow rowSheet3_req2 = SharedTestMethods.writeRow(model, "Sheet3", "c_1", "3", col, "REQ2");
		assertTrue(rowSheet3_req2.getVisibleCellValue(columnSheet3).equals("REQ2"));
		assertTrue(rowSheet3_req2.getAllVisibleCellValuesAsIs(columnSheet3).size() == 1);
		assertTrue(rowSheet3_req2.getVisibleCellValue(model.getWorksheetByType("Sheet3").getColumn("c_1")).equals("3"));

		SpreadsheetRow rowSheet3_req3 = SharedTestMethods.writeRow(model, "Sheet3", "c_1", "4", col, "REQ3");
		assertTrue(rowSheet3_req3.getVisibleCellValue(columnSheet3).equals("REQ3"));
		assertTrue(rowSheet3_req3.getAllVisibleCellValuesAsIs(columnSheet3).size() == 1);
		assertTrue(rowSheet3_req3.getVisibleCellValue(model.getWorksheetByType("Sheet3").getColumn("c_1")).equals("4"));

		SpreadsheetRow rowSheet2 = SharedTestMethods.writeRow(model, "Sheet2", "c_1", "1", col, rowSheet3);
		assertTrue(rowSheet2.getVisibleCellValue(columnSheet2).equals("REQ1, REQ2"));
		assertTrue(rowSheet2.getAllVisibleCellValuesAsIs(columnSheet2).size() == 2);
		assertTrue(rowSheet2.getVisibleCellValue(model.getWorksheetByType("Sheet2").getColumn("c_1")).equals("1"));

		assertTrue(model.getAllOfType("Sheet3").size() == 4);
		model.getWorksheetByType("Sheet3").removeRow(rowSheet3);
		assertTrue(model.getAllOfType("Sheet3").size() == 3);

		SpreadsheetRow rowSheet1 = SharedTestMethods.writeRow(model, "Sheet1", "c_1", "1", col, rowSheet2);
		assertTrue(rowSheet1.getVisibleCellValue(columnSheet1).equals("REQ1, REQ2"));
		assertTrue(rowSheet1.getAllVisibleCellValuesAsIs(columnSheet1).size() == 2);
		assertTrue(rowSheet1.getVisibleCellValue(model.getWorksheetByType("Sheet1").getColumn("c_1")).equals("1"));

		final SpreadsheetPropertySetter setter = new SpreadsheetPropertySetter(model);
		setter.setObject(rowSheet3_req2);
		setter.setProperty(columnSheet3.getPrefixedIndex());
		setter.invoke("REQ4");

		assertTrue(rowSheet3_req1.getAllVisibleCellValuesAsIs(columnSheet3).size() == 1);
		assertTrue(rowSheet3_req1.getVisibleCellValue(columnSheet3).equals("REQ1"));
		assertTrue(rowSheet3_req2.getAllVisibleCellValuesAsIs(columnSheet3).size() == 1);
		assertTrue(rowSheet3_req2.getVisibleCellValue(columnSheet3).equals("REQ4"));
		assertTrue(rowSheet2.getAllVisibleCellValuesAsIs(columnSheet2).size() == 2);
		assertTrue(rowSheet2.getVisibleCellValue(columnSheet2).equals("REQ1,REQ4"));
		assertTrue(rowSheet1.getAllVisibleCellValuesAsIs(columnSheet1).size() == 1); // many to many between column
																						// sheets
		assertTrue(rowSheet1.getVisibleCellValue(columnSheet1).equals("REQ1"));
	}

}
