package ch.supsi.isteps.monitoringapp.views;

import java.io.File;

import org.apache.commons.lang3.tuple.Pair;

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
import ch.supsi.isteps.monitoringapp.tools.CPS;
import ch.supsi.isteps.monitoringapp.tools.LayerType;
import ch.supsi.isteps.monitoringapp.tools.SmartObject;

public class LoginView extends VerticalLayout implements View {

	private static final long serialVersionUID = -6817261082150489212L;
	private AbstractPlatformFacade _facade = SingletonFacade.getInstance();;

	public LoginView() {

		setSizeFull();
		final VerticalLayout layout = new VerticalLayout();
		final TextField userName = new TextField();
		userName.focus();
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
				navigator.navigateTo("loginError");
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

//		// for test purposes to be removed when tests are completed
//		Button btntest = new Button("Test me");
//		btntest.addClickListener(e -> {
//			try {
//				// code used to call actions server side
//
//				_facade.insertNewInExistingCPS("dummy1");
//				_facade.insertNewInExistingSmartObject("so1");
//				_facade.insertNewInExistingSmartObject("so1356");
//				//
//				// SmartObject so = _facade.getSmartObject("so1356");
//				// so.addAttribute(Pair.of("attributeso13dfgd", "valueso13f"));
//				// so.addAttribute(Pair.of("attributeso23dfgdfg", "valueso23ffdg"));
//				// _facade.saveSmartObject(so);
//				//
//				//
//				// _facade.getExistingSmartObjects();
//				//
//				_facade.removeExistingCPS("dummy1");
//				_facade.removeExistingSmartObject("so1356");
//				_facade.removeExistingSmartObject("so1");
//
//			} catch (Exception e1) {
//				e1.printStackTrace();
//			}
//		});
//		addComponent(btntest);
//		// to be removed till here

		addComponent(layout);
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}
}