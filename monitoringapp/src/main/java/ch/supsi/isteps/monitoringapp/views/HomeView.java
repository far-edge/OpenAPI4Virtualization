package ch.supsi.isteps.monitoringapp.views;

import java.io.File;

import com.vaadin.navigator.View;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Image;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.Label;

@SuppressWarnings("deprecation")
public class HomeView extends VerticalLayout implements View {
	static final long serialVersionUID = -1953416689182167095L;

	public HomeView() {

		final String text = "<div class=\"service-block\">\r\n"
				+ "                <div class=\"services_box_right\">\r\n"
				+ "                    <h3>FAR-EDGE Vision</h3>\r\n" + "                    <p>\r\n"
				+ "                        </p><p style=\"text-align: justify;\">In today’s competitive global environment, manufacturers are striving to build hyper-efficient and highly flexible plants, towards meeting variable market demand including mass customization, with only minimal increase in production costs. This requires scalable and advanced manufacturing systems implementing&nbsp;automation techniques that deploy and reconfigure automation systems and production resources (e.g., workstations, robots) at the lowest possible cost.<br>\r\n"
				+ "<br>\r\n"
				+ "Key challenges for the adoption of decentralized automation architectures from manufacturers (including edge computing) are:</p>\r\n"
				+ "\r\n" + "<ul>\r\n"
				+ "	<li style=\"text-align: justify;\">IoT/Sensor implementation, deployments and standards still in their infancy.</li>\r\n"
				+ "	<li style=\"text-align: justify;\">Lack of a well-defined and smooth migration path to distributing and virtualizing the automation pyramid.</li>\r\n"
				+ "	<li style=\"text-align: justify;\">Lack of shared situational awareness and semantic interoperability across the heterogeneous components, devices and systems (including manufacturing Sensor-based automation environments).</li>\r\n"
				+ "	<li style=\"text-align: justify;\">Lack of open, secure and standards-based platforms for decentralized factory automation.</li>\r\n"
				+ "</ul>\r\n" + "                <p></p>\r\n" + "                </div>\r\n" + "</div>";

		Panel panel = new Panel("Welcome to the FAR-EDGE application");
		// adding image
		String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
		// Image as a file resource
		FileResource resource = new FileResource(new File(basepath + "/VAADIN/themes/demo/faredge.png"));
		// Show the image in the application
		Image image = new Image("", resource);
		image.setWidth(1221, Unit.PIXELS);
		image.setHeight(704, Unit.PIXELS);

		// build all the content
		VerticalLayout content = new VerticalLayout();
		content.addComponent(new Label(text, ContentMode.HTML));
		content.addComponent(image);
		panel.setContent(content);
		addComponent(panel);

	}
}