package ch.supsi.isteps.monitoringapp.ui;

import org.apache.commons.lang3.tuple.Pair;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.v7.event.ItemClickEvent;
import com.vaadin.v7.event.ItemClickEvent.ItemClickListener;
import com.vaadin.v7.ui.Table;

import ch.supsi.isteps.monitoringapp.data.RealToDigitalSyncData;
import ch.supsi.isteps.monitoringapp.faredgeplatform.client.AbstractPlatformFacade;
import ch.supsi.isteps.monitoringapp.faredgeplatform.client.SingletonFacade;
import ch.supsi.isteps.monitoringapp.tools.Element;
import ch.supsi.isteps.monitoringapp.tools.ElementType;
import ch.supsi.isteps.monitoringapp.tools.LayerType;

@SuppressWarnings("deprecation")
public class ElementDetailsUIWindow extends Window {

	private static final long serialVersionUID = 1L;
	private AbstractPlatformFacade _facade = SingletonFacade.getInstance();;
	Element element;
	private int selectedTableIndex = 0;

	@SuppressWarnings("deprecation")
	public ElementDetailsUIWindow(String elementName, LayerType layer) {

		if(layer.equals(LayerType.sensor)) {
			element = _facade.getSensor(elementName);
		}
		else if(layer.equals(LayerType.logical)) {
			element = _facade.getSmartObject(elementName);
		}

		VerticalLayout subContent = new VerticalLayout();
		TextField txtName = new TextField();
		txtName.setValue(element.getElementName());
		txtName.setReadOnly(true);

		subContent.addComponent(new Label("Element Name"));
		subContent.addComponent(txtName);
		TextField txtLayerName = new TextField();
		txtLayerName.setValue(element.getLayerName());
		subContent.addComponent(new Label("Layer name"));
		subContent.addComponent(txtLayerName);

		TextField txtArchetype = new TextField();
		txtArchetype.setValue(element.getElementType());
		subContent.addComponent(new Label("Archetype"));
		subContent.addComponent(txtArchetype);
		setContent(subContent);

		// Put some components in it
		Table table = new Table("Element Attributes");
		table.setWidth("400px");
		table.setSelectable(true);
		// Define two columns for the built-in container
		table.addContainerProperty("Attribute name", String.class, null);
		table.addContainerProperty("Value",  String.class, null);
		reloadDataTable(table, element);
		// Show exactly the currently contained rows (items)
		table.setPageLength(table.size());

		
		table.addListener(new ItemClickListener() { // With ItemClickListener

			private static final long serialVersionUID = 1L;

			public void itemClick(ItemClickEvent event) {
		    	//Notification.show("selected " + event.getItemId());
		    	selectedTableIndex = (int) event.getItemId();
		    }
		});
		
		//button save to save the new configuration and close the window
		
		Button btnSave = new Button("Save");
		btnSave.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
				//element.setElementName(txtName.getValue()); elementName cannot be changed
				element.setLayerName(txtLayerName.getValue());
				element.setElementType(txtArchetype.getValue());
				if(layer.equals(LayerType.sensor)) {
					_facade.updateSensor(element);
				}
				else if(layer.equals(LayerType.logical)) {
					_facade.updateSmartObject(element);
				}
				System.out.println("New element configuration has been saved");
				close();
			}
		});

		subContent.addComponent(table);
		
		GridLayout form = new GridLayout(2, 3);

		TextField txtAttributeName = new TextField("Attribute name:");
		TextField txtAttributeValue = new TextField("Value:");
		  
		form.addComponent(txtAttributeName);
		form.addComponent(txtAttributeValue);
		
		
		HorizontalLayout tableControls = new HorizontalLayout();
		Button btnAdd = new Button("Add", new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				if(txtAttributeName.getValue().equals("") || txtAttributeValue.getValue().equals("")) {
					Notification.show("Please fill Attribute name and value");
					return;
				}
				Pair<String, String> sss = Pair.of(txtAttributeName.getValue(), txtAttributeValue.getValue());
				element.addAttribute(sss);
				reloadDataTable(table, element);
				txtAttributeName.setValue("");
				txtAttributeValue.setValue("");
			}
		});
		Button btnDelete = new Button("Delete", new ClickListener() {
			public void buttonClick(ClickEvent event) {
				
				String key = (String)table.getContainerProperty(selectedTableIndex,"Attribute name").getValue();
				String value = (String)table.getContainerProperty(selectedTableIndex,"Value").getValue();
				System.out.println(key + "  " + value);
				Pair<String, String> attributeToBeRemoved = Pair.of(key, value);
				element.removeAttribute(attributeToBeRemoved);
				reloadDataTable(table, element);
			}
		});

		tableControls.addComponent(btnAdd);
		tableControls.addComponent(btnDelete);
		
		subContent.addComponent(tableControls);
		subContent.addComponent(form);
		subContent.addComponent(btnSave);
		
		// Center it in the browser window
		center();
		// Set window size.
		setHeight("600px");
		setWidth("500px");
	}
	

	private void reloadDataTable(Table table, Element element) {
		table.removeAllItems();
		int i = 1;
		for (Pair<String, String> attribute : element.getAttributes()) {
			if(!attribute.getLeft().equals(RealToDigitalSyncData.ELEMENT_NAME) && 
					!attribute.getLeft().equals(RealToDigitalSyncData.STATUS) && 
					!attribute.getLeft().equals(RealToDigitalSyncData.PAIRING)) {
				
				table.addItem(new String[] { attribute.getLeft(), attribute.getRight() }, new Integer(i));
			}
			
			i++;
		}
	}

	
}