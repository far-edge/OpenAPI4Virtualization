package ch.supsi.isteps.virtualfactory.openapi.persistence;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("elementRepository")
public interface ElementRepository extends CrudRepository<Element, Serializable> {

	@Query(nativeQuery = true, value = "SELECT * FROM elements e WHERE e.elementName = :elementName")
	List<Element> retrieveElementByName(@Param("elementName") String elementName);
	
	@Query(nativeQuery = true, value = "SELECT * FROM elements e WHERE e.elementName = :elementName and e.layerName = :layerName")
	List<Element> retrieveElementByNameAndLayer(@Param("elementName") String elementName,@Param("layerName") String layerName);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "DELETE FROM elements e WHERE e.elementName = :elementName")
	void removeElementByName(@Param("elementName") String elementName);

	@Query(nativeQuery = true, value = "SELECT * FROM elements e WHERE e.layerName = :layerName")
	List<Element> retrieveAllElementsByLayer(@Param("layerName") String layerName);

	@Query(nativeQuery = true, value = "SELECT * FROM elements e WHERE e.archetypeName = :archetypeName")
	List<Element> retrieveAllElementsByArchetype(@Param("archetypeName") String archetypeName);

}