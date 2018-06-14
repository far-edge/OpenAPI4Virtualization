package ch.supsi.isteps.virtualfactory.openapi.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import ch.supsi.isteps.virtualfactory.realtodigitalsync.dataclient.APILevel0Mapper;
import ch.supsi.isteps.virtualfactory.tools.dispenser.UUIDDispenser;

@Service
public class SingletonPersisistence {

	@Autowired
	private static ElementRepository _elementRepository;
	@Autowired
	private static AttributeRepository _attributeRepository;
	@Autowired
	private static AssetRepository _assetRepository;
	@Autowired
	private static LayerRepository _layerRepository;
	@Autowired
	private static LinkRepository _linkRepository;

	private static AbstractPersistence instance;

	

	public static AbstractPersistence iniciate(String persistenceSelection,ElementRepository elementRepository,
			AttributeRepository attributeRepository, AssetRepository assetRepository, LayerRepository layerRepository,
			LinkRepository linkRepository) {
		_elementRepository = elementRepository;
		_attributeRepository = attributeRepository;
		_assetRepository = assetRepository;
		_layerRepository = layerRepository;
		_linkRepository = linkRepository;
		if (instance == null) {
			try {
				System.out.println("INITIALIZATION PERSISTENCE");

				System.out.println();
				switch (persistenceSelection) {
				case "InMemory":
					instance = new InMemoryDataModelPersistence(new UUIDDispenser(), new UUIDDispenser());
					break;

				case "Database":
					instance = new JpaPostgresSQLPersistence(_elementRepository, _attributeRepository, _assetRepository,
							_layerRepository, _linkRepository);
					break;
				default:
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return instance;
	}

	public static AbstractPersistence getInstance() {
		return instance;
	}

}
