package ch.supsi.isteps.monitoringapp.ui;

import static com.github.appreciated.app.layout.builder.AppLayoutBuilder.Position.HEADER;

import java.util.function.Consumer;

import javax.servlet.annotation.WebServlet;

import com.github.appreciated.app.layout.behaviour.AppLayout;
import com.github.appreciated.app.layout.behaviour.Behaviour;
import com.github.appreciated.app.layout.builder.AppLayoutBuilder;
import com.github.appreciated.app.layout.builder.design.AppBarDesign;
import com.github.appreciated.app.layout.builder.elements.SubmenuBuilder;
import com.github.appreciated.app.layout.builder.entities.DefaultBadgeHolder;
import com.github.appreciated.app.layout.builder.entities.DefaultNotificationHolder;
import com.github.appreciated.app.layout.component.MenuHeader;
import com.github.appreciated.app.layout.interceptor.DefaultViewNameInterceptor;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Viewport;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import ch.supsi.isteps.monitoringapp.data.ConfigurationData;
import ch.supsi.isteps.monitoringapp.faredgeplatform.client.AbstractPlatformFacade;
import ch.supsi.isteps.monitoringapp.faredgeplatform.client.SingletonFacade;
import ch.supsi.isteps.monitoringapp.views.ConfigurationStep1LoadView;
import ch.supsi.isteps.monitoringapp.views.ConfigurationStep2DetailsView;
import ch.supsi.isteps.monitoringapp.views.ConfigurationStep3ActivateView;
import ch.supsi.isteps.monitoringapp.views.ConfigurationWizardView;
import ch.supsi.isteps.monitoringapp.views.DownloadConfigurationView;
import ch.supsi.isteps.monitoringapp.views.HomeView;
import ch.supsi.isteps.monitoringapp.views.LoginErrorView;
import ch.supsi.isteps.monitoringapp.views.LoginView;
import ch.supsi.isteps.monitoringapp.views.LogoutView;
import ch.supsi.isteps.monitoringapp.views.SensorsView;
import ch.supsi.isteps.monitoringapp.wizardsteps.LoadStep;

@Viewport("initial-scale=1, maximum-scale=1")
@Theme("demo")
@Title("FAREDGE")
@Push(transport = Transport.WEBSOCKET_XHR)

public class MainUI extends UI {

	private DefaultNotificationHolder notifications = new DefaultNotificationHolder();
	private DefaultBadgeHolder badge = new DefaultBadgeHolder();
	private VerticalLayout holder;
	private AbstractPlatformFacade _facade = SingletonFacade.getInstance();

	//private AbstractPlatformFacade platformClient = new FakePlatformFacade();
	//private AbstractPlatformFacade platformClient = new FaredgePlatformFacade(ConfigurationData.REAL_TO_DIGITAL_SYNCHRONIZATION_URL);

	private Navigator navigator;

	public boolean sessionExists = false;

	@Override
	protected void init(VaadinRequest request) {
		navigator = new Navigator(this, this);
		navigator.addView("loginError", new LoginErrorView());
		navigator.addView("", new LoginView());
		holder = new VerticalLayout();
		holder.setMargin(false);

		try {
			sessionExists = (boolean) getUI().getSession().getAttribute("ExistingSession");
			System.out.println("Session " + getUI().getSession().getAttribute("ExistingSession"));
		} catch (Exception e) {
			getUI().getSession().setAttribute("ExistingSession", false);
		}


		if (sessionExists) {
			setDrawerVariant(Behaviour.LEFT_RESPONSIVE);
		} else {
			holder.addComponent(new LoginView());
		}
		setContent(holder);
		holder.setSizeFull();
	}

	private void setDrawerVariant(Behaviour variant) {
		holder.removeAllComponents();
		
		AppLayoutBuilder builder = AppLayoutBuilder.get(variant).withTitle("VF CONFIGURATION TOOL")
				.withViewNameInterceptor(new DefaultViewNameInterceptor()).withDefaultNavigationView(HomeView.class)
				.withDesign(AppBarDesign.MATERIAL).withNavigatorConsumer(navigator -> {
				}).add(new MenuHeader("Version 0.0.1", new ThemeResource("Far.png")), HEADER)
				.add("Home", VaadinIcons.HOME, new HomeView())

				.add(SubmenuBuilder.get("Plant configuration", VaadinIcons.PLUS)
						.add("Dowload prototype", VaadinIcons.DOWNLOAD, new DownloadConfigurationView())
						.add("Configuration wizard", VaadinIcons.COGS, new ConfigurationWizardView())
						.add("Upload step", VaadinIcons.UPLOAD, new ConfigurationStep1LoadView())
						.add("Details & Pairing step", VaadinIcons.COMPILE, new ConfigurationStep2DetailsView())
						.add("Activate step", VaadinIcons.BOLT, new ConfigurationStep3ActivateView())
//						.add("CPS Configuration", VaadinIcons.COGS, new CPSConfigurationView())
						.build())
				.add(SubmenuBuilder.get("Connection setup", VaadinIcons.PLUS)
						.addClickable("Fill mockup data", VaadinIcons.COG, new Button.ClickListener() {
							public void buttonClick(ClickEvent event) {
								System.out.println("You have pressed Fill mockup data");
								_facade.setup();
							}
						}).addClickable("Clear mockup data", VaadinIcons.RECYCLE, new Button.ClickListener() {
							public void buttonClick(ClickEvent event) {
								System.out.println("You have pressed Clear mockup data");
								// clear the mock
								_facade.clearAll();
							}
						}).build());
		if (_facade.isConnected()) {
			builder.addClickable("Disconnect", VaadinIcons.EXCHANGE, new Button.ClickListener() {
				public void buttonClick(ClickEvent event) {
					connectionLogic(event);
					System.out.println("calling navigateTo(\"Show sensor layer\");");
					navigator.navigateTo("Show sensor layer");
					//Page.getCurrent().reload();
				}
			});
		} else {
			builder.addClickable("Connect", VaadinIcons.EXCHANGE, new Button.ClickListener() {
				public void buttonClick(ClickEvent event) {
					connectionLogic(event);
					System.out.println("calling navigateTo(\"Show sensor layer\");");
					navigator.navigateTo("Show sensor layer");
					//Page.getCurrent().reload();
				}
			});
		}
		builder.add("Show sensor layer", VaadinIcons.FACTORY, new SensorsView())
				.addClickable("Logout", VaadinIcons.EXIT_O, new Button.ClickListener() {
					public void buttonClick(ClickEvent event) {
						System.out.println("You have pressed Logout");
						getUI().getSession().setAttribute("ExistingSession", false);
						holder.removeAllComponents();
						holder.addComponent(new LogoutView());
					}
				})
				 .withNavigatorConsumer(navigator -> {
			          this.navigator = navigator;
			          navigator.addView("Show sensor layer", new SensorsView());
			       });
		AppLayout drawer = builder.build();
		drawer.addStyleName("left");
		
		//to be removed to enable again the buttons
		drawer = menuButtonsDisable(drawer);
//		drawer.getMenuElementHolder().getComponent(2).setEnabled(false);
//		drawer.getMenuElementHolder().getComponent(3).setEnabled(false);
//		drawer.getMenuElementHolder().getComponent(4).setEnabled(false);

		holder.addComponent(drawer);
		
		getNavigator().navigateTo("");
	}

	private void openModeSelector(Behaviour variant) {
		// UI.getCurrent().addWindow(new BehaviourSelector(variant, variant1 ->
		// setDrawerVariant(variant1)));
	}
	
	private AppLayout menuButtonsDisable(AppLayout drawer) {
//		drawer.getMenuElementHolder().getComponent(2).setEnabled(false);
//		drawer.getMenuElementHolder().getComponent(3).setEnabled(false);
//		drawer.getMenuElementHolder().getComponent(4).setEnabled(false);
		return drawer;
	}

	private void connectionLogic(ClickEvent event) {

		if (_facade.isConnected()) {
			System.out.println("platformClient is connected: " + _facade.isConnected());
			_facade.disconnect();
			System.out.println("Platform has been disconnected, setting button to connect");
			event.getButton().setCaption("Connect");
		} else {
			System.out.println("platformClient is connected: " + _facade.isConnected());
			_facade.connect();
			System.out.println("Platform has been connected, setting button to disconnect");
			event.getButton().setCaption("Disconnect");
		}

	}

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = MainUI.class)
	public static class Servlet extends VaadinServlet {
		private static final long serialVersionUID = -5236984933324437945L;
	}

	class BehaviourSelector extends Window {
		public BehaviourSelector(Behaviour current, Consumer<Behaviour> consumer) {
			setModal(true);
			setClosable(true);
			setCaption("Select Behaviour");
			VerticalLayout layout = new VerticalLayout();
			setContent(layout);
			// RadioButtonGroup<Behaviour> group = new RadioButtonGroup<>();
			// group.addStyleName(ValoTheme.OPTIONGROUP_LARGE);
			// group.setItems(Behaviour.LEFT,
			// Behaviour.LEFT_OVERLAY,
			// Behaviour.LEFT_RESPONSIVE,
			// Behaviour.LEFT_HYBRID,
			// Behaviour.LEFT_HYBRID_SMALL,
			// Behaviour.LEFT_RESPONSIVE_HYBRID,
			// Behaviour.LEFT_RESPONSIVE_HYBRID_NO_APP_BAR,
			// Behaviour.LEFT_RESPONSIVE_HYBRID_OVERLAY_NO_APP_BAR,
			// Behaviour.LEFT_RESPONSIVE_OVERLAY,
			// Behaviour.LEFT_RESPONSIVE_OVERLAY_NO_APP_BAR,
			// Behaviour.LEFT_RESPONSIVE_SMALL,
			// Behaviour.LEFT_RESPONSIVE_SMALL_NO_APP_BAR,
			// Behaviour.TOP,
			// Behaviour.TOP_LARGE);
			// group.setSelectedItem(current);
			// layout.addComponent(group);
			// group.addSelectionListener(singleSelectionEvent -> {
			// consumer.accept(singleSelectionEvent.getSelectedItem().orElse(Behaviour.LEFT_RESPONSIVE));
			// close();
			// });
		}
	}
}