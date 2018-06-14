package ch.supsi.isteps.monitoringapp.old;

import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.v7.ui.ListSelect;

import ch.supsi.isteps.monitoringapp.data.ConfigurationData;
import ch.supsi.isteps.monitoringapp.faredgeplatform.client.AbstractPlatformFacade;
import ch.supsi.isteps.monitoringapp.faredgeplatform.client.SingletonFacade;
import ch.supsi.isteps.monitoringapp.tools.ElementType;
import ch.supsi.isteps.monitoringapp.tools.LayerType;
import ch.supsi.isteps.monitoringapp.ui.ElementDetailsUIWindow;

@SuppressWarnings("deprecation")
public class SOConfigurationView extends VerticalLayout implements View {

	private static final long serialVersionUID = 7272636940887181004L;
	private AbstractPlatformFacade _facade = SingletonFacade.getInstance();;

	
	public SOConfigurationView() {

		setMargin(true);
		setSpacing(true);

		ListSelect existingSmartObject = new ListSelect("Existing SmartObjects");


		GridLayout grid = new GridLayout(4, 5);
		grid.setSpacing(true);

		// START EXISTING SmartObjects
		// define the selection list with the existing SmartObjects
		existingSmartObject.setNullSelectionAllowed(false);
		existingSmartObject.addItems(_facade.getSmartObjects());
		existingSmartObject.setWidth("200px");
		existingSmartObject.setRows(7);
		existingSmartObject.setImmediate(true);

		//add new button to edit SmartObject Attributes
		Button btnSmartObjectDetails = new Button("Manage attributes");
		btnSmartObjectDetails.setWidth("200px");

		btnSmartObjectDetails.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
				if (existingSmartObject.getValue() != null) {
					System.out.println("To see details : " + existingSmartObject.getValue());
					
					//add window
					ElementDetailsUIWindow subWindow = new ElementDetailsUIWindow(existingSmartObject.getValue().toString(),LayerType.sensor);
					UI.getCurrent().addWindow(subWindow);
				}
			}
		});
		
		// text box for the insertion of a new CPS
		TextField txtNewSmartObject = new TextField();
		txtNewSmartObject.setValue("New SO name..");
		txtNewSmartObject.setWidth("200px");
		txtNewSmartObject.addFocusListener(new FocusListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void focus(FocusEvent event) {
				txtNewSmartObject.setValue("");
			}
		});
		// button to add the SmartObject to the list
		Button btnAddNewSmartObject = new Button("Add");
		btnAddNewSmartObject.setWidth("200px");

		btnAddNewSmartObject.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
				if (!txtNewSmartObject.isEmpty()) {
					existingSmartObject.addItem(txtNewSmartObject.getValue());
					// save only the delta

					_facade.createSmartObject(txtNewSmartObject.getValue());
					txtNewSmartObject.clear();

				}
			}
		});
		Button btnDeleteSmartObject = new Button("Delete");
		btnDeleteSmartObject.setWidth("200px");
		btnDeleteSmartObject.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {

				if (existingSmartObject.getValue() != null) {
					_facade.deleteSmartObject(existingSmartObject.getValue().toString());
						existingSmartObject.removeAllItems();
						existingSmartObject.markAsDirty();
						existingSmartObject.addItems(_facade.getSmartObjects());
						existingSmartObject.requestRepaint();
						existingSmartObject.markAsDirty();

				}
			}
		});
		grid.addComponent(existingSmartObject, 0, 0);
		grid.setComponentAlignment(existingSmartObject, Alignment.MIDDLE_LEFT);
		
		grid.addComponent(txtNewSmartObject, 0, 1);
		grid.setComponentAlignment(txtNewSmartObject, Alignment.MIDDLE_LEFT);

		grid.addComponent(btnAddNewSmartObject, 0, 2);
		grid.setComponentAlignment(btnAddNewSmartObject, Alignment.MIDDLE_LEFT);
		
		grid.addComponent(btnSmartObjectDetails, 0, 3);
		grid.setComponentAlignment(btnSmartObjectDetails, Alignment.MIDDLE_LEFT);

		grid.addComponent(btnDeleteSmartObject, 0, 4);
		grid.setComponentAlignment(btnDeleteSmartObject, Alignment.MIDDLE_LEFT);

		// END EXISTING CPS
		
		addComponent(grid);

	}

	@Override
	public void enter(ViewChangeEvent event) {
	}

}
