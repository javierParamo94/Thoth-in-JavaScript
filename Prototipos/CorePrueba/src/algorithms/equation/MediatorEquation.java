package algorithms.equation;

import java.io.StringReader;


import org.jgraph.JGraph;
import org.jgraph.graph.GraphLayoutCache;

import algorithms.InitializeException;
import algorithms.MediatorAlgorithm;
import algorithms.equivalence.EquivalenceFA;
import algorithms.regExp.ahosethi.AhoSethiUllman;
import algorithms.regExp.ahosethi.AhoTree;
import algorithms.regExp.parserjavacc.ParseException;
import algorithms.regExp.parserjavacc.ParserExpReg;

import core.TerminalEmpty;
import core.TerminalEnd;
import core.finiteautomaton.FiniteAutomaton;
import view.utils.Messages;
import view.utils.ShowDialog;
import view2.controller.CellsCreatorHelper;

/**
 * <b>Descripción</b><br>
 * Clase que hace de mediador con la clase física que obtiene una expresión 
 * regular a partir del método de las ecuaciones características.
 * <p>
 * <b>Detalles</b><br>
 * Se encarga de mediar entre la parte gráfica y la parte física.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Evita el acomplamiento entre la parte física y la parte visual.
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @version 1.0
 */
public class MediatorEquation implements MediatorAlgorithm{
    
    // Attributes -----------------------------------------------------------------
    
    /**
     * Algoritmo visual. Pantalla donde se muestra la ejecución del algoritmo.
     */
    private VisualEquation mVisual;
    
    /**
     * Algoritmo de las ecuaciones características.
     */
    private CharacteristicEquation mAlgorithm;
    
    /**
     * Indica el primer paso del algoritmo.
     */
    private boolean mFirstTime;

	private FiniteAutomaton aut;
    
    // Methods --------------------------------------------------------------------
    
    /**
     * Constructor del mediador.<br>
     * Se asegura de que el algoritmo se puede realizar y de la inicialización de todos
     * los parámetros y variables.
     * 
     * @param visual Algoritmo visual. Pantalla donde se va a mostrar el algoritmo.
     * @param automaton Aut visual sobre el que se va a ejecutar el algoritmo.
     * @param gCache Caché
     */
    public MediatorEquation (VisualEquation visual, FiniteAutomaton automaton, GraphLayoutCache gCache) {
        mVisual = visual;
        aut = automaton;
        mFirstTime = true;
        
        try{
            mAlgorithm = new CharacteristicEquation(aut);
            mAlgorithm.firstStep();
            JGraph graph = new JGraph(gCache);
            CellsCreatorHelper.scaleAndCenterGraph(graph, mVisual.getShowingPaneSize());
            graph.setEnabled(true);
            graph.setAntiAliased(true);
            mVisual.setShowingPane(graph);
        }catch(Exception e){
            ShowDialog.noInitialFinalState();
            mVisual = null;
        }
        
    }//MediatorEquation
    
    /**
     * Realiza cada paso del algoritmo, rellenando la tabla.
     * 
     * @return True si puede seguir ejecutando el algoritmo y false en caso contrario.
     */
    public boolean next () {
            //Primera vez
        if(mFirstTime){
            for(int i=0; i<mAlgorithm.mEquations.size(); i++)
                mVisual.mText.setText(mVisual.mText.getText() + Messages.STATE + " \"" +
                                      mAlgorithm.getAutomaton().getStates().elementAt(i).toString() + 
                                      "\":  " + mAlgorithm.mEquations.elementAt(i).toString() + "\n");
            mFirstTime = false;
            return true;
        }
            //Siguientes
        if(!mAlgorithm.nextStep()){
            mVisual.mSolution.setText(mAlgorithm.getSolution());
            disableButtons();
            return false;
        }
        
        mVisual.mText.setText(mVisual.mText.getText() + "\n" + Messages.ARDEN_APLY + "\n");
        for(int i=0; i<mAlgorithm.mEquations.size(); i++)
            mVisual.mText.setText(mVisual.mText.getText() + Messages.STATE + " \"" +
                                  mAlgorithm.getAutomaton().getStates().elementAt(i).toString() + 
                                  "\":  " + mAlgorithm.mEquations.elementAt(i).toString() + "\n");
        
        return true;
    }//next
    
    /**
     * Ejecuta todos los pasos del algoritmo.
     */
    public void all () {
        while(next());
        
    }//all
    
    /**
     * Acepta la expresión regular y se la asigna al panel de autómata que
     * tengamos seleccionado.
     */
    public void accept () {
        exit();
        
    }//accept

    /**
     * Habilita o deshabilita los botones y paneles una vez que se ha
     * completado el algoritmo.
     */
    private void disableButtons () {
        mVisual.mSolution.setEnabled(true);
        mVisual.mSolution.setEditable(false);
        mVisual.mValidate.setEnabled(true);
        mVisual.mCheck.setEnabled(true);
        mVisual.mAccept.setEnabled(true);
        mVisual.mNext.setEnabled(false);
        mVisual.mAll.setEnabled(false);
        
    }//disableButtons
    
    /**
     * Comprueba la equivalencia de dos expresiones regulares.<br>
     * Creará dos autómatas utilizando el método más eficaz(Aho-Sethi-Ullman)
     * y después realizará la equivalencia entre ellos.<br>
     * Muestra un diálogo indicando si son o no equivalentes.
     * 
     * @param exp Expresión regular a comparar con la solución del algoritmo
     */
    public void check (String exp){
        AhoTree tree1, tree2;
        AhoSethiUllman aho1, aho2;
        EquivalenceFA equivalence;
        TerminalEnd end = new TerminalEnd();
        String solution = mVisual.mSolution.getText().substring(mVisual.mSolution.getText().indexOf("= ")+1) + end;
        
            //Si la expresión regular no termina con $ se lo ponemos
        if(!exp.endsWith(end.toString()))
            exp += end;
        solution += end;
        
        if(mAlgorithm.getSolution().contains(new TerminalEmpty().toString())){
            ShowDialog.errorCompareER();
            return;
        }
        
        try{
            tree1 = ParserExpReg.getInstance(new StringReader(exp)).buildAhoTree();
            tree2 = ParserExpReg.getInstance(new StringReader(solution)).buildAhoTree();
        }catch (Error error){
            ShowDialog.messageError(error.getMessage());
            return;
        }
        catch (ParseException parExcep){
            ShowDialog.messageError(parExcep.getMessage());
            return;
        }
        
        aho1 = new AhoSethiUllman(tree1);
        aho2 = new AhoSethiUllman(tree2);
        aho1.allSteps();
        aho2.allSteps();
        
        try{
            equivalence = new EquivalenceFA(aho1.getSolution(), aho2.getSolution());
            ShowDialog.checkRegExp(equivalence.allSteps());
        }catch(InitializeException e){
            ShowDialog.errorCompareER();
        }
        
    }//check
    
    /**
     * Desreferencia el mediador del visual y cierra la pantalla visual.
     */
    public void exit () {
    	mVisual.liberate();
        
    }//exit

}//MediatorEquation
