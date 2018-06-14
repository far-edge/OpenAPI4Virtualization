package ch.supsi.isteps.monitoringapp.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class LoginErrorView extends VerticalLayout implements View {

	private static final long serialVersionUID = -8658850495912469417L;

	public LoginErrorView() {
		setSizeFull();
		setMargin(true);
		setSpacing(true);
		Label label = new Label("login Error");
		addComponent(label);
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}
}
