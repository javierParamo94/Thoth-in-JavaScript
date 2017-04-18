package view.grammar.mediator;

import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import view.grammar.visual.VisualCYK;
import view.utils.Colors;
import view.utils.Messages;
import view.utils.ShowDialog;
import core.Terminal;
import core.TerminalEmpty;
import core.TerminalEpsilon;
import core.grammar.*;
import core.grammar.cleaner.CYKAlgorithm;
import core.grammar.cleaner.Chomsky;
import core.grammar.cleaner.EliminateSA;

/**
 * <b>Descripción</b><br>
 * Clase que hace de mediador en el algoritmo de Cocke-Younger-Kasami.<br>
 * <p>
 * <b>Detalles</b><br>
 * Se encarga de mediar entre la parte gráfica y la parte física.<br>
 * Ilumina y ayuda al usuario a comprender el algoritmo.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Evita el acomplamiento entre la parte visual y la física.<br>
 * Muestra en texto información adicional.
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @version 1.0
 */
public class MediatorCYK {
    
    // Attributes --------------------------------------------------------------------
    
    /**
     * Algoritmo de limpieza asociado al mediador.
     */
    private CYKAlgorithm mCYK;
    
    /**
     * Gramática a la que se va a aplicar el algoritmo de CYK.
     */
    private Grammar mGrammar;
    
    /**
     * Palabra a reconocer.
     */
    private Vector<Terminal> mWord;
    
    /**
     * Longitud de la palabra a reconocer.
     */
    private int mLength;
    
    /**
     * Algoritmo visual al que está asociado.
     */
    private VisualCYK mVisual;
    
    /**
     * Nos indica la fila por la que vamos.
     */
    private int mI;
    
    /**
     * Nos indica la columna por la que vamos.
     */
    private int mJ;
    
    // Methods -----------------------------------------------------------------------
    
    /**
     * Constructor completo del mediador del algoritmo de CYK.
     * 
     * @param vcyk Pantalla del algoritmo.
     * @param grammar Gramática que va a ser usada.
     */
    public MediatorCYK (VisualCYK vcyk, Grammar grammar) {
        mVisual = vcyk;
        
            //Mostramos el diálogo
        if(ShowDialog.questionCYK() == JOptionPane.YES_OPTION){
                //Quitamos épsilon y pasamos a Chomsky
            EliminateSA esa = new EliminateSA(grammar);
            if(esa.firstStep()){
                esa = new EliminateSA(grammar);
                esa.allSteps();
                grammar = esa.getSolution();
            }
            Chomsky chomsky = new Chomsky(grammar);
            if(chomsky.firstStep()){
                chomsky = new Chomsky(grammar);
                chomsky.allSteps();
                grammar = chomsky.getSolution();
            }
            mVisual.mVisible = true;
        }
        else
            mVisual.mVisible = false;
        
        mGrammar = grammar;
        mVisual.mTextGrammar.setText(mGrammar.completeToString());
    }//MediatorCYK
    
    /**
     * Aceptar Palabra.<br>
     * Comprueba que la palabra introducida sea válida y además inicializa el
     * algoritmo.
     */
    public void acceptWord () {
        Vector<Terminal> word = new Vector<Terminal>(5, 5);
        String s = mVisual.mWord.getText();
        
        if(s.length() == 0){
            mVisual.mWord.setText(new TerminalEpsilon().toString());
            word.add(new TerminalEpsilon());
            showInformation(Messages.VALID_WORD + " \"" +
                    TerminalEpsilon.EPSILON_CHARACTER + "\".");
        }
        else{   //Comprobamos que todo sean terminales.
            for(int i=0; i<s.length(); i++){
                if(!isTerminal(s.charAt(i))){
                    showInformation(Messages.NO_WORD);
                    return;
                }
                word.add(new Terminal(s.charAt(i)));
            }
            showInformation(Messages.VALID_WORD + " \"" + s + "\".");
        }
            //Creamos el algoritmo
        mWord = word;
        mLength = mWord.size();
        mCYK = new CYKAlgorithm(mGrammar, mWord);
        mCYK.allSteps();
        setButtons(true);
        
        showTable(initiateTable());
        
        mI = 0;
        mJ = 0;
        
    }//acceptWord
    
    /**
     * Muestra el mensaje que se le pasa en la parte de información inferior
     * de la pantalla.
     */
    private void showInformation (String s) {
        mVisual.mInformation.setText(mVisual.mInformation.getText() + s + "\n");
        
    }//noWord
    
    /**
     * Comprueba si el carácter es un terminal.
     * 
     * @param c String de entrada.
     * @return True si es terminal, false en caso contrario.
     */
    private boolean isTerminal (char c) {
        
            //Si es minúscula o número es terminal
        if(Character.isLowerCase(c)
                || Character.isDigit(c))
            return true;
            //Si es ( ) + - · / ó * es terminal
        switch(c){
            case '(':
            case ')':
            case '+':
            case '-':
            case '·':
            case '*':
            case '/':
            case TerminalEpsilon.EPSILON_CHARACTER:
                return true;
            default:
                return false;
        }
        
    }//isTerminal
    
    /**
     * Inicializa una matriz de objetos que va a ser el contenido de la jTable.
     * 
     * @return Matriz de objetos con la tabla inicializada.
     */
    private Object[][] initiateTable () {
        Object[][] data = new Object[mWord.size()+1][mWord.size()+1];
        
            //Cabecera
        data[0][0] = "N";
        for(int i=1; i<mWord.size()+1; i++)
            data[0][i] = "j = " + i;
        
        for(int i=1; i<mWord.size()+1; i++)
            for(int j=0; j<mWord.size()+1; j++)
                    //Primera columna
                if(j == 0)
                    data[i][j] = "(" + mWord.elementAt(i-1) + ")   i = " + i;
                else
                    data[i][j] = "";
        
        return data;
    }//initiateTable
    
    /**
     * Muestra la tabla con los datos que se le pasan.
     * 
     * @param data Datos de las celdas de la tabla.
     */
    private void showTable (Object[][] data) {
        final int length = data.length;
        mVisual.mTable = new JTable(data, data[0]){
            public boolean isCellEditable(int i, int j){
                return false;
            }
            public boolean isCellSelected(int i, int j){
                if(i + j <= length)
                    return true;
                
                return false;
            }
        };
        
            //Borramos la vieja tabla
        mVisual.mPanelTable.setVisible(false);
        mVisual.mPanelTable.removeAll();
            //Propiedades de la JTable
        mVisual.mTable.setEnabled(false);
        mVisual.mTable.setShowGrid(false);
        mVisual.mTable.setSelectionBackground(Colors.yellow());
        ((DefaultTableCellRenderer)mVisual.mTable.getDefaultRenderer(Object.class)).
            setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
        
        mVisual.mPanelTable.add(mVisual.mTable);
        mVisual.mPanelTable.setVisible(true);
        
    }//showTable
    
    /**
     * Comprueba si la palabra se puede obtener de la gramática.
     */
    public void checkWord () {
        if(mCYK.getAccept())
            showInformation(Messages.RECOGNIZED_WORD);
        else
            showInformation(Messages.NON_RECOGNIZED_WORD);
        
    }//checkWord
    
    /**
     * Borra el JTextField donde se introduce la palabra a reconocer.
     */
    public void cancelWord () {
        Object[][] o = {{"", ""}};
        
        mVisual.mWord.setText("");
        showInformation(Messages.WORD_DELETE);
        showTable(o);
        setButtons(false);
        
    }//cancelWord
    
    /**
     * Realiza cada paso del algoritmo, rellenando la tabla y mostrando la
     * información de cada paso.
     * 
     * @return True si quedan pasos por ejecutar y false en caso contrario.
     */
    public boolean next () {
        String temp;
        
            //Si hemos completado la columna
        if(mI >= mLength){
            mI = 0;
            mJ++;
            mLength--;
        }
            //Añadimos una nueva celda
        temp = mCYK.getTable().get(mI, mJ).toString();
        temp = temp.substring(1, temp.length()-1);
        if(temp.equals(""))
            temp = new TerminalEmpty().toString();
        mVisual.mTable.getModel().setValueAt(temp, mI+1, mJ+1);
        showInformation("N " + (mI+1) + " " + (mJ+1) + " :: " + temp);
            //Hemos finalizado el reconocimiento
        if(mLength == 1){
            setButtons(false);
            checkWord();
            return false;
        }
        
        mI++;
        return true;
    }//next
    
    /**
     * Ejecuta todos los pasos del algoritmo y te muestra si ha sido reconocida
     * o no la palabra introducida.
     */
    public void check () {
        while(next());
        
    }//check

    /**
     * Habilita o deshabilita los botones de siguiente paso y validar.
     * 
     * @param b Si es true habilita los botones y si es false los deshabilita.
     */
    public void setButtons (boolean b) {
        mVisual.mNext.setEnabled(b);
        mVisual.mCheck.setEnabled(b);
        
    }//setButtons
    
    /**
     * Cierra la ventana visual y elimina las referencias.
     */
    public void exit () {
        mVisual.setVisible(false);
        mVisual.mMediator = null;
        mVisual = null;
        
    }//exit
    
}//MediatorCYK
