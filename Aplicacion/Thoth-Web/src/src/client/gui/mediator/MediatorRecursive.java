package src.client.gui.mediator;

import com.google.gwt.core.client.GWT;

import src.client.GrammarServiceClientImp;
import src.client.core.grammar.Grammar;
import src.client.core.grammar.cleaner.EliminateDirectRecursion;
import src.client.core.grammar.cleaner.EliminateIndirectRecursion;
import src.client.gui.utils.ShowDialog;

/**
 * <b>Descripción</b><br>
 * Se encarga de eliminar la recursividad a izquierdas de una gramática.
 * <p>
 * <b>Detalles</b><br>
 * Elimina tanto la recursividad directa como la indirecta.<br>
 * Si no tenía recursividad lo avisa y no ejecuta los algoritmos.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Eliminar la recursividad a izquierdas de una gramática.
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @version 1.0
 */
public class MediatorRecursive {

	// Methods
	// -------------------------------------------------------------------

	/**
	 * Constructor completo del mediador.<br>
	 * Se encarga de comprobar si la gramática es recursiva a izquierdas y si es
	 * directa o indirectamente.<br>
	 * Elimina la recursividad de un sólo paso mostrándote la nueva gramática en
	 * otra pestaña.
	 * 
	 * @param frame
	 *            Frame al que estará asociado el algoritmo de recursión.
	 * @param grammar
	 *            Gramática a la que se quiere eliminar la recursión.
	 */
	public MediatorRecursive(Grammar grammar) {
		EliminateDirectRecursion edr;
		EliminateIndirectRecursion eir;
		boolean flag = false;

		if (grammar.isDirectRecursive()) {
			edr = new EliminateDirectRecursion(grammar);
			edr.allSteps();
			grammar = edr.getSolution();
			flag = true;
		}
		if (grammar.isIndirectRecursive()) {
			eir = new EliminateIndirectRecursion(grammar);
			eir.allSteps();
			grammar = eir.getSolution();
			flag = true;
		}
		if (flag) {

			new GrammarServiceClientImp(GWT.getModuleBaseURL()
					+ "grammarservice", grammar);

		}
		// else
		// ShowDialog.nonRecursive();

		// ((PanelGrammar)Application.getInstance().getCurrentTab()).checkContent();
	}// MediatorRecursion

}// MediatorRecursion
