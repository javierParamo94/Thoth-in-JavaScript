package src.client.gui.mediator;

import com.google.gwt.core.client.GWT;

import src.client.GrammarServiceClientImp;
import src.client.core.grammar.Grammar;
import src.client.gui.mainGui;
import src.client.gui.utils.RenameSymbolDialog;
import src.client.gui.utils.ShowDialog;

/**
 * <b>Descripción</b><br>
 * Clase que se encarga de renombrar símbolos en la pantalla de usuario.
 * <p>
 * <b>Detalles</b><br>
 * Inicializa las opciones de la parte visual y se encarga de todo el peso del
 * renombrado de símbolos (tanto terminales como no terinales).
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Evita el acomplamiento entre la parte visual y la física.<br>
 * Permite renombrar símbolos.
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @author Francisco Javier Páramo Arnáiz
 * @version 2.0
 */
public class MediatorRenameSymbol {

	// Attributes
	// -----------------------------------------------------------------

	/**
	 * Vista del DialogBox al que está asociado.
	 */
	public RenameSymbolDialog mVisualDialogBox;
	/**
	 * Variable que contendrá la gramática.
	 */
	public Grammar mGrammar;
	/**
	 * Vista principal del panel de gramática.
	 */
	public mainGui mVisualMain;

	// Methods
	// --------------------------------------------------------------------

	/**
	 * Constructor completo del mediador de renombrado de símbolos.
	 * 
	 * @param visualMain
	 *            vista principal.
	 * @param visualDialog
	 *            vista del DialogBox de renombrar.
	 * @param grammar
	 *            gramática que se le pasa.
	 */
	public MediatorRenameSymbol(mainGui visualMain,
			RenameSymbolDialog visualDialog, Grammar grammar) {
		mVisualDialogBox = visualDialog;
		mVisualMain = visualMain;
		mGrammar = grammar;
		buildChoice();
	}// MediatorRenameSymbol

	/**
	 * Construye las sugerencias del recuadro de sustituir.
	 */
	private void buildChoice() {
		Object[] ter = mGrammar.getTerminals().toArray();
		Object[] noTer = mGrammar.getNonTerminals().toArray();
		String[] options = new String[ter.length + noTer.length];

		// Creamos options con todos lo terminales y no terminales.
		for (int i = 0; i < noTer.length; i++) {
			options[i] = noTer[i].toString();
			mVisualDialogBox.suggestionNodes.add(options[i]);
		}
		// Como el recuadro de sugerencias no es case Sensitive, añado un
		// espacio a los no terminales para que se diferencien de los
		// terminales.
		for (int i = 0, size = noTer.length; i < ter.length; i++) {
			options[size + i] = ter[i].toString();
			mVisualDialogBox.suggestionNodes.add(options[size + i] + " ");
		}
	}// buildChoice

	/**
	 * Renombra el símbolo que se haya seleccionado o introducido por otro
	 * nuevo.
	 */
	public void accept() {
		// recoge el texto del recuadro de sugerrencias y elimina los espacios.
		String find = mVisualDialogBox.mChoiceFind.getText().replaceAll("\\s+",
				"");
		String rename = mVisualDialogBox.mChoiceRename.getText();
		// en el caso de no introducir nada o cuando contenga % emite un error.
		if (find.length() == 0 || find.contains("%"))
			ShowDialog.nonSymbolRename();
		else {

			String solution = mGrammar.toString();
			String sol = solution.substring(7);
			sol = sol.replace(find, rename);
			String renamedText = (solution.substring(0, 7).concat(sol));
			// Creo una nueva vista con la nueva gramática en formato string.
			if (renamedText.equals(solution))
				ShowDialog.nonChangesRename();
			else {
				mVisualMain.editorGrammarPanel.clear();
				mVisualMain.barMenuPanel.clear();
				new GrammarServiceClientImp(GWT.getModuleBaseURL()
						+ "grammarservice", renamedText);
			}
		}
	}// accept
}// MediatorRenameSymbol
