package src.client.gui.mediator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RichTextArea;

import src.client.GrammarServiceClientImp;
import src.client.core.grammar.Grammar;
import src.client.core.grammar.Production;
import src.client.core.grammar.cleaner.Cleaning;
import src.client.core.grammar.cleaner.EliminateSNT;
import src.client.gui.utils.ShowDialog;
import src.client.gui.utils.HTMLConverter;
import src.client.gui.visual.VisualSNT;

/**
 * <b>Descripción</b><br>
 * Clase que hace de mediador entre el algoritmo de eliminación de símbolos no
 * terminables y la pantalla que muestra dicho algoritmo.
 * <p>
 * <b>Detalles</b><br>
 * Se encarga de mediar entre la parte gráfica y la parte física.<br>
 * Ilumina y ayuda al usuario a comprender el algoritmo.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Evita el acomplamiento entre la parte visual y la física.<br>
 * Ilumina las producciones que cambian.
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @version 1.0
 */
public class MediatorSNT {

	// Attributes
	// --------------------------------------------------------------------
	/**
	 * Algoritmo de limpieza asociado al mediador.
	 */
	private Cleaning mCleanAlgorithm;

	/**
	 * Gramática a limpiar.
	 */
	private Grammar mGrammar;

	/**
	 * Bandera usada para saber si ha realizado el primer paso del algoritmo.
	 */
	private boolean mFlagFirst;

	/**
	 * Algoritmo visual al que está asociado.
	 */
	private VisualSNT mVisual;

	// Methods
	// -----------------------------------------------------------------------

	/**
	 * Constructor completo del mediador del algoritmo de limpieza de símbolos
	 * no terminables.
	 * 
	 * @param snt
	 *            Pantalla del algoritmo.
	 * @param grammar
	 *            Gramática a limpiar.
	 */
	public MediatorSNT(VisualSNT snt, Grammar grammar) {
		mCleanAlgorithm = new EliminateSNT(grammar);
		mGrammar = grammar;
		mFlagFirst = true;
		mVisual = snt;
		mVisual.mOld.setHTML(HTMLConverter.toHTML(mGrammar.completeToString()));
		mVisual.mNew.setHTML(HTMLConverter.toHTML(mCleanAlgorithm.getSolution()
				.completeToString()));

		if (!mCleanAlgorithm.firstStep()) {
			ShowDialog.nonTerminalSymbols();
			mVisual.mVisible = false;
			mVisual.mMediator = null;
		} else
			mCleanAlgorithm = new EliminateSNT(grammar);

	}// MediatorSNT

	/**
	 * Cada paso del algoritmo.
	 */
	public void next() {
		// Primer paso
		if (mFlagFirst) {
			if (!mCleanAlgorithm.firstStep()) {
				finish();
				ShowDialog.nonTerminalSymbols();
				mVisual.mVisible = false;
				exit();
				return;
			}
			mVisual.mNew.setHTML(HTMLConverter.toHTML(mCleanAlgorithm
					.getSolution().completeToString()));
			for (Production prod : mCleanAlgorithm.getSolution()
					.getProductions()) {
				highLight(mVisual.mOld, prod.toString(), true);
				highLight(mVisual.mNew, prod.toString(), true);
			}
			mFlagFirst = false;
		} // Siguiente paso
		else if (!mCleanAlgorithm.nextStep())
			finish();
		else {
			mVisual.mNew.setHTML(HTMLConverter.toHTML(mCleanAlgorithm
					.getSolution().completeToString()));
			removeAllLighter();
			highLight(mVisual.mOld, mCleanAlgorithm.getSolution()
					.getProductions().lastElement().toString(), true);
			highLight(mVisual.mNew, mCleanAlgorithm.getSolution()
					.getProductions().lastElement().toString(), true);
		}

	}// next

	/**
	 * Todos los pasos del algoritmo.
	 */
	public void all() {
		mCleanAlgorithm = new EliminateSNT(mGrammar);

		if (!mCleanAlgorithm.allSteps()) {
			ShowDialog.nonTerminalSymbols();
			mVisual.mVisible = false;
		} else
			mVisual.mNew.setHTML(HTMLConverter.toHTML(mCleanAlgorithm
					.getSolution().completeToString()));

		finish();
	}// all

	/**
	 * Aceptar.<br>
	 * Crea una nueva vista con la gramática nueva.
	 */
	public void accept() {

		if (mVisual.mNew.getText().length() > 0) {

			mVisual.vPanel.clear();
			new GrammarServiceClientImp(GWT.getModuleBaseURL()
					+ "grammarservice", mCleanAlgorithm.getSolution());

		}
	}// accept

	/**
	 * 
	 * Ilumina/Resalta el texto de los paneles donde se encuentran las dos
	 * gramáticas que coincidan con pattern.
	 * 
	 * @param pane
	 *            Panel en el que se encuentra el texto
	 * @param pattern
	 *            Texto a iluminar
	 * @param green
	 *            Booleano que determina el color de la iluminación.
	 */
	private void highLight(RichTextArea pane, String pattern, boolean green) {
		String text = "", text1 = "";
		int posEnd = 0, posStart = 0;
		String openMark = "", closeMark = "";

		// Elección del color del highLight
		if (green) {
			openMark = "<mark class=\"green\">";
			closeMark = "</mark>";
		} else {
			openMark = "<mark class=\"red\">";
			closeMark = "</mark>";
		}

		if (pattern.equals(""))
			return;

		// Eliminar posible \n al final del patrón.
		pattern = pattern.replace("\n", "");

		text = pane.getHTML();
		while ((posEnd = text.indexOf(pattern, posEnd)) >= 0) {

			text1 += text.substring(posStart, posEnd) + openMark + pattern
					+ closeMark;

			posEnd += pattern.toString().length();
			posStart = posEnd;
		}
		text1 += text.substring(posStart, pane.getHTML().length());
		pane.setHTML(text1);

	}// highLight

	/**
	 * Deshabilita los botones de Siguiente y Todos los pasos y habilita el de
	 * aceptar.
	 */
	public void finish() {
		mVisual.btnOneStep.setEnabled(false);
		mVisual.btnAllSteps.setEnabled(false);
		mVisual.btnAcept.setEnabled(true);
		mVisual.btnAcept.setVisible(true);
		mVisual.btnAcept.setFocus(true);
		removeAllLighter();

	}// finish

	/**
	 * Quita todos los marcadores de los paneles de las gramáticas.
	 */
	public void removeAllLighter() {
		String str, str1, str2, str3;

		str = mVisual.mNew.getHTML().replaceAll("<mark class=\"green\">", "")
				.replaceAll("<mark class=\"red\">", "");
		str1 = str.replaceAll("</mark>", "");
		str2 = mVisual.mOld.getHTML().replaceAll("<mark class=\"green\">", "")
				.replaceAll("<mark class=\"red\">", "");
		str3 = str2.replaceAll("</mark>", "");

		mVisual.mNew.setHTML(str1);
		mVisual.mOld.setHTML(str3);

	}// removeAllLighter

	/**
	 * Crea una nueva vista con la grmática vieja.
	 */
	public void exit() {

		mVisual.vPanel.clear();
		new GrammarServiceClientImp(GWT.getModuleBaseURL() + "grammarservice",
				mGrammar);
	}// exit
}// MediatorSNT
