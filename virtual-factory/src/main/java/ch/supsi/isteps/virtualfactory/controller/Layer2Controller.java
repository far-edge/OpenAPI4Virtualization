package ch.supsi.isteps.virtualfactory.controller;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;

import org.hibernate.mapping.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import ch.supsi.isteps.virtualfactory.openapi.persistence.AbstractPersistence;
import ch.supsi.isteps.virtualfactory.openapi.persistence.AssetRepository;
import ch.supsi.isteps.virtualfactory.openapi.persistence.AttributeRepository;
import ch.supsi.isteps.virtualfactory.openapi.persistence.ElementRepository;
import ch.supsi.isteps.virtualfactory.openapi.persistence.LayerRepository;
import ch.supsi.isteps.virtualfactory.openapi.persistence.LinkRepository;
import ch.supsi.isteps.virtualfactory.openapi.persistence.SingletonPersisistence;
import ch.supsi.isteps.virtualfactory.realtodigitalsync.data.RealToDigitalSyncData;
import ch.supsi.isteps.virtualfactory.realtodigitalsync.dataclient.OpenAPIForVirtualizationLevel1;
import ch.supsi.isteps.virtualfactory.tools.Fields;

@RestController
@Profile("layer2")
public class Layer2Controller {

	private OpenAPIForVirtualizationLevel1 _openAPIForVirtualizationLevel1AsFields;

	@Autowired
	private ElementRepository _elementRepository;
	@Autowired
	private AttributeRepository _attributeRepository;
	@Autowired
	private AssetRepository _assetRepository;
	@Autowired
	private LayerRepository _layerRepository;
	@Autowired
	private LinkRepository _linkRepository;

	public Layer2Controller(@Value("${virtualfactory.persistence-selection}") String persistenceSelection,
			ElementRepository elementRepository, AttributeRepository attributeRepository,
			AssetRepository assetRepository, LayerRepository layerRepository, LinkRepository linkRepository) {

		_elementRepository = elementRepository;
		_attributeRepository = attributeRepository;
		_assetRepository = assetRepository;
		_layerRepository = layerRepository;
		_linkRepository = linkRepository;

		AbstractPersistence persistence = SingletonPersisistence.iniciate(persistenceSelection, _elementRepository,
				_attributeRepository, _assetRepository, _layerRepository, _linkRepository);
		_openAPIForVirtualizationLevel1AsFields = new OpenAPIForVirtualizationLevel1();
	}

	// BEGIN OF THE OPEN API FOR VIRTUALIZATION INTERFACE
	@RequestMapping(value = "/connection-status", method = RequestMethod.GET)
	@ResponseBody
	public String connectionStatus() {
		return _openAPIForVirtualizationLevel1AsFields.connectionStatus();
	}

	@RequestMapping(value = "/setup/volvo/", method = RequestMethod.GET)
	@ResponseBody
	public String setupVolvo() {
		setup();
		return "{message:\"Installed\"}";
	}

	private void setup2() {
//		_openAPIForVirtualizationLevel1AsFields.saveLayer("productLayer", "The product layer");
//		
//		_openAPIForVirtualizationLevel1AsFields.saveElement("productLayer", "PRODUCTS", "products");
//		_openAPIForVirtualizationLevel1AsFields.saveLink("productLayer", "products", RealToDigitalSyncData.FROM_LAYER_TO_ELEMENT);
//
//		
//		_openAPIForVirtualizationLevel1AsFields.saveElement("productLayer", "PRODUCT", "Product A819371_17120002");
//		_openAPIForVirtualizationLevel1AsFields.addAttributeForElement("productLayer", "Product A819371_17120002", "id", "A819371_17120002");
//		_openAPIForVirtualizationLevel1AsFields.addAttributeForElement("productLayer", "Product A819371_17120002", "inBuffer", "true");
//		_openAPIForVirtualizationLevel1AsFields.addAttributeForElement("productLayer", "Product A819371_17120002", "status",	"waitingForWheelAlignemt");
//		_openAPIForVirtualizationLevel1AsFields.saveLink("products", "Product A819371_17120002", RealToDigitalSyncData.FROM_LAYER_TO_ELEMENT);
//		
//		_openAPIForVirtualizationLevel1AsFields.saveElement("productLayer", "PRODUCT", "Product A819371_17120001");
//		_openAPIForVirtualizationLevel1AsFields.addAttributeForElement("productLayer", "Product A819371_17120001", "id", "A819371_17120001");
//		_openAPIForVirtualizationLevel1AsFields.addAttributeForElement("productLayer", "Product A819371_17120001", "inBuffer", "true");
//		_openAPIForVirtualizationLevel1AsFields.addAttributeForElement("productLayer", "Product A819371_17120001", "status",	"waitingForWheelAlignemt");
//		_openAPIForVirtualizationLevel1AsFields.saveLink("products", "Product A819371_17120001", RealToDigitalSyncData.FROM_LAYER_TO_ELEMENT);
		
	}
	private void setup() {
		// create Layers
		_openAPIForVirtualizationLevel1AsFields.saveLayer("plantLayer", "The plant layer");
		_openAPIForVirtualizationLevel1AsFields.saveLayer("logicalLayer", "The logical layer");
		_openAPIForVirtualizationLevel1AsFields.saveLayer("productLayer", "The product layer");
		// Create plant
		_openAPIForVirtualizationLevel1AsFields.saveElement("plantLayer", "PLANT", "PlantVolvoTrucksGothenburg");

		// Create link PlantLayer -> PlantVolvoTrucksGothenburg
		_openAPIForVirtualizationLevel1AsFields.saveLink("plantLayer", "PlantVolvoTrucksGothenburg",	RealToDigitalSyncData.FROM_LAYER_TO_ELEMENT);

		// Create link PlantVolvoTrucksGothenburg -> ProductLayer
		_openAPIForVirtualizationLevel1AsFields.saveLink("PlantVolvoTrucksGothenburg", "productLayer", RealToDigitalSyncData.FROM_ELEMENT_TO_LAYER);
		// Create link PlantVolvoTrucksGothenburg -> LogicalLayer
		_openAPIForVirtualizationLevel1AsFields.saveLink("PlantVolvoTrucksGothenburg", "logicalLayer", RealToDigitalSyncData.FROM_ELEMENT_TO_LAYER);
		// create element Products
		_openAPIForVirtualizationLevel1AsFields.saveElement("productLayer", "PRODUCTS", "products");
		// Create link ProductLayer -> Products
		_openAPIForVirtualizationLevel1AsFields.saveLink("productLayer", "products",RealToDigitalSyncData.FROM_LAYER_TO_ELEMENT);
		
		// create element Product A819371_17120002
		_openAPIForVirtualizationLevel1AsFields.saveElement("productLayer", "PRODUCT", "product A819371_17120002");
		_openAPIForVirtualizationLevel1AsFields.addAttributeForElement("productLayer", "product A819371_17120002", "id", "A819371_17120002");
		_openAPIForVirtualizationLevel1AsFields.addAttributeForElement("productLayer", "product A819371_17120002", "inBuffer", "true");
		_openAPIForVirtualizationLevel1AsFields.addAttributeForElement("productLayer", "product A819371_17120002", "status",	"waitingForWheelAlignemt");
		_openAPIForVirtualizationLevel1AsFields.addAttributeForElement("productLayer", "product A819371_17120002", "designatedCompletionTime", "2018/05/03 12:00:00");
		_openAPIForVirtualizationLevel1AsFields.addAttributeForElement("productLayer", "product A819371_17120002", "deliveryInformation", "Ferry");
		_openAPIForVirtualizationLevel1AsFields.addAttributeForElement("productLayer", "product A819371_17120002", "inLineAdustmentProbabilty", "0.10");
		_openAPIForVirtualizationLevel1AsFields.addAttributeForElement("productLayer", "product A819371_17120002", "estimatedAdjustmentTime", "120");
		// Create link products -> Product A819371_17120002
		_openAPIForVirtualizationLevel1AsFields.saveLink("products", "product A819371_17120002",RealToDigitalSyncData.FROM_ELEMENT_TO_ELEMENT);

		// create element PlannedProcessingTimePerStation #A819371_17120002
		_openAPIForVirtualizationLevel1AsFields.saveElement("productLayer", "PLANNEDPROCESSINGTIMEPERSTATION","PlannedProcessingTimePerStation #A819371_17120002");
		// Create link ProductLayer -> Product A819371_17120002
		_openAPIForVirtualizationLevel1AsFields.saveLink("product A819371_17120002", "PlannedProcessingTimePerStation #A819371_17120002",RealToDigitalSyncData.FROM_ELEMENT_TO_ELEMENT);
				
				
		// create element PlannedProcessingTimePerStation #47
		_openAPIForVirtualizationLevel1AsFields.saveElement("productLayer", "PLANNEDPROCESSINGTIMEPERSTATION","plannedProcessingTimePerStation #47");
		_openAPIForVirtualizationLevel1AsFields.addAttributeForElement("productLayer", "plannedProcessingTimePerStation #47", "id", "47");
		_openAPIForVirtualizationLevel1AsFields.addAttributeForElement("productLayer", "plannedProcessingTimePerStation #47", "PlannedProcessingTime", "100");
		_openAPIForVirtualizationLevel1AsFields.addAttributeForElement("productLayer", "plannedProcessingTimePerStation #47", "estimatedDeviationfromPlannedProcessingTime", "0.05");
		// Create link Product A819371_17120002 -> PlannedProcessingTimePerStation #47
		_openAPIForVirtualizationLevel1AsFields.saveLink("PlannedProcessingTimePerStation #A819371_17120002", "plannedProcessingTimePerStation #47", RealToDigitalSyncData.FROM_ELEMENT_TO_ELEMENT);

		// create element PlannedProcessingTimePerStation #48
		_openAPIForVirtualizationLevel1AsFields.saveElement("productLayer", "PLANNEDPROCESSINGTIMEPERSTATION", "plannedProcessingTimePerStation #48");
		_openAPIForVirtualizationLevel1AsFields.addAttributeForElement("productLayer", "plannedProcessingTimePerStation #48", "id", "48");
		_openAPIForVirtualizationLevel1AsFields.addAttributeForElement("productLayer", "plannedProcessingTimePerStation #48", "PlannedProcessingTime", "182");
		_openAPIForVirtualizationLevel1AsFields.addAttributeForElement("productLayer", "plannedProcessingTimePerStation #48", "estimatedDeviationfromPlannedProcessingTime", "0.05");
		// Crete link "Product A819371_17120002" -> PlannedProcessingTimePerStation #48
		_openAPIForVirtualizationLevel1AsFields.saveLink("PlannedProcessingTimePerStation #A819371_17120002", "plannedProcessingTimePerStation #48", RealToDigitalSyncData.FROM_ELEMENT_TO_ELEMENT);

		// create element Product A819379_17133978
		_openAPIForVirtualizationLevel1AsFields.saveElement("productLayer", "PRODUCT", "product A819379_17133978");
		_openAPIForVirtualizationLevel1AsFields.addAttributeForElement("productLayer", "product A819379_17133978", "id", "A819379_17133978");
		_openAPIForVirtualizationLevel1AsFields.addAttributeForElement("productLayer", "product A819379_17133978", "inBuffer", "true");
		_openAPIForVirtualizationLevel1AsFields.addAttributeForElement("productLayer", "product A819379_17133978", "status", "delivered");
		_openAPIForVirtualizationLevel1AsFields.addAttributeForElement("productLayer", "product A819379_17133978", "designatedCompletionTime", "2018/05/02 10:00:00");
		_openAPIForVirtualizationLevel1AsFields.addAttributeForElement("productLayer", "product A819379_17133978", "deliveryInformation", "Ferry");
		_openAPIForVirtualizationLevel1AsFields.addAttributeForElement("productLayer", "product A819379_17133978", "inLineAdustmentProbabilty", "0.10");
		_openAPIForVirtualizationLevel1AsFields.addAttributeForElement("productLayer", "product A819379_17133978", "estimatedAdjustmentTime", "120");
		// Create link ProductLayer -> Product A819379_17133978
		_openAPIForVirtualizationLevel1AsFields.saveLink("products", "product A819379_17133978", RealToDigitalSyncData.FROM_LAYER_TO_ELEMENT);

		// create element PlannedProcessingTimePerStation #A819379_17133978
		_openAPIForVirtualizationLevel1AsFields.saveElement("productLayer", "PLANNEDPROCESSINGTIMEPERSTATION","PlannedProcessingTimePerStation #A819379_17133978");
		// Create link ProductLayer -> Product A819379_17133978
		_openAPIForVirtualizationLevel1AsFields.saveLink("product A819379_17133978", "PlannedProcessingTimePerStation #A819379_17133978",RealToDigitalSyncData.FROM_ELEMENT_TO_ELEMENT);
				
		// create element PlannedProcessingTimePerStation #49
		_openAPIForVirtualizationLevel1AsFields.saveElement("productLayer", "PLANNEDPROCESSINGTIMEPERSTATION", "PlannedProcessingTimePerStation #49");
		_openAPIForVirtualizationLevel1AsFields.addAttributeForElement("productLayer", "PlannedProcessingTimePerStation #49", "id", "49");
		_openAPIForVirtualizationLevel1AsFields.addAttributeForElement("productLayer", "PlannedProcessingTimePerStation #49", "PlannedProcessingTime", "130");
		_openAPIForVirtualizationLevel1AsFields.addAttributeForElement("productLayer", "PlannedProcessingTimePerStation #49", "estimatedDeviationfromPlannedProcessingTime", "0.05");
		// Create link Product A819379_17133978 -> PlannedProcessingTimePerStation #49
		_openAPIForVirtualizationLevel1AsFields.saveLink("PlannedProcessingTimePerStation #A819379_17133978", "PlannedProcessingTimePerStation #49", RealToDigitalSyncData.FROM_ELEMENT_TO_ELEMENT);

		// create element PlannedProcessingTimePerStation #50
		_openAPIForVirtualizationLevel1AsFields.saveElement("productLayer", "PLANNEDPROCESSINGTIMEPERSTATION", "PlannedProcessingTimePerStation #50");
		_openAPIForVirtualizationLevel1AsFields.addAttributeForElement("productLayer", "PlannedProcessingTimePerStation #50", "id", "50");
		_openAPIForVirtualizationLevel1AsFields.addAttributeForElement("productLayer", "PlannedProcessingTimePerStation #50", "PlannedProcessingTime", "189");
		_openAPIForVirtualizationLevel1AsFields.addAttributeForElement("productLayer", "PlannedProcessingTimePerStation #50", "estimatedDeviationfromPlannedProcessingTime", "0.05");
		// Create link Product A819379_17133978 -> PlannedProcessingTimePerStation #50
		_openAPIForVirtualizationLevel1AsFields.saveLink("PlannedProcessingTimePerStation #A819379_17133978", "PlannedProcessingTimePerStation #50", RealToDigitalSyncData.FROM_ELEMENT_TO_ELEMENT);

		// create LogicalLayer
		/****************************************************************************************************
		 * Create sequence output
		 *****************************************************************************************************/

		_openAPIForVirtualizationLevel1AsFields.saveElement("logicalLayer", "SEQUENCEOUTPUT", "sequenceOutput");
		// Create link LogicalLayer -> Buffer 1
		_openAPIForVirtualizationLevel1AsFields.saveLink("logicalLayer", "sequenceOutput", RealToDigitalSyncData.FROM_LAYER_TO_ELEMENT);
		
		// Create optimal sequence
		_openAPIForVirtualizationLevel1AsFields.saveElement("logicalLayer", "OPTIMALSEQUENCE", "optimalSequence");
		// Create link sequenceOutput -> optimalSequence
		_openAPIForVirtualizationLevel1AsFields.saveLink("sequenceOutput", "optimalSequence", RealToDigitalSyncData.FROM_ELEMENT_TO_ELEMENT);

		// Create OptimalSequence 1
		_openAPIForVirtualizationLevel1AsFields.saveElement("logicalLayer", "OPTIMALSEQUENCE", "OptimalSequence #1");
		_openAPIForVirtualizationLevel1AsFields.addAttributeForElement("logicalLayer", "OptimalSequence #1", "position", "1");
		_openAPIForVirtualizationLevel1AsFields.addAttributeForElement("logicalLayer", "OptimalSequence #1", "truckID", "A819371_17120002");
		// Create link SequenceOutput -> OptimalSequence 1
		_openAPIForVirtualizationLevel1AsFields.saveLink("optimalSequence", "OptimalSequence #1", RealToDigitalSyncData.FROM_ELEMENT_TO_ELEMENT);

		// Create OptimalSequence 2
		_openAPIForVirtualizationLevel1AsFields.saveElement("logicalLayer", "OPTIMALSEQUENCE", "OptimalSequence #2");
		_openAPIForVirtualizationLevel1AsFields.addAttributeForElement("logicalLayer", "OptimalSequence #2", "position", "2");
		_openAPIForVirtualizationLevel1AsFields.addAttributeForElement("logicalLayer", "OptimalSequence #2", "truckID", "A819379_17133978");
		// Create link SequenceOutput -> OptimalSequence 2
		_openAPIForVirtualizationLevel1AsFields.saveLink("optimalSequence", "OptimalSequence #2",	RealToDigitalSyncData.FROM_ELEMENT_TO_ELEMENT);
		
		/****************************************************************************************************
		 * Create stations
		 *****************************************************************************************************/
		// Create optimal stations
		_openAPIForVirtualizationLevel1AsFields.saveElement("logicalLayer", "STATIONS", "stations");
		// Create link logicalLayer -> stations
		_openAPIForVirtualizationLevel1AsFields.saveLink("logicalLayer", "stations", RealToDigitalSyncData.FROM_LAYER_TO_ELEMENT);

		 //create element Station 47
		_openAPIForVirtualizationLevel1AsFields.saveElement("logicalLayer", "STATION", "station #47");
		_openAPIForVirtualizationLevel1AsFields.addAttributeForElement("logicalLayer", "station #47", "id", "47");
		_openAPIForVirtualizationLevel1AsFields.addAttributeForElement("logicalLayer", "station #47", "failureRate", "0.05");
		// Create link stations -> Station 47
		_openAPIForVirtualizationLevel1AsFields.saveLink("stations", "station #47", RealToDigitalSyncData.FROM_LAYER_TO_ELEMENT);

		// create element Station 48
		_openAPIForVirtualizationLevel1AsFields.saveElement("logicalLayer", "STATION", "station #48");
		_openAPIForVirtualizationLevel1AsFields.addAttributeForElement("logicalLayer", "station #48", "id", "48");
		_openAPIForVirtualizationLevel1AsFields.addAttributeForElement("logicalLayer", "station #48", "failureRate", "0.02");
		// Create link stations -> Station 48
		_openAPIForVirtualizationLevel1AsFields.saveLink("stations", "station #48", RealToDigitalSyncData.FROM_LAYER_TO_ELEMENT);

		/****************************************************************************************************
		 * Create buffers
		 *****************************************************************************************************/
		
		// Create buffers
		_openAPIForVirtualizationLevel1AsFields.saveElement("logicalLayer", "BUFFERS", "buffers");
		// Create link logicalLayer -> buffers
		_openAPIForVirtualizationLevel1AsFields.saveLink("logicalLayer", "buffers", RealToDigitalSyncData.FROM_LAYER_TO_ELEMENT);
				
		_openAPIForVirtualizationLevel1AsFields.saveElement("logicalLayer", "BUFFER", "buffer #1");
		_openAPIForVirtualizationLevel1AsFields.addAttributeForElement("logicalLayer", "buffer #1", "id", "1");
		_openAPIForVirtualizationLevel1AsFields.addAttributeForElement("logicalLayer", "buffer #1", "bufferChangedEvent", "true");
		// Create link LogicalLayer -> Buffer 1
		_openAPIForVirtualizationLevel1AsFields.saveLink("buffers", "buffer #1", RealToDigitalSyncData.FROM_ELEMENT_TO_ELEMENT);

		// Create productsInTheBuffer
		_openAPIForVirtualizationLevel1AsFields.saveElement("logicalLayer", "PRODUCTSINTHEBUFFER", "productsInTheBuffer");
		// Create link productsInTheBuffer -> productsInTheBuffer
		_openAPIForVirtualizationLevel1AsFields.saveLink("buffer #1", "productsInTheBuffer", RealToDigitalSyncData.FROM_ELEMENT_TO_ELEMENT);
		
		// Create ProductsInTheBuffer A819371_17120002
		_openAPIForVirtualizationLevel1AsFields.saveElement("logicalLayer", "PRODUCTSINTHEBUFFER", "productsInTheBuffer #A819371_17120002");
		_openAPIForVirtualizationLevel1AsFields.addAttributeForElement("logicalLayer", "productsInTheBuffer #A819371_17120002", "id", "A819371_17120002");
		// Create link Buffer 1 -> ProductsInTheBuffer A819371_17120002
		_openAPIForVirtualizationLevel1AsFields.saveLink("productsInTheBuffer", "productsInTheBuffer #A819371_17120002", RealToDigitalSyncData.FROM_ELEMENT_TO_ELEMENT);

		// Create ProductsInTheBuffer A819379_17133978
		_openAPIForVirtualizationLevel1AsFields.saveElement("logicalLayer", "PRODUCTSINTHEBUFFER", "productsInTheBuffer #A819379_17133978");
		_openAPIForVirtualizationLevel1AsFields.addAttributeForElement("logicalLayer", "productsInTheBuffer #A819379_17133978", "id", "A819379_17133978");
		// Create link Buffer 1 -> ProductsInTheBuffer A819371_17120002
		_openAPIForVirtualizationLevel1AsFields.saveLink("productsInTheBuffer", "productsInTheBuffer #A819379_17133978", RealToDigitalSyncData.FROM_ELEMENT_TO_ELEMENT);

		
	}

	@RequestMapping("/clear-all")
	@ResponseBody
	public Boolean clearAll() {
		return _openAPIForVirtualizationLevel1AsFields.clearAll();
	}

	@RequestMapping("layer/{aLayerName}")
	@ResponseBody
	public String retrieveLayerConfiguration(@PathVariable String aLayerName) {
		//return retrieveMockConfiguration();
		return retrieveLayerCoinfiguration(aLayerName);
	}

	
	private String retrieveMockConfiguration() {
		String a = "{\r\n" + "	\"plantLayer\":{\r\n" + "		\"plantVolvoTrucksGothenburg\":{\r\n"
				+ "			\"productLayer\":{\r\n" + "				\"products\":[\r\n" + "						{\r\n"
				+ "							\"id\": \"A819371_17120002\",\r\n"
				+ "							\"inBuffer\": true,\r\n"
				+ "							\"status\": \"waitingForWheelAlignemt\",\r\n"
				+ "							\"designatedCompletionTime\": \"2018/05/03 12:00:00\",\r\n"
				+ "							\"deliveryInformation\": \"Ferry\",\r\n"
				+ "							\"inLineAdustmentProbabilty\": 0.10,\r\n"
				+ "							\"estimatedAdjustmentTime\": \"120\",							\r\n"
				+ "							\"plannedProcessingTimePerStation\": [\r\n"
				+ "									{\r\n" + "										\"id\": \"47\",\r\n"
				+ "										\"PlannedProcessingTime\": \"100\",\r\n"
				+ "										\"estimatedDeviationfromPlannedProcessingTime\": 0.05\r\n"
				+ "										\r\n" + "									},\r\n"
				+ "									{\r\n" + "										\"id\": \"48\",\r\n"
				+ "										\"PlannedProcessingTime\": \"182\",\r\n"
				+ "										\"estimatedDeviationfromPlannedProcessingTime\": 0.05\r\n"
				+ "									}\r\n" + "								]\r\n"
				+ "						},\r\n" + "						{\r\n"
				+ "							\"id\": \"A819379_17133978\",\r\n"
				+ "							\"inBuffer\": true,\r\n"
				+ "							\"status\": \"delivered\",\r\n"
				+ "							\"designatedCompletionTime\": \"2018/05/02 10:00:00\",\r\n"
				+ "							\"deliveryInformation\": \"Ferry\",\r\n"
				+ "							\"inLineAdustmentProbabilty\": 0.10,\r\n"
				+ "							\"estimatedAdjustmentTime\": \"120\",							\r\n"
				+ "							\"plannedProcessingTimePerStation\": [\r\n"
				+ "									{\r\n" + "										\"id\": \"47\",\r\n"
				+ "										\"PlannedProcessingTime\": \"130\",\r\n"
				+ "										\"estimatedDeviationfromPlannedProcessingTime\": 0.05\r\n"
				+ "										\r\n" + "									},\r\n"
				+ "									{\r\n" + "										\"id\": \"48\",\r\n"
				+ "										\"PlannedProcessingTime\": \"189\",\r\n"
				+ "										\"estimatedDeviationfromPlannedProcessingTime\": 0.05\r\n"
				+ "									}\r\n" + "								]\r\n"
				+ "					}\r\n" + "				]\r\n" + "			},\r\n"
				+ "			\"logicalLayer\": {\r\n" + "					\"sequenceOutput\": {\r\n"
				+ "						\"optimalSequence\": [\r\n" + "								{\r\n"
				+ "									\"position\": 1,\r\n"
				+ "									\"truckID\": \"A819371_17120002\"\r\n"
				+ "								},\r\n" + "								{\r\n"
				+ "									\"position\": 2,\r\n"
				+ "									\"truckID\": \"A819379_17133978\"\r\n"
				+ "								}\r\n" + "							]\r\n"
				+ "					},					\r\n" + "					\"stations\": [\r\n"
				+ "						{\r\n" + "							\"id\": \"47\",\r\n"
				+ "							\"failureRate\": 0.05\r\n" + "							\r\n"
				+ "						},\r\n" + "						{\r\n"
				+ "							\"id\": \"48\",\r\n"
				+ "							\"failureRate\": 0.02							\r\n"
				+ "						}\r\n" + "					],\r\n" + "					\"buffers\": [\r\n"
				+ "						{\r\n" + "							\"id\": 1,\r\n"
				+ "							\"bufferChangedEvent\": true,\r\n"
				+ "							\"productsInTheBuffer\": [\r\n" + "								{\r\n"
				+ "									\"id\": \"A819371_17120002\"\r\n"
				+ "								},\r\n" + "								{\r\n"
				+ "									\"id\": \"A819379_17133978\"\r\n"
				+ "								}\r\n" + "							]\r\n"
				+ "							\r\n" + "						}\r\n" + "					]\r\n"
				+ "			\r\n" + "			}\r\n" + "		}\r\n" + "	}\r\n" + "}";
		return a;
	}

	private String retrieveLayerCoinfiguration(String aLayerName) {
		String tree = getTree(aLayerName, RealToDigitalSyncData.LAYER_NAME);
		return tree;

	}

	private String getTree(String elementName, String type) {
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		String others = "";
		// retrieving the structure
		Fields links = Fields.fromRaw(_openAPIForVirtualizationLevel1AsFields.retrieveLinkBySourceElement(elementName));
		//harmonize the element to be able to find always the name
		Fields element = Fields.empty();
		switch (type) {
		case RealToDigitalSyncData.LAYER_NAME:
			element.put(RealToDigitalSyncData.ELEMENT_NAME, elementName);
			break;
		case RealToDigitalSyncData.ELEMENT_NAME:
			element = Fields.fromRaw(_openAPIForVirtualizationLevel1AsFields.getElement(elementName));
			break;
		}
		Fields attributes = Fields.fromRaw(_openAPIForVirtualizationLevel1AsFields.getElementAttributes(element.firstValueFor(RealToDigitalSyncData.LAYER_NAME), elementName));
		//exolore the tree in a recursive way till there are no more links from the element
		if (links.allKeysStartingWith(RealToDigitalSyncData.ID).size() < 1) {
			System.out.println("No other links found");
		} else {
			System.out.println("Links found: Continue searching");
			// build list of links
			ArrayNode jsonArray = mapper.createArrayNode();
			if(links.allKeysStartingWith(RealToDigitalSyncData.ID).size() > 1) { //necessary in case there is a list of links
				//for each link explore the subtree and return the json
				for (String linkKeyId : links.allKeysStartingWith(RealToDigitalSyncData.ID)) {
					jsonArray.add(exploreSubTree(links.firstValueFor(linkKeyId)));
				}
				json = jsonArray.toString();
			}
			else {//in case there is just one link it's not necessary to have a list - return the json of the element
				json = exploreSubTree(links.firstValueFor(RealToDigitalSyncData.ID));
			}
		}
		//build the json of the current element including also the json structure of the subtree
		try {
			others = buildJsonFromElement(element, attributes,json);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(others);
		return others;
	}
	
	private String exploreSubTree(String linkKeyId) {
		String json = "";
		System.out.println("searching for the link id " + linkKeyId);
		Fields link = Fields.fromRaw(_openAPIForVirtualizationLevel1AsFields.retrieveLinkById(linkKeyId));
		if (link.firstValueFor(RealToDigitalSyncData.LINK_TYPE).equals(RealToDigitalSyncData.FROM_ELEMENT_TO_LAYER)) {
			// add layer name
			System.out.println("link goes to layer, continue search " + link.firstValueFor(RealToDigitalSyncData.TO_ELEMENT));
			json = getTree(link.firstValueFor(RealToDigitalSyncData.TO_ELEMENT),RealToDigitalSyncData.LAYER_NAME);
			System.out.println(json);
		} else {
			System.out.println("link goes to element, continue search " + link.firstValueFor(RealToDigitalSyncData.TO_ELEMENT));
			json = getTree(link.firstValueFor(RealToDigitalSyncData.TO_ELEMENT),RealToDigitalSyncData.ELEMENT_NAME);
			System.out.println(json);
		}
		return json;
	}

	private String buildJsonFromElement(Fields element, Fields attributes, String subtree) throws JsonProcessingException,IOException {
		// build attributes list
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode jsonElement = mapper.createObjectNode();
		//iterate through the attributes and add them to the jsonElement
		for (String key : attributes.keys()) {
			if (!key.equals(RealToDigitalSyncData.ELEMENT_NAME)) {
				String value = attributes.firstValueFor(key);
				jsonElement.put(key, value);
			}
		}

		//add the subtree to the json structure
		if(!subtree.equals("")) {
			if(attributes.keys().size() > 1) {//add the subtree at the same level of the attributes
				JsonNode actualObj = mapper.readTree(subtree);
				for (Iterator<Entry<String,JsonNode>> it = actualObj.fields();  it.hasNext() ;) {
					    Entry<String,JsonNode> e = it.next();
					    jsonElement.put(e.getKey(), e.getValue());
				}
				
				//putobject does not work as expected (it leaves the parentesis)
				//jsonElement.putObject(subtree);
			}else{//in case there are no attributes and subtree needs a key 
				jsonElement.put(element.firstValueFor(RealToDigitalSyncData.ELEMENT_NAME), subtree);
			}
		}
		
		return mapper.writeValueAsString(jsonElement);

	}

	@RequestMapping("layer/{aLayerName}/element/{anElementName}/attribute")
	@ResponseBody
	public String retrieveAttributesByElement(@PathVariable String anElementName, @PathVariable String aLayerName) {
		return _openAPIForVirtualizationLevel1AsFields.retrieveAttributesByElement(aLayerName, anElementName);
	}

	@RequestMapping(value = "/layer/{aLayerName}/element", method = RequestMethod.POST)
	public String saveElement(@RequestBody MultiValueMap<String, String> params) {
		// TODO to change the strings to RealToDigitalSynchData constants
		String elementName = "";
		String layerName = "";
		String archetype = "";

		if (params.get("anElementName").size() > 0) {
			elementName = params.get("anElementName").get(0);
		}
		if (params.get("aLayerName").size() > 0) {
			layerName = params.get("aLayerName").get(0);
		}
		if (params.get("anArchetype").size() > 0) {
			archetype = params.get("anArchetype").get(0);
		}
		return _openAPIForVirtualizationLevel1AsFields.saveElement(elementName, layerName, archetype);
	}

	@RequestMapping(value = "/layer/{aLayerName}/element/{anElementName}", method = RequestMethod.GET)
	@ResponseBody
	public String getElement(@PathVariable String anElementName) {
		return _openAPIForVirtualizationLevel1AsFields.getElement(anElementName);
	}

	@RequestMapping(value = "/layer/{aLayerName}/element", method = RequestMethod.GET)
	@ResponseBody
	public String layerElements(@PathVariable String aLayerName) {
		return _openAPIForVirtualizationLevel1AsFields.layerElements(aLayerName);
	}

	@RequestMapping(value = "/layer/{aLayerName}/element/{anElementName}", method = RequestMethod.PUT, consumes = "text/plain", headers = "Accept=text/plain")
	@ResponseBody
	public String updateElement(@PathVariable String anElementName, @RequestBody String params) {
		return _openAPIForVirtualizationLevel1AsFields.updateElement(anElementName, params);
	}

	@RequestMapping(value = "/layer/{aLayerName}/element/{anElementName}", method = RequestMethod.DELETE)
	public String removeElement(@PathVariable String anElementName) {
		return _openAPIForVirtualizationLevel1AsFields.removeElement(anElementName);
	}

	@RequestMapping(value = "/layer/{aLayerName}/element/{anElementName}/attribute", method = RequestMethod.POST)
	@ResponseBody
	public String addAttributeForElement(@PathVariable String aLayerName, @PathVariable String anElementName,
			@RequestBody MultiValueMap<String, String> params) {
		String key = "";
		if (params.get(RealToDigitalSyncData.KEY).size() > 0) {
			key = params.get(RealToDigitalSyncData.KEY).get(0);
		}
		String value = "";
		if (params.get(RealToDigitalSyncData.VALUE).size() > 0) {
			value = params.get(RealToDigitalSyncData.VALUE).get(0);
		}
		System.out.println("Attribute value: " + value);
		return _openAPIForVirtualizationLevel1AsFields.addAttributeForElement(aLayerName, anElementName, key, value);
	}

	// @RequestMapping(value = "/element/{anElementName}/attribute", method =
	// RequestMethod.GET)
	// @ResponseBody
	// public String getElementAttributes(@PathVariable String anElementName) {
	// return
	// _openAPIForVirtualizationLevel1AsFields.getElementAttributes(anElementName);
	// }

	// @RequestMapping(value = "/active-sensors", method = RequestMethod.GET)
	// @ResponseBody
	// public String activeSensors() {
	// String s = _openAPIForVirtualizationLevel1AsFields.activeSensors();
	// return s;
	// }

	// @RequestMapping(value = "/sensor-data/{anArchetype}", method =
	// RequestMethod.GET)
	// @ResponseBody
	// public String sensorData(@PathVariable String anArchetype) {
	// String s = _openAPIForVirtualizationLevel1AsFields.sensorData(anArchetype);
	// return s;
	// }
	// @RequestMapping(value = "/connect-platform", method = RequestMethod.GET)
	// @ResponseBody
	// public Boolean connect() {
	// return _openAPIForVirtualizationLevel1AsFields.connect();
	// }
	// // TODO: rethink methods is GET right?
	// @RequestMapping(value = "/disconnect-platform", method = RequestMethod.GET)
	// @ResponseBody
	// public Boolean disconnect() {
	// return _openAPIForVirtualizationLevel1AsFields.disconnect();
	// }
	//
	// @RequestMapping("/setup")
	// @ResponseBody
	// public Boolean setup() {
	// return _openAPIForVirtualizationLevel1AsFields.setup();
	// }

	// @RequestMapping(value = "/layer/{aLayerName}/element/attribute/paired",
	// method = RequestMethod.GET)
	// @ResponseBody
	// public String getPairedElements() {
	// return _openAPIForVirtualizationLevel1AsFields.getPairedElements();
	// }

	// @RequestMapping(value = "/layer/{aLayerName}/element/attribute/pairings",
	// method = RequestMethod.GET)
	// @ResponseBody
	// public String getPairings() {
	// return _openAPIForVirtualizationLevel1AsFields.getPairings();
	// }

	// @RequestMapping(value =
	// "layer/{aLayerName}/element/{anElementName}/attribute/pairing", method =
	// RequestMethod.DELETE)
	// @ResponseBody
	// public void removePairing(@PathVariable String anElementName) {
	// _openAPIForVirtualizationLevel1AsFields.removePairing(anElementName);
	// }

	// @RequestMapping(value = "/layer/{aLayerName}/element/attribute/active",
	// method = RequestMethod.GET)
	// @ResponseBody
	// public String getActiveElements() {
	// return _openAPIForVirtualizationLevel1AsFields.getActiveElements();
	// }

	// @RequestMapping(value = "/assets", method = RequestMethod.POST)
	// public String createAsset(@RequestBody String filePath) {
	// return _openAPIForVirtualizationLevel1AsFields.createAsset(filePath);
	// }

	// @RequestMapping("/assets")
	// @ResponseBody
	// public String retrieveAllAssets() {
	// return _openAPIForVirtualizationLevel1AsFields.retrieveAllAssets();
	// }

	// @RequestMapping("/assets/{filePath}/details")
	// @ResponseBody
	// public String assetDetails(@PathVariable String filePath) {
	// return _openAPIForVirtualizationLevel1AsFields.assetDetails(filePath);
	// }

	// @RequestMapping(value = "/assets/{filePath}", method = RequestMethod.DELETE)
	// public void removeAsset(@PathVariable String filePath) {
	// _openAPIForVirtualizationLevel1AsFields.removeAsset(filePath);
	// }

	// private List<Pair<String, String>> getAttributes(Fields fieldsList) {
	// List<Pair<String, String>> keysList = new ArrayList<>();
	// String attributeKeys = fieldsList.firstValueFor("keys");
	// List<String> keys = new ArrayList<>();
	// if (attributeKeys.length() > 0) {
	// keys = Arrays.asList(attributeKeys.split(","));
	// for (String key : keys) {
	// keysList.add(Pair.of(key, fieldsList.firstValueFor(key)));
	// }
	// }
	// return keysList;
	// }
	//
	// private String retrieveElementsByAttribute(String key, String value) {
	// Fields result = Fields.empty();
	// Fields fields =
	// Fields.fromRaw(_openAPIForVirtualizationLevel1AsFields.layerElements(ConstantData.SENSOR));
	//
	// Fields elements = fields.selectPrefix(RealToDigitalSyncData.ELEMENT_NAME);
	// for (Map.Entry<String, String> entry : Fields.toMap(elements).entrySet()) {
	// System.out.println(entry.getKey() + "/" + entry.getValue());
	// Fields attributes = Fields
	// .fromRaw(_openAPIForVirtualizationLevel1AsFields.retrieveAttributesByElement(entry.getValue()));
	// for (Map.Entry<String, String> attribute :
	// Fields.toMap(attributes).entrySet()) {
	// if (attribute.getKey().equals(key) && attribute.getValue().equals(value)) {
	// System.out.println("Added to results");
	// result.put(RealToDigitalSyncData.ELEMENT_NAME, entry.getValue());
	// }
	// }
	// }
	// String output = "";
	// for (Map.Entry<String, String> entry : Fields.toMap(result).entrySet()) {
	// System.out.println(entry.getKey() + "|" + entry.getValue());
	// output += entry.getKey() + "=" + entry.getValue() + "|";
	//
	// }
	// return output;
	// }
}
