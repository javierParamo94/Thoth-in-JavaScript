package src.client.gui.mediator;



import com.google.gwt.user.client.Window;

import src.client.core.grammar.*;
import src.client.core.grammar.cleaner.Cleaning;
import src.client.core.grammar.cleaner.EliminateSA;
import src.client.gui.Application;
/*import view.utils.Colors;
import view.utils.Messages;
import view.application.Application;
import view.application.Actions;
import view.grammar.PanelGrammar;*/
import src.client.gui.utils.ShowDialog;
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
 * @version 1.0
 */
public class MediatorSA {
    
    // Attributes --------------------------------------------------------------------
    
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
    
    
    // Methods -----------------------------------------------------------------------
    
    /**
     * Constructor completo del mediador del algoritmo de limpieza de símbolos
     * anulables.
     * 
     * @param sa Pantalla del algoritmo.
     * @param grammar Gramática a limpiar.
     */
    public MediatorSA (VisualSA sa, Grammar grammar) {
        mCleanAlgorithm = new EliminateSA(grammar);
        mGrammar = grammar;
        mFlagFirst = true;
        mVisual = sa;
        mVisual.mOld.setText(mGrammar.completeToString());
        
        if(!mCleanAlgorithm.firstStep()){
            ShowDialog.nonCancelSymbols();
            exit();
            //mVisual.mVisible = false;
            //mVisual = null;
        }
        else
            mCleanAlgorithm = new EliminateSA(grammar);
    }//MediatorSNA
    
    /**
     * Siguiente paso del algoritmo.
     */
    public void next () {
        String temp;

            //FirstStep
        if(mFlagFirst){
            mCleanAlgorithm.firstStep();
            mVisual.mNew.setText(mCleanAlgorithm.getSolution().completeToString());
                //Iluminamos mOld y mNew
            for(Production prod : mGrammar.getProductions())
                //if(mCleanAlgorithm.getSolution().getProductions().contains(prod))
                    //highLight(mVisual.mNew, prod.toString(), new MyGreenHighLight());
                //else
                    //highLight(mVisual.mOld, prod.toString(), new MyRedHighLight());
            mTempSize = mCleanAlgorithm.getSolution().completeToString().length()-3;
            setAux();
            mFlagFirst = false;
        }
        else{   //NextStep
            //removeAllHighLight();
            setAux();
            //highLightAux();
                //Última iteración
            if(!mCleanAlgorithm.nextStep())
                finish();
                //En cada iteración
            mVisual.mNew.setText(mCleanAlgorithm.getSolution().completeToString());
            temp = mCleanAlgorithm.getSolution().completeToString();
            //highLight(mVisual.mNew, temp.substring(mTempSize, temp.length()-3), new MyGreenHighLight());
            mTempSize = mCleanAlgorithm.getSolution().completeToString().length()-3;
        }
        
    }//next
    
    /**
     * Todos los pasos del algoritmo.
     */
    public void all () {
        mCleanAlgorithm = new EliminateSA(mGrammar);
        
        if(!mCleanAlgorithm.allSteps()){
            ShowDialog.nonCancelSymbols();
            mVisual.mVisible = false;
        }
        else
            mVisual.mNew.setText(mCleanAlgorithm.getSolution().completeToString());
        
        //removeAllHighLight();
        finish();
        
    }//all
    
    /**
     * Aceptar.<br>
     * Cierra el diálogo y crea una nueva pestaña con la gramática.
     */
    public void accept () {
        
        Application app = Application.getInstance();
        
       /* if(mVisual.mNew.getText().length()>0){
            app.createTab(Messages.GRAMMAR + " " + Actions.mCountGram,
                    new PanelGrammar(mCleanAlgorithm.getSolution()));
            Actions.mCountGram++;
            app.getCurrentTab().setChanges(true);
        }
        
        ((PanelGrammar)app.getCurrentTab()).checkContent();*/
        exit();
        
    }//accept
    
    /**
     * Asigna al JTextPane mAux el valor en cada paso.
     */
    private void setAux () {
        String temp;
        
        temp = ((EliminateSA)mCleanAlgorithm).getCancel().toString();
        mVisual.mAux.setText(temp.substring(1, temp.length()-1));
        mTempSize = mCleanAlgorithm.getSolution().completeToString().length()-3;
        
    }//setAux
    
    /**
     * Ilumina/Resalta el no terminal anulable en cada momento.
     */
   /* private void highLightAux () {
        highLight(mVisual.mAux, ((EliminateSA)mCleanAlgorithm).currentCancel().toString(),
                new MyRedHighLight());
        
    }//highLightAux*/
    
    /**
     * Ilumina/Resalta el texto del panel donde se encuentra la antigua gramática que
     * coincida con pattern. 
     * 
     * @param pattern Patrón de texto a iluminar.
     */
    /*private void highLight (JTextPane pane, String pattern, 
                            DefaultHighlighter.DefaultHighlightPainter light) {
        String text;
        int pos = 0;
        
        if(pattern.equals(""))
            return;
        
        try{
            Highlighter hilite = pane.getHighlighter();
            text = pane.getText();
            while((pos = text.indexOf(pattern, pos)) >= 0 ){
                hilite.addHighlight(pos, pos + pattern.length(), light);
                pos += pattern.toString().length();
            }
        }catch(BadLocationException e){}

    }//highLight*/
    
    /**
     * Clase privada de apoyo para asignar el color a la zona resaltada.
     */
   /* private class MyGreenHighLight extends DefaultHighlighter.DefaultHighlightPainter {
        public MyGreenHighLight () {
            super(Colors.green());
        }//MyGreenHighLight
    }//MyGreenHighLight*/
    
    /**
     * Clase privada de apoyo para asignar el color a la zona resaltada.
     */
   /* private class MyRedHighLight extends DefaultHighlighter.DefaultHighlightPainter {
        public MyRedHighLight () {
            super(Colors.red());
        }//MyRedHighLight
    }//MyRedHighLight*/
    
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
   /* private void removeAllHighLight() {
        mVisual.mOld.getHighlighter().removeAllHighlights();
        mVisual.mNew.getHighlighter().removeAllHighlights();
        mVisual.mAux.getHighlighter().removeAllHighlights();
        
    }//removeAllHighLight()*/
    
    /**
     * Cierra la ventana visual y elimina las referencias.
     */
    public void exit () {
    	Window.Location.reload();
        /*mVisual.setVisible(false);
        mVisual.mMediator = null;
        mVisual = null;*/
        
    }//exit
    
}//MediatorSA