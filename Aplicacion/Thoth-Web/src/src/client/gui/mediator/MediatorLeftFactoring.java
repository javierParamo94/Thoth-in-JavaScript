package src.client.gui.mediator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RichTextArea;

import src.client.GrammarServiceClientImp;
import src.client.core.Symbol;
import src.client.core.grammar.Grammar;
import src.client.core.grammar.Production;
import src.client.core.grammar.cleaner.Cleaning;
import src.client.core.grammar.cleaner.LeftFactoring;
import src.client.gui.visual.VisualLeftFactoring;
import src.client.gui.utils.HTMLConverter;
import src.client.gui.utils.ShowDialog;

/**
 * <b>Descripción</b><br>
 * Clase que hace de mediador entre el algoritmo de factorización por la izquierda.
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
public class MediatorLeftFactoring {
    
    // Attributes --------------------------------------------------------------------
    
    /**
     * Algoritmo de limpieza asociado al mediador.
     */
    private Cleaning mCleanAlgorithm;
    
    /**
     * Gramática a limpiar.
     */
    private Grammar mGrammar;
    
    /**
     * Algoritmo visual al que está asociado.
     */
    private VisualLeftFactoring mVisual;
    
    // Methods -----------------------------------------------------------------------
    
    /**
     * Constructor completo del mediador del algoritmo de factorización por la izquierda.
     * 
     * @param vlf Pantalla del algoritmo.
     * @param grammar Gramática a limpiar.
     */
    public MediatorLeftFactoring (VisualLeftFactoring vlf, Grammar grammar) {
        mCleanAlgorithm = new LeftFactoring(grammar);
        mVisual = vlf;
        if(!mCleanAlgorithm.firstStep()){
            //ShowDialog.nonFactorSymbols();
            mVisual.mVisible = false;
        }
        
        mGrammar = grammar;
        mVisual.mOld.setHTML(HTMLConverter.toHTML(mGrammar.completeToString()));
        mVisual.mNew.setHTML(HTMLConverter.toHTML(mCleanAlgorithm.getSolution().completeToString()));
        
    }//MediatorLeftFactoring
    
    /**
     * Cada paso del algoritmo.
     */
    public void next () {
            //NextStep
        removeAllHighLight();
        mVisual.mAux.setText("");
            //Última iteración
        if(!mCleanAlgorithm.nextStep())
            finish();
        else{
            //En cada iteración
            setAux();
            setNew();
        }
        
    }//next
    
    /**
     * Todos los pasos del algoritmo.
     */
    public void all () {
        mCleanAlgorithm = new LeftFactoring(mGrammar);
        
        if(!mCleanAlgorithm.allSteps()){
            //ShowDialog.nonFactorSymbols();
            mVisual.mVisible = false;
        }
        else
            mVisual.mNew.setText(mCleanAlgorithm.getSolution().completeToString());
        removeAllHighLight();
        finish();
        
    }//all
    
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
     * Asigna al JTextPane mAux el valor en cada paso e ilumina lo que corresponda.
     */
    private void setAux () {
        String temp = new String();
        
        for(Symbol s : ((LeftFactoring)mCleanAlgorithm).getPrefix())
            temp += s.toString();
        
        mVisual.mAux.setHTML(HTMLConverter.toHTML(temp));
        
        highLight(mVisual.mAux, temp, true);
    }//setAux
    
    /**
     * Asigna al JTextPane mNew el valor en cada paso e ilumina lo que corresponda.
     */
    private void setNew () {
        Symbol s = ((LeftFactoring)mCleanAlgorithm).getNewProd().
                                firstElement().getLeft().firstElement();
        
        mVisual.mNew.setHTML(HTMLConverter.toHTML(mCleanAlgorithm.getSolution().completeToString()));
        
        for(Production prod : mCleanAlgorithm.getSolution().getProductions())
            if(prod.getRight().contains(s))
                highLight(mVisual.mNew, prod.toString(), true);
        
        for(Production prod : ((LeftFactoring)mCleanAlgorithm).getNewProd())
            highLight(mVisual.mNew, prod.toString(), true);
    }//setNew
    
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
     * Deshabilita los botones de Siguiente y Todos los pasos y habilita el de aceptar.
     */
    public void finish () {
        mVisual.mAux.setText("");
        mVisual.btnOneStep.setEnabled(false);
        mVisual.btnAllSteps.setEnabled(false);
        mVisual.btnAcept.setEnabled(true);
        
    }//finish
    
	/**
	 * Deselecciona la zona resaltada.
	 */
	private void removeAllHighLight() {
		String str, str1, str2, str3;
		str = mVisual.mNew.getHTML().replaceAll("<mark class=\"green\">", "")
				.replaceAll("<mark class=\"red\">", "");
		str1 = str.replaceAll("</mark>", "");
		str2 = mVisual.mOld.getHTML().replaceAll("<mark class=\"green\">", "")
				.replaceAll("<mark class=\"red\">", "");
		str3 = str2.replaceAll("</mark>", "");

		mVisual.mNew.setHTML(str1);
		mVisual.mOld.setHTML(str3);

	}// removeAllHighLight()*/
    
	/**
	 * Crea una nueva vista con la grmática vieja.
	 */
	public void exit() {

		mVisual.vPanel.clear();
		new GrammarServiceClientImp(GWT.getModuleBaseURL() + "grammarservice",
				mGrammar);

	}// exit
}//MediatorLeftFactoring