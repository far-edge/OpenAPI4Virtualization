package ch.supsi.isteps.virtualfactory;

import org.junit.Before;

import ch.supsi.isteps.virtualfactory.openapi.businesslogic.DataModelSystemFactory;
import ch.supsi.isteps.virtualfactory.openapi.data.DataModelCommandData;
import ch.supsi.isteps.virtualfactory.openapi.persistence.InMemoryDataModelPersistence;
import ch.supsi.isteps.virtualfactory.tools.Fields;
import ch.supsi.isteps.virtualfactory.tools.data.ToolData;
import ch.supsi.isteps.virtualfactory.tools.dispenser.IncrementalDispenser;

public class DataModelSystemTest extends BaseTest{

	@Before
	public void setup() {
		IncrementalDispenser elementIdDispenser = new IncrementalDispenser(123);
		IncrementalDispenser assetsIdDispenser = new IncrementalDispenser(123);
		 _dataModel = DataModelSystemFactory.create(new InMemoryDataModelPersistence(elementIdDispenser, assetsIdDispenser));
		
		_dataModel.execute(Fields.single(ToolData.COMMAND_NAME, DataModelCommandData.CLEAR), Fields.empty());
	}

	
}