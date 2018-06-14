package ch.supsi.isteps.monitoringapp.wizardsteps;

import java.util.ArrayList;

import java.util.Collection;

import org.vaadin.teemu.wizards.WizardStep;

import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.ui.TwinColSelect;

import ch.supsi.isteps.monitoringapp.faredgeplatform.client.AbstractPlatformFacade;
import ch.supsi.isteps.monitoringapp.faredgeplatform.client.SingletonFacade;
@SuppressWarnings("deprecation")
public class ActivateStep implements WizardStep {

	private AbstractPlatformFacade _facade = SingletonFacade.getInstance();;

	public String getCaption() {
		return "Activation";
	}

	
	public Component getContent() {
		VerticalLayout content = new VerticalLayout();
		TwinColSelect selectionCPS = new TwinColSelect("Select the active CPS from the list of the existing ones.");

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
		content.addComponent(selectionCPS);
		// END ACTIVE CPS SELECTION
		content.setMargin(true);
		return content;
	}


	public boolean onAdvance() {
		return true;
	}

	public boolean onBack() {
		return true;
	}

}
