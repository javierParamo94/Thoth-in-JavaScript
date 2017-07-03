package src.client;

import src.client.core.grammar.Grammar;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * <b>Descripción</b><br>
 * Interfaz para la comunicación RPC entre cliente y servidor
 * <p>
 * <b>Detalles</b><br>
 * Interfaz asociada a otra, GrammarServiceAsync, que establecen la comunicación
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
@RemoteServiceRelativePath("grammarservice")
public interface GrammarService extends RemoteService {

	/**
	 * Funcion checkContent en la interfaz GrammarService
	 * @param grammar gramatica
	 * @return 
	 */
	public Grammar checkContent(String grammar);
}//GrammarService
