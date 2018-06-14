package ch.supsi.isteps.virtualfactory.triggers;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import ch.supsi.isteps.virtualfactory.openapi.businesslogic.DataModelSystemFactory;
import ch.supsi.isteps.virtualfactory.openapi.data.DataModelCommandData;
import ch.supsi.isteps.virtualfactory.openapi.persistence.InMemoryDataModelPersistence;
import ch.supsi.isteps.virtualfactory.realtodigitalsync.data.RealToDigitalSyncData;
import ch.supsi.isteps.virtualfactory.tools.Fields;
import ch.supsi.isteps.virtualfactory.tools.XMap;
import ch.supsi.isteps.virtualfactory.tools.XSystem;
import ch.supsi.isteps.virtualfactory.tools.command.AbstractCommand;
import ch.supsi.isteps.virtualfactory.tools.command.UnknownCommand;
import ch.supsi.isteps.virtualfactory.tools.data.ToolData;
import ch.supsi.isteps.virtualfactory.tools.dispenser.IncrementalDispenser;

public class SimulationTriggerTest {

	private XSystem _triggerSystem;
	private XSystem _dataModel;
	private XSystem _originalDataModel;

	@Before
	public void setup() {
		IncrementalDispenser elementIdDispenser = new IncrementalDispenser(123);
		IncrementalDispenser assetsIdDispenser = new IncrementalDispenser(123);
		_dataModel = DataModelSystemFactory.create(new InMemoryDataModelPersistence(elementIdDispenser, assetsIdDispenser));
		_originalDataModel = DataModelSystemFactory.create(new InMemoryDataModelPersistence(elementIdDispenser, assetsIdDispenser));
		_dataModel.execute(Fields.single(ToolData.COMMAND_NAME, DataModelCommandData.CLEAR), Fields.empty());
		XMap<XSystem> proxies = new XMap<XSystem>(XSystem.single(new UnknownCommand("response")));
		_triggerSystem = TriggerSystemFactory.createUsing(new RemoteProxyFactory(XSystem.single(new AbstractCommand() {
			@Override
			public void execute(Fields anInput, Fields anOutput) {
				anOutput.put("called", "yes");
			}
		})), proxies, _dataModel, _originalDataModel);
	}
	
	@Test
	public void registerSaveElementTrigger() {
		Fields firstOutput = Fields.empty();
		_triggerSystem.execute(Fields.single(ToolData.COMMAND_NAME, TriggerCommandData.REGISTER_TRIGGER).put("called","onArchetypeSave").put("onEvent", "saveElement").put("checkKeys", "archetype").put("archetype", "BOL").put("callback", "http://localhost:8090/callback"), firstOutput);
		assertEquals("outcome=true|message=trigger registered", firstOutput.toRaw());
		
		Fields secondOutput = Fields.empty();
		_triggerSystem.execute(Fields.single(ToolData.COMMAND_NAME, TriggerCommandData.APPLY_TRIGGER).put("called", "onArchetypeSave"), secondOutput);
		assertEquals("outcome=true", secondOutput.toRaw());
		
		Fields saveElementInput = Fields.single(ToolData.COMMAND_NAME, DataModelCommandData.SAVE_ELEMENT);
		saveElementInput.put(RealToDigitalSyncData.LAYER_NAME, "LogicalLayer");
		saveElementInput.put(RealToDigitalSyncData.ARCHETYPE, "BOL");
		saveElementInput.put(RealToDigitalSyncData.ELEMENT_NAME, "BOL-1");

		Fields saveElementOutput = Fields.empty();
		_dataModel.execute(saveElementInput, saveElementOutput);
		assertEquals("outcome=true|id=124|called=yes", saveElementOutput.select("outcome", "id", "called").toRaw());
		
		_triggerSystem.execute(Fields.single(ToolData.COMMAND_NAME, TriggerCommandData.RESET_TRIGGER).put("called","onArchetypeSave"), Fields.empty());
		
		saveElementOutput.clear();
		_dataModel.execute(saveElementInput, saveElementOutput);
		assertEquals("outcome=true|id=125", saveElementOutput.select("outcome", "id", "called").toRaw());
	}
}