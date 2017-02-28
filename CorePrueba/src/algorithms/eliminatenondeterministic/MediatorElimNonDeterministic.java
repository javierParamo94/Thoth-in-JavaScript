package algorithms.eliminatenondeterministic;

import java.util.Vector;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.jgraph.graph.DefaultGraphModel;

import algorithms.InitializeException;
import algorithms.MediatorAlgorithm;

import core.State;
import core.Terminal;
import core.TerminalEpsilon;
import core.Transition;
import core.finiteautomaton.FiniteAutomaton;
import view.application.Application;
import view.application.AutomatonSplitPane;
import view.utils.Colors;
import view.utils.Messages;
import view.utils.ShowDialog;
import view2.controller.graphcontroller.LogicVisualConverter;

/**
 * <b>Descripci�n</b><br>
 * Clase que hace de mediador con la clase f�sica que elimina el no determinismo
 * de un aut�mata finito.
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
public class MediatorElimNonDeterministic implements MediatorAlgorithm{
    
    // Attributes -----------------------------------------------------------------
    
    /**
     * Algoritmo visual. Pantalla donde se muestra la ejecuci�n del algoritmo.
     */
    private VisualElimNonDeterministic mVisual;
    
    /**
     * Algoritmo de eliminaci�n de no determinismo.
     */
    private EliminateNonDeterministicFA mAlgorithm;
    
    /**
     * Aut�mta visual al que se le est� aplicando el algoritmo.
     */
    private FiniteAutomaton aut;
    
    // Methods --------------------------------------------------------------------
    
    /**
     * Constructor del mediador.<br>
     * Se asegura de que el algoritmo se puede realizar y de la inicializaci�n de todos
     * los par�metros y variables.
     * 
     * @param visual Algoritmo visual. Pantalla donde se va a mostrar el algoritmo.
     * @param automaton Aut�mata visual sobre el que se va a ejecutar el algoritmo.
     */
    public MediatorElimNonDeterministic (VisualElimNonDeterministic visual, FiniteAutomaton automaton) {
        mVisual = visual;
        aut = (FiniteAutomaton) automaton.clone();
        try{
            mAlgorithm = new EliminateNonDeterministicFA(aut);
                //Si es determinista
            if(!mAlgorithm.firstStep()){
                ShowDialog.isDeterministic();
                mVisual.mVisible = false;
                mVisual = null;
            }
            else
                initTable();            
        }catch(InitializeException e){
            ShowDialog.noInitialFinalState();
            mVisual.mVisible = false;
        }
        
    }//MediatorElimNonDeterministic
    
    /**
     * Inicializa la tabla donde se va a mostrar la funci�n de transici�n.
     */
    private void initTable () {
        Vector<Terminal> alphabet = new Vector<Terminal>();
        
        for(Terminal ter : aut.obtainAlphabet())
            if(!ter.equals(new TerminalEpsilon()))
                alphabet.add(ter);
        
        Object[] header = new String[2 + alphabet.size()];
        Object[][] o = new Object[1][2 + alphabet.size()];
        int i;
        
            //Cabecera
        header[0] = Messages.STATES+"\\"+Messages.TOKENS;
        for(i=0; i<alphabet.size(); i++)
            header[i+1] = alphabet.elementAt(i).toString();
        i++;
        header[i] = "";
        
            //Datos del primer estado
        o[0][0] = stateToString(mAlgorithm.getSolution().findState("0"));
        for(int j=0; j<alphabet.size(); j++){
            Terminal ter = alphabet.elementAt(j);
            for(Transition tran : mAlgorithm.getSolution().findState("0").getTransitionsOut())
                if(tran.getIn().equals(ter))
                    o[i][j+1] = tran.getNextState().toString();
        }
        o[0][alphabet.size()+1] = mAlgorithm.getSolution().findState("0").getLabel();
        
        DefaultTableModel model = new DefaultTableModel(o, header);
        mVisual.mTable = new JTable(model) {
            public boolean isCellEditable(int i, int j){
                return false;
            }
        };
        mVisual.mTable.setSelectionBackground(Colors.yellow());
        
        mVisual.mTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        for(int j=1; j<mVisual.mTable.getColumnModel().getColumnCount(); j++)
            mVisual.mTable.getColumnModel().getColumn(j).setPreferredWidth(30);
            
        setText("0", mAlgorithm.getSolution().findState("0").getLabel(), true);
        
    }//buildTable
    
    /**
     * Devuelve el nombre del estado decorado con par�ntesis si es final y con ">>" en caso
     * de que sea el estado inicial.
     * 
     * @param st Estado del que queremos obtener su nombre.
     * @return Nombre del estado
     */
    private String stateToString (State st) {
        String temp = "";
        
        if(st.equals(mAlgorithm.getSolution().getInitialState()))
            temp += ">>";
        
        if(st.isFinal())
            temp += ("("+st.toString()+")");
        else
            temp += st.toString();
        
        return temp;
    }//stateToString
    
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
     * Crea una nueva fila en la tabla transici�n y la rellena.
     */
    private void fillTable () {
        Vector<Terminal> alphabet = aut.getAlphabet();
        alphabet.remove(new TerminalEpsilon());
        FiniteAutomaton fa = mAlgorithm.getSolution();
        Object[][] o = new Object[1][2 + alphabet.size()];
        
        mVisual.mInformation.setText("");
            //Para cada estado del aut�mata nuevo
        for(int j, i=0; i<fa.getStates().size(); i++){
            if(i < mVisual.mTable.getModel().getRowCount()){
                State st = fa.getStates().elementAt(i);
                for(j=0; j<mVisual.mTable.getModel().getColumnCount(); j++)
                    if(j == 0)
                        mVisual.mTable.setValueAt(stateToString(st), i, j);
                    else if(j <= alphabet.size()){
                        mVisual.mTable.setValueAt("", i, j);
                        for(Transition tran : st.getTransitionsOut())
                            if(tran.getIn().equals(alphabet.elementAt(j-1)))
                                mVisual.mTable.setValueAt(tran.getNextState().toString(), i, j);
                    }
                    else
                        mVisual.mTable.setValueAt(st.getLabel(), i, j);
                
                setText(st.getName(), st.getLabel(), false);
            }
            else{
                ((DefaultTableModel)mVisual.mTable.getModel()).addRow(o);
                i--;
            }
        }
        
    }//fillTable
    
    /**
     * Muestra por el JText informaci�n de la eliminaci�n en formato:<br>
     * El estado: st<br>
     * Engloba los estados: states.
     * 
     * @param s Nombre del nuevo estado.
     * @param states Nombre de los estados que engloba.
     * @param b Si es true borra antes el panel.
     */
    private void setText (String st, String states, boolean b) {
        String temp = mVisual.mInformation.getText();
        
        if(b)
            temp = Messages.THE_STATE + ": " + st + "\n" +
                    Messages.INCLUDE + ": " + states + "\n";
        else
            temp += Messages.THE_STATE + ": " + st + "\n" +
                    Messages.INCLUDE + ": " + states + "\n";
        
        mVisual.mInformation.setText(temp);
    }//setText
    
    /**
     * Ejecuta todos los pasos del algoritmo.
     */
    public void all () {
        while(next());
        
    }//all
    
    /**
     * Crea el aut�mata visual y cierra la ventana.
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
    
}//MediatorElimNonDeterministic
