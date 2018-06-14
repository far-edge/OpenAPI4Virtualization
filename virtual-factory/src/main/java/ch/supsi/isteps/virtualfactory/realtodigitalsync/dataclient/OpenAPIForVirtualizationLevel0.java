package ch.supsi.isteps.virtualfactory.realtodigitalsync.dataclient;

import ch.supsi.isteps.prototype.tools.AbstractOpenAPIForVirtualization;
import ch.supsi.isteps.virtualfactory.openapi.data.DataModelCommandData;
import ch.supsi.isteps.virtualfactory.openapi.responseformatter.AbstractResponseFormatter;
import ch.supsi.isteps.virtualfactory.realtodigitalsync.data.RealToDigitalSyncData;
import ch.supsi.isteps.virtualfactory.tools.Fields;
import ch.supsi.isteps.virtualfactory.tools.XSystem;
import ch.supsi.isteps.virtualfactory.tools.data.ToolData;

public class OpenAPIForVirtualizationLevel0 extends AbstractOpenAPIForVirtualization{

	private XSystem _dataModelSystem;
	private AbstractResponseFormatter _responseFormatter;

	public OpenAPIForVirtualizationLevel0(XSystem dataModelSystem, AbstractResponseFormatter responseFormatter) {
		_dataModelSystem = dataModelSystem;
		_responseFormatter = responseFormatter;
	}

	@Override
	public String saveElement(String aLayerName, String anArchetype, String anElementName) {
		Fields saveElementInput = Fields.single(ToolData.COMMAND_NAME, DataModelCommandData.SAVE_ELEMENT);
		saveElementInput.put(RealToDigitalSyncData.LAYER_NAME, aLayerName);
		saveElementInput.put(RealToDigitalSyncData.ARCHETYPE, anArchetype);
		saveElementInput.put(RealToDigitalSyncData.ELEMENT_NAME, anElementName);
		Fields saveElementOutput = Fields.empty();
		_dataModelSystem.execute(saveElementInput, saveElementOutput);
		return saveElementOutput.firstValueFor(RealToDigitalSyncData.ID);
	}
	
	public String retrieveElementByName(String elementName) {
		Fields retrieveElementInput = Fields.single(ToolData.COMMAND_NAME, DataModelCommandData.RETRIEVE_ELEMENT_BY_NAME);
		retrieveElementInput.put(RealToDigitalSyncData.ELEMENT_NAME, elementName);
		Fields retrieveElementOutput = Fields.empty();
		_dataModelSystem.execute(retrieveElementInput, retrieveElementOutput);
		return _responseFormatter.format(retrieveElementOutput.rejectKeysStartingWith("outcome"));
	}
	
	@Override
	public String removeElementByName(String anElementName) {
		Fields removeElementInput = Fields.single(ToolData.COMMAND_NAME, DataModelCommandData.REMOVE_ELEMENT_BY_NAME);
		removeElementInput.put(RealToDigitalSyncData.ELEMENT_NAME, anElementName);
		Fields removeElementOutput = Fields.empty();
		_dataModelSystem.execute(removeElementInput, removeElementOutput);
		//return _responseFormatter.format(removeElementOutput.rejectKeysStartingWith("outcome"));
		return _responseFormatter.format(removeElementOutput);
	}
	
	public String retrieveAllElementsByLayer(String aLayerName) {
		Fields retrieveAllElementsByLayerInput = Fields.single(ToolData.COMMAND_NAME, DataModelCommandData.RETRIEVE_ALL_ELEMENTS_BY_LAYER);
		retrieveAllElementsByLayerInput.put(RealToDigitalSyncData.LAYER_NAME, aLayerName);
		Fields retrieveAllElementsByLayerOutput = Fields.empty();
		_dataModelSystem.execute(retrieveAllElementsByLayerInput, retrieveAllElementsByLayerOutput);
		return _responseFormatter.format(retrieveAllElementsByLayerOutput.rejectKeysStartingWith("outcome"));
	}
	
	public String retrieveAllElementsByArchetype(String anArchetype) {
		Fields retrieveAllElementsByArchetypeInput = Fields.single(ToolData.COMMAND_NAME, DataModelCommandData.RETRIEVE_ALL_ELEMENTS_BY_ARCHETYPE);
		retrieveAllElementsByArchetypeInput.put(RealToDigitalSyncData.ARCHETYPE, anArchetype);
		Fields retrieveAllElementsByArchetypeOutput = Fields.empty();
		_dataModelSystem.execute(retrieveAllElementsByArchetypeInput, retrieveAllElementsByArchetypeOutput);
		return _responseFormatter.format(retrieveAllElementsByArchetypeOutput.rejectKeysStartingWith("outcome"));
	}
	
	@Override
	public void saveAttribute(String layerName, String anElementKey, String aKey, String aValue) {
		Fields saveAttributeInput = Fields.single(ToolData.COMMAND_NAME, DataModelCommandData.SAVE_ATTRIBUTE);
		saveAttributeInput.put(RealToDigitalSyncData.ELEMENT_NAME, anElementKey);
		saveAttributeInput.put(RealToDigitalSyncData.LAYER_NAME, layerName);
		saveAttributeInput.put(RealToDigitalSyncData.KEY, aKey);
		saveAttributeInput.put(RealToDigitalSyncData.VALUE, aValue);
		_dataModelSystem.execute(saveAttributeInput, Fields.empty());
	}

	public String retrieveAttributesByElement(String aLayerName, String elementName) {
		Fields retrieveAttributesByElementInput = Fields.single(ToolData.COMMAND_NAME, DataModelCommandData.RETRIEVE_ALL_ATTRIBUTES_BY_ELEMENT_NAME);
		retrieveAttributesByElementInput.put(RealToDigitalSyncData.ELEMENT_NAME, elementName);
		retrieveAttributesByElementInput.put(RealToDigitalSyncData.LAYER_NAME, aLayerName);
		Fields retrieveAllAttributesOutput = Fields.empty();
		_dataModelSystem.execute(retrieveAttributesByElementInput, retrieveAllAttributesOutput);
		return retrieveAllAttributesOutput.rejectKeysStartingWith("outcome").toRaw();
	}
	
	public String removeAttribute(String anElementName, String aKey, String aValue) {
		Fields removeAttributeInput = Fields.single(ToolData.COMMAND_NAME, DataModelCommandData.REMOVE_ATTRIBUTE_OF_ELEMENT);
		removeAttributeInput.put(RealToDigitalSyncData.ELEMENT_NAME, anElementName);
		removeAttributeInput.put(RealToDigitalSyncData.KEY, aKey);
		Fields removeAttributeOutput = Fields.empty();
		_dataModelSystem.execute(removeAttributeInput, removeAttributeOutput);
		return _responseFormatter.format(removeAttributeOutput.rejectKeysStartingWith("outcome"));
	}
	
	public void saveLayer(String layerName, String layerDescription) {
		Fields saveLayerInput = Fields.single(ToolData.COMMAND_NAME, DataModelCommandData.SAVE_LAYER);
		saveLayerInput.put(RealToDigitalSyncData.LAYER_NAME, layerName);
		saveLayerInput.put(RealToDigitalSyncData.LAYER_DESCRIPTION, layerDescription);
		_dataModelSystem.execute(saveLayerInput, Fields.empty());
	}
	
	
	public void saveLink(String fromElement, String toElement, String linkType) {
		Fields saveLinkInput = Fields.single(ToolData.COMMAND_NAME, DataModelCommandData.SAVE_LINK);
		saveLinkInput.put(RealToDigitalSyncData.FROM_ELEMENT, fromElement);
		saveLinkInput.put(RealToDigitalSyncData.TO_ELEMENT, toElement);
		saveLinkInput.put(RealToDigitalSyncData.LINK_TYPE, linkType);
		_dataModelSystem.execute(saveLinkInput, Fields.empty());
	}
	
	public String retrieveLinkBySourceElement(String fromElement) {
		Fields retrieveLinkBySourceElementInput = Fields.single(ToolData.COMMAND_NAME, DataModelCommandData.RETRIEVE_LINK_BY_SOURCE_ELEMENT);
		retrieveLinkBySourceElementInput.put(RealToDigitalSyncData.FROM_ELEMENT, fromElement);
		Fields retrieveLinkBySourceElementOutput = Fields.empty();
		_dataModelSystem.execute(retrieveLinkBySourceElementInput, retrieveLinkBySourceElementOutput);
		return _responseFormatter.format(retrieveLinkBySourceElementOutput.rejectKeysStartingWith("outcome"));
	}
	
	public String retrieveLinkById(String id) {
		Fields retrieveLinkByIdInput = Fields.single(ToolData.COMMAND_NAME, DataModelCommandData.RETRIEVE_LINK_BY_ID);
		retrieveLinkByIdInput.put(RealToDigitalSyncData.ID, id);
		Fields retrieveLinkByIdOutput = Fields.empty();
		_dataModelSystem.execute(retrieveLinkByIdInput, retrieveLinkByIdOutput);
		return _responseFormatter.format(retrieveLinkByIdOutput.rejectKeysStartingWith("outcome"));
	}

	public String saveAsset(String aFilePath) {
		Fields saveAssetInput = Fields.single(ToolData.COMMAND_NAME, DataModelCommandData.SAVE_ASSET);
		saveAssetInput.put(RealToDigitalSyncData.ASSET_FILE_NAME, aFilePath.split("/")[4]);
		saveAssetInput.put(RealToDigitalSyncData.ASSET_FILE_PATH, aFilePath);
		Fields saveAssetOutput = Fields.empty();
		_dataModelSystem.execute(saveAssetInput, saveAssetOutput);
		return _responseFormatter.format(saveAssetOutput.rejectKeysStartingWith("outcome"));
	}

	public String retrieveAllAssets() {
		Fields retrieveAllAssetInput = Fields.single(ToolData.COMMAND_NAME, DataModelCommandData.RETRIEVE_ALL_ASSETS);
		Fields retrieveAllAssetOutput = Fields.empty();
		_dataModelSystem.execute(retrieveAllAssetInput, retrieveAllAssetOutput);
		return _responseFormatter.format(retrieveAllAssetOutput.rejectKeysStartingWith("outcome"));
	}

	public String retrieveAssetDetails(String filePath) {
		Fields retrieveAssetDetailsInput = Fields.single(ToolData.COMMAND_NAME, DataModelCommandData.RETRIEVE_ASSET_DETAILS_BY_FILE_NAME);
		retrieveAssetDetailsInput.put(RealToDigitalSyncData.ASSET_FILE_NAME, filePath);
		Fields retrieveAssetDetailsOutput = Fields.empty();
		_dataModelSystem.execute(retrieveAssetDetailsInput, retrieveAssetDetailsOutput);
		return _responseFormatter.format(retrieveAssetDetailsOutput.rejectKeysStartingWith("outcome"));
	}
	
	public void removeAsset(String filePath) {
		Fields retrieveAssetDetailsInput = Fields.single(ToolData.COMMAND_NAME, DataModelCommandData.REMOVE_ASSET_BY_FILE_NAME);
		retrieveAssetDetailsInput.put(RealToDigitalSyncData.ASSET_FILE_NAME, filePath);
		Fields retrieveAssetDetailsOutput = Fields.empty();
		_dataModelSystem.execute(retrieveAssetDetailsInput, retrieveAssetDetailsOutput);
	}

	public void clearAll() {
		_dataModelSystem.execute(Fields.single(ToolData.COMMAND_NAME, DataModelCommandData.CLEAR), Fields.empty());
	}
}