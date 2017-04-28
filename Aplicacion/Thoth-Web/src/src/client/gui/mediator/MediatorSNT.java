package src.client.gui.mediator;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RichTextArea;

import src.client.GrammarServiceClientImp;
import src.client.core.grammar.Grammar;
import src.client.core.grammar.Production;
import src.client.core.grammar.cleaner.Cleaning;
import src.client.core.grammar.cleaner.EliminateSNT;
import src.client.gui.Application;
import src.client.gui.mainGui;
import src.client.gui.utils.ShowDialog;
import src.client.gui.utils.HTMLConverter;
import src.client.gui.visual.VisualSNT;

/**
 * <b>Descripción</b><br>
 * Clase que hace de mediador entre el algoritmo de eliminación de símbolos no terminables
 * y la pantalla que muestra dicho algoritmo.
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
     * Bandera usada para saber si ha realizado el primer paso del algoritmo.
     */
    private boolean mFlagFirst;
    
    /**
     * Algoritmo visual al que está asociado.
     */
    private VisualSNT mVisual;
    private mainGui gui;
    private GrammarServiceClientImp serviceImp;
    
    // Methods -----------------------------------------------------------------------
    
    /**
     * Constructor completo del mediador del algoritmo de limpieza de símbolos no
     * terminables.
     * 
     * @param snt Pantalla del algoritmo.
     * @param grammar Gramática a limpiar.
     */
    public MediatorSNT (VisualSNT snt, Grammar grammar) {
        mCleanAlgorithm = new EliminateSNT(grammar);
        mGrammar = grammar;
        mFlagFirst = true;
        mVisual = snt;
        mVisual.mOld.setHTML(HTMLConverter.toHTML(mGrammar.completeToString()));
        mVisual.mNew.setHTML(HTMLConverter.toHTML(mCleanAlgorithm.getSolution().completeToString()));
        
        if(!mCleanAlgorithm.firstStep()){
        	ShowDialog.nonTerminalSymbols();
            mVisual.mVisible = false;
            mVisual.mMediator = null;
        }
        else
            mCleanAlgorithm = new EliminateSNT(grammar);
        
    }//MediatorSNT
    
    
    /**
     * Cada paso del algoritmo.
     */
    public void next () {
           //Primer paso 
        if(mFlagFirst){
            if(!mCleanAlgorithm.firstStep()){
                finish();
                ShowDialog.nonTerminalSymbols();
                mVisual.mVisible = false;
                exit();
                return;
            }
            mVisual.mNew.setHTML(HTMLConverter.toHTML(mCleanAlgorithm.getSolution().completeToString()));
            for(Production prod : mCleanAlgorithm.getSolution().getProductions()){
                highLight(mVisual.mOld, prod.toString());
                highLight(mVisual.mNew, prod.toString());
            }
            mFlagFirst = false;
        }   //Siguiente paso
        else
            if(!mCleanAlgorithm.nextStep())
                finish();
            else{
                mVisual.mNew.setHTML(HTMLConverter.toHTML(mCleanAlgorithm.getSolution().completeToString()));
                removeAllLighter();
                highLight(mVisual.mOld, mCleanAlgorithm.getSolution().getProductions().lastElement().toString());
                highLight(mVisual.mNew, mCleanAlgorithm.getSolution().getProductions().lastElement().toString());
            }
        
    }//next
    
    /**
     * Todos los pasos del algoritmo.
     */
    public void all () {
        mCleanAlgorithm = new EliminateSNT(mGrammar);
        
        if(!mCleanAlgorithm.allSteps()){
            ShowDialog.nonTerminalSymbols();
            mVisual.mVisible = false;
        }
        else
            mVisual.mNew.setHTML(HTMLConverter.toHTML(mCleanAlgorithm.getSolution().completeToString()));
        
        finish();
    }//all
    
    /**
     * Aceptar.<br>
     * Cierra el diálogo y crea una nueva pestaña con la gramática.
     */
    public void accept () {
        Application app = Application.getInstance();
        
        if(mVisual.mNew.getText().length()>0){
        	//serviceImp.getManGUI(); 
        	//new mainGui(serviceImp);
        	//gui.tabPanel.add(mVisual.mNew,"nuevo");
        	//Window.Location.reload();
        	//mVisual.mNew.setText
            /*app.createTab(Messages.GRAMMAR + " " + Actions.mCountGram,
                    new PanelGrammar(mCleanAlgorithm.getSolution()));
            Actions.mCountGram++;
            app.getCurrentTab().setChanges(true);*/
        }
        
        //((PanelGrammar)app.getCurrentTab()).checkContent();
        exit();
        //gui.tabPanel.add(mVisual.mNew,"nuevo");
    }//accept
    
    /**
     * Ilumina/Resalta el texto del panel donde se encuentra la antigua gramática que
     * coincida con pattern. 
     * 
     * @param pattern Patrón de texto a iluminar.
     */
    private void highLight (RichTextArea pane, String pattern) {
        String text, text1 = "";
        int posEnd = 0, posStart = 0;
        String greenCol = "<mark>";
        
        if(pattern.equals(""))
            return;
        
        pattern = pattern.toString().substring(0, pattern.toString().length() - 1);
        text = pane.getHTML();
        while((posEnd = text.indexOf(pattern, posEnd)) >= 0 ){
        	
        	text1 += text.substring(posStart,posEnd) + greenCol + pattern + "</mark>" ;

            posEnd += pattern.toString().length();
            posStart = posEnd;
            }
        text1 += text.substring(posStart, pane.getHTML().length());
        pane.setHTML(text1);
    

    }//highLight
    

	/**
     * Clase privada de apoyo para asignar el color a la zona resaltada.
     */
   /* private class MyHighLight extends DefaultHighlighter.DefaultHighlightPainter {
        public MyHighLight () {
            super(Colors.green());
        }//MyHighLight
    }//MyHighLight
    
    /**
     * Deshabilita los botones de Siguiente y Todos los pasos y habilita el de aceptar.
     */
    public void finish () {
        mVisual.btnOneStep.setEnabled(false);
        mVisual.btnAllSteps.setEnabled(false);
        mVisual.btnAcept.setEnabled(true);
        removeAllLighter();
        
    }//finish
    
    /**
     * Quita todos los marcadores de los paneles de las gramáticas.
     */
    public void removeAllLighter() {
    	
    	String str, str1, str2, str3;
    	str = mVisual.mNew.getHTML().replaceAll("<mark>", "");
    	str1 = str.replaceAll("</mark>", "");
    	str2 = mVisual.mOld.getHTML().replaceAll("<mark>", "");
    	str3 = str2.replaceAll("</mark>", "");
    	
    	mVisual.mNew.setHTML(str1);
    	mVisual.mOld.setHTML(str3);
    	
    }//removeAllLighter
    
    /**
     * Cierra la ventana visual y elimina las referencias.
     */
    public void exit () {
    	Window.Location.reload();
    	/*mVisual.setVisible(false);
        mVisual.mMediator = null;
        mVisual = null;
        mVisual.vPanel.clear();
        mVisual.vPanel = null;
        new mainGui(serviceImp);*/
        //vPanel.add(currentPage);
        
    }//exit
    
}//MediatorSNT
