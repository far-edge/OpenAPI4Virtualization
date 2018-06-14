package ch.supsi.isteps.monitoringapp.old;

import java.util.ArrayList;
import java.util.Collection;

import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.ui.ListSelect;
import com.vaadin.v7.ui.TwinColSelect;

import ch.supsi.isteps.monitoringapp.faredgeplatform.client.AbstractPlatformFacade;
import ch.supsi.isteps.monitoringapp.faredgeplatform.client.SingletonFacade;

@SuppressWarnings("deprecation")
public class ConfigurationView extends VerticalLayout implements View {

	private static final long serialVersionUID = 7272636940887181004L;
	private AbstractPlatformFacade _facade = SingletonFacade.getInstance();;

	public ConfigurationView() {
		
		setMargin(true);
		setSpacing(true);
		final VerticalLayout layoutFirst = new VerticalLayout();
		final VerticalLayout layoutSecond = new VerticalLayout();

		addComponent(layoutFirst);

		ListSelect pairedobjects = new ListSelect("Paired CPS - JAR");

		// layout for the pairing SENSOR-JAR
		HorizontalLayout horizontalPairing = new HorizontalLayout();

		// define the selection list with the existing CPS
		ListSelect existingCPS = new ListSelect("Existing CPSs");
		existingCPS.setNullSelectionAllowed(false);
		existingCPS.addItems(_facade.getExistingSensors());
		existingCPS.setWidth("200px");
		existingCPS.setRows(5);
		existingCPS.setImmediate(true);


		// text box for the insertion of a new CPS
		TextField newCPS = new TextField();
		newCPS.setValue("New CPS name..");
		newCPS.setWidth("200px");
		newCPS.addFocusListener(new FocusListener() {
			
			@Override
			public void focus(FocusEvent event) {
				newCPS.setValue("");	
			}
		});
		// button to add the CPS to the list
		Button btAddNewCPS = new Button("Add");
		btAddNewCPS.setWidth("200px");

		btAddNewCPS.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
				if (!newCPS.isEmpty()) {
					existingCPS.addItem(newCPS.getValue());
					//save only the delta
					try {
						_facade.createSensor(newCPS.getValue());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					newCPS.clear();
					//platformClient.saveExistingCPS(getElements(existingCPS));
				}
			}
		});

		// vertical layout for the selection list + textbox + add button
		VerticalLayout existingCPSLayout = new VerticalLayout();
		existingCPSLayout.addComponent(existingCPS);
		existingCPSLayout.addComponent(newCPS);
		existingCPSLayout.addComponent(btAddNewCPS);

		ListSelect loadedJars = new ListSelect("Installed JARs");
		loadedJars.setNullSelectionAllowed(false);
		loadedJars.addItems(_facade.getJars());
		loadedJars.setWidth("200px");
		loadedJars.setRows(5);
		Label spacing = new Label();
		spacing.setWidth("60px");

		Button pair = new Button("Pair");
		pair.setWidth("200px");
		
		loadedJars.setImmediate(true);

		pair.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {

				if ((existingCPS.getValue() != null) && (loadedJars.getValue() != null) && !inList(pairedobjects, existingCPS.getValue().toString())) {
					String newPairing = existingCPS.getValue() + " - " + loadedJars.getValue(); 
					pairedobjects.addItem(existingCPS.getValue() + " - " + loadedJars.getValue());
					Notification.show("New pairing has been added");
					_facade.savePairing(newPairing);
				} else {
					
					if (existingCPS.getValue() == null)
						Notification.show("Missing sensor selection for the pairing");
					if (loadedJars.getValue() == null)
						Notification.show("Missing JAR selection for the pairing");
					if(inList(pairedobjects, existingCPS.getValue().toString()))
						Notification.show("Key " + existingCPS.getValue().toString() + " already paired");
				}
			}
		});
		// vertical layout for the jars list + pair button
		VerticalLayout jarsLayout = new VerticalLayout();
		jarsLayout.addComponent(loadedJars);
		jarsLayout.addComponent(pair);

		// add the selectionlists to the layout
		horizontalPairing.addComponent(existingCPSLayout);
		horizontalPairing.addComponent(spacing);
		horizontalPairing.addComponent(jarsLayout);
		layoutFirst.addComponent(horizontalPairing);

		// list of the pairing SENSOR-JAR
		pairedobjects.setNullSelectionAllowed(false);
		pairedobjects.addItems(_facade.getPairings());
		pairedobjects.setWidth("460px");
		pairedobjects.setRows(5);
		layoutFirst.addComponent(pairedobjects);

		Button next = new Button("Next ->");
		next.setWidth("100px");
		next.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
				removeComponent(layoutFirst);
				addComponent(layoutSecond);
			}
		});
		layoutFirst.addComponent(next);
		Button removeEntry = new Button("Delete entry");
		removeEntry.setWidth("200px");
		removeEntry.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
				if(pairedobjects.getValue()!= null)
				{
					pairedobjects.removeItem(pairedobjects.getValue());
					_facade.savePairing(pairedobjects.getValue().toString());
				}
				else
				{
					Notification.show("No element to be removed selected.");
				}
			}
		});
		//layoutFirst.addComponent(removeEntry);
		// Create a grid layout
		GridLayout gridFirst = new GridLayout(3, 3);

		gridFirst.setWidth("460px");
		gridFirst.setHeight("200px");

		gridFirst.addComponent(removeEntry, 0, 0);
		gridFirst.setComponentAlignment(removeEntry, Alignment.MIDDLE_LEFT);

		gridFirst.addComponent(next, 2, 2);
		gridFirst.setComponentAlignment(next, Alignment.BOTTOM_RIGHT);
		layoutFirst.addComponent(gridFirst);

		// second layout visible when the next button is pressed
		TwinColSelect selectionCPS = new TwinColSelect("Select the active CPS from the list of the existing ones.");
		selectionCPS.setNullSelectionAllowed(false);

		// Insert items in the select
		selectionCPS.addItems(_facade.getExistingSensors());

		// Put some items in the select

		for (Object cps : _facade.getActiveSensors()) {
			System.out.println("Selected " + cps);
			selectionCPS.select(cps);
		}
		selectionCPS.setRows(5);

		selectionCPS.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			public void valueChange(ValueChangeEvent event) {
				ArrayList<String> list = new ArrayList<String>();
				//platformClient.saveActiveCPS(selectionCPS.getValue().toString());
			}
		});
		selectionCPS.setImmediate(true);
		layoutSecond.addComponent(selectionCPS);
		Button back = new Button("<- Back");
		back.setWidth("100px");
		back.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
				removeComponent(layoutSecond);
				addComponent(layoutFirst);
			}
		});
		Button save = new Button("Save!", click -> Notification.show("Configuration has been saved"));
		save.setWidth("100px");
		layoutSecond.addComponents(back, save);
		
		
		// Create a grid layout
		GridLayout gridSecond = new GridLayout(3, 3);

		gridSecond.setWidth("400px");
		gridSecond.setHeight("200px");

		gridSecond.addComponent(back, 0, 2);
		gridSecond.setComponentAlignment(back, Alignment.BOTTOM_LEFT);

		gridSecond.addComponent(save, 2, 2);
		gridSecond.setComponentAlignment(save, Alignment.BOTTOM_CENTER);
		layoutSecond.addComponent(gridSecond);

	}

	@Override
	public void enter(ViewChangeEvent event) {
	}

	private ArrayList<String> getElements(ListSelect listSelect) 
	{
		ArrayList<String> list = new ArrayList<String>();
		list.addAll((Collection<String>) listSelect.getItemIds());
		return list;
	}
	private Boolean inList(ListSelect list, String inputElement) 
	{
		Boolean found = false;
		ArrayList<String> items = new ArrayList<String>();
		items.addAll((Collection<String>) list.getItemIds());
		for (String item : items) {
			if(item.contains(inputElement))
			{
				System.out.println("Element in List");
				found = true;
			}
			if (found)
				break;
		}
		return found;
	}
}