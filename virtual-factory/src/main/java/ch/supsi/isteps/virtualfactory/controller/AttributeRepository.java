package ch.supsi.isteps.virtualfactory.controller;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ch.supsi.isteps.virtualfactory.openapi.persistence.Attribute;

@Repository
public interface AttributeRepository extends CrudRepository<Attribute, Serializable>{

	List<Attribute> findByElementName(String elementName);
	
	List<Attribute> findByElementId(String elementId);

}
