package ch.supsi.isteps.monitoringapp.old;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.tuple.Pair;

import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.FailedListener;
import com.vaadin.ui.Upload.ProgressListener;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.ui.ListSelect;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TwinColSelect;

import ch.supsi.isteps.monitoringapp.data.ConfigurationData;
import ch.supsi.isteps.monitoringapp.faredgeplatform.client.AbstractPlatformFacade;
import ch.supsi.isteps.monitoringapp.faredgeplatform.client.SingletonFacade;
import ch.supsi.isteps.monitoringapp.tools.ElementType;
import ch.supsi.isteps.monitoringapp.tools.LayerType;
import ch.supsi.isteps.monitoringapp.ui.ElementDetailsUIWindow;

@SuppressWarnings("deprecation")
public class NewConfigurationView extends VerticalLayout implements View {

	private static final long serialVersionUID = 7272636940887181004L;
	private AbstractPlatformFacade _facade = SingletonFacade.getInstance();;

	public NewConfigurationView() {

		setMargin(true);
		setSpacing(true);

		ListSelect existingCPS = new ListSelect("Existing CPSs");
		ListSelect loadedJars = new ListSelect("Installed JARs");
		ListSelect pairedobjects = new ListSelect("Paired CPS - JAR");
		TwinColSelect selectionCPS = new TwinColSelect("Select the active CPS from the list of the existing ones.");

		// START UPLOAD COMPONENT
		class UploadBox extends CustomComponent
				implements Receiver, ProgressListener, FailedListener, SucceededListener {
			private static final long serialVersionUID = -46336015006190050L;

			// Put upload in this memory buffer that grows automatically
			ByteArrayOutputStream os = new ByteArrayOutputStream(10240);

			// Name of the uploaded file
			String filename;
			Upload upload = new Upload();
			ProgressBar progress = new ProgressBar(0.0f);

			// Show uploaded file in this placeholder
			Image image = new Image("Uploaded File");

			public UploadBox() {
				// Create the upload component and handle all its events
				// Upload upload = new Upload("", null);
				upload.setReceiver(this);
				upload.addProgressListener(this);
				upload.addFailedListener(this);
				upload.addSucceededListener(this);

				// Put the upload and image display in a panel
				Panel panel = new Panel("Upload your new configuration in a Jar file here");
				panel.setWidth("400px");
				VerticalLayout panelContent = new VerticalLayout();
				panelContent.setSpacing(true);
				panel.setContent(panelContent);
				panelContent.addComponent(upload);
				panelContent.addComponent(progress);
				panelContent.addComponent(image);

				progress.setWidth("300px");
				progress.setVisible(false);
				image.setVisible(false);

				setCompositionRoot(panel);
			}

			public OutputStream receiveUpload(String filename, String mimeType) {
				this.filename = filename;

				// platformClient.saveConfigurationBAOS(os, filename);
				this.os.reset(); // Needed to allow re-uploading
				return os;
			}

			@Override
			public void updateProgress(long readBytes, long contentLength) {
				progress.setVisible(true);
				if (contentLength == -1)
					progress.setIndeterminate(true);
				else {
					progress.setIndeterminate(false);
					progress.setValue(((float) readBytes) / ((float) contentLength));
				}
			}

			public void uploadSucceeded(SucceededEvent event) {
				image.setVisible(true);
				image.setCaption("Uploaded File " + filename + " has length " + os.toByteArray().length);

				// Display the image as a stream resource from
				// the memory buffer
				StreamSource source = new StreamSource() {
					private static final long serialVersionUID = -4905654404647215809L;

					public InputStream getStream() {
						return new ByteArrayInputStream(os.toByteArray());
					}
				};
				_facade.uploadBAOS(os, filename);
				if (image.getSource() == null)
					// Create a new stream resource
					image.setSource(new StreamResource(source, filename));
				else { // Reuse the old resource
					StreamResource resource = (StreamResource) image.getSource();
					resource.setStreamSource(source);
					resource.setFilename(filename);
				}
				image.markAsDirty();
			}

			@Override
			public void uploadFailed(FailedEvent event) {
				Notification.show("Upload failed", Notification.Type.ERROR_MESSAGE);
			}

		}
		UploadBox uploadbox = new UploadBox();

		uploadbox.upload.addSucceededListener(succeed -> {
			loadedJars.removeAllItems();
			loadedJars.addItems(_facade.getJars());
		});
		addComponent(uploadbox);

		// END UPLOAD COMPONENT

		GridLayout grid = new GridLayout(4, 5);
		grid.setSpacing(true);

		// START EXISTING CPS
		// define the selection list with the existing CPS
		existingCPS.setNullSelectionAllowed(false);
		existingCPS.addItems(_facade.getExistingSensors());
		existingCPS.setWidth("200px");
		existingCPS.setRows(7);
		existingCPS.setImmediate(true);

		// add new button to edit CPS Attributes
		Button btnCPSDetails = new Button("Manage attributes");
		btnCPSDetails.setWidth("200px");

		btnCPSDetails.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
				System.out.println("Active cps 1 " + _facade.getActiveSensors());
				if (existingCPS.getValue() != null) {
					System.out.println("To see details : " + existingCPS.getValue());

					// add window
					ElementDetailsUIWindow subWindow = new ElementDetailsUIWindow(existingCPS.getValue().toString(),
							LayerType.sensor);
					UI.getCurrent().addWindow(subWindow);
				}
			}
		});

		// text box for the insertion of a new CPS
		TextField txtNewCPS = new TextField();
		txtNewCPS.setValue("New CPS name..");
		txtNewCPS.setWidth("200px");
		txtNewCPS.addFocusListener(new FocusListener() {
			@Override
			public void focus(FocusEvent event) {
				txtNewCPS.setValue("");
			}
		});
		// button to add the CPS to the list
		Button btnAddNewCPS = new Button("Add");
		btnAddNewCPS.setWidth("200px");

		btnAddNewCPS.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
				if (!txtNewCPS.isEmpty()) {
					existingCPS.addItem(txtNewCPS.getValue());
					// save only the delta
					try {
						_facade.createSensor(txtNewCPS.getValue());
					} catch (Exception e) {
						e.printStackTrace();
					}
					txtNewCPS.clear();
					// platformClient.saveExistingCPS(getElements(existingCPS));
				}
			}
		});
		Button btnDeleteCPS = new Button("Delete");
		btnDeleteCPS.setWidth("200px");
		btnDeleteCPS.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
				System.out.println("Active cps 1 " + _facade.getActiveSensors());
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
					System.out.println("Active cps 2 " + _facade.getActiveSensors());
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
						System.out.println("Active cps 3" + _facade.getActiveSensors());
						_facade.deleteSensor(existingCPS.getValue().toString());
						existingCPS.removeAllItems();
						existingCPS.markAsDirty();
						existingCPS.addItems(_facade.getExistingSensors());
						System.out.println("Active cps 4 " + _facade.getActiveSensors());
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

		addComponent(grid);

		// START PAIRED OBJECTS
		// list of the pairing SENSOR-JAR
		addComponent(new Label(""));
		pairedobjects.setNullSelectionAllowed(false);
		pairedobjects.addItems(_facade.getPairings());
		pairedobjects.setWidth("460px");
		pairedobjects.setRows(5);
		addComponent(pairedobjects);
		Button btnRemovePairedEntry = new Button("Delete entry");
		btnRemovePairedEntry.setWidth("200px");
		btnRemovePairedEntry.addClickListener(new ClickListener() {
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
						_facade.removePairing(toBeRemoved);
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
		addComponent(btnRemovePairedEntry);
		// END PAIREDOBJECTS

		addComponent(new Label(""));

		// START ACTIVE CPS SELECTION
		selectionCPS.setNullSelectionAllowed(true);

		// Insert items in the left list
		selectionCPS.addItems(_facade.getPairedSensors());

		// Put some items in the select right list
		for (Object cps : _facade.getActiveSensors()) {
			selectionCPS.select(cps);
		}
		selectionCPS.setRows(5);

		selectionCPS.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			public void valueChange(ValueChangeEvent event) {
				ArrayList<String> list = new ArrayList<String>((Collection) event.getProperty().getValue());
				_facade.saveActiveSensor(list);
			}
		});
		selectionCPS.setImmediate(true);
		addComponent(selectionCPS);
		// END ACTIVE CPS SELECTION

	}

	@Override
	public void enter(ViewChangeEvent event) {
	}

	private ArrayList<String> getElements(ListSelect listSelect) {
		ArrayList<String> list = new ArrayList<String>();
		list.addAll((Collection<String>) listSelect.getItemIds());
		return list;
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