package src.client.gui.mediator;

import com.google.gwt.user.client.Window;

import src.client.core.grammar.*;
import src.client.core.grammar.cleaner.Cleaning;
import src.client.core.grammar.cleaner.EliminateSNA;
import src.client.gui.Application;
import src.client.gui.utils.ShowDialog;
import src.client.gui.visual.VisualSNA;

/**
 * <b>Descripción</b><br>
 * Clase que hace de mediador entre el algoritmo de eliminación de símbolos no alcanzables
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
public class MediatorSNA {
    
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
    private VisualSNA mVisual;
    
    // Methods -----------------------------------------------------------------------
    
    /**
     * Constructor completo del mediador del algoritmo de limpieza de símbolos no
     * alcanzables.
     * 
     * @param sna Pantalla del algoritmo.
     * @param grammar Gramática a limpiar.
     */
    public MediatorSNA (VisualSNA sna, Grammar grammar) {
        mCleanAlgorithm = new EliminateSNA(grammar);
        mGrammar = grammar;
        mFlagFirst = true;
        mVisual = sna;
        mVisual.mOld.setText(mGrammar.completeToString());
        mVisual.mNew.setText(mCleanAlgorithm.getSolution().completeToString());
        
        if(!mCleanAlgorithm.firstStep()){
            ShowDialog.nonReachableSymbols();
            mVisual.mVisible = false;
            mVisual.mMediator = null;
        }
        else
            mCleanAlgorithm = new EliminateSNA(grammar);
    }//MediatorSNA
    
    /**
     * Siguiente paso del algoritmo.
     */
    public void next () {
            //Primer paso del algoritmo
        if(mFlagFirst){
            if(!mCleanAlgorithm.firstStep()){
                finish();
                ShowDialog.nonReachableSymbols();
                mVisual.mVisible = false;
                exit();
                return;
            }
            mVisual.mNew.setText(mCleanAlgorithm.getSolution().completeToString());
            for(Production prod : mCleanAlgorithm.getSolution().getProductions()){
                //highLight(mVisual.mOld, prod.toString());
                //highLight(mVisual.mNew, prod.toString());
            }
            mFlagFirst = false;
        }   //Siguiente paso
        else
            if(!mCleanAlgorithm.nextStep())
                finish();
            else{
                mVisual.mNew.setText(mCleanAlgorithm.getSolution().completeToString());
                removeAllHighLight();
                //highLight(mVisual.mOld, mCleanAlgorithm.getSolution().getProductions().lastElement().toString());
                //highLight(mVisual.mNew, mCleanAlgorithm.getSolution().getProductions().lastElement().toString());
            }
        
    }//next
    
    /**
     * Todos los pasos del algoritmo.
     */
    public void all () {
        mCleanAlgorithm = new EliminateSNA(mGrammar);
        
        if(!mCleanAlgorithm.allSteps()){
            ShowDialog.nonReachableSymbols();
            mVisual.mVisible = false;
        }
        else
            mVisual.mNew.setText(mCleanAlgorithm.getSolution().completeToString());
        
        finish();
    }//all
    
    /**
     * Aceptar.<br>
     * Cierra el diálogo y crea una nueva pestaña con la gramática.
     */
    public void accept () {
        Application app = Application.getInstance();
        
        if(mVisual.mNew.getText().length()>0){
            /*app.createTab(Messages.GRAMMAR + " " + Actions.mCountGram,
                    new PanelGrammar(mCleanAlgorithm.getSolution()));
            Actions.mCountGram++;
            app.getCurrentTab().setChanges(true);*/
        }
        
        //((PanelGrammar)app.getCurrentTab()).checkContent();
        exit();
        
    }//accept
    
    /**
     * Ilumina/Resalta el texto del panel donde se encuentra la antigua gramática que
     * coincida con pattern. 
     * 
     * @param pattern Patrón de texto a iluminar.
     */
  /*  private void highLight (JTextPane pane, String pattern) {
        String text;
        int pos = 0;
        
        if(pattern.equals(""))
            return;
        
        try{
            Highlighter hilite = pane.getHighlighter();
            text = pane.getText();
            while((pos = text.indexOf(pattern, pos)) >= 0 ){
                hilite.addHighlight(pos, pos + pattern.length(), new MyHighLight());
                pos += pattern.toString().length();
            }
        }catch(BadLocationException e){}

    }//highLight*/
    
    /**
     * Clase privada de apoyo para asignar el color a la zona resaltada.
     */
    /*private class MyHighLight extends DefaultHighlighter.DefaultHighlightPainter {
        public MyHighLight () {
            super(Colors.green());
        }//MyHighLight
    }//MyHighLight*/
    
    /**
     * Deshabilita los botones de Siguiente y Todos los pasos y habilita el de aceptar.
     */
    public void finish () {
        mVisual.btnOneStep.setEnabled(false);
        mVisual.btnAllSteps.setEnabled(false);
        mVisual.btnAcept.setEnabled(true);
        removeAllHighLight();
        
    }//finish
    
    /**
     * Quita todos los marcadores de los paneles de las gramáticas.
     */
    public void removeAllHighLight() {
        /*mVisual.mOld.getHighlighter().removeAllHighlights();
        mVisual.mNew.getHighlighter().removeAllHighlights();*/
        
    }//removeAllLighter
    

    /**
     * Cierra la ventana visual y elimina las referencias.
     */
    public void exit () {
    	Window.Location.reload();
        /*mVisual.setVisible(false);
        mVisual.mMediator = null;
        mVisual = null;*/
        
    }//exit
    
}//MediatorSNA
