package ch.supsi.isteps.virtualfactory.openapi.data;

public class DataModelCommandData {

	// LAYERS
	public static final String SAVE_LAYER = "saveLayer";
	public static final String RETRIEVE_LAYER_BY_NAME = "retrieveLayerByName";
	public static final String REMOVE_LAYER_BY_NAME = "removeLayerByName";
	
	// ELEMENTS
	public static final String SAVE_ELEMENT = "saveElement";
	public static final String RETRIEVE_ELEMENT_BY_NAME = "retrieveElementByName";
	public static final String REMOVE_ELEMENT_BY_NAME = "removeElementByName";
	public static final String RETRIEVE_ALL_ELEMENTS_BY_LAYER = "retrieveAllElementsByLayer";
	public static final String RETRIEVE_ALL_ELEMENTS_BY_ARCHETYPE = "retrieveAllElementsByArchetype";

	// ATTRIBUTES
	public static final String SAVE_ATTRIBUTE = "saveAttribute";
	public static final String RETRIEVE_ATTRIBUTE_BY_KEY = "retrieveAttributeByKey";
	public static final String REMOVE_ATTRIBUTE_BY_KEY = "removeAttributeByKey";
	public static final String RETRIEVE_ALL_ATTRIBUTES_BY_ELEMENT_NAME = "retrieveAllAttributesByElementName";
	public static final String REMOVE_ALL_ATTRIBUTES_BY_ELEMENT_NAME = "removeAllAttributesByElementName";
	public static final String REMOVE_ATTRIBUTE_OF_ELEMENT = "removeAttributeOfElement";
	// LINKS
	public static final String SAVE_LINK = "saveLink";
	public static final String RETRIEVE_LINK_BY_SOURCE_ELEMENT = "retrieveLinkBySourceElement";
	public static final String REMOVE_LINK_BY_SOURCE_ELEMENT = "removeLinkBySourceElement";
	public static final String RETRIEVE_LINK_BY_ID = "retrieveLinkById";
	// ASSET
	public static final String SAVE_ASSET = "saveAsset";
	public static final String RETRIEVE_ALL_ASSETS = "retrieveAllAssets";
	public static final String REMOVE_ASSET_BY_FILE_NAME = "removeAssetByFileName";
	public static final String RETRIEVE_ASSET_BY_FILE_NAME = "retrieveAssetByFileName";
    public static final String RETRIEVE_ASSET_DETAILS_BY_FILE_NAME = "retrieveDetailsAssetByFileName";
	
	// UTILS
	public static final String CLEAR = "clear";
	
}