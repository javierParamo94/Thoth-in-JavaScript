package src.client.gui.mediator;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RichTextArea;

import src.client.core.grammar.Grammar;
import src.client.core.grammar.Production;
import src.client.core.grammar.cleaner.Cleaning;
import src.client.core.grammar.cleaner.EliminateIndirectRecursion;
import src.client.gui.Application;
import src.client.gui.utils.HTMLConverter;
import src.client.gui.utils.ShowDialog;
import src.client.gui.visual.VisualIndirectRecursive;

/**
 * <b>Descripción</b><br>
 * Clase que hace de mediador entre el algoritmo de eliminación de recursividad 
 * indirecta.
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
public class MediatorIndirectRecursive {
    
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
    private VisualIndirectRecursive mVisual;
    
    // Methods -----------------------------------------------------------------------
    
    /**
     * Constructor completo del mediador del algoritmo de recursividad indirecta.
     * 
     * @param vir Pantalla del algoritmo.
     * @param grammar Gramática a limpiar.
     */
    public MediatorIndirectRecursive (VisualIndirectRecursive vir, Grammar grammar) {
        mCleanAlgorithm = new EliminateIndirectRecursion(grammar);
        mGrammar = grammar;
        mVisual = vir;
        mVisual.mOld.setHTML(HTMLConverter.toHTML(mGrammar.completeToString()));
        mVisual.mNew.setHTML(HTMLConverter.toHTML(mCleanAlgorithm.getSolution().completeToString()));
        
        if(!mCleanAlgorithm.firstStep()){
            //ShowDialog.nonRecursiveIndir();
            mVisual.mVisible = false;
        }
    }//MediatorIndirectRecursive
    
    /**
     * Cada paso del algoritmo.
     */
    public void next () {
            //Borramos todos los resaltos
        removeAllHighLight();
            //Última iteración
        if(!mCleanAlgorithm.nextStep()){
            mVisual.mAux.setHTML(HTMLConverter.toHTML(""));
            mVisual.mRec.setHTML(HTMLConverter.toHTML(""));
            mVisual.mNew.setHTML(HTMLConverter.toHTML(mCleanAlgorithm.getSolution().completeToString()));
            finish();
        }
        else{
                //En cada iteración
            setAux();
            mVisual.mNew.setHTML(HTMLConverter.toHTML(mCleanAlgorithm.getSolution().completeToString()));
            setRec();
        }
        
    }//next
    
    /**
     * Todos los pasos del algoritmo.
     */
    public void all () {
        mCleanAlgorithm = new EliminateIndirectRecursion(mGrammar);
        
        if(!mCleanAlgorithm.allSteps()){
            //ShowDialog.nonRecursiveIndir();
            exit();
        }
        else
            mVisual.mNew.setHTML(HTMLConverter.toHTML(mCleanAlgorithm.getSolution().completeToString()));
        
        removeAllHighLight();
        finish();
        
    }//all
    
    /**
     * Aceptar.<br>
     * Cierra el diálogo y crea una nueva pestaña con la gramática.
     */
    public void accept () {
        Application app = Application.getInstance();
        
        /*if(mVisual.mNew.getText().length()>0){
            app.createTab(Messages.GRAMMAR + " " + Actions.mCountGram,
                    new PanelGrammar(mCleanAlgorithm.getSolution()));
            Actions.mCountGram++;
            app.getCurrentTab().setChanges(true);
        }
        ((PanelGrammar)app.getCurrentTab()).checkContent();*/
        exit();
        
    }//accept
    
    /**
     * Asigna al JTextPane mAux el valor en cada paso y le ilumina.
     */
    private void setAux () {
        Production prod = ((EliminateIndirectRecursion)mCleanAlgorithm).getOldProd();
        String temp = prod.toString().substring(0, prod.toString().length()-1);
        
        mVisual.mAux.setHTML(HTMLConverter.toHTML(temp));
        highLight(mVisual.mAux, temp, false);
        highLight(mVisual.mOld, temp, false);
    }//setAux
    
    /**
     * Asigna al JTextPane mRec el valor en cada paso.<br>
     * Ilumina tanto éste JTextPane como mNew.
     */
    private void setRec () {
        String temp = new String();
        
        for(Production prod : ((EliminateIndirectRecursion)mCleanAlgorithm).getAllProds()){
            highLight(mVisual.mNew, prod.toString(), true);
            temp += prod.toString();
        }
        mVisual.mRec.setText(temp);
        
        for(Production prod : ((EliminateIndirectRecursion)mCleanAlgorithm).getRecDirProds())
            highLight(mVisual.mNew, prod.toString(), true);
        
    }//setRec
    
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
        mVisual.btnOneStep.setEnabled(false);
        mVisual.btnAllSteps.setEnabled(false);
        mVisual.btnAcept.setEnabled(true);
        
    }//finish
    
    /**
     * Deselecciona la zona resaltada.
     */
    private void removeAllHighLight() {
		String str, str1, str2, str3, str4, str5, str6, str7;
		str = mVisual.mNew.getHTML().replaceAll("<mark class=\"green\">", "")
				.replaceAll("<mark class=\"red\">", "");
		str1 = str.replaceAll("</mark>", "");
		str2 = mVisual.mOld.getHTML().replaceAll("<mark class=\"green\">", "")
				.replaceAll("<mark class=\"red\">", "");
		str3 = str2.replaceAll("</mark>", "");
		str4 = mVisual.mAux.getHTML().replaceAll("<mark class=\"green\">", "")
				.replaceAll("<mark class=\"red\">", "");
		str5 = str4.replaceAll("</mark>", "");
		str6 = mVisual.mAux.getHTML().replaceAll("<mark class=\"green\">", "")
				.replaceAll("<mark class=\"red\">", "");
		str7 = str6.replaceAll("</mark>", "");

		mVisual.mNew.setHTML(str1);
		mVisual.mOld.setHTML(str3);
		mVisual.mAux.setHTML(str5);
		mVisual.mRec.setHTML(str7);
        
    }//removeAllHighLight()
    
    /**
     * Cierra la ventana visual y elimina las referencias.
     */
    public void exit () {
    	Window.Location.reload();
    	/*
        mVisual.setVisible(false);
        mVisual.mMediator = null;
        mVisual = null;*/
        
    }//exit
    
}//MediatorIndirectRecursive
