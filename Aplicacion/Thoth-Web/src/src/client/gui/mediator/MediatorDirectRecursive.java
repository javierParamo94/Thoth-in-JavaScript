package src.client.gui.mediator;


import com.google.gwt.user.client.Window;

import src.client.gui.Application;
import src.client.gui.utils.ShowDialog;
import src.client.gui.visual.VisualDirectRecursive;
import src.client.core.grammar.*;
import src.client.core.grammar.cleaner.Cleaning;
import src.client.core.grammar.cleaner.EliminateDirectRecursion;

/**
 * <b>Descripción</b><br>
 * Clase que hace de mediador entre el algoritmo de eliminación de recursividad directa
 * y la pantalla que lo muestra.
 * <p>
 * <b>Detalles</b><br>
 * Se encarga de mediar entre la parte gráfica y la parte física.<br>
 * Ilumina las producciones según se van creando, borrando,... haciendo más fácil la
 * comprensión del algoritmo.
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
    private VisualDirectRecursive mVisual;
    
    // Methods -----------------------------------------------------------------------
    
    /**
     * Constructor completo del mediador del algoritmo de eliminación de recursividad.
     * 
     * @param vdr Pantalla del algoritmo.
     * @param grammar Gramática a limpiar.
     */
    public MediatorDirectRecursive (VisualDirectRecursive vdr, Grammar grammar) {
        mCleanAlgorithm = new EliminateDirectRecursion(grammar);
        mGrammar = grammar;
        mFlagFirst = true;
        mVisual = vdr;
        mVisual.mOld.setText(mGrammar.completeToString());
        mVisual.mNew.setText(mCleanAlgorithm.getSolution().completeToString());
        
        if(!mCleanAlgorithm.firstStep()){
            //ShowDialog.nonRecursiveDir();
            mVisual.mVisible = false;
        }
        else
            mCleanAlgorithm = new EliminateDirectRecursion(grammar);
        
    }//MediatorDirectRecursive
    
    /**
     * Primer paso del algoritmo.
     */
    public void next () {
        
        if(mFlagFirst){
            if(!mCleanAlgorithm.firstStep()){
                finish();
                //ShowDialog.nonRecursiveDir();
                mVisual.mVisible = false;
                return;
            }   //FirstStep
            setAux();
            for(Production prod : ((EliminateDirectRecursion)mCleanAlgorithm).getRecProductions())
                //highLight(mVisual.mOld, prod.toString(), new MyGreenHighLight());
            mFlagFirst = false;
        }
        else{   //NextStep
            //removeAllHighLight();
                //Última iteración
            if(!mCleanAlgorithm.nextStep()){
                finish();
            }
                //En cada iteración
            mVisual.mNew.setText(mCleanAlgorithm.getSolution().completeToString());
            //highLightAux();
            //highLightNew();
        }
        
    }//next
    
    /**
     * Todos los pasos del algoritmo.
     */
    public void all () {
        mCleanAlgorithm = new EliminateDirectRecursion(mGrammar);
        
        if(!mCleanAlgorithm.allSteps()){
            //ShowDialog.nonRecursiveDir();
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
     * Asigna al JTextPane mAux el valor en cada paso.
     */
    private void setAux () {
        String temp = new String();
        
        for(Production prod : ((EliminateDirectRecursion)mCleanAlgorithm).getRecProductions())
            temp += prod.toString();
        
        mVisual.mAux.setText(temp);
    }//setAux
    
    /**
     * Resalta en verde las producciones que se han creado.
     */
   /* private void highLightNew () {
        for(Production prod : ((EliminateDirectRecursion)mCleanAlgorithm).getNewProductions())
            highLight(mVisual.mNew, prod.toString(), new MyGreenHighLight());
        
    }//highLightNew
    
    /**
     * Resalta en rojo las producciones que van a ser borradas.
     */
   /* private void highLightAux () {
        for(Production prod : ((EliminateDirectRecursion)mCleanAlgorithm).getRecProductions())
            highLight(mVisual.mAux, prod.toString(), new MyRedHighLight());
        
    }//highLightAux
    
    /**
     * Ilumina/Resalta el texto del panel donde se encuentra la antigua gramática que
     * coincida con pattern. 
     * 
     * @param pane JTextPane que va a ser resaltado.
     * @param pattern Patrón de texto a iluminar.
     * @param light Highlighter a aplicar.
     */
   /* private void highLight (JTextPane pane, String pattern,
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

    }//highLight
    
    /**
     * Clase privada de apoyo para asignar el color a la zona resaltada.
     */
  /*  private class MyGreenHighLight extends DefaultHighlighter.DefaultHighlightPainter {
        public MyGreenHighLight () {
            super(Colors.green());
        }//MyHighLight
    }//MyHighLight
    
    /**
     * Clase privada de apoyo para asignar el color a la zona resaltada.
     */
/*    private class MyRedHighLight extends DefaultHighlighter.DefaultHighlightPainter {
        public MyRedHighLight () {
            super(Colors.red());
        }//MyHighLight
    }//MyHighLight
    
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
        
    }//removeAllHighLight()
    
    /**
     * Cierra la ventana visual y elimina las referencias.
     */
    public void exit () {
    	Window.Location.reload();
        /*mVisual.setVisible(false);
        mVisual.mMediator = null;
        mVisual = null;*/
        
    }//exit
    
}//MediatorDirectRecursive