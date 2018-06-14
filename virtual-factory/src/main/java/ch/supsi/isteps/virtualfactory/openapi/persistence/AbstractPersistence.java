package ch.supsi.isteps.virtualfactory.openapi.persistence;

import ch.supsi.isteps.virtualfactory.tools.Fields;

public abstract class AbstractPersistence {

	// LAYER
	public abstract Fields saveLayer(Fields someFields);
	public abstract Fields retrieveLayerByName(Fields someFields);
	public abstract Fields removeLayerByName(Fields someFields);
	// ELEMENT
	public abstract Fields saveElement(Fields someFields);
	public abstract Fields retrieveElementByName(Fields someFields);
	public abstract Fields removeElementByName(Fields someFields);
	public abstract Fields retrieveAllElementsByLayer(Fields someFields);
	public abstract Fields retrieveAllElementsByArchetype(Fields someFields);
	// ATTRIBUTES
	public abstract Fields saveAttribute(Fields someFields);
	public abstract Fields retrieveAttributeByKey(Fields someFields);
	public abstract Fields removeAttributeByKey(Fields someFields);
	public abstract Fields retrieveAllAttributesByElement(Fields someFields);
	public abstract Fields removeAllAttributesByElement(Fields someFields);
	public abstract Fields removeAttributeOfElement(Fields someFields);
	// LINK
	public abstract Fields saveLink(Fields someFields);
	public abstract Fields removeLinkBySourceElement(Fields someFields);
	public abstract Fields retrieveLinkBySourceElement(Fields someFields);
	public abstract Fields retrieveLinkById(Fields somefields);
	
	public abstract Fields saveAsset(Fields someFields);
	public abstract Fields retrieveAllAssets();
	public abstract Fields retrieveAssetByFileName(Fields someFields);
	public abstract void removeAssetByFileName(Fields someFields);

	// UTILS
	public abstract void clear();
}