package ch.supsi.isteps.monitoringapp.views;

import org.vaadin.teemu.wizards.Wizard;
import org.vaadin.teemu.wizards.event.WizardCancelledEvent;
import org.vaadin.teemu.wizards.event.WizardCompletedEvent;
import org.vaadin.teemu.wizards.event.WizardProgressListener;
import org.vaadin.teemu.wizards.event.WizardStepActivationEvent;
import org.vaadin.teemu.wizards.event.WizardStepSetChangedEvent;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

import ch.supsi.isteps.monitoringapp.wizardsteps.LoadStep;
import ch.supsi.isteps.monitoringapp.wizardsteps.ActivateStep;
import ch.supsi.isteps.monitoringapp.wizardsteps.DetailsStep;

public class ConfigurationWizardView extends VerticalLayout implements View, WizardProgressListener {

	private static final long serialVersionUID = 134874685151382047L;
	private Wizard wizard;

	public ConfigurationWizardView() {
		// setup the main window

		setMargin(true);
		setSizeUndefined();

		// create the Wizard component and add the steps
		wizard = new Wizard();
		wizard.setUriFragmentEnabled(true);
		wizard.addListener(this);
		wizard.addStep(new LoadStep(), "loading");
		wizard.addStep(new DetailsStep(), "details");
		// wizard.addStep(new PairStep(), "pairing");
		wizard.addStep(new ActivateStep(), "activation");
		// wizard.setHeight("900px");
		wizard.setWidth("1100px");

		wizard.getCancelButton().setVisible(false);
		wizard.getFinishButton().setCaption("Save");
		addComponent(wizard);
		setComponentAlignment(wizard, Alignment.TOP_CENTER);
	}

	public void wizardCompleted(WizardCompletedEvent event) {
		endWizard("Wizard Completed!");
	}

	public void activeStepChanged(WizardStepActivationEvent event) {
		// display the step caption as the window title
		Page.getCurrent().setTitle(event.getActivatedStep().getCaption());
	}

	public void stepSetChanged(WizardStepSetChangedEvent event) {
		// NOP, not interested on this event
	}

	public void wizardCancelled(WizardCancelledEvent event) {
		endWizard("Wizard Cancelled!");
	}

	private void endWizard(String message) {
		Notification.show("Configuration has been saved");
		// wizard.setVisible(false);
		// Notification.show(message);
		// Page.getCurrent().setTitle(message);
		// Button startOverButton = new Button("Run the demo again",
		// new Button.ClickListener() {
		// private static final long serialVersionUID = 1L;
		// public void buttonClick(ClickEvent event) {
		// //UI.getCurrent().setContent(new IntroStep().getContent());
		// }
		// });
		// addComponent(startOverButton);
		// setComponentAlignment(startOverButton, Alignment.MIDDLE_CENTER);
	}
	

}