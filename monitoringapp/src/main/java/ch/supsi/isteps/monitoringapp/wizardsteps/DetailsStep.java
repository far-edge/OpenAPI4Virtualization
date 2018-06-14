package ch.supsi.isteps.monitoringapp.wizardsteps;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.tuple.Pair;
import org.vaadin.teemu.wizards.WizardStep;

import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.ListSelect;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TwinColSelect;

import ch.supsi.isteps.monitoringapp.faredgeplatform.client.AbstractPlatformFacade;
import ch.supsi.isteps.monitoringapp.faredgeplatform.client.SingletonFacade;
import ch.supsi.isteps.monitoringapp.tools.LayerType;
import ch.supsi.isteps.monitoringapp.ui.ElementDetailsUIWindow;

@SuppressWarnings("deprecation")
public class DetailsStep implements WizardStep {

	private AbstractPlatformFacade _facade = SingletonFacade.getInstance();

	public String getCaption() {
		return "Details & Pairing";
	}

	@Override
	public Component getContent() {

		System.out.println("In the platform getContent " + _facade.isConnected());
		Layout content = new VerticalLayout();

		ListSelect existingCPS = new ListSelect("Existing CPSs");
		ListSelect loadedJars = new ListSelect("Installed JARs");
		ListSelect pairedobjects = new ListSelect("Paired CPS - JAR");
		TwinColSelect selectionCPS = new TwinColSelect("Select the active CPS from the list of the existing ones.");

		GridLayout grid = new GridLayout(4, 5);
		grid.setSpacing(true);

		// START EXISTING CPS
		// define the selection list with the existing CPS
		existingCPS.setNullSelectionAllowed(false);
		existingCPS.addItems(_facade.getSmartObjects());
		existingCPS.setWidth("200px");
		existingCPS.setRows(7);
		existingCPS.setImmediate(true);

		// add new button to edit CPS Attributes
		Button btnCPSDetails = new Button("Manage attributes");
		btnCPSDetails.setWidth("200px");

		btnCPSDetails.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -6984842929432032797L;

			@Override
			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
				if (existingCPS.getValue() != null) {
					System.out.println("To see details : " + existingCPS.getValue());

					// add window
					ElementDetailsUIWindow subWindow = new ElementDetailsUIWindow(existingCPS.getValue().toString(),LayerType.logical);
					UI.getCurrent().addWindow(subWindow);
				}
			}
		});

		// text box for the insertion of a new CPS
		TextField txtNewCPS = new TextField();
		txtNewCPS.setValue("New CPS name..");
		txtNewCPS.setWidth("200px");
		txtNewCPS.addFocusListener(new FocusListener() {
			private static final long serialVersionUID = -8296707576905272205L;

			@Override
			public void focus(FocusEvent event) {
				txtNewCPS.setValue("");
			}
		});
		// button to add the CPS to the list
		Button btnAddNewCPS = new Button("Add");
		btnAddNewCPS.setWidth("200px");

		btnAddNewCPS.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1579456963169428590L;

			@Override
			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
				if (!txtNewCPS.isEmpty()) {
					System.out.println(existingCPS.getVisibleItemIds());
					if(existingCPS.getVisibleItemIds().contains(txtNewCPS.getValue())) {	
							Notification.show("Creation not possible: Element with the same name already found in the list of existing elements.", Notification.TYPE_ERROR_MESSAGE);	
					}
					else {
						// save only the delta
						try {
							_facade.createSmartObject(txtNewCPS.getValue());
							existingCPS.addItem(txtNewCPS.getValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						txtNewCPS.clear();
					}
					
				}
			}
		});
		Button btnDeleteCPS = new Button("Delete");
		btnDeleteCPS.setWidth("200px");
		btnDeleteCPS.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 4391438651706334799L;

			@Override
			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
				//System.out.println("Active cps 1 " + _facade.getActiveSensors());
				if (existingCPS.getValue() != null) {
					System.out.println("To be removed: " + existingCPS.getValue());
					Boolean found = false;
					Object[] pairings = _facade.getPairings().toArray();
					for (int i = 0; i < pairings.length; i++) {
						String cps = getCPSNameFromPairing(pairings[i].toString());
						if (cps.equals(existingCPS.getValue())) {
							found = true;
							break;
						}
					}
					//System.out.println("Active cps 2 " + _facade.getActiveSensors());
					if (found || _facade.getActiveSensors().contains(existingCPS.getValue())) {
						if (found) {
							Notification.show(
									"Delete not possible: CPS found in the list of pairings. Please remove pairing first.",
									Notification.TYPE_ERROR_MESSAGE);
						} else {
							Notification.show(
									"Delete not possible: CPS found in the active CPSs list. Please disable it first.",
									Notification.TYPE_ERROR_MESSAGE);
						}
					} else {
						//System.out.println("Active cps 3" + _facade.getActiveSensors());
						_facade.deleteSensor(existingCPS.getValue().toString());
						existingCPS.removeAllItems();
						existingCPS.markAsDirty();
						existingCPS.addItems(_facade.getSmartObjects());
						selectionCPS.requestRepaint();
						selectionCPS.markAsDirty();
					}
				}
			}
		});
		grid.addComponent(existingCPS, 0, 0);
		grid.setComponentAlignment(existingCPS, Alignment.MIDDLE_LEFT);

		grid.addComponent(txtNewCPS, 0, 1);
		grid.setComponentAlignment(txtNewCPS, Alignment.MIDDLE_LEFT);

		grid.addComponent(btnAddNewCPS, 0, 2);
		grid.setComponentAlignment(btnAddNewCPS, Alignment.MIDDLE_LEFT);

		grid.addComponent(btnCPSDetails, 0, 3);
		grid.setComponentAlignment(btnCPSDetails, Alignment.MIDDLE_LEFT);

		grid.addComponent(btnDeleteCPS, 0, 4);
		grid.setComponentAlignment(btnDeleteCPS, Alignment.MIDDLE_LEFT);

		// END EXISTING CPS
		// START DETAILS TABLE
		Panel panel = new Panel();
		GridLayout panelGrid = new GridLayout(2, 1);
		panelGrid.setSpacing(true);
		Table tableDetails = new Table("JAR details");
		// tableDetails.setStyleName("table");
		tableDetails.setHeight("170");
		tableDetails.setWidth("500");
		tableDetails.addContainerProperty("Key", String.class, null);
		tableDetails.addContainerProperty("Value", String.class, null);
		// panelGrid.addComponent(tableDetails, 1, 0);
		// panelGrid.setComponentAlignment(tableDetails, Alignment.MIDDLE_RIGHT);
		// END DETAILS TABLE

		// START INSTALLED JARS
		loadedJars.setNullSelectionAllowed(false);
		loadedJars.addItems(_facade.getJars());
		loadedJars.setWidth("200px");
		loadedJars.setRows(7);
		Label spacing = new Label();
		spacing.setWidth("60px");

		Button btnDetails = new Button("View details");
		btnDetails.setWidth("200px");
		btnDetails.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 2419120557105604022L;

			@Override
			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
				if (loadedJars.getValue() != null) {
					tableDetails.removeAllItems();
					FillConfigurationDetailstable(_facade.getJarDetails(loadedJars.getValue().toString()));
					// VerticalLayout popupContent = new VerticalLayout();
					// popupContent.addComponent(tableDetails);
					// PopupView popup = new PopupView(null, popupContent);
					// popup.setPopupVisible(true);
					// Set window position.
					// popup.setSizeFull();
					// addComponent(popup);
					for (com.vaadin.ui.Window win : UI.getCurrent().getWindows()) {

						System.out.println(win.getCaption());
					}
				} else {
					Notification.show("No element selected.");
				}
			}

			private void FillConfigurationDetailstable(ArrayList<Pair<String, String>> jarDetails) {
				ArrayList<Pair<String, String>> tableElements = new ArrayList<>();
				for (Pair<String, String> eachPair : jarDetails) {
					if (eachPair.getLeft().contains("category")) {
						String key = eachPair.getRight();
						Boolean found = false;
						int index = 0;
						while (!found) {
							if (jarDetails.get(index).getLeft().equals(key)) {
								found = true;
							} else {
								index++;
							}
						}
						if (found) {
							tableElements.add(Pair.of(key, jarDetails.get(index).getRight()));
						}

					}
				}

				int i = 1;
				for (Pair<String, String> eachPair : tableElements) {
					tableDetails.addItem(new String[] { eachPair.getLeft(), eachPair.getRight() }, new Integer(i));
					i++;
				}

			}
		});
		Button btnpPair = new Button("Pair");
		btnpPair.setWidth("200px");

		loadedJars.setImmediate(true);

		btnpPair.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -279328464162342815L;

			@Override
			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {

				if ((existingCPS.getValue() != null) && (loadedJars.getValue() != null)
						&& !inList(pairedobjects, existingCPS.getValue().toString())) {
					String newPairing = existingCPS.getValue() + " - " + loadedJars.getValue();
					pairedobjects.addItem(existingCPS.getValue() + " - " + loadedJars.getValue());
					Notification.show("New pairing has been added");
					_facade.savePairing(newPairing);
					_facade.savePairedSensor(existingCPS.getValue().toString(), loadedJars.getValue().toString());
					selectionCPS.addItem(existingCPS.getValue());
				} else {

					if (existingCPS.getValue() == null)
						Notification.show("Missing sensor selection for the pairing");
					if (loadedJars.getValue() == null)
						Notification.show("Missing JAR selection for the pairing");
					if (inList(pairedobjects, existingCPS.getValue().toString()))
						Notification.show("Key " + existingCPS.getValue().toString() + " already paired");
				}
			}
		});

		Button btnDeleteJar = new Button("Delete");
		btnDeleteJar.setWidth("200px");
		btnDeleteJar.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 9110463523742547006L;

			@Override
			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
				if (loadedJars.getValue() != null) {
					Boolean found = false;
					Object[] pairings = _facade.getPairings().toArray();
					for (int i = 0; i < pairings.length; i++) {
						if (pairings[i].toString().contains(loadedJars.getValue().toString())) {
							found = true;
							break;
						}
					}
					if (found) {
						Notification.show(
								"Delete not possible: CPS found in the list of pairings. Please remove pairing first.",
								Notification.TYPE_ERROR_MESSAGE);
					} else {
						// remove the jar
						System.out.println("To be removed: " + loadedJars.getValue());
						_facade.removeLoadedJAR(loadedJars.getValue().toString());
						System.out.println("Removed from list");
						loadedJars.removeAllItems();
						loadedJars.markAsDirty();
						loadedJars.addItems(_facade.getJars());
						tableDetails.removeAllItems();
					}
				}
			}
		});

		HorizontalLayout hr = new HorizontalLayout();
		hr.addComponent(loadedJars);
		hr.addComponent(tableDetails);

		panel.setContent(hr);

		grid.addComponent(panel, 1, 0);
		grid.setComponentAlignment(panel, Alignment.MIDDLE_LEFT);

		Label l = new Label();
		l.setWidth("715px");
		grid.addComponent(l, 1, 4);

		// grid.addComponent(loadedJars, 1, 0);
		// grid.setComponentAlignment(loadedJars, Alignment.MIDDLE_LEFT);

		// grid.addComponent(tableDetails, 2, 0);
		// grid.setComponentAlignment(tableDetails, Alignment.MIDDLE_LEFT);

		grid.addComponent(btnDetails, 1, 1);
		grid.setComponentAlignment(btnDetails, Alignment.MIDDLE_LEFT);

		grid.addComponent(btnpPair, 1, 2);
		grid.setComponentAlignment(btnpPair, Alignment.MIDDLE_LEFT);

		grid.addComponent(btnDeleteJar, 1, 3);
		grid.setComponentAlignment(btnDeleteJar, Alignment.MIDDLE_LEFT);
		// END INSTALLED JARS

		content.addComponent(grid);

		// START PAIRED OBJECTS
		// list of the pairing SENSOR-JAR
		content.addComponent(new Label(""));
		pairedobjects.setNullSelectionAllowed(false);
		pairedobjects.addItems(_facade.getPairings());
		pairedobjects.setWidth("460px");
		pairedobjects.setRows(5);
		content.addComponent(pairedobjects);
		Button btnRemovePairedEntry = new Button("Delete entry");
		btnRemovePairedEntry.setWidth("200px");
		btnRemovePairedEntry.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -3124362647774931629L;

			@Override
			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
				if (pairedobjects.getValue() != null) {
					String toBeRemoved = pairedobjects.getValue().toString();
					String cps = getCPSNameFromPairing(toBeRemoved);

					System.out.println("CPS extracted: " + cps);
					System.out.println("Active CPS: ");
					for (int i = 0; i < _facade.getActiveSensors().size(); i++) {
						System.out.println("Active " + _facade.getActiveSensors().toArray()[i]);
					}
					if (_facade.getActiveSensors().contains(cps)) {

						Notification.show(
								"Delete not possible: CPS found in the active CPSs list. Please disable it first.",
								Notification.TYPE_ERROR_MESSAGE);
					} else {
						System.out.println("To be removed: " + toBeRemoved);
						// remove from the list of pairings in GUI
						pairedobjects.removeItem(toBeRemoved);
						// remove from the list
						_facade.removePairing(cps);
						// remove cps from the twin selecion list and also remove it from the list of
						// paired CPS visualized
						List<String> pairedCPSList = _facade.getPairedSensors();
						List<String> toBeRemovedCPS = new ArrayList<>();
						for (String pairedCPS : pairedCPSList) {
							if (toBeRemoved.contains(pairedCPS)) {
								toBeRemovedCPS.add(pairedCPS);
							}
						}
						System.out.println(toBeRemovedCPS);
						for (String cpsToBeRemoved : toBeRemovedCPS) {
							_facade.deletePairedSensor(cpsToBeRemoved);
							selectionCPS.removeItem(cpsToBeRemoved);
						}
					}
				} else {
					Notification.show("No element to be removed selected.");
				}
			}
		});
		content.addComponent(btnRemovePairedEntry);
		// END PAIREDOBJECTS

		return content;
	}

	public boolean onAdvance() {
		return true;
	}

	public boolean onBack() {
		return true;
	}

	private Boolean inList(ListSelect list, String inputElement) {
		Boolean found = false;
		ArrayList<String> items = new ArrayList<String>();
		items.addAll((Collection<String>) list.getItemIds());
		for (String item : items) {
			if (item.contains(inputElement)) {
				System.out.println("Element in List");
				found = true;
			}
			if (found)
				break;
		}
		return found;
	}

	private String getCPSNameFromPairing(String inputPairing) {
		String cps = "";
		Pattern regex = Pattern.compile(".+?(?=\\ -)");
		Matcher regexMatcher = regex.matcher(inputPairing);
		if (regexMatcher.find()) {
			cps = regexMatcher.group();
		}
		return cps;
	}

}