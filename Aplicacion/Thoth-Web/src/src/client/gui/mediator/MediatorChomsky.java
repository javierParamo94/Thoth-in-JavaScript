package src.client.gui.mediator;

import java.util.Vector;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HTML;

import src.client.GrammarServiceClientImp;
import src.client.core.grammar.*;
import src.client.core.grammar.cleaner.Chomsky;
import src.client.core.grammar.cleaner.Cleaner;
import src.client.gui.visual.VisualChomsky;
import src.client.gui.utils.HTMLConverter;
import src.client.gui.utils.ShowDialog;

/**
 * <b>Descripción</b><br>
 * Clase que hace de mediador en la forma normal de Chomsky.
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
public class MediatorChomsky {

	// Attributes
	// --------------------------------------------------------------------
	/**
	 * Algoritmo de limpieza asociado al mediador.
	 */
	private Chomsky mCleanAlgorithm;

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
	private VisualChomsky mVisual;

	// Methods
	// -----------------------------------------------------------------------

	/**
	 * Constructor completo del mediador del algoritmo de limpieza de
	 * producciones no generativas.
	 * 
	 * @param chomsky
	 *            Pantalla del algoritmo.
	 * @param grammar
	 *            Gramática a limpiar.
	 */
	public MediatorChomsky(VisualChomsky chomsky, Grammar grammar) {
		mGrammar = grammar;
		mFlagFirst = true;
		mVisual = chomsky;

		// Mostramos el diálogo
		// if(ShowDialog.clearQuestion() == JOptionPane.YES_OPTION){
		// Limpiamos
		Cleaner clean = new Cleaner(mGrammar);

		if (!clean.toClean()) {
			ShowDialog.cleanError();
			mVisual.mVisible = false;
			return;
		}
		mGrammar = clean.getSolution();
		mCleanAlgorithm = new Chomsky(mGrammar);
		mVisual.mOld.setHTML(HTMLConverter.toHTML(mGrammar.completeToString()));
		mVisual.mNew.setHTML(HTMLConverter.toHTML(mCleanAlgorithm.getSolution()
				.completeToString()));
		mVisual.mVisible = true;
		// }
		/*
		 * else mVisual.mVisible = false;
		 */
	}// MediatorChomsky

	/**
	 * Primer paso del algoritmo.
	 */
	public void next() {
		String temp;
		Vector<Production> prods;

		if (mFlagFirst) {
			if (!mCleanAlgorithm.firstStep()) {
				finish();
				ShowDialog.chomskyError();
				return;
			} // FirstStep
			prods = mCleanAlgorithm.getChanges();
			mVisual.mNew.setHTML(HTMLConverter.toHTML(mCleanAlgorithm
					.getSolution().completeToString()));

			for (Production prod : prods)
				highLight(mVisual.mNew, prod.toString(), true);
			mFlagFirst = false;
		} else { // NextStep
			removeAllHighLight();
			// Última iteración
			if (!mCleanAlgorithm.nextStep()) {
				finish();
				mVisual.mAux.setHTML(HTMLConverter.toHTML(""));
				return;
			}
			prods = mCleanAlgorithm.getChanges();
			mVisual.mNew.setHTML(HTMLConverter.toHTML(mCleanAlgorithm
					.getSolution().completeToString()));
			temp = prods.firstElement().toString();
			mVisual.mAux.setHTML(HTMLConverter.toHTML(temp.substring(0,
					temp.length() - 1)));
			highLight(mVisual.mAux, temp.substring(0, temp.length() - 1), false);
			for (int i = 1; i < prods.size(); i++)
				highLight(mVisual.mNew, prods.elementAt(i).toString(), true);

		}

	}// next

	/**
	 * Todos los pasos del algoritmo.
	 */
	public void all() {
		mCleanAlgorithm = new Chomsky(mGrammar);

		if (!mCleanAlgorithm.allSteps())
			ShowDialog.chomskyError();
		else
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
	 * 
	 * Ilumina/Resalta el texto de los paneles donde se encuentran las dos
	 * gramáticas que coincidan con pattern.
	 * 
	 * @param mNew
	 *            Panel en el que se encuentra el texto
	 * @param pattern
	 *            Texto a iluminar
	 * @param green
	 *            Booleano que determina el color de la iluminación.
	 */

	private void highLight(HTML mNew, String pattern, boolean green) {
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

		text = mNew.getHTML();
		while ((posEnd = text.indexOf(pattern, posEnd)) >= 0) {

			text1 += text.substring(posStart, posEnd) + openMark + pattern
					+ closeMark;

			posEnd += pattern.toString().length();
			posStart = posEnd;
		}
		text1 += text.substring(posStart, mNew.getHTML().length());
		mNew.setHTML(text1);

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
}// MediatorChomsky
