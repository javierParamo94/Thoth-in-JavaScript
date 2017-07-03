package src.client.gui.utils;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * <b>Descripción</b><br>
 * Clase para mostrar el Acerca de...
 * <p>
 * <b>Detalles</b><br>
 * Se trata de un popup panel que muestra los datos de los autores.
 * </p>
 * 
 * @author Francisco Javier Páramo Arnáiz
 * @version 1.0
 */
public class AboutDialog extends DialogBox {

	// Attributes
	// -----------------------------------------------------------------

	/**
	 * Variable para la internacionalización de los textos
	 */
	private MessageMessages mMsg = GWT.create(MessageMessages.class);

	/**
	 * Botón aceptar para realizar el renombrado.
	 */
	private Button mAccept;

	/**
	 * Botón para el enlace del proyecto en gitHub
	 */
	private PushButton btnGuit;

	// Methods
	// -----------------------------------------------------------------

	/**
	 * Constructor completo del DialogBox con info del proyecto.
	 * 
	 */
	public AboutDialog() {

		// características del recuadro de diálogo.
		setText(mMsg.about());
		setAnimationEnabled(true);
		setGlassEnabled(true);
		center();
		// Panel donde van colocados los elementos
		VerticalPanel aboutTextPanel = new VerticalPanel();
		VerticalPanel generalPanel = new VerticalPanel();
		HorizontalPanel topPanel = new HorizontalPanel();
		HorizontalPanel coopPanel = new HorizontalPanel();
		HorizontalPanel buttonPanel = new HorizontalPanel();

		HorizontalPanel authorPanel = new HorizontalPanel();
		HTML author = new HTML (mMsg.author());
		author.setStyleName("header");
		authorPanel.add(author);
		
		HorizontalPanel tutorsPanel = new HorizontalPanel();
		HTML tutors = new HTML (mMsg.tutors());
		tutors.setStyleName("header");
		tutorsPanel.add(tutors);
		
		topPanel.setSpacing(10);
		coopPanel.setSpacing(10);
		buttonPanel.setSpacing(10);

		HTML about = new HTML("Project developed by:");
		about.setStyleName("Panel-Text");
		
		// Enlaces a los correos de los autores
		Anchor linkAlumn = new Anchor("Francisco Javier Páramo Arnaiz", false,
				"mailto:fpa0019@alu.ubu.es");

		Anchor linkTutor1 = new Anchor("D. Álvar Arnaiz González", false,
				"mailto:alvarag@ubu.es");
		Anchor linkTutor2 = new Anchor("Dr. César Ignacio García Osorio",
				false, "mailto:cgosorio@ubu.es");

		btnGuit = new PushButton(new Image("/images/github.png"));


		topPanel.add(btnGuit);
		topPanel.add(about);
		authorPanel.add(linkAlumn);
		
		tutorsPanel.add(linkTutor1);
		tutorsPanel.add(linkTutor2);
		aboutTextPanel.add(topPanel);
		coopPanel.add(authorPanel);
		coopPanel.add(tutorsPanel);
		aboutTextPanel.add(coopPanel);
		// Botón
		mAccept = new Button(mMsg.accept());
		buttonPanel.add(mAccept);

		generalPanel.add(aboutTextPanel);
		generalPanel.add(buttonPanel);
		setWidget(generalPanel);

		buildListeners();
	}// AboutDialog

	/**
	 * Asigna la funcionalidad de los botones.
	 */
	public void buildListeners() {
		// Botón aceptar.
		mAccept.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				AboutDialog.this.hide();
			}
		});
		// Botón de gitHub
		btnGuit.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Window.open(
						"https://github.com/javierParamo94/Thoth-in-JavaScript",
						"_blank", "");
			}
		});

	}// buildListeners
}// AboutDialog

