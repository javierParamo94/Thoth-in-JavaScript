package src.client.gui;

import src.client.gui.utils.MessageMessages;

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
 * 
 * @author User
 *
 */
public class CommandLang implements Command {

	// Attributes
	// -----------------------------------------------------------------------
	/**
	 * Variable para la internacionalización de los textos.
	 */
	private MessageMessages sms = GWT.create(MessageMessages.class);
	
	/**
	 * Variable en la cual se va a guardar el indice del idioma a traducir.
	 */
	private String locale;
	
	/**
	 * Ventana de diálogo para elegir si se desea o no reiniciar
	 */
	private DialogBox choiceDialog = new DialogBox();

	// Methods
	// -----------------------------------------------------------------------
	/**
	 * Constructor de la clase.
	 * 
	 * @param locale
	 *            contiene el idicador del idioma al que se va a tradurcir
	 */
	public CommandLang(String locale) {
		this.locale = locale;
	}

	/**
	 * Ejecución asociada a la clase
	 */
	public void execute() {

		choiceDialog.setAnimationEnabled(true);
		choiceDialog.setGlassEnabled(true);

		HorizontalPanel buttonPane = new HorizontalPanel();
		buttonPane.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		//Opción "si" en el diálogo de texto
		Button yesBtn = new Button(sms.yes());
		yesBtn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				//El recuadro desaparece y ejecuta el refresco de la aplicación.
				choiceDialog.hide();
				UrlBuilder newUrl = Window.Location.createUrlBuilder();
				newUrl.setParameter("locale", locale);
				Window.Location.assign(newUrl.buildString());
			}
		});
		//Opción "no" en el diálogo de texto
		Button noBtn = new Button(sms.no());
		noBtn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				choiceDialog.hide();
				//yes = false;
			}
		});

		buttonPane.add(yesBtn);
		buttonPane.add(noBtn);
		choiceDialog.center();
		buttonPane.setWidth("75%");
		choiceDialog.add(buttonPane);
		choiceDialog.setText(sms.restartchanges() + " " + sms.ucontinue());
		choiceDialog.show();
	}
}//CommandLang
