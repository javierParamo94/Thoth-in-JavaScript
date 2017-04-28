package src.client.gui.mediator;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RichTextArea;

import src.client.core.grammar.Production;
import src.client.core.grammar.cleaner.Cleaning;
import src.client.core.grammar.cleaner.EliminatePNG;
import src.client.gui.Application;
import src.client.gui.utils.HTMLConverter;
import src.client.gui.visual.VisualPNG;
import src.client.core.grammar.Grammar;

/**
 * <b>Descripción</b><br>
 * Clase que hace de mediador en la eliminación de produccciones no generativas.
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
public class MediatorPNG {
    
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
    private VisualPNG mVisual;
    
    /**
     * Número temporal para saber qué producciones iluminar en cada momento.
     */
    private int mTempSize;
    
    // Methods -----------------------------------------------------------------------
    
    /**
     * Constructor completo del mediador del algoritmo de limpieza de producciones no
     * generativas.
     * 
     * @param png Pantalla del algoritmo.
     * @param grammar Gramática a limpiar.
     */
    public MediatorPNG (VisualPNG png, Grammar grammar) {
        mCleanAlgorithm = new EliminatePNG(grammar);
        mGrammar = grammar;
        mFlagFirst = true;
        mVisual = png;
        mVisual.mOld.setHTML(HTMLConverter.toHTML(mGrammar.completeToString()));
        mVisual.mNew.setHTML(HTMLConverter.toHTML(mCleanAlgorithm.getSolution().completeToString()));
        mTempSize = mCleanAlgorithm.getSolution().completeToString().length() - 3;
        
        if(!mCleanAlgorithm.firstStep()){
            //ShowDialog.nonUniSymbols();
            mVisual.mVisible = false;
            mVisual = null;
        }
        else
            mCleanAlgorithm = new EliminatePNG(grammar);
        
    }//MediatorPNG
    
    /**
     * Siguiente paso del algoritmo.
     */
    public void next () {
        String temp;
        
        if(mFlagFirst){
            if(!mCleanAlgorithm.firstStep()){
                finish();
                exit();
                return;
            }   //FirstStep
            setAux();
            mVisual.mNew.setHTML(HTMLConverter.toHTML(mCleanAlgorithm.getSolution().completeToString()));
            temp = mCleanAlgorithm.getSolution().completeToString();
            highLight(mVisual.mNew, temp.substring(mTempSize, (temp.length()-3)) ,1);
            mTempSize = mCleanAlgorithm.getSolution().completeToString().length()-3;
            mFlagFirst = false;
        }
        else{   //NextStep
            removeAllHighLight();
            mVisual.mAux.setText("");
                //Última iteración
            if(!mCleanAlgorithm.nextStep()){
            	mVisual.mNew.setHTML(HTMLConverter.toHTML(mCleanAlgorithm.getSolution().completeToString()));
                for(Production prod : ((EliminatePNG)mCleanAlgorithm).getUnitedProductions())
                    highLight(mVisual.mOld, prod.toString(), 0);
                finish();
                return;
            }
            else
                setAux();
            //En cada iteración
            mVisual.mNew.setHTML(HTMLConverter.toHTML(mCleanAlgorithm.getSolution().completeToString()));
            temp = mCleanAlgorithm.getSolution().completeToString();
            highLight(mVisual.mNew, temp.substring(mTempSize, temp.length()-3),1);
            mTempSize = mCleanAlgorithm.getSolution().completeToString().length()-3;
        }
        
    }//next
    
    /**
     * Todos los pasos del algoritmo.
     */
    public void all () {
        mCleanAlgorithm = new EliminatePNG(mGrammar);
        
        if(!mCleanAlgorithm.allSteps()){
            //ShowDialog.nonUniSymbols();
            mVisual.mVisible = false;
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
            app.createTab(Messages.GRAMMAR  + " " + Actions.mCountGram,
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
        
        temp = ((EliminatePNG)mCleanAlgorithm).getUnited().toString();
        mVisual.mAux.setText("U (" + ((EliminatePNG)mCleanAlgorithm).getUnited().firstElement() +
                ") = " + temp.substring(1, temp.length()-1));
    }//setAux
    
    /**
     * Ilumina/Resalta el texto del panel donde se encuentra la antigua gramática que
     * coincida con pattern. 
     * 
     * @param pane JTextPane que va a ser resaltado.
     * @param pattern Patrón de texto a iluminar.
     * @param light Highlighter a aplicar.
     */
    private void highLight (RichTextArea pane, String pattern, int flag){
    	String text, text1 = "";
    int posEnd = 0, posStart = 0;
    String colour = "", colour2 = "";
    
    if (flag == 1){
    	colour = "<mark>";
    	colour2 = "</mark>";
    }else{
    	colour = "<mark2>";
    	colour2 = "</mark2>";
    }
    if(pattern.equals(""))
        return;
    
    pattern = pattern.toString().substring(0, pattern.toString().length() - 1);
    text = pane.getHTML();
    while((posEnd = text.indexOf(pattern, posEnd)) >= 0 ){
    	
    	text1 += text.substring(posStart,posEnd) + colour + pattern + colour2 ;

        posEnd += pattern.toString().length();
        posStart = posEnd;
        }
    text1 += text.substring(posStart, pane.getHTML().length());
    pane.setHTML(text1);

    }//highLight

    
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
    	String str, str1, str2, str3, str4, str5;
    	str = mVisual.mNew.getHTML().replaceAll("<mark>", "").replaceAll("<mark2>", "");
    	str1 = str.replaceAll("</mark>", "").replaceAll("</mark2>", "");
    	str2 = mVisual.mOld.getHTML().replaceAll("<mark>", "").replaceAll("<mark2>", "");
    	str3 = str2.replaceAll("<mark2>", "").replaceAll("</mark2>", "");
        
    	mVisual.mNew.setHTML(str1);
    	mVisual.mOld.setHTML(str3);
    	
    }//removeAllHighLight()*/
    

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
    
}//MediatorPNG