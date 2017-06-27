package src.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * <b>Descripción</b><br>
 * Interfaz para la comunicación RPC entre cliente y servidor
 * <p>
 * <b>Detalles</b><br>
 * Interfaz asociada a otra, GrammarService, que establecen la comunicación
 * asíncorna con el servidor.<br>
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * comunicación cliente-servidor.
 * </p>
 * 
 * @author Francisco Javier Páramo Arnaiz.
 * @version 1.0
 */
public interface GrammarServiceAsync {
	/**
	 *  checkContent en la interfaz GrammarServiceasync
	 *  
	 * @param grammar gramatica
	 * @param callback 
	 */
	void checkContent(String grammar, AsyncCallback callback);
}
