package algorithms.regExp.ahosethi;

import java.util.Vector;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.jgraph.graph.DefaultGraphModel;

import algorithms.MediatorAlgorithm;
import algorithms.tree.Node;

import core.State;
import view.application.Application;
import view.application.AutomatonSplitPane;
import view.utils.Colors;
import view.utils.Messages;
import view2.controller.graphcontroller.LogicVisualConverter;

/**
 * <b>Descripción</b><br>
 * Clase que hace de mediador con la clase física de Aho-Sethi-Ullman.
 * <p>
 * <b>Detalles</b><br>
 * Se encarga de mediar entre la parte gráfica y la parte física.<br>
 * El algoritmo obtiene un autómata finito a partir de una expresión regular
 * mediante el método de Aho-Sethi-Ullman.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Evita el acomplamiento entre la parte física y la parte visual.
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @version 1.0
 */
public class MediatorAho implements MediatorAlgorithm{
    
    // Attributes -----------------------------------------------------------------
    
    /**
     * Algoritmo visual. Pantalla donde se muestra la ejecución del algoritmo.
     */
    public VisualAho mVisual;
    
    /**
     * Algoritmo de eliminación de no determinismo.
     */
    private AhoSethiUllman mAlgorithm;
    
    /**
     * Autómta visual al que se le está aplicando el algoritmo.
     */
    private AhoTree mTree;
    
    /**
     * Contador que indica que columna de la tabla debemos completar.
     */
    private int mCount;
    
    /**
     * Contador que indica que fila de la tabla debemos completar.
     */
    private int mCount2;
    
    /**
     * Contador que almacena el número de estados del autómata resultante.
     */
    private int mStateCount;
    
    /**
     * Expresión regular.
     */
    private String mExpression;
    
    // Methods --------------------------------------------------------------------
    
    /**
     * Constructor del mediador.<br>
     * Se asegura de que el algoritmo se puede realizar y de la inicialización de todos
     * los parámetros y variables.
     * 
     * @param visual Algoritmo visual. Pantalla donde se va a mostrar el algoritmo.
     * @param tree Árbol de Aho-Sethi-Ullman sobre el que se va a realizar el algoritmo.
     * @param expression Expresión regular que representa el árbol.
     */
    public MediatorAho (VisualAho visual, AhoTree tree, String expression) {
        mVisual = visual;
        mTree = tree;
        mAlgorithm = new AhoSethiUllman(mTree);
        mAlgorithm.firstStep();
        mCount2 = -1;
        mExpression = expression;
        
        initTable();
        
    }//MediatorAho
    
    /**
     * Inicializa la tabla donde se va a mostrar la función de transición y
     * la tabla de siguiente posición.
     */
    private void initTable () {
        Object[] header = new Object[2];
        Object[][] o = new Object[mTree.getRoot().getLeafNumber()][2];
        Vector<Integer> temp;
        String string;
        int count = 0, delete = 0;
        State state;
        
            //Cabecera
        header[0] = "n";
        header[1] = Messages.NEXT_POS;
        Vector<Node> v = mTree.preOrden();
        for(int i=0; i<v.size(); i++)
            if(v.elementAt(i).isLeaf())
                if(!((AhoNode)v.elementAt(i)).isCancel()){
                    temp = new Vector<Integer>(1, 0);
                    temp.add(((AhoNode)v.elementAt(i)).getFirst().firstElement());
                    o[count][0] = count + 1;
                    string = mTree.nextPosition(v.elementAt(i).getToken(), temp).toString();
                    o[count][1] = string.substring(1, string.length() - 1);
                    count ++;
                }
                else
                    delete++;
        
        DefaultTableModel model = new DefaultTableModel(o, header);
        mVisual.mTable1 = new JTable(model) {
            public boolean isCellEditable(int i, int j){
                return false;
            }
        };
        
        while(delete > 0){
            ((DefaultTableModel)mVisual.mTable1.getModel()).removeRow(mVisual.mTable1.getRowCount() - 1);
            delete--;
        }
        
        mCount = mTree.getAlphabet().size();
        mStateCount = mAlgorithm.getSolution().getStates().size();
        header = new Object[2 + mCount];
        o = new Object[1][2+mCount];
        
        header[0] = "";
        for(int i=1; i<header.length-1; i++)
            header[i] = mTree.getAlphabet().elementAt(i-1).toString();
        header[1 + mCount] = "";
        state = mAlgorithm.getSolution().getInitialState();
        string = state.toString();
        if(state.isFinal())
            string = "(" + string +")";
        o[0][0] = ">>" + string;
        o[0][1+mCount] = mAlgorithm.getSolution().getInitialState().getLabel();
        
        model = new DefaultTableModel(o, header);
        mVisual.mTable2 = new JTable(model) {
            public boolean isCellEditable(int i, int j){
                return false;
            }
        };
        
        mVisual.mTable1.setSelectionBackground(Colors.yellow());
        ((DefaultTableCellRenderer)mVisual.mTable1.getDefaultRenderer(Object.class)).
        setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
        mVisual.mTable2.setSelectionBackground(Colors.yellow());
        ((DefaultTableCellRenderer)mVisual.mTable2.getDefaultRenderer(Object.class)).
        setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
        
    }//buildTable
    
    /**
     * Realiza cada paso del algoritmo, rellenando la tabla.
     * 
     * @return True si puede seguir ejecutando el algoritmo y false en caso contrario.
     */
    public boolean next () {
            //Siguientes
        if(mCount == mTree.getAlphabet().size()){
            mCount = 1;
            mCount2++;
            if(!mAlgorithm.nextStep()){
                disableButtons();
                mVisual.mAccept.setEnabled(true);
                return false;
            }
        }
        else
            mCount++;
        
        fillTable();
        
        return true;
    }//next
    
    /**
     * Actualiza la tabla de particiones.
     */
    private void fillTable () {
        State state = mAlgorithm.mStates.elementAt(mCount-1);
        String temp = state.getName();
        
        if(state.isFinal())
            temp = "(" + temp + ")";
        if(mAlgorithm.getSolution().getInitialState().equals(state))
            temp = ">>" + temp;
        mVisual.mTable2.setValueAt(temp, mCount2, mCount);
        
        if(mAlgorithm.getSolution().getStates().size() != mStateCount &&
                mAlgorithm.getSolution().getStates().indexOf(state) == mStateCount){
            Object o[] = new String[2+mTree.getAlphabet().size()];
            o[0] = temp;
            o[1 + mTree.getAlphabet().size()] = state.getLabel();
            ((DefaultTableModel)mVisual.mTable2.getModel()).addRow(o);
            mStateCount++;
        }
        
    }//fillTrace
    
    /**
     * Ejecuta todos los pasos del algoritmo.
     */
    public void all () {
        while(next());
        
    }//check
    
    /**
     * Crea el autómata visual y cierra la ventana.<br>
     * Si la ventana seleccionada no contiene ningún autómata lo creará
     * en esa pestaña, si por el contrario ya tiene uno añadirá una nueva.
     */
    public void accept () {
    	Application app = Application.getInstance();
        LogicVisualConverter con = new LogicVisualConverter();
        DefaultGraphModel model = con.logicToVisual(mAlgorithm.getSolution(),LogicVisualConverter.DEFAULT_SIZE,true);
        
        app.createTab(mAlgorithm.getSolution().getName(), new AutomatonSplitPane(model,mAlgorithm.getSolution()));
        app.getCurrentTab().setChanges(true);
        app.getCurrentTab().checkContent();
       
        //TODO: en los algoritmos de expresiones regulares
    	//Se ponia la expresión regular en la pestaña RegExp del PanelAutomaton
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
     * Desreferencia el mediador del visual y cierra la pantalla visual.
     */
    public void exit () {
        mVisual.liberate();
    }//exit
    
}//MediatorAho

