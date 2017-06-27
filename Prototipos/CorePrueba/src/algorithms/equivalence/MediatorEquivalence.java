package algorithms.equivalence;

import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.jgraph.graph.GraphLayoutCache;

import algorithms.InitializeException;
import algorithms.MediatorAlgorithm;
import algorithms.eliminatenondeterministic.EliminateNonDeterministicFA;
import algorithms.minimize.MinimizeFA;

import core.State;
import core.Terminal;
import core.finiteautomaton.FiniteAutomaton;

import view.utils.Colors;
import view.utils.ShowDialog;

/**
 * <b>Descripción</b><br>
 * Clase que hace de mediador con la clase física que compara autómatas finitos.
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
public class MediatorEquivalence implements MediatorAlgorithm{

    // Attributes -----------------------------------------------------------------

    /**
     * Algoritmo visual. Pantalla donde se muestra la ejecución del algoritmo.
     */
    private VisualEquivalence mVisual;

    /**
     * Algoritmo de equivalencia.
     */
    private EquivalenceFA mAlgorithm;

    /**
     * Autómata visual al que se le aplica el algoritmo.
     */
    public FiniteAutomaton autA;

    /**
     * Autómata visual al que se le aplica el algoritmo.
     */
    public FiniteAutomaton autB;

    /**
     * Contador para rellenar los campos de la tabla.
     */
    private int mCount;

    /**
     * Contador para ejecutar el siguiente paso del algoritmo.
     */
    private int mStep;

    /**
     * Indica el primer paso del algoritmo.
     */
    private boolean mFirstTime;

    // Methods --------------------------------------------------------------------

    /**
     * Constructor del mediador de equivalencia.<br>
     * Se asegura de que el algoritmo se puede realizar y de la inicialización de todos
     * los parámetros y variables.
     *
     * @param visual Algoritmo visual. Pantalla donde se va a mostrar el algoritmo.
     * @param gCacheA caché A
     * @param autoA Autómata visual sobre el que se va a ejecutar el algoritmo.
     * @param gCacheB caché B
     * @param autoB Autómata visual sobre el que se va a ejecutar el algoritmo.
     */
    public MediatorEquivalence (VisualEquivalence visual, GraphLayoutCache gCacheA,FiniteAutomaton autoA,GraphLayoutCache gCacheB, FiniteAutomaton autoB) {
        mVisual = visual;
        autA = autoA.clone();
        autB = autoB.clone();
        mFirstTime = true;

        try{
            if(!autA.isDeterministic(new Vector<State>()) || !autB.isDeterministic(new Vector<State>()))
                if(ShowDialog.questionMinimize() != JOptionPane.YES_OPTION){
                    mVisual.mVisible = false;
                    mVisual = null;
                    return;
                }

            //TODO set mShowingA y B
            autA = buildAutomaton(autA);
            autA.obtainAlphabet();
            autB = buildAutomaton(autB);
            autB.obtainAlphabet();

            if(!autB.getAlphabet().containsAll(autB.getAlphabet()) ||
                    !autB.getAlphabet().containsAll(autA.getAlphabet())){
                minimize();
                return;
            }

            mCount = autA.getAlphabet().size();
            mStep = autA.getAlphabet().size();
            mAlgorithm = new EquivalenceFA(autA, autB);
            mAlgorithm.firstStep();
            initTable();

        }catch(InitializeException e){
            ShowDialog.noInitialFinalState();
            mVisual.mVisible = false;
        }

    }//MediatorEquivalence

    /**
     * Se encarga de minimizar los autómatas y compararles ya que no poseen el mismo
     * alfabeto y por lo tanto no se pueden comparar por estados equivalentes.
     *
     * @throws InitializeException Si los autómatas no tienen estado inicial y/o final.
     */
    private void minimize () throws InitializeException {
        MinimizeFA minimalA = new MinimizeFA(autA);
        MinimizeFA minimalB = new MinimizeFA(autB);
        minimalA.allSteps();
        minimalB.allSteps();
        mAlgorithm = new EquivalenceFA(minimalA.getSolution(), minimalB.getSolution());
        ShowDialog.equivalence(mAlgorithm.allSteps());
        mVisual.mVisible = false;
        mVisual = null;

    }//minimize

    /**
     * Se encarga de comprobar que el autómata es determinista y completo.<br>
     * Si no lo es lo transforma para poder saber si es equivalente a otro autómata.
     *
     * @return aut El autómata visual modificado si era necesario.
     * @throws InitializeException Si el autómata no tenía al menos un estado final y un inicial.
     */
    public FiniteAutomaton buildAutomaton (FiniteAutomaton aut) throws InitializeException{
        FiniteAutomaton sol;

        if(!aut.isDeterministic(new Vector<State>())){
            EliminateNonDeterministicFA deterA = new EliminateNonDeterministicFA(aut);
            deterA.allSteps();
            if(!((FiniteAutomaton)deterA.getSolution().clone()).completeAutomaton())
                deterA.getSolution().completeAutomaton();

            sol = deterA.getSolution();
        }
        else{
            if(!((FiniteAutomaton) aut.clone()).completeAutomaton()){
                aut.completeAutomaton();
            }
            sol = aut; 
            
        }

        return sol;
    }//buildAutomaton

    /**
     * Inicializa la tabla donde se va a mostrar la función de transición.
     */
    private void initTable () {
        Vector<Terminal> alphabet = autA.getAlphabet();
        Object[] header = new String[1 + alphabet.size()];

        header[0] = "";
            //Cabecera
        for(int i=0; i<alphabet.size(); i++)
            header[1+i] = alphabet.elementAt(i).toString();

        DefaultTableModel model = new DefaultTableModel(new Object[0][0], header);
        mVisual.mTable = new JTable(model) {
            public boolean isCellEditable(int i, int j){
                return false;
            }
        };
        mVisual.mTable.setSelectionBackground(Colors.yellow());
        ((DefaultTableCellRenderer)mVisual.mTable.getDefaultRenderer(Object.class)).
        setHorizontalAlignment(DefaultTableCellRenderer.CENTER);

    }//initTable

    /**
     * Realiza cada paso del algoritmo, rellenando la tabla.
     *
     * @return True si puede seguir ejecutando el algoritmo y false en caso contrario.
     */
    public boolean next () {
            //firstStep
        if(mFirstTime)
            return firstStep();

        //mVisualA.mColor = Colors.blue();
        //mVisualB.mColor = Colors.blue();

            //Siguientes
        if(mCount >= mStep){
            if(!mAlgorithm.nextStep()){
                fillTable();
                mCount = 1;
                //highLight();
                ShowDialog.equivalence(mAlgorithm.getAccept());
                disableButtons();
                mVisual.mAccept.setEnabled(true);
                return false;
            }
            fillTable();
            return true;
        }

        Vector<State> temp = mAlgorithm.mTable.mDestinyStates.elementAt(mVisual.mTable.getRowCount()-1);
        String sol = temp.elementAt(mCount * 2).toString() + " , " + temp.elementAt(mCount * 2 + 1);
        mVisual.mTable.setValueAt(sol, mVisual.mTable.getRowCount()-1, mCount+1);

        /*mVisualA = mAutomatonA.findState((StateFA)mAutomatonA.getAutomaton().
                findState(temp.elementAt(mCount * 2).toString()));
        mVisualB = mAutomatonB.findState((StateFA)mAutomatonB.getAutomaton().
                findState(temp.elementAt(mCount * 2 + 1).toString()));
        highLight();*/
        mCount++;

        return true;
    }//next

    /**
     * Ejecuta el primer paso de equivalencia de autómatas.<br>
     * Ilumina los estados iniciales a verde si los dos son equivalentes, por
     * el contrario les ilumina a rojo si no los son;
     *
     * @return True si el algoritmo puede continuar, false si ya se ha comprobado
     * que no son equivalentes.
     */
    private boolean firstStep () {
        mFirstTime = false;
        //mVisualA = mAutomatonA.getInitial();
        //mVisualB = mAutomatonB.getInitial();

        if(autA.getInitialState().isFinal() != autB.getInitialState().isFinal()){
            //highLight();
            ShowDialog.equivalence(false);
            disableButtons();
            mVisual.mAccept.setEnabled(true);
            return false;
        }
        if(mStep == 0){
            //highLight();
            ShowDialog.equivalence(true);
            disableButtons();
            mVisual.mAccept.setEnabled(true);
            return false;
        }

        //highLight();
        return true;

    }//firstStep

    /**
     * Actualiza la tabla de particiones.
     */
    private void fillTable () {
        EquivalenceTable table = mAlgorithm.mTable;
        Object[] row = new Object[1+mStep];
        Vector<State> temp = new Vector<State>();

        if(mStep > 0){
            if(mVisual.mTable.getRowCount() == table.mDestinyStates.size()){
                temp = table.mDestinyStates.elementAt(mVisual.mTable.getRowCount()-1);
                row[0] = table.mPairStates.elementAt(mVisual.mTable.getRowCount()-1);
                row[1] = temp.firstElement().toString() + "," + temp.elementAt(1).toString();
            }else{
                temp = table.mDestinyStates.elementAt(mVisual.mTable.getRowCount());
                row[0] = table.mPairStates.elementAt(mVisual.mTable.getRowCount());
                row[1] = temp.firstElement().toString() + "," + temp.elementAt(1).toString();
                ((DefaultTableModel)mVisual.mTable.getModel()).addRow(row);
            }
            /*mVisualA = mAutomatonA.findState((StateFA)mAutomatonA.getAutomaton().
                    findState(temp.firstElement().toString()));
            mVisualB = mAutomatonB.findState((StateFA)mAutomatonB.getAutomaton().
                    findState(temp.elementAt(1).toString()));
            highLight();*/
        }

    }//fillTable

    /**
     * Ejecuta todos los pasos del algoritmo.
     */
    public void all () {
        while(next());

    }//check

    /**
     * Habilita o deshabilita los botones de siguiente paso y todos pasos.
     */
    private void disableButtons () {
        mVisual.mNext.setEnabled(false);
        mVisual.mAll.setEnabled(false);

    }//disableButtons

    /**
     * Ilumina los colores de los estados actuales de los dos autómatas.<br>
     * Si ambos son equivalentes se iluminarán a verde, si por el contrario uno
     * es final y el otro no lo es se iluminarán a rojo.
     */
    /*private void highLight () {
        if(mVisualA.getState().isFinal() != mVisualB.getState().isFinal()){
            mVisualA.mColor = Colors.red();
            mVisualB.mColor = Colors.red();
        }
        else{
            mVisualA.mColor = Colors.green();
            mVisualB.mColor = Colors.green();
        }
        //mVisual.mShowingA.repaint();
        //mVisual.mShowingB.repaint();

    }//highLight*/

    /**
     * Desreferencia el mediador del visual y cirrar la pantalla visual.
     */
    public void exit () {
        mVisual.liberate();

    }//exit

	@Override
	public void accept() {
		exit();
	}

}//MediatorEquivalence
