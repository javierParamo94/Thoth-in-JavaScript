package src.client.gui.mediator;

import com.google.gwt.user.client.Window;

import src.client.core.Symbol;
import src.client.core.grammar.Grammar;
import src.client.core.grammar.Production;
import src.client.core.grammar.cleaner.Cleaning;
import src.client.core.grammar.cleaner.LeftFactoring;
import src.client.gui.visual.VisualLeftFactoring;
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
        mVisual.mOld.setText(mGrammar.completeToString());
        mVisual.mNew.setText(mCleanAlgorithm.getSolution().completeToString());
        
    }//MediatorLeftFactoring
    
    /**
     * Cada paso del algoritmo.
     */
    public void next () {
            //NextStep
        //removeAllHighLight();
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
        //removeAllHighLight();
        finish();
        
    }//all
    
    /**
     * Aceptar.<br>
     * Cierra el diálogo y crea una nueva pestaña con la gramática.
     */
    public void accept () {
       /* Application app = Application.getInstance();
        
        if(mVisual.mNew.getText().length()>0){
            app.createTab(Messages.GRAMMAR + " " + Actions.mCountGram,
                    new PanelGrammar(mCleanAlgorithm.getSolution()));
            Actions.mCountGram++;
            app.getCurrentTab().setChanges(true);
        }        
        ((PanelGrammar)app.getCurrentTab()).checkContent();*/
        exit();
        
    }//accept
    
    /**
     * Asigna al JTextPane mAux el valor en cada paso e ilumina lo que corresponda.
     */
    private void setAux () {
        String temp = new String();
        
        for(Symbol s : ((LeftFactoring)mCleanAlgorithm).getPrefix())
            temp += s.toString();
        
        mVisual.mAux.setText(temp);
        
        //highLight(mVisual.mAux, temp, new MyGreenHighLight());
    }//setAux
    
    /**
     * Asigna al JTextPane mNew el valor en cada paso e ilumina lo que corresponda.
     */
    private void setNew () {
        Symbol s = ((LeftFactoring)mCleanAlgorithm).getNewProd().
                                firstElement().getLeft().firstElement();
        
        mVisual.mNew.setText(mCleanAlgorithm.getSolution().completeToString());
        
       // for(Production prod : mCleanAlgorithm.getSolution().getProductions())
           // if(prod.getRight().contains(s))
                //highLight(mVisual.mNew, prod.toString(), new MyGreenHighLight());
        
        //for(Production prod : ((LeftFactoring)mCleanAlgorithm).getNewProd())
            //highLight(mVisual.mNew, prod.toString(), new MyGreenHighLight());
    }//setNew
    
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
   /* private class MyGreenHighLight extends DefaultHighlighter.DefaultHighlightPainter {
        public MyGreenHighLight () {
            super(Colors.green());
        }//MyGreenHighLight
    }//MyGreenHighLight
    
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
    	/*
        mVisual.setVisible(false);
        mVisual.mMediator = null;
        mVisual = null;*/
        
    }//exit
    
}//MediatorLeftFactoring