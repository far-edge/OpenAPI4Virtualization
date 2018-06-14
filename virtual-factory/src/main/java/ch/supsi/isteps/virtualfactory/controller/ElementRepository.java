package ch.supsi.isteps.virtualfactory.controller;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ch.supsi.isteps.virtualfactory.openapi.persistence.Element;

@Repository
public interface ElementRepository extends CrudRepository<Element, Serializable>{

	
	List<Element> retrieveElementByName(String elementName);
	
	
	List<Element> retrieveElementByNameAndLayer(String elementName,String layerName);

}