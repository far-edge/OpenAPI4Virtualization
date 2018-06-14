package ch.supsi.isteps.monitoringapp.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

import ch.supsi.isteps.monitoringapp.wizardsteps.DetailsStep;

public class ConfigurationStep2DetailsView extends VerticalLayout implements View {
	private static final long serialVersionUID = 7029323565336640264L;
	
	private Panel contentPanel;
	
	public ConfigurationStep2DetailsView() {
		contentPanel = new Panel();
		contentPanel.setContent(new DetailsStep().getContent());
		addComponent(contentPanel);
	}
	
	@Override
	public void enter(ViewChangeEvent event) {	
		contentPanel.setContent(new DetailsStep().getContent());		
	}
}