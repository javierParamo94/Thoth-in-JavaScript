package src.client.gui.mediator;

import com.google.gwt.core.client.GWT;

import src.client.GrammarServiceClientImp;
import src.client.core.grammar.Grammar;
import src.client.core.grammar.cleaner.Cleaner;

/**
 * <b>Descripción</b><br>
 * Clase que hace de mediador en la limpieza de gramáticas.
 * <p>
 * <b>Detalles</b><br>
 * Se encarga de mediar entre la parte gráfica y la parte física.<br>
 * Si la gramática ha sido limpiada con éxito crea una nueva pestaña con ella.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Evita el acomplamiento entre la parte visual y la física.<br>
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @version 1.0
 */
public class MediatorClear {

	// Attributes
	// ----------------------------------------------------------------

	/**
	 * Grámatica que va a ser limpiada.
	 */
	private Grammar mGrammar;

	// Methods
	// -------------------------------------------------------------------

	/**
	 * Constructor básico.<br>
	 * Inicializa la gramática que va a ser limpiada.
	 * 
	 * @param grammar
	 *            Indica la gramática que va a ser limpiada.
	 */
	public MediatorClear(Grammar grammar) {
		mGrammar = grammar;

	}// MediatorClear

	/**
	 * Realiza la limpieza de la gramática.<br>
	 * Ejecuta los algoritmos de limpieza de gramáticas.
	 * 
	 * @return Si ha podido limpiar o no la gramática:<br>
	 *         - 0 No ha habido cambios en la gramática (ya estaba limpia)<br>
	 *         - 1 Ha sido limpiada con éxito<br>
	 *         - 2 No ha podido finalizarse el algoritmo de limpieza
	 */
	public int doAll() {
		Cleaner clean = new Cleaner(mGrammar);

		if (!clean.toClean())
			return 2;

		if (clean.getSolution().equals(mGrammar))
			return 0;

		mGrammar = clean.getSolution();
		finish();

		return 1;
	}// doAll

	/**
	 * Finaliza la limpieza creando una nueva vista con al gramática limpia.
	 */
	private void finish() {
		if (mGrammar.toString().length() > 0) {

			new GrammarServiceClientImp(GWT.getModuleBaseURL()
					+ "grammarservice", mGrammar);

		}
	}// finish

}// MediatorClear
