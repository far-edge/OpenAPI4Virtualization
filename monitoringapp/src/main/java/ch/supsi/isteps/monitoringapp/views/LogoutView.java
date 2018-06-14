package ch.supsi.isteps.monitoringapp.views;

import java.io.File;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Button;
import com.vaadin.ui.Image;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import ch.supsi.isteps.monitoringapp.faredgeplatform.client.AbstractPlatformFacade;
import ch.supsi.isteps.monitoringapp.faredgeplatform.client.SingletonFacade;

public class LogoutView extends VerticalLayout implements View {

	private static final long serialVersionUID = -8781563222569538252L;
	private AbstractPlatformFacade _facade = SingletonFacade.getInstance();;

	public LogoutView() {
		setSizeFull();
		final VerticalLayout layout = new VerticalLayout();
		final TextField userName = new TextField();
		userName.setCaption("Username:");

		final PasswordField password = new PasswordField();
		password.setCaption("Password:");

		Button button = new Button("Sign In");
		button.setClickShortcut(KeyCode.ENTER);
		button.addClickListener(e -> {
			Navigator navigator = UI.getCurrent().getNavigator();
			if (_facade.compareCredentials(userName.getValue(), password.getValue())) {
				getUI().getSession().setAttribute("ExistingSession", true);
				Page.getCurrent().reload();
			} else {
				navigator.addView("loginError", new LoginErrorView());
				navigator.navigateTo("loginError");
				Page.getCurrent().reload();
			}
		});
		String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
		FileResource resource = new FileResource(
				new File(basepath + "/VAADIN/themes/demo/faredge_logo_ecosystems.png"));
		// Show the image in the application
		Image image = new Image("", resource);
		image.setWidth(200, Unit.PIXELS);
		image.setHeight(88, Unit.PIXELS);

		layout.addComponents(image, userName, password, button);
		layout.setMargin(true);
		layout.setSpacing(true);
		this.addComponent(layout);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}

}
