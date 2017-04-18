package src.client.gui.mediator;

import com.google.gwt.user.client.Window;

import src.client.core.grammar.Grammar;
import src.client.core.grammar.Production;
import src.client.core.grammar.cleaner.Cleaning;
import src.client.core.grammar.cleaner.EliminateIndirectRecursion;
import src.client.gui.Application;
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
        mVisual.mOld.setText(mGrammar.completeToString());
        mVisual.mNew.setText(mCleanAlgorithm.getSolution().completeToString());
        
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
        //removeAllHighLight();
            //Última iteración
        if(!mCleanAlgorithm.nextStep()){
            mVisual.mAux.setText("");
            mVisual.mRec.setText("");
            mVisual.mNew.setText(mCleanAlgorithm.getSolution().completeToString());
            finish();
        }
        else{
                //En cada iteración
            setAux();
            mVisual.mNew.setText(mCleanAlgorithm.getSolution().completeToString());
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
     * Asigna al JTextPane mAux el valor en cada paso y le ilumina.
     */
    private void setAux () {
        Production prod = ((EliminateIndirectRecursion)mCleanAlgorithm).getOldProd();
        String temp = prod.toString().substring(0, prod.toString().length()-1);
        
        mVisual.mAux.setText(temp);
        //highLight(mVisual.mAux, temp, new MyRedHighLight());
        //highLight(mVisual.mOld, temp, new MyRedHighLight());
    }//setAux
    
    /**
     * Asigna al JTextPane mRec el valor en cada paso.<br>
     * Ilumina tanto éste JTextPane como mNew.
     */
    private void setRec () {
        String temp = new String();
        
        for(Production prod : ((EliminateIndirectRecursion)mCleanAlgorithm).getAllProds()){
            //highLight(mVisual.mNew, prod.toString(), new MyGreenHighLight());
            temp += prod.toString();
        }
        mVisual.mRec.setText(temp);
        
        /*for(Production prod : ((EliminateIndirectRecursion)mCleanAlgorithm).getRecDirProds())
            highLight(mVisual.mNew, prod.toString(), new MyGreenHighLight());*/
        
    }//setRec
    
    /**
     * Ilumina/Resalta el texto del panel donde se encuentra la antigua gramática que
     * coincida con pattern. 
     * 
     * @param pane JTextPane que va a ser resaltado.
     * @param pattern Patrón de texto a iluminar.
     * @param light Highlighter a aplicar.
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

    }//highLight
    
    /**
     * Clase privada de apoyo para asignar el color a la zona resaltada.
     */
   /* private class MyGreenHighLight extends DefaultHighlighter.DefaultHighlightPainter {
        public MyGreenHighLight () {
            super(Colors.green());
        }//MyGreenHighLight
    }//MyGreenHighLight
    
    /**
     * Clase privada de apoyo para asignar el color a la zona resaltada.
     */
  /*  private class MyRedHighLight extends DefaultHighlighter.DefaultHighlightPainter {
        public MyRedHighLight () {
            super(Colors.red());
        }//MyRedHighLight
    }//MyRedHighLight
    
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
  /*  private void removeAllHighLight() {
        mVisual.mOld.getHighlighter().removeAllHighlights();
        mVisual.mNew.getHighlighter().removeAllHighlights();
        mVisual.mRec.getHighlighter().removeAllHighlights();
        mVisual.mAux.getHighlighter().removeAllHighlights();
        
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
