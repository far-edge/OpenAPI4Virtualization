package ch.supsi.isteps.virtualfactory.openapi.persistence;

import java.io.Serializable;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("assetRepository")
public interface AssetRepository extends CrudRepository<Asset, Serializable> {

	@Query(nativeQuery = true, value = "SELECT * FROM assets a WHERE a.filePath LIKE CONCAT('%',:assetFileName,'%')")
	Asset retrieveAssetByFileName(@Param("assetFileName") String assetFileName);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "DELETE FROM assets a WHERE a.filePath LIKE CONCAT('%',:assetFileName,'%')")
	void removeAssetByFileName(@Param("assetFileName") String assetFileName);

}