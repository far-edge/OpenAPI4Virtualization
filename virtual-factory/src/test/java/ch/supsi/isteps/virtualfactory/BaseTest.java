package ch.supsi.isteps.virtualfactory;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ch.supsi.isteps.virtualfactory.openapi.data.DataModelCommandData;
import ch.supsi.isteps.virtualfactory.realtodigitalsync.data.RealToDigitalSyncData;
import ch.supsi.isteps.virtualfactory.tools.Fields;
import ch.supsi.isteps.virtualfactory.tools.XSystem;
import ch.supsi.isteps.virtualfactory.tools.data.ToolData;

public class BaseTest {

	protected XSystem _dataModel;

	// LAYER
		@Test
		public void saveLayer() {
			Fields saveLayerInput = Fields.single(ToolData.COMMAND_NAME, DataModelCommandData.SAVE_LAYER);
			saveLayerInput.put(RealToDigitalSyncData.LAYER_NAME, "LogicalLayer");
			saveLayerInput.put(RealToDigitalSyncData.LAYER_DESCRIPTION, "Warehouse");
			Fields saveLayerOutput = Fields.empty();
			_dataModel.execute(saveLayerInput, saveLayerOutput);
			assertEquals("outcome=true", saveLayerOutput.toRaw());

			Fields retrieveLayerByNameInput = Fields.single(ToolData.COMMAND_NAME,
					DataModelCommandData.RETRIEVE_LAYER_BY_NAME);
			retrieveLayerByNameInput.put(RealToDigitalSyncData.LAYER_NAME, "LogicalLayer");
			Fields retrieveLayerByNameOutput = Fields.empty();
			_dataModel.execute(retrieveLayerByNameInput, retrieveLayerByNameOutput);
			assertEquals("outcome=true|layerName=LogicalLayer|layerDescription=Warehouse",
					retrieveLayerByNameOutput.toRaw());
		}

		@Test
		public void removeLayer() {
			saveLayer();
			Fields removeLayerInput = Fields.single(ToolData.COMMAND_NAME, DataModelCommandData.REMOVE_LAYER_BY_NAME);
			removeLayerInput.put(RealToDigitalSyncData.LAYER_NAME, "LogicalLayer");
			Fields removeLayerOutput = Fields.empty();
			_dataModel.execute(removeLayerInput, removeLayerOutput);
			assertEquals("outcome=true", removeLayerOutput.toRaw());

			Fields retrieveLayerByNameInput = Fields.single(ToolData.COMMAND_NAME,
					DataModelCommandData.RETRIEVE_LAYER_BY_NAME);
			retrieveLayerByNameInput.put(RealToDigitalSyncData.LAYER_NAME, "LogicalLayer");
			Fields retrieveLayerByNameOutput = Fields.empty();
			_dataModel.execute(retrieveLayerByNameInput, retrieveLayerByNameOutput);
			assertEquals("outcome=true", retrieveLayerByNameOutput.toRaw());
		}

		// ELEMENT
		@Test
		public void saveElement() {
			Fields saveElementOutput = saveElement("Bay", "Bay1");
			assertEquals("outcome=true|id=124", saveElementOutput.toRaw());

			Fields retrieveElementInput = Fields.single(ToolData.COMMAND_NAME,
					DataModelCommandData.RETRIEVE_ELEMENT_BY_NAME);
			retrieveElementInput.put(RealToDigitalSyncData.ELEMENT_NAME, "Bay1");
			Fields retrieveElementOutput = Fields.empty();
			_dataModel.execute(retrieveElementInput, retrieveElementOutput);
			assertEquals("outcome=true|id=124|layerName=LogicalLayer|archetype=Bay|elementName=Bay1",
					retrieveElementOutput.toRaw());
		}

		@Test
		public void retrieveElementByName() {
			saveElement("Bay", "Bay1"); // id = 124
			saveElement("Conveyor", "Conveyor1"); // id = 125

			Fields retrieveElementByNameInput = Fields.single(ToolData.COMMAND_NAME, DataModelCommandData.RETRIEVE_ELEMENT_BY_NAME);
			retrieveElementByNameInput.put(RealToDigitalSyncData.ELEMENT_NAME, "Bay1");
			Fields removeElementOutput = Fields.empty();
			_dataModel.execute(retrieveElementByNameInput, removeElementOutput);
			assertEquals("outcome=true", removeElementOutput.toRaw());

			Fields retrieveElementInput = Fields.single(ToolData.COMMAND_NAME, DataModelCommandData.RETRIEVE_ELEMENT_BY_NAME);
			retrieveElementInput.put(RealToDigitalSyncData.ELEMENT_NAME, "Bay1");
			Fields retrieveElementByNameOutput = Fields.empty();
			_dataModel.execute(retrieveElementInput, retrieveElementByNameOutput);
			assertEquals("outcome=true|id=125|layerName=LogicalLayer|archetype=Conveyor|elementName=Conveyor1", retrieveElementByNameOutput.toRaw());
		}

		@Test
		public void removeElementByName() {
			saveElement("Bay", "Bay1");

			Fields removeElementInput = Fields.single(ToolData.COMMAND_NAME, DataModelCommandData.REMOVE_ELEMENT_BY_NAME);
			removeElementInput.put(RealToDigitalSyncData.ELEMENT_NAME, "Bay1");
			Fields removeElementOutput = Fields.empty();
			_dataModel.execute(removeElementInput, removeElementOutput);
			assertEquals("outcome=true", removeElementOutput.toRaw());

			Fields retrieveElementInput = Fields.single(ToolData.COMMAND_NAME,
					DataModelCommandData.RETRIEVE_ELEMENT_BY_NAME);
			retrieveElementInput.put(RealToDigitalSyncData.ELEMENT_NAME, "Bay1");
			Fields retrieveElementOutput = Fields.empty();
			_dataModel.execute(retrieveElementInput, retrieveElementOutput);
			assertEquals("outcome=true", retrieveElementOutput.toRaw());
		}

		@Test
		public void retrieveAllElementsByLayer() {
			saveElement("Bay", "Bay1");
			saveElement("Bay", "Bay2");

			Fields retrieveAllElementsByLayerInput = Fields.single(ToolData.COMMAND_NAME,
					DataModelCommandData.RETRIEVE_ALL_ELEMENTS_BY_LAYER);
			retrieveAllElementsByLayerInput.put(RealToDigitalSyncData.LAYER_NAME, "LogicalLayer");
			Fields retrieveAllElementsByLayerOutput = Fields.empty();
			_dataModel.execute(retrieveAllElementsByLayerInput, retrieveAllElementsByLayerOutput);
			assertEquals(
					"outcome=true|id.1=124|id.2=125|layerName.1=LogicalLayer|layerName.2=LogicalLayer|archetype.1=Bay|archetype.2=Bay|elementName.1=Bay1|elementName.2=Bay2",
					retrieveAllElementsByLayerOutput.toRaw());
		}

		@Test
		public void retrieveAllElementsByArchetype() {
			saveElement("Bay", "Bay1");
			saveElement("Bay", "Bay2");
			saveElement("EOL", "Bay3");

			Fields retrieveAllElementsByLayerInput = Fields.single(ToolData.COMMAND_NAME,
					DataModelCommandData.RETRIEVE_ALL_ELEMENTS_BY_ARCHETYPE);
			retrieveAllElementsByLayerInput.put(RealToDigitalSyncData.ARCHETYPE, "EOL");
			Fields retrieveAllElementsByLayerOutput = Fields.empty();
			_dataModel.execute(retrieveAllElementsByLayerInput, retrieveAllElementsByLayerOutput);
			assertEquals("outcome=true|id=126|layerName=LogicalLayer|archetype=EOL|elementName=Bay3",
					retrieveAllElementsByLayerOutput.toRaw());
		}

		// ATTRIBUTE
		@Test
		public void saveAttribute() {
			Fields saveAttributeOutput = saveAttribute("Position", "1");
			assertEquals("outcome=true", saveAttributeOutput.toRaw());

			Fields retrieveElementInput = Fields.single(ToolData.COMMAND_NAME,
					DataModelCommandData.RETRIEVE_ATTRIBUTE_BY_KEY);
			retrieveElementInput.put(RealToDigitalSyncData.KEY, "Position");
			Fields retrieveElementOutput = Fields.empty();
			_dataModel.execute(retrieveElementInput, retrieveElementOutput);
			assertEquals("outcome=true|elementName=Bay1|Position=1", retrieveElementOutput.toRaw());
		}

		@Test
		public void removeAttributeByKey() {
			saveAttribute("Position", "1");
			saveAttribute("Status", "ACTIVE");

			Fields removeElementInput = Fields.single(ToolData.COMMAND_NAME, DataModelCommandData.REMOVE_ATTRIBUTE_BY_KEY);
			removeElementInput.put(RealToDigitalSyncData.KEY, "Position");
			Fields removeElementOutput = Fields.empty();
			_dataModel.execute(removeElementInput, removeElementOutput);
			assertEquals("outcome=true", removeElementOutput.toRaw());

			Fields retrieveElementInput = Fields.single(ToolData.COMMAND_NAME,
					DataModelCommandData.RETRIEVE_ATTRIBUTE_BY_KEY);
			retrieveElementInput.put(RealToDigitalSyncData.KEY, "Position");
			Fields retrieveElementOutput = Fields.empty();
			_dataModel.execute(retrieveElementInput, retrieveElementOutput);
			assertEquals("outcome=true|elementName=Bay1|Position=1", retrieveElementOutput.toRaw());

			retrieveElementInput = Fields.single(ToolData.COMMAND_NAME, DataModelCommandData.RETRIEVE_ATTRIBUTE_BY_KEY);
			retrieveElementInput.put(RealToDigitalSyncData.KEY, "Status");
			retrieveElementOutput = Fields.empty();
			_dataModel.execute(retrieveElementInput, retrieveElementOutput);
			assertEquals("outcome=true|elementName=Bay1|Status=ACTIVE", retrieveElementOutput.toRaw());
		}

		@Test
		public void retrieveAllAttributesByElement() {
			saveAttribute("Position", "1");
			saveAttribute("Status", "ACTIVE");

			Fields retrieveElementInput = Fields.single(ToolData.COMMAND_NAME,
					DataModelCommandData.RETRIEVE_ALL_ATTRIBUTES_BY_ELEMENT_NAME);
			retrieveElementInput.put(RealToDigitalSyncData.ELEMENT_NAME, "Bay1");
			Fields retrieveElementOutput = Fields.empty();
			_dataModel.execute(retrieveElementInput, retrieveElementOutput);
			assertEquals("outcome=true|elementName=Bay1|Position=1|Status=ACTIVE", retrieveElementOutput.toRaw());
		}

		@Test
		public void removeAllAttributesByElementName() {
			saveAttribute("Position", "1");
			saveAttribute("Status", "ACTIVE");

			Fields removeElementInput = Fields.single(ToolData.COMMAND_NAME,
					DataModelCommandData.REMOVE_ALL_ATTRIBUTES_BY_ELEMENT_NAME);
			removeElementInput.put(RealToDigitalSyncData.ELEMENT_NAME, "Bay1");
			Fields removeElementOutput = Fields.empty();
			_dataModel.execute(removeElementInput, removeElementOutput);
			assertEquals("outcome=true", removeElementOutput.toRaw());

			Fields retrieveElementInput = Fields.single(ToolData.COMMAND_NAME,
					DataModelCommandData.RETRIEVE_ALL_ATTRIBUTES_BY_ELEMENT_NAME);
			retrieveElementInput.put(RealToDigitalSyncData.ELEMENT_NAME, "Bay1");
			Fields retrieveElementOutput = Fields.empty();
			_dataModel.execute(retrieveElementInput, retrieveElementOutput);
			assertEquals("outcome=true|elementName=Bay1", retrieveElementOutput.toRaw());
		}

		// LINK
		@Test
		public void saveLink() {
			Fields saveLinkInput = Fields.single(ToolData.COMMAND_NAME, DataModelCommandData.SAVE_LINK);
			saveLinkInput.put(RealToDigitalSyncData.FROM_ELEMENT, "Sorter");
			saveLinkInput.put(RealToDigitalSyncData.TO_ELEMENT, "Plant");
			saveLinkInput.put(RealToDigitalSyncData.LINK_TYPE, "isContained");
			Fields saveLinkOutput = Fields.empty();
			_dataModel.execute(saveLinkInput, saveLinkOutput);
			assertEquals("outcome=true", saveLinkOutput.toRaw());

			Fields retrieveLinkByFromInput = Fields.single(ToolData.COMMAND_NAME,
					DataModelCommandData.RETRIEVE_LINK_BY_SOURCE_ELEMENT);
			retrieveLinkByFromInput.put(RealToDigitalSyncData.FROM_ELEMENT, "Sorter");
			Fields retrieveLinkByFromOutput = Fields.empty();
			_dataModel.execute(retrieveLinkByFromInput, retrieveLinkByFromOutput);
			assertEquals("outcome=true|fromElement=Sorter|toElement=Plant|linkType=isContained",
					retrieveLinkByFromOutput.toRaw());
		}

		@Test
		public void removeLink() {
			saveLink();

			Fields removeLinkInput = Fields.single(ToolData.COMMAND_NAME,
					DataModelCommandData.REMOVE_LINK_BY_SOURCE_ELEMENT);
			removeLinkInput.put(RealToDigitalSyncData.FROM_ELEMENT, "Sorter");
			Fields removeLinkOutput = Fields.empty();
			_dataModel.execute(removeLinkInput, removeLinkOutput);
			assertEquals("outcome=true", removeLinkOutput.toRaw());

			Fields retrieveLinkInput = Fields.single(ToolData.COMMAND_NAME,
					DataModelCommandData.RETRIEVE_LINK_BY_SOURCE_ELEMENT);
			retrieveLinkInput.put(RealToDigitalSyncData.FROM_ELEMENT, "Sorter");
			Fields retrieveLinkOutput = Fields.empty();
			_dataModel.execute(retrieveLinkInput, retrieveLinkOutput);
			assertEquals("outcome=true|fromElement=Sorter", retrieveLinkOutput.toRaw());
		}

		// ASSETS
		@Test
		public void saveAsset() {
			Fields saveAssetInput = Fields.single(ToolData.COMMAND_NAME, DataModelCommandData.SAVE_ASSET);
			saveAssetInput.put(RealToDigitalSyncData.ASSET_FILE_NAME, "whirlpool.jar");
			saveAssetInput.put(RealToDigitalSyncData.ASSET_FILE_PATH, "http://localhost:8080/whirlpool/save");
			Fields saveAssetOutput = Fields.empty();
			_dataModel.execute(saveAssetInput, saveAssetOutput);
			assertEquals("outcome=true|id=124", saveAssetOutput.toRaw());

			Fields retrieveAssetDetailsInput = Fields.single(ToolData.COMMAND_NAME,
					DataModelCommandData.RETRIEVE_ASSET_BY_FILE_NAME);
			retrieveAssetDetailsInput.put(RealToDigitalSyncData.ASSET_FILE_NAME, "whirlpool.jar");
			Fields retrieveAssetDetailsOutput = Fields.empty();
			_dataModel.execute(retrieveAssetDetailsInput, retrieveAssetDetailsOutput);
			assertEquals("outcome=true|assetFileName=whirlpool.jar|assetFilePath=http://localhost:8080/whirlpool/save",
					retrieveAssetDetailsOutput.toRaw());
		}

		@Test
		public void removeAssetByFileName() {
			Fields saveAssetOutput = saveAsset("whirlpool.jar", "http://localhost:8080/whirlpool/save");
			assertEquals("outcome=true|id=124", saveAssetOutput.toRaw());

			Fields removeAssetDetailsInput = Fields.single(ToolData.COMMAND_NAME,
					DataModelCommandData.REMOVE_ASSET_BY_FILE_NAME);
			removeAssetDetailsInput.put(RealToDigitalSyncData.ASSET_FILE_NAME, "whirlpool.jar");
			Fields removeAssetDetailsOutput = Fields.empty();
			_dataModel.execute(removeAssetDetailsInput, removeAssetDetailsOutput);

			Fields retrieveAssetDetailsInput = Fields.single(ToolData.COMMAND_NAME,
					DataModelCommandData.RETRIEVE_ASSET_BY_FILE_NAME);
			retrieveAssetDetailsInput.put(RealToDigitalSyncData.ASSET_FILE_NAME, "whirlpool.jar");
			Fields retrieveAssetDetailsOutput = Fields.empty();
			_dataModel.execute(retrieveAssetDetailsInput, retrieveAssetDetailsOutput);
			assertEquals("outcome=true", retrieveAssetDetailsOutput.toRaw());
		}

		@Test
		public void retrieveAllAssets() {
			saveAsset("whirlpool.jar", "http://localhost:8080/whirlpool/save");
			saveAsset("whirlpool-123.jar", "http://localhost:8080/whirlpool/control");

			Fields retrieveAllAssetInput = Fields.single(ToolData.COMMAND_NAME, DataModelCommandData.RETRIEVE_ALL_ASSETS);
			Fields retrieveAllAssetOutput = Fields.empty();
			_dataModel.execute(retrieveAllAssetInput, retrieveAllAssetOutput);
			assertEquals(
					"outcome=true|assetFileName.1=whirlpool.jar|assetFileName.2=whirlpool-123.jar|assetFilePath.1=http://localhost:8080/whirlpool/save|assetFilePath.2=http://localhost:8080/whirlpool/control",
					retrieveAllAssetOutput.toRaw());
		}

		// FIXTURE
		private Fields saveElement(String anArchetype, String anElementName) {
			Fields saveElementInput = Fields.single(ToolData.COMMAND_NAME, DataModelCommandData.SAVE_ELEMENT);
			saveElementInput.put(RealToDigitalSyncData.LAYER_NAME, "LogicalLayer");
			saveElementInput.put(RealToDigitalSyncData.ARCHETYPE, anArchetype);
			saveElementInput.put(RealToDigitalSyncData.ELEMENT_NAME, anElementName);
			Fields saveElementOutput = Fields.empty();
			_dataModel.execute(saveElementInput, saveElementOutput);
			return saveElementOutput;
		}

		private Fields saveAttribute(String aKey, String aValue) {
			Fields saveAttributeInput = Fields.single(ToolData.COMMAND_NAME, DataModelCommandData.SAVE_ATTRIBUTE);
			saveAttributeInput.put(RealToDigitalSyncData.ELEMENT_NAME, "Bay1");
			saveAttributeInput.put(RealToDigitalSyncData.KEY, aKey);
			saveAttributeInput.put(RealToDigitalSyncData.VALUE, aValue);
			Fields saveAttributeOutput = Fields.empty();
			_dataModel.execute(saveAttributeInput, saveAttributeOutput);
			return saveAttributeOutput;
		}

		private Fields saveAsset(String aFileName, String aFilePath) {
			Fields saveAssetInput = Fields.single(ToolData.COMMAND_NAME, DataModelCommandData.SAVE_ASSET);
			saveAssetInput.put(RealToDigitalSyncData.ASSET_FILE_NAME, aFileName);
			saveAssetInput.put(RealToDigitalSyncData.ASSET_FILE_PATH, aFilePath);
			Fields saveAssetOutput = Fields.empty();
			_dataModel.execute(saveAssetInput, saveAssetOutput);
			return saveAssetOutput;
		}
}
