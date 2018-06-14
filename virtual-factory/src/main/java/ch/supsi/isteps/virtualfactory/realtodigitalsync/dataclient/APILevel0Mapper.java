package ch.supsi.isteps.virtualfactory.realtodigitalsync.dataclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import ch.supsi.isteps.virtualfactory.openapi.businesslogic.DataModelSystemFactory;
import ch.supsi.isteps.virtualfactory.openapi.data.DataModelCommandData;
import ch.supsi.isteps.virtualfactory.openapi.persistence.AbstractPersistence;
import ch.supsi.isteps.virtualfactory.openapi.persistence.AssetRepository;
import ch.supsi.isteps.virtualfactory.openapi.persistence.AttributeRepository;
import ch.supsi.isteps.virtualfactory.openapi.persistence.ElementRepository;
import ch.supsi.isteps.virtualfactory.openapi.persistence.InMemoryDataModelPersistence;
import ch.supsi.isteps.virtualfactory.openapi.persistence.JpaPostgresSQLPersistence;
import ch.supsi.isteps.virtualfactory.openapi.persistence.LayerRepository;
import ch.supsi.isteps.virtualfactory.openapi.persistence.LinkRepository;
import ch.supsi.isteps.virtualfactory.openapi.persistence.SingletonPersisistence;
import ch.supsi.isteps.virtualfactory.openapi.responseformatter.AsFieldsResponseFormatter;
import ch.supsi.isteps.virtualfactory.realtodigitalsync.data.RealToDigitalSyncData;
import ch.supsi.isteps.virtualfactory.tools.Fields;
import ch.supsi.isteps.virtualfactory.tools.data.ToolData;
import ch.supsi.isteps.virtualfactory.tools.dispenser.UUIDDispenser;


public class APILevel0Mapper {

	private OpenAPIForVirtualizationLevel0 _openAPIForVirtualizationLevel0;
	
	public APILevel0Mapper() {
		
		try {
			//_openAPIForVirtualizationLevel0 = new OpenAPIForVirtualizationLevel0(DataModelSystemFactory.create(persistence), new AsFieldsResponseFormatter());
			_openAPIForVirtualizationLevel0 = new OpenAPIForVirtualizationLevel0(DataModelSystemFactory.create(SingletonPersisistence.getInstance()), new AsFieldsResponseFormatter());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	public OpenAPIForVirtualizationLevel0 getAPILevel0() {
		return _openAPIForVirtualizationLevel0;
	}

	public String saveElement(String aLayerName, String anArchetype, String anElementName) {
		return _openAPIForVirtualizationLevel0.saveElement(aLayerName, anArchetype, anElementName);
	}

	public String retrieveElementByName(String elementName) {
		return _openAPIForVirtualizationLevel0.retrieveElementByName(elementName);
	}

	public String removeElementByName(String anElementName) {
		return _openAPIForVirtualizationLevel0.removeElementByName(anElementName);
	}

	public String retrieveAllElementsByLayer(String aLayerName) {
		return _openAPIForVirtualizationLevel0.retrieveAllElementsByLayer(aLayerName);
	}

	public String retrieveAllElementsByArchetype(String anArchetype) {
		return _openAPIForVirtualizationLevel0.retrieveAllElementsByArchetype(anArchetype);
	}

	public void saveAttribute(String layerName, String anElementKey, String aKey, String aValue) {
		_openAPIForVirtualizationLevel0.saveAttribute(layerName, anElementKey, aKey, aValue);
	}

	public String retrieveAttributesByElement(String aLayerName, String elementName) {
		return _openAPIForVirtualizationLevel0.retrieveAttributesByElement(aLayerName, elementName);
	}

	public String removeAttribute(String anElementName, String aKey, String aValue) {
		return _openAPIForVirtualizationLevel0.removeAttribute(anElementName, aKey, aValue);
	}
	
	public void saveLayer(String layerName, String layerDescription) {
		_openAPIForVirtualizationLevel0.saveLayer(layerName, layerDescription);
	}
	
	public void saveLink(String fromElement, String toElement, String linkType) {
		_openAPIForVirtualizationLevel0.saveLink(fromElement, toElement, linkType);
	}
	public String retrieveLinkBySourceElement(String fromElement) {
		return _openAPIForVirtualizationLevel0.retrieveLinkBySourceElement(fromElement);
	}
	
	public String retrieveLinkById(String id) {
		return _openAPIForVirtualizationLevel0.retrieveLinkById(id);
	}

	public String saveAsset(String aFilePath) {
		return _openAPIForVirtualizationLevel0.saveAsset(aFilePath);
	}

	public String retrieveAllAssets() {
		return _openAPIForVirtualizationLevel0.retrieveAllAssets();
	}

	public String retrieveAssetDetails(String filePath) {
		return _openAPIForVirtualizationLevel0.retrieveAssetDetails(filePath);
	}

	public void removeAsset(String filePath) {
		_openAPIForVirtualizationLevel0.removeAsset(filePath);
	}

	public void clearAll() {
		_openAPIForVirtualizationLevel0.clearAll();
	}

}
