package ch.supsi.isteps.monitoringapp.views;

//import com.vaadin.data.util.BeanItemContainer;
//import com.vaadin.event.ItemClickEvent;
//import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Layout;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.event.ItemClickEvent;
import com.vaadin.v7.event.ItemClickEvent.ItemClickListener;
import com.vaadin.v7.ui.Grid;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;

import ch.supsi.isteps.monitoringapp.faredgeplatform.client.AbstractPlatformFacade;
import ch.supsi.isteps.monitoringapp.faredgeplatform.client.SingletonFacade;
import ch.supsi.isteps.monitoringapp.grid.KeyValueColumns;
import ch.supsi.isteps.monitoringapp.grid.SingleValueColumn;
import ch.supsi.isteps.monitoringapp.tools.Fields;

@SuppressWarnings("deprecation")
public class SensorsView extends VerticalLayout implements View {

	private static final long serialVersionUID = 8714765183631352380L;
	private AbstractPlatformFacade _facade = SingletonFacade.getInstance();;

	public SensorsView() {
		generateContent();
	}

	@Override
	public void enter(ViewChangeEvent event) {
		generateContent();
	}

	public void generateContent() {
		this.removeAllComponents();
		setSizeUndefined();
		setMargin(true);
		setSpacing(true);
		final Layout layout = new HorizontalLayout();
		Grid sensorGrid = new Grid();
		sensorGrid.setWidth("200px");
		if (_facade.isConnected()) {
			String sensorElements =  _facade.retrieveSensorElements();
			if(sensorElements == null) {
				sensorElements = "";
			}

			Fields retrieveSensorElements = Fields.fromRaw(sensorElements);
			BeanItemContainer<SingleValueColumn> container = new BeanItemContainer<>(SingleValueColumn.class);
			for (String each : retrieveSensorElements.allKeysStartingWith("activeSensors")) {
				container.addItem(new SingleValueColumn(retrieveSensorElements.firstValueFor(each)));
			}
			layout.addComponent(sensorGrid);
			sensorGrid.setContainerDataSource(container);
			HorizontalLayout horizontalLayout = new HorizontalLayout();
			sensorGrid.addItemClickListener(new ItemClickListener() {
				private static final long serialVersionUID = 7059863001721586637L;

				@Override
				public void itemClick(ItemClickEvent event) {
					horizontalLayout.removeAllComponents();
					if (event.getPropertyId() != null) {
						String sensorId = String.valueOf(event.getItem().getItemProperty("name").getValue());
						Fields result = Fields.fromRaw(_facade.retrieveSensorDataElements(sensorId));
						Grid currentTable = new Grid();
						BeanItemContainer<SingleValueColumn> container = new BeanItemContainer<>(
								SingleValueColumn.class);
						for (String each : result.allKeysStartingWith("elementName")) {
							container.addItem(new SingleValueColumn(result.firstValueFor(each)));
						}
						currentTable.setContainerDataSource(container);
						HorizontalLayout attributesLayout = new HorizontalLayout();
						currentTable.addItemClickListener(new ItemClickListener() {
							private static final long serialVersionUID = 1L;

							@Override
							public void itemClick(ItemClickEvent event) {
								attributesLayout.removeAllComponents();
								Grid attributesTable = new Grid();
								String elementName = String.valueOf(event.getItem().getItemProperty("name").getValue());
								Fields attributes = Fields.fromRaw(_facade.retrieveAttributesByElement(elementName));
								BeanItemContainer<KeyValueColumns> container = new BeanItemContainer<>(
										KeyValueColumns.class);
								for (String each : attributes.keys()) {
									container.addItem(new KeyValueColumns(each, attributes.firstValueFor(each)));
								}
								attributesTable.setContainerDataSource(container);
								attributesLayout.addComponent(attributesTable);
							}
						});
						horizontalLayout.addComponent(currentTable);
						horizontalLayout.addComponent(attributesLayout);
					} else {
						horizontalLayout.addComponent(new Label("Nothing selected"));
					}
				}
			});
			layout.addComponent(horizontalLayout);
			addComponent(layout);
		} else {
			Label noAvailableData = new Label("No available data");
			addComponent(noAvailableData);
		}
	}
}
