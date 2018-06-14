package ch.supsi.isteps.monitoringapp.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FileResource;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

import ch.supsi.isteps.monitoringapp.faredgeplatform.client.AbstractPlatformFacade;
import ch.supsi.isteps.monitoringapp.faredgeplatform.client.SingletonFacade;
import elemental.html.File;

public class DownloadConfigurationView extends VerticalLayout implements View {

	private static final long serialVersionUID = 7272636940887181004L;
	private AbstractPlatformFacade _facade = SingletonFacade.getInstance();;

	public DownloadConfigurationView() {

		setSizeFull();
		setMargin(true);
		setSpacing(true);

		// A container with a defined width.
		Panel panel = new Panel("Description on how to use the downloaded jar");
		panel.setWidth("400px");

		panel.setContent(new Label("The project has to be unzipped and it will contain <br> "
				+ "the following classes to be filled in the following way:<br>" + "<ul>"
				+ "  <li><b>senml.jar</b></li>" + "  <li>Class A: it's the XXX and contains the...</li>"
				+ "  <li>Class B: it's the YYY and contains the...</li>" + "</ul> "
				+ "examples to be followed for the implementation...", ContentMode.HTML));


		Button btn = new Button("Download");
		btn.setWidth("200px");
		Resource res = _facade.downloadConfigurationTemplate();
		FileDownloader fd = new FileDownloader(res);
		fd.extend(btn);
		

		final GridLayout grid = new GridLayout(1, 2);
		grid.setWidth("80%");
		grid.setHeight("80%");
		grid.addComponent(panel, 0, 0);
		grid.addComponent(btn, 0, 1);
		addComponent(grid);

	}

	@Override
	public void enter(ViewChangeEvent event) {
	}
}
