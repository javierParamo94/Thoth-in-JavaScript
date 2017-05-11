package src.client.gui.mediator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HTML;

import src.client.GrammarServiceClientImp;
import src.client.gui.utils.HTMLConverter;
import src.client.gui.utils.ShowDialog;
import src.client.gui.visual.VisualDirectRecursive;
import src.client.core.grammar.*;
import src.client.core.grammar.cleaner.Cleaning;
import src.client.core.grammar.cleaner.EliminateDirectRecursion;

/**
 * <b>Descripción</b><br>
 * Clase que hace de mediador entre el algoritmo de eliminación de recursividad
 * directa y la pantalla que lo muestra.
 * <p>
 * <b>Detalles</b><br>
 * Se encarga de mediar entre la parte gráfica y la parte física.<br>
 * Ilumina las producciones según se van creando, borrando,... haciendo más
 * fácil la comprensión del algoritmo.
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
public class MediatorDirectRecursive {

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
	private VisualDirectRecursive mVisual;

	// Methods
	// -----------------------------------------------------------------------

	/**
	 * Constructor completo del mediador del algoritmo de eliminación de
	 * recursividad.
	 * 
	 * @param vdr
	 *            Pantalla del algoritmo.
	 * @param grammar
	 *            Gramática a limpiar.
	 */
	public MediatorDirectRecursive(VisualDirectRecursive vdr, Grammar grammar) {
		mCleanAlgorithm = new EliminateDirectRecursion(grammar);
		mGrammar = grammar;
		mFlagFirst = true;
		mVisual = vdr;
		mVisual.mOld.setHTML(HTMLConverter.toHTML(mGrammar.completeToString()));
		mVisual.mNew.setHTML(HTMLConverter.toHTML(mCleanAlgorithm.getSolution()
				.completeToString()));

		if (!mCleanAlgorithm.firstStep()) {
			ShowDialog.nonRecursiveDir();
			mVisual.mVisible = false;
		} else
			mCleanAlgorithm = new EliminateDirectRecursion(grammar);

	}// MediatorDirectRecursive

	/**
	 * Primer paso del algoritmo.
	 */
	public void next() {

		if (mFlagFirst) {
			if (!mCleanAlgorithm.firstStep()) {
				finish();
				ShowDialog.nonRecursiveDir();
				mVisual.mVisible = false;
				return;
			} // FirstStep
			setAux();
			for (Production prod : ((EliminateDirectRecursion) mCleanAlgorithm)
					.getRecProductions())
				highLight(mVisual.mOld, prod.toString(), true);
			mFlagFirst = false;
		} else { // NextStep
			removeAllHighLight();
			// Última iteración
			if (!mCleanAlgorithm.nextStep()) {
				finish();
			}
			// En cada iteración
			mVisual.mNew.setHTML(HTMLConverter.toHTML(mCleanAlgorithm
					.getSolution().completeToString()));
			highLightAux();
			highLightNew();
		}

	}// next

	/**
	 * Todos los pasos del algoritmo.
	 */
	public void all() {
		mCleanAlgorithm = new EliminateDirectRecursion(mGrammar);

		if (!mCleanAlgorithm.allSteps()) {
			ShowDialog.nonRecursiveDir();
			mVisual.mVisible = false;
		} else
			mVisual.mNew.setHTML(HTMLConverter.toHTML(mCleanAlgorithm
					.getSolution().completeToString()));
		removeAllHighLight();
		finish();

	}// all

	/**
	 * Aceptar.<br>
	 * Crea una nueva vista con la gramática nueva.
	 */
	public void accept() {

		if (mVisual.mNew.getText().length() > 0) {

			mVisual.generalPanel.clear();
			new GrammarServiceClientImp(GWT.getModuleBaseURL()
					+ "grammarservice", mCleanAlgorithm.getSolution());

		}
	}// accept

	/**
	 * Asigna al JTextPane mAux el valor en cada paso.
	 */
	private void setAux() {
		String temp = new String();

		for (Production prod : ((EliminateDirectRecursion) mCleanAlgorithm)
				.getRecProductions())
			temp += prod.toString();

		mVisual.mAux.setHTML(HTMLConverter.toHTML(temp));
	}// setAux

	/**
	 * Resalta en verde las producciones que se han creado.
	 */
	private void highLightNew() {
		for (Production prod : ((EliminateDirectRecursion) mCleanAlgorithm)
				.getNewProductions())
			highLight(mVisual.mNew, prod.toString(), true);

	}// highLightNew

	/**
	 * Resalta en rojo las producciones que van a ser borradas.
	 */
	private void highLightAux() {
		for (Production prod : ((EliminateDirectRecursion) mCleanAlgorithm)
				.getRecProductions())
			highLight(mVisual.mAux, prod.toString(), false);

	}// highLightAux

	/**
	 * 
	 * Ilumina/Resalta el texto de los paneles donde se encuentran las dos
	 * gramáticas que coincidan con pattern.
	 * 
	 * @param mOld
	 *            Panel en el que se encuentra el texto
	 * @param pattern
	 *            Texto a iluminar
	 * @param green
	 *            Booleano que determina el color de la iluminación.
	 */

	private void highLight(HTML mOld, String pattern, boolean green) {
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

		text = mOld.getHTML();
		while ((posEnd = text.indexOf(pattern, posEnd)) >= 0) {

			text1 += text.substring(posStart, posEnd) + openMark + pattern
					+ closeMark;

			posEnd += pattern.toString().length();
			posStart = posEnd;
		}
		text1 += text.substring(posStart, mOld.getHTML().length());
		mOld.setHTML(text1);

	}// highLight

	/**
	 * Deshabilita los botones de Siguiente y Todos los pasos y habilita el de
	 * aceptar.
	 */
	public void finish() {
		mVisual.btnOneStep.setEnabled(false);
		mVisual.btnAllSteps.setEnabled(false);
		mVisual.btnAcept.setEnabled(true);

	}// finish

	/**
	 * Deselecciona la zona resaltada.
	 */
	private void removeAllHighLight() {
		String str, str1, str2, str3, str4, str5;
		str = mVisual.mNew.getHTML().replaceAll("<mark class=\"green\">", "")
				.replaceAll("<mark class=\"red\">", "");
		str1 = str.replaceAll("</mark>", "");
		str2 = mVisual.mOld.getHTML().replaceAll("<mark class=\"green\">", "")
				.replaceAll("<mark class=\"red\">", "");
		str3 = str2.replaceAll("</mark>", "");
		str4 = mVisual.mAux.getHTML().replaceAll("<mark class=\"green\">", "")
				.replaceAll("<mark class=\"red\">", "");
		str5 = str4.replaceAll("</mark>", "");

		mVisual.mNew.setHTML(str1);
		mVisual.mOld.setHTML(str3);
		mVisual.mAux.setHTML(str5);

	}// removeAllHighLight()

	/**
	 * Crea una nueva vista con la grmática vieja.
	 */
	public void exit() {

		mVisual.generalPanel.clear();
		new GrammarServiceClientImp(GWT.getModuleBaseURL() + "grammarservice",
				mGrammar);

	}// exit
}// MediatorDirectRecursive