package algorithms.convert;

import java.awt.Dimension;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultGraphModel;

import algorithms.MediatorAlgorithm;

import core.grammar.Grammar;
import view.application.Application;
import view.application.AutomatonSplitPane;
import view.utils.Colors;
import view.utils.ShowDialog;
import view2.controller.graphcontroller.LogicVisualConverter;

/**
 * <b>Descripci�n</b><br>
 * Clase que hace de mediador con la clase f�sica que obtiene un aut�amata finito
 * a partir de una gram�tica.
 * <p>
 * <b>Detalles</b><br>
 * Se encarga de mediar entre la parte gr�fica y la parte f�sica.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Evita el acomplamiento entre la parte f�sica y la parte visual.
 * </p>
 * 
 * @author �lvar Arn�iz Gonz�lez, Andr�s Arn�iz Moreno
 * @version 1.0
 */
public class MediatorObtainFA implements MediatorAlgorithm{
    
    // Attributes -----------------------------------------------------------------
    
    /**
     * Algoritmo visual. Pantalla donde se muestra la ejecuci�n del algoritmo.
     */
    private VisualObtainFA mVisual;
    
    /**
     * Algoritmo de creaci�n de aut�matas finitos mediante derivadas.
     */
    private ObtainFA mAlgorithm;
    
    /**
     * �ndice que nos marca la producci�n actual.
     */
    private int mProdIndex;
    
    // Methods --------------------------------------------------------------------
    
    /**
     * Constructor del mediador.<br>
     * Se asegura de que el algoritmo se puede realizar y de la inicializaci�n de todos
     * los par�metros y variables.
     * 
     * @param visualObtain Algoritmo visual. Pantalla donde se va a mostrar el algoritmo.
     * @param grammar Gram�tica a convertir en aut�mata. 
     */
    public MediatorObtainFA (VisualObtainFA visualObtain, Grammar grammar) {
        mVisual = visualObtain;
        mAlgorithm = new ObtainFA(grammar);
        
        if(!mAlgorithm.firstStep()){
            ShowDialog.noRightLinear();
            mVisual.mVisible = false;
            mVisual = null;
            return;
        }
        mVisual.mTextGrammar.setText(grammar.completeToString());
        
        JGraph graph = LogicVisualConverter.createGraph(mAlgorithm.getSolution(),new Dimension(550, 450));
        mVisual.setShowingPane(graph);
        mProdIndex = 0;
        
    }//MediatorObtainFA
    
    /**
     * Realiza cada paso del algoritmo.<br>
     * Muestra el aut�mata tal y como est� en cada momento e ilumina la
     * producci�n de la que se est� obteniendo un estado.
     * 
     * @return True si puede seguir ejecutando el algoritmo y false en caso contrario.
     */
    public boolean next () {
        boolean flag = true;
        
        mVisual.mTextGrammar.getHighlighter().removeAllHighlights();
        
            //�ltimo paso
        if(!mAlgorithm.nextStep()){
            disableButtons();
            flag = false;
        }
        
        mVisual.setShowingPane(LogicVisualConverter.createGraph(mAlgorithm.getSolution(), new Dimension(550, 500)));
        highLight(mVisual.mTextGrammar, mAlgorithm.getGrammar().getProductions().elementAt(mProdIndex).toString());
        mProdIndex++;
        
        return flag;
    }//next
    
    /**
     * Ilumina/Resalta el texto del panel que se le pasa que
     * coincida con pattern. 
     * 
     * @param pane Panel sobre el que iluminar.
     * @param pattern Patr�n de texto a iluminar.
     */
    private void highLight (JTextPane pane, String pattern) {
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
    }//highLight
    
    /**
     * Clase privada de apoyo para asignar el color a la zona resaltada.
     */
    private class MyHighLight extends DefaultHighlighter.DefaultHighlightPainter {
        public MyHighLight () {
            super(Colors.yellow());
        }//MyHighLight
    }//MyHighLight
    
    /**
     * Ejecuta todos los pasos del algoritmo.
     */
    public void all () {
        mVisual.mTextGrammar.getHighlighter().removeAllHighlights();
        while(mAlgorithm.nextStep());
        
        mVisual.setShowingPane(LogicVisualConverter.createGraph(mAlgorithm.getSolution(), new Dimension(550, 500)));
        disableButtons();
        
    }//check
    
    /**
     * Crea el aut�mata visual y cierra la ventana.<br>
     * Si la ventana seleccionada no contiene ning�n aut�mata lo crear�
     * en esa pesta�a, si por el contrario ya tiene uno a�adir� una nueva.
     */
    public void accept () {
    	Application app = Application.getInstance();
        LogicVisualConverter con = new LogicVisualConverter();
        DefaultGraphModel model = con.logicToVisual(mAlgorithm.getSolution(),LogicVisualConverter.DEFAULT_SIZE,true);
        
        app.createTab(mAlgorithm.getSolution().getName(), new AutomatonSplitPane(model,mAlgorithm.getSolution()));
        app.getCurrentTab().setChanges(true);
        app.getCurrentTab().checkContent();
        
        exit();
        
        exit();
    }//accept

    /**
     * Habilita o deshabilita los botones de siguiente paso y todos pasos.
     */
    private void disableButtons () {
        mVisual.mNext.setEnabled(false);
        mVisual.mAll.setEnabled(false);
        mVisual.mAccept.setEnabled(true);
        
    }//disableButtons
    
    /**
     * Desreferencia el mediador del visual y cirrar la pantalla visual.
     */
    public void exit () {
        mVisual.liberate();
        
    }//exit
    
}//MediatorObtainFA
