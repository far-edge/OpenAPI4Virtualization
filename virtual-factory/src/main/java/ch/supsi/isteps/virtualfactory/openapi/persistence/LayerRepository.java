package ch.supsi.isteps.virtualfactory.openapi.persistence;

import java.io.Serializable;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("layerRepository")
public interface LayerRepository extends CrudRepository<Layer, Serializable> {

	@Query(nativeQuery = true, value = "SELECT * FROM layers l WHERE l.layerName = :layerName")
	Layer retrieveLayerByName(@Param("layerName") String layerName);
	
	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "DELETE FROM layers l WHERE l.layerName = :layerName")
	void removeLayerByName(@Param("layerName") String layerName);

}
