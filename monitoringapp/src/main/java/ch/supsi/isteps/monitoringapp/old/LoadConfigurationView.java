package ch.supsi.isteps.monitoringapp.old;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.v7.ui.ListSelect;
import com.vaadin.v7.ui.Table;

import ch.supsi.isteps.monitoringapp.data.ConfigurationData;
import ch.supsi.isteps.monitoringapp.faredgeplatform.client.AbstractPlatformFacade;
import ch.supsi.isteps.monitoringapp.faredgeplatform.client.SingletonFacade;
import ch.supsi.isteps.monitoringapp.views.UploadBoxView;

@SuppressWarnings("deprecation")
public class LoadConfigurationView extends VerticalLayout implements View {

	private static final long serialVersionUID = 7272636940887181004L;
	private OutputStream outputFile = null;
	private AbstractPlatformFacade _facade = SingletonFacade.getInstance();;

	public LoadConfigurationView() {

		setSizeFull();
		setMargin(true);
		setSpacing(true);
		final Layout layout = new VerticalLayout();
		addComponent(layout);
		ListSelect loadedJars = new ListSelect("Installed JARs");

		UploadBoxView uploadbox = new UploadBoxView();
		uploadbox.upload.addSucceededListener(succeed -> {
			loadedJars.removeAllItems();
			loadedJars.addItems(_facade.getJars());
		});

		// layout.addComponent(uploadbox);

		loadedJars.setNullSelectionAllowed(false);
		loadedJars.addItems(_facade.getJars());
		loadedJars.setWidth("200px");
		loadedJars.setRows(5);

		Table table = new Table();
		table.setHeight("77");

		Button details = new Button("View details");
		details.setWidth("200px");
		details.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
				if (loadedJars.getValue() != null) {
					table.removeAllItems();
					FillConfigurationDetailstable(_facade.getJarDetails(loadedJars.getValue().toString()));
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
				List<String> tableEntry = new ArrayList<String>();
				for (Pair<String, String> eachPair : tableElements) {
					table.addContainerProperty(eachPair.getLeft(), String.class, null);
					tableEntry.add(eachPair.getRight());
				}
				table.addItem(tableEntry.toArray(), new Integer(1));
			}
		});

		loadedJars.setImmediate(true);
		// layout.addComponent(loadedJars);
		// layout.addComponent(details);
		GridLayout gridFirst = new GridLayout(3, 3);

		gridFirst.setWidth("750px");
		gridFirst.setHeight("200px");

		gridFirst.addComponent(uploadbox, 0, 0);
		gridFirst.setComponentAlignment(uploadbox, Alignment.MIDDLE_LEFT);

		gridFirst.addComponent(loadedJars, 2, 0);
		gridFirst.setComponentAlignment(loadedJars, Alignment.TOP_RIGHT);
		gridFirst.addComponent(details, 2, 1);
		gridFirst.setComponentAlignment(details, Alignment.TOP_RIGHT);

		layout.addComponent(gridFirst);
		layout.addComponent(table);

	}

	@Override
	public void enter(ViewChangeEvent event) {
	}
}
