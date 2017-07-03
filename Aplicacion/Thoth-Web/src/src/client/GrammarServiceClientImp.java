package src.client;

import src.client.core.grammar.Grammar;
import src.client.gui.mainGui;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * <b>Descripción</b><br>
 * Clase que agrupa las funciones de la gramática con peticiones al servidor.
 * <p>
 * <b>Funcionalidad</b><br>
 * Checkeo de la gramática.
 * </p>
 * 
 * @author Francisco Javier Páramo Arnaiz.
 * @version 1.0
 */
public class GrammarServiceClientImp {

	/**
	 * Servicio
	 */
	private GrammarServiceAsync service;

	/**
	 * Variable de la clase mainGui
	 */
	private mainGui mainGUI;

	/**
	 * Contructor de GrammarService en el lado del cliente
	 * 
	 * @param url
	 */
	public GrammarServiceClientImp(String url) {
		System.out.println(url);
		this.service = GWT.create(GrammarService.class);
		ServiceDefTarget endpoint = (ServiceDefTarget) this.service;
		endpoint.setServiceEntryPoint(url);

		this.mainGUI = new mainGui(this);
	}// GrammarServiceClientImp

	/**
	 * Contructor de GrammarService en el lado del cliente al que se le pasa
	 * ademas una gramatica
	 * 
	 * @param url
	 * @param grammar
	 *            Gramatica
	 */
	public GrammarServiceClientImp(String url, Grammar grammar) {
		System.out.println(url);
		this.service = GWT.create(GrammarService.class);
		ServiceDefTarget endpoint = (ServiceDefTarget) this.service;
		endpoint.setServiceEntryPoint(url);

		this.mainGUI = new mainGui(this, grammar);
	}// GrammarServiceClientImp

	/**
	 * Contructor de GrammarService en el lado del cliente al que se le pasa
	 * ademas un texto.
	 * 
	 * @param url
	 * @param grammarText
	 *            Gramatica en formato texto.
	 */
	public GrammarServiceClientImp(String url, String grammarText) {
		System.out.println(url);
		this.service = GWT.create(GrammarService.class);
		ServiceDefTarget endpoint = (ServiceDefTarget) this.service;
		endpoint.setServiceEntryPoint(url);

		this.mainGUI = new mainGui(this, grammarText);
	}// GrammarServiceClientImp

	/**
	 * Función de ayuda que llama a la clase mainGui.
	 * 
	 * @return intancia de la clase.
	 */
	public mainGui getManGUI() {
		return this.mainGUI;
	}

	/**
	 * Checkeo del contenido en el textArea de mainGui realiza la peticion al
	 * servidor.
	 * 
	 * @param grammar
	 *            gramatica a checkear.
	 */
	public void checkContent(String grammar) {
		this.service.checkContent(grammar, new AsyncCallback<Object>() {

			@Override
			public void onFailure(Throwable caught) {

				if (caught instanceof Exception)
					Window.alert("Eroooorororororor!");

			}

			@Override
			public void onSuccess(Object result) {
				if (result instanceof Grammar) {
					Grammar grammar = ((Grammar) result);
					mainGUI.updateLabel(grammar);
				}

			}

		});
	}// checkContent

}// GrammarServiceClientImp
