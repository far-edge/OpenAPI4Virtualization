package ch.supsi.isteps.virtualfactory.openapi.persistence;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("attributeRepository")
public interface AttributeRepository extends CrudRepository<Attribute, Serializable> {

	@Query(nativeQuery = true, value = "SELECT * FROM attributes a WHERE a.elementName = :elementName")
	List<Attribute> findByElementName(@Param("elementName") String elementName);
	
	@Query(nativeQuery = true, value = "SELECT * FROM attributes a WHERE a.elementId = :elementId")
	List<Attribute> findByElementId(@Param("elementId") String elementId);

	@Query(nativeQuery = true, value = "SELECT * FROM attributes a WHERE a.key = :key")
	List<Attribute> retrieveAttributeByKey(@Param("key") String key);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "DELETE FROM attributes a WHERE a.key = :key")
	void removeAttributeByKey(@Param("key") String key);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "DELETE FROM attributes a WHERE a.elementName = :elementName")
	void removeAllAttributesByElement(@Param("elementName") String elementName);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "DELETE FROM attributes a WHERE a.elementName = :elementName and a.key = :key")
	void removeAttributeOfElement(@Param("key") String key, @Param("elementName") String elementName);

}
