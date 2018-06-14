package ch.supsi.isteps.virtualfactory.openapi.persistence;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("linkRepository")
public interface LinkRepository extends CrudRepository<Link, Serializable>{

//	@Query(nativeQuery = true, value = "SELECT * FROM links l WHERE l.id = :linkId")
//	List<Link> retrieveLinkById(@Param("linkId") String linkId);
	
	@Query(nativeQuery = true, value = "SELECT * FROM links l WHERE l.id = :linkId")
	List<Link> retrieveLinkById(@Param("linkId") long linkId);
	
	@Query(nativeQuery = true, value = "SELECT * FROM links l WHERE l.fromElement = :fromElement")
	List<Link> retrieveLinkBySourceElement(@Param("fromElement") String fromElement);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "DELETE FROM links l WHERE l.fromElement = :fromElement")
	void removeLinkBySourceElement(@Param("fromElement") String fromElement);
}
