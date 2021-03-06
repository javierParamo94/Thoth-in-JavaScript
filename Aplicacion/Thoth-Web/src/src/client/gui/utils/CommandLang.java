package src.client.gui.utils;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;

/**
 * <b>Descripción</b><br>
 * Clase para la creación de una ventana de diálogo para avisar de que se
 * reiniciará el programa.
 * <p>
 * <b>Detalles</b><br>
 * El aviso da la opción de poder continuar y reiniciar o por el contrario no
 * realizar ningún cambio.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Al pulsar el botón de "si" se reiniciará mostrando los elementos textuales en
 * el idioma elegido. En caso de elegir "no" la ventana desaparece sin cambios<br>
 * </p>
 * 
 * @author Francisco Javier Páramo Arnáiz
 * @version 1.0
 */
public class CommandLang implements Command {

	// Attributes
	// -----------------------------------------------------------------------
	/**
	 * Variable para la internacionalización de los textos.
	 */
	private MessageMessages mMsg = GWT.create(MessageMessages.class);
	/**
	 * Variable en la cual se va a guardar el indice del idioma a traducir.
	 */
	private String locale;
	/**
	 * Dialogo de texto.
	 */
	private DialogBox mChoiceDialog = new DialogBox();

	// Methods
	// -----------------------------------------------------------------------

	/**
	 * Constructor del dialogo al elegir un idioma a traducir.
	 * 
	 * @param locale
	 *            contiene el idioma al que se va a tradurcir
	 */
	public CommandLang(String locale) {
		this.locale = locale;
	}

	/**
	 * Ejecución asociada a la clase
	 */
	public void execute() {

		mChoiceDialog.setAnimationEnabled(true);
		mChoiceDialog.setGlassEnabled(true);

		HorizontalPanel buttonPane = new HorizontalPanel();
		buttonPane.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		// Opción "si" en el diálogo de texto
		Button yesBtn = new Button(mMsg.yes());
		yesBtn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// El recuadro desaparece y ejecuta el refresco de la
				// aplicación.
				mChoiceDialog.hide();
				UrlBuilder newUrl = Window.Location.createUrlBuilder();
				newUrl.setParameter("locale", locale);
				Window.Location.assign(newUrl.buildString());
			}
		});
		// Opción "no" en el diálogo de texto
		Button noBtn = new Button(mMsg.no());
		noBtn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				mChoiceDialog.hide();
			}
		});

		buttonPane.add(yesBtn);
		buttonPane.add(noBtn);
		mChoiceDialog.center();
		buttonPane.setWidth("75%");
		mChoiceDialog.add(buttonPane);
		mChoiceDialog.setText(mMsg.restartchanges() + " " + mMsg.ucontinue());
		mChoiceDialog.show();
	}
}// CommandLang
