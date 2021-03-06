package src.client.gui.mediator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HTML;

import src.client.GrammarServiceClientImp;
import src.client.core.grammar.*;
import src.client.core.grammar.cleaner.Cleaning;
import src.client.core.grammar.cleaner.EliminateSA;
import src.client.gui.utils.ShowDialog;
import src.client.gui.utils.HTMLConverter;
import src.client.gui.visual.VisualSA;

/**
 * <b>Descripción</b><br>
 * Clase que hace de mediador en la eliminación de símbolos anulables.
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
 * @author Francisco Javier Páramo Arnaiz
 * @version 2.0
 */
public class MediatorSA {

	// Attributes
	// --------------------------------------------------------------------
	/**
	 * Algoritmo de limpieza asociado al mediador.
	 */
	public Cleaning mCleanAlgorithm;

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
	private VisualSA mVisual;

	/**
	 * Número temporal para saber qué producciones iluminar en cada momento.
	 */
	private int mTempSize;

	// Methods
	// -----------------------------------------------------------------------

	/**
	 * Constructor completo del mediador del algoritmo de limpieza de símbolos
	 * anulables.
	 * 
	 * @param sa
	 *            Pantalla del algoritmo.
	 * @param grammar
	 *            Gramática a limpiar.
	 */
	public MediatorSA(VisualSA sa, Grammar grammar) {
		mCleanAlgorithm = new EliminateSA(grammar);
		mGrammar = grammar;
		mFlagFirst = true;
		mVisual = sa;
		mVisual.mOld.setHTML(HTMLConverter.toHTML(mGrammar.completeToString()));

		if (!mCleanAlgorithm.firstStep()) {
			ShowDialog.nonCancelSymbols();
			exit();
			mVisual.mVisible = false;
			mVisual = null;
		} else
			mCleanAlgorithm = new EliminateSA(grammar);
	}// MediatorSNA

	/**
	 * Siguiente paso del algoritmo.
	 */
	public void next() {
		String temp;

		// FirstStep
		if (mFlagFirst) {
			mCleanAlgorithm.firstStep();
			mVisual.mNew.setHTML(HTMLConverter.toHTML(mCleanAlgorithm
					.getSolution().completeToString()));
			// Iluminamos mOld y mNew
			for (Production prod : mGrammar.getProductions())
				if (mCleanAlgorithm.getSolution().getProductions()
						.contains(prod))
					highLight(mVisual.mNew, prod.toString(), true);
				else
					highLight(mVisual.mOld, prod.toString(), false);
			mTempSize = mCleanAlgorithm.getSolution().completeToString()
					.length() - 3;
			setAux();
			mFlagFirst = false;

		} else { // NextStep
			removeAllHighLight();
			setAux();
			highLightAux();
			// Última iteración
			if (!mCleanAlgorithm.nextStep())
				finish();
			// En cada iteración
			mVisual.mNew.setHTML(HTMLConverter.toHTML(mCleanAlgorithm
					.getSolution().completeToString()));
			temp = mCleanAlgorithm.getSolution().completeToString();

			// Iluminar producción a producción.
			for (String prod : temp.substring(mTempSize, temp.length() - 3)
					.split("\n"))
				highLight(mVisual.mNew, prod, true);

			mTempSize = mCleanAlgorithm.getSolution().completeToString()
					.length() - 3;
		}
	}// next

	/**
	 * Todos los pasos del algoritmo.
	 */
	public void all() {
		mCleanAlgorithm = new EliminateSA(mGrammar);

		if (!mCleanAlgorithm.allSteps()) {
			ShowDialog.nonCancelSymbols();
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

			mVisual.mGeneralPanel.clear();
			new GrammarServiceClientImp(GWT.getModuleBaseURL()
					+ "grammarservice", mCleanAlgorithm.getSolution());

		}
	}// accept

	/**
	 * Asigna al RichTextPane mAux el valor en cada paso.
	 */
	private void setAux() {
		String temp;

		temp = ((EliminateSA) mCleanAlgorithm).getCancel().toString();
		mVisual.mAux.setHTML(HTMLConverter.toHTML(temp.substring(1,
				temp.length() - 1)));
		mTempSize = mCleanAlgorithm.getSolution().completeToString().length() - 3;
	}// setAux

	/**
	 * Ilumina/Resalta el no terminal anulable en cada momento.
	 */
	private void highLightAux() {

		highLight(mVisual.mAux, ((EliminateSA) mCleanAlgorithm).currentCancel()
				.toString(), false);
	}// highLightAux*/

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
		mVisual.mBtnOneStep.setEnabled(false);
		mVisual.mBtnAllSteps.setEnabled(false);
		mVisual.mBtnAcept.setEnabled(true);

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

	}// removeAllHighLight()*/

	/**
	 * Crea una nueva vista con la grmática vieja.
	 */
	public void exit() {

		mVisual.mGeneralPanel.clear();
		new GrammarServiceClientImp(GWT.getModuleBaseURL() + "grammarservice",
				mGrammar);

	}// exit

}// MediatorSA