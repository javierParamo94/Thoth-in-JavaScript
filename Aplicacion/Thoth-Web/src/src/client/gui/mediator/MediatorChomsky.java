package src.client.gui.mediator;

import java.util.Vector;

import javax.swing.text.Highlighter;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;

import src.client.core.grammar.*;
import src.client.core.grammar.cleaner.Chomsky;
import src.client.core.grammar.cleaner.Cleaner;
import src.client.gui.visual.VisualChomsky;
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
    
    // Attributes --------------------------------------------------------------------
	private HTML html = new HTML();
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
    
    // Methods -----------------------------------------------------------------------
    
    /**
     * Constructor completo del mediador del algoritmo de limpieza de producciones no
     * generativas.
     * 
     * @param chomsky Pantalla del algoritmo.
     * @param grammar Gramática a limpiar.
     */
    public MediatorChomsky (VisualChomsky chomsky, Grammar grammar) {
        mGrammar = grammar;
        mFlagFirst = true;
        mVisual = chomsky;
        
            //Mostramos el diálogo
        //if(ShowDialog.clearQuestion() == JOptionPane.YES_OPTION){
                //Limpiamos
            Cleaner clean = new Cleaner(mGrammar);
            
            if(!clean.toClean()){
                //ShowDialog.cleanError();
                mVisual.mVisible = false;
                return;
            }
            mGrammar = clean.getSolution();
            mCleanAlgorithm = new Chomsky(mGrammar);
            mVisual.mOld.setText(mGrammar.completeToString());
            mVisual.mNew.setText(mCleanAlgorithm.getSolution().completeToString());
            mVisual.mVisible = true;
       // }
        //else
           // mVisual.mVisible = false;
        
    }//MediatorChomsky
    
    /**
     * Primer paso del algoritmo.
     */
    public void next () {
        String temp;
        Vector<Production> prods;
        
        if(mFlagFirst){
            if(!mCleanAlgorithm.firstStep()){
                finish();
                //ShowDialog.chomskyError();
                return;
            }   //FirstStep
            prods = mCleanAlgorithm.getChanges();
            mVisual.mNew.setText(mCleanAlgorithm.getSolution().completeToString());
            
            for(Production prod:prods)
                highLight(mVisual.mNew, prod.toString()/*, new MyGreenHighLight()*/);
            mFlagFirst = false;
        }
        else{   //NextStep
            //removeAllHighLight();
                //Última iteración
            if(!mCleanAlgorithm.nextStep()){
                finish();
                mVisual.mAux.setText("");
                return;
            }
            prods = mCleanAlgorithm.getChanges();
            mVisual.mNew.setText(mCleanAlgorithm.getSolution().completeToString());
            temp = prods.firstElement().toString();
            mVisual.mAux.setText(temp.substring(0, temp.length()-1));
            highLight(mVisual.mAux, temp.substring(0, temp.length()-1));//,
                    //new MyRedHighLight());
            for(int i=1; i<prods.size(); i++ )
                highLight(mVisual.mNew, prods.elementAt(i).toString());
        
        }
        
    }//next
    
    /**
     * Todos los pasos del algoritmo.
     */
    public void all () {
        mCleanAlgorithm = new Chomsky(mGrammar);
        
        if(!mCleanAlgorithm.allSteps())
            ShowDialog.chomskyError();
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
     * Ilumina/Resalta el texto del panel donde se encuentra la antigua gramática que
     * coincida con pattern. 
     * 
     * @param pane JTextPane que va a ser resaltado.
     * @param pattern Patrón de texto a iluminar.
     * @param light Highlighter a aplicar.
     */
    private void highLight (TextArea pane, String pattern) {
        String text;
		String aux;
        int pos = 0;
        StringBuilder sb = new StringBuilder();
        
        if(pattern.equals(""))
            return;
        
        //Highlighter hilite = pane.getHighlighter();
        
        text = pane.getText();
        
        while((pos = text.indexOf(pattern, pos)) >= 0 ){
            //hilite.addHighlight(pos, pos + pattern.length(), light);
            //for (int i = pos; i < pos + pattern.length(); i++){
            	//sb.append("<span style='font-weight:bold;'>" + text.charAt(pos) + "</span>");
        		//pane.setSelectionRange(pos, pos + pattern.length());
        	//pane.getSelectedText();
        	pane.getCursorPos();
            	pane.getElement().getStyle().setColor("red");
            	
           // }
            pos += pattern.toString().length();
        }


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
   /* private class MyRedHighLight extends DefaultHighlighter.DefaultHighlightPainter {
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
   /* private void removeAllHighLight() {
        mVisual.mOld.getHighlighter().removeAllHighlights();
        mVisual.mAux.getHighlighter().removeAllHighlights();
        mVisual.mNew.getHighlighter().removeAllHighlights();
        
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
    
}//MediatorChomsky
