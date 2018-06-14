package ch.supsi.isteps.monitoringapp.views;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Image;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.FailedListener;
import com.vaadin.ui.Upload.ProgressListener;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;

import ch.supsi.isteps.monitoringapp.faredgeplatform.client.AbstractPlatformFacade;
import ch.supsi.isteps.monitoringapp.faredgeplatform.client.SingletonFacade;

import com.vaadin.ui.VerticalLayout;

public class UploadBoxView extends CustomComponent
		implements Receiver, ProgressListener, FailedListener, SucceededListener {
	private static final long serialVersionUID = -46336015006190050L;
	private AbstractPlatformFacade _facade = SingletonFacade.getInstance();;

	// Put upload in this memory buffer that grows automatically
	ByteArrayOutputStream os = new ByteArrayOutputStream(10240);

	// Name of the uploaded file
	String filename;
	public Upload upload = new Upload();
	ProgressBar progress = new ProgressBar(0.0f);

	// Show uploaded file in this placeholder
	Image image = new Image("Uploaded File");

	public UploadBoxView() {
		// Create the upload component and handle all its events
		// Upload upload = new Upload("", null);
		upload.setReceiver(this);
		upload.addProgressListener(this);
		upload.addFailedListener(this);
		upload.addSucceededListener(this);

		// Put the upload and image display in a panel
		Panel panel = new Panel("Upload your new configuration in a Jar file here");
		panel.setWidth("400px");
		VerticalLayout panelContent = new VerticalLayout();
		panelContent.setSpacing(true);
		panel.setContent(panelContent);
		panelContent.addComponent(upload);
		panelContent.addComponent(progress);
		panelContent.addComponent(image);

		progress.setVisible(false);
		image.setVisible(false);

		setCompositionRoot(panel);
	}

	public OutputStream receiveUpload(String filename, String mimeType) {
		this.filename = filename;
		this.os.reset(); // Needed to allow re-uploading
		return os;
	}

	@Override
	public void updateProgress(long readBytes, long contentLength) {
		progress.setVisible(true);
		if (contentLength == -1)
			progress.setIndeterminate(true);
		else {
			progress.setIndeterminate(false);
			progress.setValue(((float) readBytes) / ((float) contentLength));
		}
	}

	public void uploadSucceeded(SucceededEvent event) {
		// image.setVisible(true);
		// image.setCaption("Uploaded File " + filename + " has length " +
		// os.toByteArray().length);
		_facade.uploadBAOS(os, filename);
		StreamSource source = new StreamSource() {
			private static final long serialVersionUID = -4905654404647215809L;

			public InputStream getStream() {
				return new ByteArrayInputStream(os.toByteArray());
			}
		};
		if (image.getSource() == null)
			image.setSource(new StreamResource(source, filename));
		else {
			StreamResource resource = (StreamResource) image.getSource();
			resource.setStreamSource(source);
			resource.setFilename(filename);
		}
		// image.markAsDirty();
	}

	@Override
	public void uploadFailed(FailedEvent event) {
		Notification.show("Upload failed", Notification.Type.ERROR_MESSAGE);
	}
}
