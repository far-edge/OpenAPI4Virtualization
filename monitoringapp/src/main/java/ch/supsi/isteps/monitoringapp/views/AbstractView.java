package ch.supsi.isteps.monitoringapp.views;

import com.vaadin.navigator.View;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.themes.ValoTheme;

abstract class AbstractView extends HorizontalLayout implements View {
	private static final long serialVersionUID = -4031273835967536174L;

	public AbstractView() {
		HorizontalLayout layout = new HorizontalLayout();
		layout.setSizeFull();
		Label label = new Label("< " + getViewName() + " >");
		label.addStyleName(ValoTheme.LABEL_H2);
		layout.addComponent(label);
		layout.setComponentAlignment(label, Alignment.MIDDLE_CENTER);
		Panel panel = new Panel(layout);
		panel.setSizeFull();
		addComponent(panel);
		setMargin(true);
		setSizeFull();
	}

	abstract String getViewName();
}