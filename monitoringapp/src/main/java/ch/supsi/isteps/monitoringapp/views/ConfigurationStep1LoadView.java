package ch.supsi.isteps.monitoringapp.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

import ch.supsi.isteps.monitoringapp.wizardsteps.LoadStep;

public class ConfigurationStep1LoadView extends VerticalLayout implements View {
	private static final long serialVersionUID = -7301672385951672947L;

	private Panel contentPanel;
	
	public ConfigurationStep1LoadView() {
		contentPanel = new Panel();
		contentPanel.setContent(new LoadStep().getContent());
		addComponent(contentPanel);
	}
	
	@Override
	public void enter(ViewChangeEvent event) {	
		contentPanel.setContent(new LoadStep().getContent());
	}
}
