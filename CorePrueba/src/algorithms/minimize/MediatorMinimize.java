package algorithms.minimize;

import java.awt.Dimension;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultGraphModel;

import algorithms.InitializeException;
import algorithms.MediatorAlgorithm;
import algorithms.Partition;
import algorithms.eliminatenondeterministic.EliminateNonDeterministicFA;

import core.State;
import core.Terminal;
import core.finiteautomaton.FiniteAutomaton;
import view.application.Application;
import view.application.AutomatonSplitPane;
import view.utils.Colors;
import view.utils.Messages;
import view.utils.ShowDialog;
import view2.controller.CellsCreatorHelper;
import view2.controller.graphcontroller.LogicVisualConverter;

/**
 * <b>Descripción</b><br>
 * Clase que hace de mediador con la clase física que obtiene un autóamata finito
 * a partir de una gramática.
 * <p>
 * <b>Detalles</b><br>
 * Se encarga de mediar entre la parte gráfica y la parte física.<br>
 * Si es necesario elimina el no determinismo del autómata antes de poder minimizarle.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Evita el acomplamiento entre la parte física y la parte visual.
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @version 1.0
 */
public class MediatorMinimize implements MediatorAlgorithm{
    
    // Attributes -----------------------------------------------------------------
    
    /**
     * Algoritmo visual. Pantalla donde se muestra la ejecución del algoritmo.
     */
    private VisualMinimize mVisual;
    
    /**
     * Algoritmo de minimización.
     */
    private MinimizeFA mAlgorithm;
    

	private FiniteAutomaton aut;
    
    // Methods --------------------------------------------------------------------
    
    /**
     * Constructor del mediador.<br>
     * Se asegura de que el algoritmo se puede realizar y de la inicialización de todos
     * los parámetros y variables.
     * 
     * @param visual Algoritmo visual. Pantalla donde se va a mostrar el algoritmo.
     * @param vAuto Autómata visual sobre el que se va a ejecutar el algoritmo.
     */
    public MediatorMinimize (VisualMinimize visual, FiniteAutomaton automaton) {
        mVisual = visual;
        this.aut = (FiniteAutomaton) automaton.clone();
        try{
            if(!aut.isDeterministic(new Vector<State>()))
                if(ShowDialog.questionMinimize() == JOptionPane.NO_OPTION){
                    mVisual.mVisible = false;
                    mVisual = null;
                    return;
                }
                else{
                    EliminateNonDeterministicFA deter = new EliminateNonDeterministicFA(aut);
                    deter.allSteps();
                    aut = deter.getSolution();
                    mAlgorithm = new MinimizeFA(aut);
                    mAlgorithm.firstStep();
                }
            else{
                mAlgorithm = new MinimizeFA(aut);
                mAlgorithm.allSteps();
                if(aut.getStates().size() == mAlgorithm.getSolution().getStates().size()){
                    ShowDialog.isMinimal();
                    mVisual.mVisible = false;
                    mVisual = null;
                    return;
                }
                mAlgorithm = new MinimizeFA(aut);
                mAlgorithm.firstStep();
                if(!mAlgorithm.mChanged)
                    if(ShowDialog.questionMinimize2() == JOptionPane.NO_OPTION){
                        mVisual.mVisible = false;
                        mVisual = null;
                        return;
                    }
            }
            JGraph graph = LogicVisualConverter.createGraph(aut,LogicVisualConverter.DEFAULT_SIZE);
            CellsCreatorHelper.scaleAndCenterGraph(graph, new Dimension(350, 350));
            mVisual.setShowingPane(graph);
            
            initTable();
            
        }catch(InitializeException e){
            ShowDialog.noInitialFinalState();
            mVisual.mVisible = false;
        }
        
    }//MediatorMinimize
    
    /**
     * Inicializa la tabla donde se va a mostrar la tabla de particiones
     */
    private void initTable () {
        Vector<Terminal> alphabet = aut.obtainAlphabet();
        Object[] header = new String[2 + alphabet.size()];
        Object[][] o = new Object[aut.getStates().size()][2 + alphabet.size()];
        Partition part = mAlgorithm.mPartition;
            //Cabecera
        header[0] = Messages.PARTITION;
        header[1] = Messages.STATES;
        for(int i=0; i<alphabet.size(); i++)
            header[i+2] = alphabet.elementAt(i).toString();
        
            //Datos del primer estado
        for(int i=0; i<o.length; i++){
            o[i][0] = part.mPartitionA[i];
            o[i][1] = part.mListStates.elementAt(i).getName();
            for(int j=2; j<o[0].length; j++)
                o[i][j] = part.mDestiny[i][j-2];
        }

        DefaultTableModel model = new DefaultTableModel(o, header);
        mVisual.mTable = new JTable(model) {
            public boolean isCellEditable(int i, int j){
                return false;
            }
            public boolean isCellSelected(int i, int j){
                if(i >= mVisual.mTable.getRowCount() - aut.getStates().size())
                    return true;
                
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
            //Siguientes
        if(!mAlgorithm.nextStep()){
            fillTable();
            disableButtons();
            mVisual.mAccept.setEnabled(true);
            return false;
        }
        fillTable();
        
        return true;
    }//next
    
    /**
     * Actualiza la tabla de particiones.
     */
    private void fillTable () {
        Partition part = mAlgorithm.mPartition;
        int sizeRow = aut.getStates().size();
        int sizeColumn = 2 + aut.getAlphabet().size();
        int row;
        
        ((DefaultTableModel)mVisual.mTable.getModel()).addRow(new Object[sizeColumn]);
        ((DefaultTableModel)mVisual.mTable.getModel()).addRow(new Object[sizeColumn]);
            //Nuevos datos de la tabla
        for(int i=0; i<sizeRow; i++){
            ((DefaultTableModel)mVisual.mTable.getModel()).addRow(new Object[sizeColumn]);
            row = mVisual.mTable.getRowCount() - 1;
            mVisual.mTable.setValueAt(part.mPartitionA[i], row, 0);
            mVisual.mTable.setValueAt(part.mListStates.elementAt(i).getName(), row, 1);
            for(int j=2; j<sizeColumn; j++)
                mVisual.mTable.setValueAt(part.mDestiny[i][j-2], row, j);
        }
        
    }//fillTable
    
    /**
     * Ejecuta todos los pasos del algoritmo.
     */
    public void all () {
        while(next());
        
    }//check
    
    /**
     * Crea el autómata visual y cierra la ventana.
     */
    public void accept () {
    	Application app = Application.getInstance();
        LogicVisualConverter con = new LogicVisualConverter();
        DefaultGraphModel model = con.logicToVisual(mAlgorithm.getSolution(),LogicVisualConverter.DEFAULT_SIZE,true);
        
        app.createTab(mAlgorithm.getSolution().getName(), new AutomatonSplitPane(model,mAlgorithm.getSolution()));
        app.getCurrentTab().setChanges(true);
        app.getCurrentTab().checkContent();
        
        exit();
        
    }//accept

    /**
     * Habilita o deshabilita los botones de siguiente paso y todos pasos.
     */
    private void disableButtons () {
        mVisual.mNext.setEnabled(false);
        mVisual.mAll.setEnabled(false);
        
    }//disableButtons
    

    /**
     * Desreferencia el mediador del visual y cirrar la pantalla visual.
     */
    public void exit () {
    	mVisual.liberate();
        
    }//exit

}//MediatorMinimize
