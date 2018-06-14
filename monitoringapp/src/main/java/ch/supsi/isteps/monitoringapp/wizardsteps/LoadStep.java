package ch.supsi.isteps.monitoringapp.wizardsteps;

import org.vaadin.teemu.wizards.WizardStep;

import com.vaadin.ui.Component;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

import ch.supsi.isteps.monitoringapp.views.UploadBoxView;

public class LoadStep implements WizardStep {

	public String getCaption() {
		return "Load configuration JAR";
	}

	@Override
	public Component getContent() {

		Layout content = new VerticalLayout();
		UploadBoxView uploadbox = new UploadBoxView();

		content.addComponent(uploadbox);

		// VerticalLayout content = new VerticalLayout(getText(), getArrow());
		// content.setMargin(true);
		return content;
	}

	public boolean onAdvance() {
		return true;
	}

	public boolean onBack() {
		return true;
	}

}