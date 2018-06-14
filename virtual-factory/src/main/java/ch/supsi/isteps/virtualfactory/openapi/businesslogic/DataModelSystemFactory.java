package ch.supsi.isteps.virtualfactory.openapi.businesslogic;

import ch.supsi.isteps.virtualfactory.openapi.businesslogic.command.ClearRepositoryCommand;
import ch.supsi.isteps.virtualfactory.openapi.businesslogic.command.RemoveAssetByFileNameCommand;
import ch.supsi.isteps.virtualfactory.openapi.businesslogic.command.RemoveAttributeByKeyCommand;
import ch.supsi.isteps.virtualfactory.openapi.businesslogic.command.RemoveAttributeOfElementCommand;
import ch.supsi.isteps.virtualfactory.openapi.businesslogic.command.RemoveAttributesByElementNameCommand;
import ch.supsi.isteps.virtualfactory.openapi.businesslogic.command.RemoveElementByNameCommand;
import ch.supsi.isteps.virtualfactory.openapi.businesslogic.command.RemoveLayerByNameCommand;
import ch.supsi.isteps.virtualfactory.openapi.businesslogic.command.RemoveLinkBySourceElementCommand;
import ch.supsi.isteps.virtualfactory.openapi.businesslogic.command.RetrieveAllAssetsCommand;
import ch.supsi.isteps.virtualfactory.openapi.businesslogic.command.RetrieveAllElementsByArchetypeCommand;
import ch.supsi.isteps.virtualfactory.openapi.businesslogic.command.RetrieveAllElementsByLayerCommand;
import ch.supsi.isteps.virtualfactory.openapi.businesslogic.command.RetrieveAssetByFileNameCommand;
import ch.supsi.isteps.virtualfactory.openapi.businesslogic.command.RetrieveAssetDetailsByFileNameCommand;
import ch.supsi.isteps.virtualfactory.openapi.businesslogic.command.RetrieveAttributeByKeyCommand;
import ch.supsi.isteps.virtualfactory.openapi.businesslogic.command.RetrieveAttributesByElementNameCommand;
import ch.supsi.isteps.virtualfactory.openapi.businesslogic.command.RetrieveElementByNameCommand;
import ch.supsi.isteps.virtualfactory.openapi.businesslogic.command.RetrieveLayerByNameCommand;
import ch.supsi.isteps.virtualfactory.openapi.businesslogic.command.RetrieveLinkByIdCommand;
import ch.supsi.isteps.virtualfactory.openapi.businesslogic.command.RetrieveLinkBySourceElementCommand;
import ch.supsi.isteps.virtualfactory.openapi.businesslogic.command.SaveAssetCommand;
import ch.supsi.isteps.virtualfactory.openapi.businesslogic.command.SaveAttributeCommand;
import ch.supsi.isteps.virtualfactory.openapi.businesslogic.command.SaveElementCommand;
import ch.supsi.isteps.virtualfactory.openapi.businesslogic.command.SaveLayerCommand;
import ch.supsi.isteps.virtualfactory.openapi.businesslogic.command.SaveLinkCommand;
import ch.supsi.isteps.virtualfactory.openapi.data.DataModelCommandData;
import ch.supsi.isteps.virtualfactory.openapi.persistence.AbstractPersistence;
import ch.supsi.isteps.virtualfactory.tools.XSystem;
import ch.supsi.isteps.virtualfactory.tools.command.CommandHandler;

public class DataModelSystemFactory {

	public static XSystem create(AbstractPersistence repository) {
		CommandHandler commandHandler = new CommandHandler();
		// LAYER
		commandHandler.put(DataModelCommandData.SAVE_LAYER, new SaveLayerCommand(repository));
		commandHandler.put(DataModelCommandData.RETRIEVE_LAYER_BY_NAME, new RetrieveLayerByNameCommand(repository));
		commandHandler.put(DataModelCommandData.REMOVE_LAYER_BY_NAME, new RemoveLayerByNameCommand(repository));
		// ELEMENTS
		commandHandler.put(DataModelCommandData.SAVE_ELEMENT, new SaveElementCommand(repository));
		commandHandler.put(DataModelCommandData.RETRIEVE_ELEMENT_BY_NAME, new RetrieveElementByNameCommand(repository));
		commandHandler.put(DataModelCommandData.REMOVE_ELEMENT_BY_NAME, new RemoveElementByNameCommand(repository));
		commandHandler.put(DataModelCommandData.RETRIEVE_ALL_ELEMENTS_BY_LAYER, new RetrieveAllElementsByLayerCommand(repository));
		commandHandler.put(DataModelCommandData.RETRIEVE_ALL_ELEMENTS_BY_ARCHETYPE, new RetrieveAllElementsByArchetypeCommand(repository));
		// ATTRIBUTES
		commandHandler.put(DataModelCommandData.SAVE_ATTRIBUTE, new SaveAttributeCommand(repository));
		commandHandler.put(DataModelCommandData.RETRIEVE_ATTRIBUTE_BY_KEY, new RetrieveAttributeByKeyCommand(repository));
		commandHandler.put(DataModelCommandData.REMOVE_ATTRIBUTE_BY_KEY, new RemoveAttributeByKeyCommand(repository));
		commandHandler.put(DataModelCommandData.RETRIEVE_ALL_ATTRIBUTES_BY_ELEMENT_NAME, new RetrieveAttributesByElementNameCommand(repository));
		commandHandler.put(DataModelCommandData.REMOVE_ALL_ATTRIBUTES_BY_ELEMENT_NAME, new RemoveAttributesByElementNameCommand(repository));
		commandHandler.put(DataModelCommandData.REMOVE_ATTRIBUTE_OF_ELEMENT, new RemoveAttributeOfElementCommand(repository));
		// LINKS
		commandHandler.put(DataModelCommandData.SAVE_LINK, new SaveLinkCommand(repository));
		commandHandler.put(DataModelCommandData.RETRIEVE_LINK_BY_SOURCE_ELEMENT, new RetrieveLinkBySourceElementCommand(repository));
		commandHandler.put(DataModelCommandData.RETRIEVE_LINK_BY_ID, new RetrieveLinkByIdCommand(repository));
		commandHandler.put(DataModelCommandData.REMOVE_LINK_BY_SOURCE_ELEMENT, new RemoveLinkBySourceElementCommand(repository));
		// ASSETS
		commandHandler.put(DataModelCommandData.SAVE_ASSET, new SaveAssetCommand(repository));
		commandHandler.put(DataModelCommandData.REMOVE_ASSET_BY_FILE_NAME, new RemoveAssetByFileNameCommand(repository));
		commandHandler.put(DataModelCommandData.RETRIEVE_ALL_ASSETS, new RetrieveAllAssetsCommand(repository));
		commandHandler.put(DataModelCommandData.RETRIEVE_ASSET_DETAILS_BY_FILE_NAME, new RetrieveAssetDetailsByFileNameCommand(repository));
		commandHandler.put(DataModelCommandData.RETRIEVE_ASSET_BY_FILE_NAME, new RetrieveAssetByFileNameCommand(repository));

		// UTILS
		commandHandler.put(DataModelCommandData.CLEAR, new ClearRepositoryCommand(repository));
		return new XSystem("Data Model System", commandHandler);
	}
}