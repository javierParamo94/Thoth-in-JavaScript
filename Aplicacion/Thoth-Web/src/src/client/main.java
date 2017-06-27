package src.client;

import src.client.GrammarServiceClientImp;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * <b>Descripción</b><br>
 * Clase principal en el módulo de GramaticaCS.
 * <p>
 * <b>Detalles</b><br>
 * Clase principal que contiene la función principal. Será la primera que se
 * ejecute y lo primero que hace es crear una implementación del servicio de
 * gramática y llamar a la clase mainGui para generar la vista principal del
 * módulo.<br>
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Funcion principal onModuleLoad.
 * </p>
 * 
 * @author Francisco Javier Páramo Arnaiz.
 * @version 1.0
 */
public class main implements EntryPoint {

	/**
	 * Función principal del módulo GramaticaCS.
	 */
	public void onModuleLoad() {

		GrammarServiceClientImp clientImp = new GrammarServiceClientImp(
				GWT.getModuleBaseURL() + "grammarservice");
		RootPanel.get().add(clientImp.getManGUI());

	}

}
