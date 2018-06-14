package ch.supsi.isteps.virtualfactory;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ch.supsi.isteps.virtualfactory.openapi.businesslogic.DataModelSystemFactory;
import ch.supsi.isteps.virtualfactory.openapi.persistence.AssetRepository;
import ch.supsi.isteps.virtualfactory.openapi.persistence.AttributeRepository;
import ch.supsi.isteps.virtualfactory.openapi.persistence.ElementRepository;
import ch.supsi.isteps.virtualfactory.openapi.persistence.JpaPostgresSQLPersistence;
import ch.supsi.isteps.virtualfactory.openapi.persistence.LayerRepository;
import ch.supsi.isteps.virtualfactory.openapi.persistence.LinkRepository;
import ch.supsi.isteps.virtualfactory.tools.dispenser.IncrementalDispenser;

@RunWith(SpringRunner.class)
@EnableAutoConfiguration
@SpringBootTest(classes = JpaTest.class)
public class JpaTest extends BaseTest{

	@Autowired
	ElementRepository _elementRepository;
	@Autowired
	AttributeRepository _attributeRepository;
	@Autowired
	private AssetRepository _assetRepository;
	@Autowired
	private LayerRepository _layerRepository;
	@Autowired
	private LinkRepository _linkRepository;

	@Before
	public void setup() {
		IncrementalDispenser elementIdDispenser = new IncrementalDispenser(123);
		IncrementalDispenser assetsIdDispenser = new IncrementalDispenser(123);
		_dataModel = DataModelSystemFactory.create(new JpaPostgresSQLPersistence(_elementRepository,
				_attributeRepository, _assetRepository, _layerRepository, _linkRepository));
		_elementRepository.deleteAll();
		_attributeRepository.deleteAll();
		_assetRepository.deleteAll();
		_layerRepository.deleteAll();
		_linkRepository.deleteAll();
	}

	

}
