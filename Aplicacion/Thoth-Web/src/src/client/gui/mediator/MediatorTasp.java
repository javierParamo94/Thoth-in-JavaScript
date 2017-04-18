package view.grammar.mediator;

import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import core.Terminal;
import core.TerminalEnd;
import core.TerminalEpsilon;
import core.grammar.*;
import core.grammar.tasp.*;
import core.grammar.cleaner.LeftFactoring;
import core.grammar.cleaner.EliminateDirectRecursion;
import core.grammar.cleaner.EliminateIndirectRecursion;
import view.grammar.visual.VisualTasp;
import view.utils.Colors;
import view.utils.Messages;
import view.utils.ShowDialog;

/**
 * <b>Descripción</b><br>
 * Clase que hace de mediador en el reconocimiento de palabras mediante TASP.
 * <p>
 * <b>Detalles</b><br>
 * Se encarga de mediar entre la parte gráfica y la parte física.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Evita el acomplamiento entre la parte visual y la física.
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @version 1.0
 */
public class MediatorTasp {
    
    // Attributes ----------------------------------------------------------------
    
    /**
     * Tabla TASP a partir de la cual se va a producir el reconocimiento.
     */
    private TaspTable mTasp;
    
    /**
     * Algoritmo de reconocimiento mediante la TASP
     */
    private WordTasp mWordTasp;
    
    /**
     * Palabra a reconocer.
     */
    private Vector<Terminal> mWord;
    
    /**
     * Algoritmo visual al que está asociado.
     */
    private VisualTasp mVisual;
    
    /**
     * Si es true indica que es el primer paso del algoritmo, y false en caso contrario. 
     */
    private boolean mFirstTime;
    
    // Methods -----------------------------------------------------------------------
    

    /**
     * Constructor básico del mediador de reconocimiento con TASP.<br>
     * Se le pasa el first, el follow y la pantalla de recnocimiento de TASP.
     * 
     * @param vTasp Pantalla del algoritmo.
     * @param ff Algoritmo FirstFollow con el que vamos a obtener la TASP.
     */
    public MediatorTasp (VisualTasp vTasp, FirstFollow ff) {
        mVisual = vTasp;
        mTasp = new TaspTable(ff);
        mFirstTime = true;
        
            //Si no se ha podido calcular el first o el follow
        if(ff.getFirst()==null || ff.getFollow()==null){
            ShowDialog.nonFirstFollow();
            mVisual.mVisible = false;
        }   //Calculamos la TASP
        else if(mTasp.calculateTasp()){
            initiateTasp();
            initiateTrace();
            mVisual.mVisible = true;
            }
            else{
                ShowDialog.grammarAmbiguous();
                mVisual.mVisible = false;
            }
        
    }//MediatorTasp
    
    /**
     * Constructor complet del mediador del algoritmo de reconocimiento
     * mediante TASP.<br>
     * Se le pasa la gramática directamente por lo que pregunta si se 
     * desea eliminar recursividad y factorizar por la izquierda. Además calcula
     * el first y el follo para poder ejecutar el algoritmo.
     * 
     * @param vTasp Pantalla del algoritmo.
     * @param gra Gramática a analizar.
     */
    public MediatorTasp (VisualTasp vTasp, Grammar gra) {
        mVisual = vTasp;
        
            //Mostramos el diálogo
        if(ShowDialog.questionFirstFollow() == JOptionPane.YES_OPTION){
                //Quitamos recursividad
            EliminateDirectRecursion edr;
            EliminateIndirectRecursion eir;
            
            if(gra.isDirectRecursive()){
                edr = new EliminateDirectRecursion(gra);
                edr.allSteps();
                gra = edr.getSolution();
            }
            if(gra.isIndirectRecursive()){
                eir = new EliminateIndirectRecursion(gra);
                eir.allSteps();
                gra = eir.getSolution();
            }
            LeftFactoring leftFac = new LeftFactoring(gra);
            if(leftFac.allSteps())
                gra = leftFac.getSolution();
                //Empezamos...
            FirstFollow ff = new FirstFollow(gra);
                //Calculamos el first y el follow
            if(!ff.calculateFirst() || !ff.calculateFollow()){
                ShowDialog.nonFirstFollow();
                vTasp.mVisible = false;
            }
            else{
                mTasp = new TaspTable(ff);
                mFirstTime = true;
                
                if(mTasp.calculateTasp()){
                    initiateTasp();
                    initiateTrace();
                    mVisual.mVisible = true;
                }
                else{
                    ShowDialog.grammarAmbiguous();
                    mVisual.mVisible = false;
                }
            }
        }
        else
            mVisual.mVisible = false;
        
    }//MediatorTasp
    
    /**
     * Inicializa la tabla TASP.
     */
    public void initiateTasp () {
        Object[] header = new String[mTasp.getNonTerminals().toArray().length+1];
        Object[][] data = new Object[mTasp.getSolution().length]
                                     [mTasp.getSolution()[0].length+1];
        
        header[0] = "";
        for(int i=1; i<header.length; i++)
            header[i] = mTasp.getNonTerminals().elementAt(i-1).toString();
        
        for(int i=0; i<data.length; i++)
            for(int j=0; j<data[0].length; j++)
                if(j == 0)
                    data[i][0] = mTasp.getTerminals().elementAt(i); 
                else
                    data[i][j] = mTasp.getSolution()[i][j-1];
        
        mVisual.mTaspTable = new JTable(data, header);
        mVisual.mTaspTable.setEnabled(false);
        ((DefaultTableCellRenderer)mVisual.mTaspTable.getDefaultRenderer(Object.class)).
        setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
        
    }//initiateTasp
    
    /**
     * Muestra la tabla donde va a ir la traza de reconocimiento.
     */
    private void initiateTrace () {
        Object[] header = {Messages.STACK, Messages.TOP, Messages.IN,
                           Messages.CHAR, Messages.OUT};
        Object[][] data = new Object[1][5]; 
        DefaultTableModel model = new DefaultTableModel(data, header);
        
        mVisual.mTraceTable = new JTable(model){
            public boolean isCellEditable(int i, int j){
                return false;
            }
        };
        
        mVisual.mTraceTable.setShowHorizontalLines(false);
        mVisual.mTraceTable.setSelectionBackground(Colors.yellow());
        ((DefaultTableModel)mVisual.mTraceTable.getModel()).removeRow(0);
        
        mVisual.mTraceTable.getColumnModel().getColumn(0).setPreferredWidth(180);
        mVisual.mTraceTable.getColumnModel().getColumn(1).setPreferredWidth(40);
        mVisual.mTraceTable.getColumnModel().getColumn(2).setPreferredWidth(180);
        mVisual.mTraceTable.getColumnModel().getColumn(3).setPreferredWidth(40);
        
    }//initiateTrace
    
    /**
     * Aceptar Palabra.<br>
     * Comprueba que la palabra introducida sea válida y además inicializa el
     * algoritmo.
     */
    public void acceptWord () {
        Vector<Terminal> word = new Vector<Terminal>(5, 5);
        String s = mVisual.mWord.getText();
        
        if(s.length() == 0){
            word.add(new TerminalEnd());
            mVisual.mWord.setText(mVisual.mWord.getText() + ((Character)TerminalEnd.END_CHARACTER).toString());
        }
        else    //Comprobamos que todo sean terminales.
            for(int i=0; i<s.length(); i++){
                if(!isTerminal(s.charAt(i))){
                    ShowDialog.invalidTerminal();
                    return;
                }
                word.add(new Terminal(s.charAt(i)));
            }
            //Si no termina con '$' se lo ponemos
        if(!word.lastElement().equals(new TerminalEnd())){
            word.add(new TerminalEnd());
            mVisual.mWord.setText(mVisual.mWord.getText() + ((Character)TerminalEnd.END_CHARACTER).toString());
        }
            //Creamos el algoritmo
        mWord = word;
        mWordTasp = new WordTasp(mTasp.getGrammar(), mTasp, mWord);
        setButtons(true);
        mFirstTime = true;
        
        while(((DefaultTableModel)mVisual.mTraceTable.getModel()).getRowCount() > 0)
            ((DefaultTableModel)mVisual.mTraceTable.getModel()).removeRow(0);
        
    }//acceptWord
    
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
            case TerminalEnd.END_CHARACTER:
                return true;
            default:
                return false;
        }
        
    }//isTerminal
    
    /**
     * Comprueba si la palabra se puede obtener de la gramática.
     */
    public void checkWord () {
        if(mWordTasp.isAccept())
            ShowDialog.recognizedWord();
        else
            ShowDialog.nonRecognizedWord();
        
    }//checkWord
    
    /**
     * Borra el JTextField donde se introduce la palabra a reconocer.
     */
    public void cancelWord () {
        mVisual.mWord.setText("");
        setButtons(false);
        while(((DefaultTableModel)mVisual.mTraceTable.getModel()).getRowCount() > 0)
            ((DefaultTableModel)mVisual.mTraceTable.getModel()).removeRow(0);
        
    }//cancelWord
    
    /**
     * Realiza cada paso del algoritmo, rellenando la tabla.
     * 
     * @return True si quedan pasos por ejecutar y false en caso contrario.
     */
    public boolean next () {
            //Primera fila
        if(mFirstTime){
            mWordTasp.firstStep();
            fillTrace(false);
            mFirstTime = false;
            return true;
        }
            //Siguientes
        if(!mWordTasp.nextStep()){
            fillTrace(true);
            checkWord();
            setButtons(false);
            return false;
        }
        fillTrace(true);
        
        return true;
    }//next
    
    /**
     * Crea una nueva fila en la tabla de traza y la rellena.
     * 
     * @param first Si es true rellena la columna de Salida.
     */
    private void fillTrace (boolean out) {
        Object[] o = new String[5];
        String temp;
        Production tempProd;
        
        temp = mWordTasp.mStack.lastElement().toString().substring(1, 
                mWordTasp.mStack.lastElement().toString().length()-1);
        temp = temp.replace(",", " ");
        o[0] = temp;
        
        o[1] = mWordTasp.mTop.lastElement().toString();
        
        temp = mWordTasp.mIn.lastElement().toString().substring(1, 
                mWordTasp.mIn.lastElement().toString().length()-1);
        temp = temp.replace(",", "");
        o[2] = temp;
        
        o[3] = mWordTasp.mChar.lastElement().toString();
        
        if(out && mWordTasp.mOut.size()>0){
            tempProd = mWordTasp.mOut.lastElement();
            if(tempProd == null)
                o[4] = "POP";
            else
                o[4] = tempProd.toString();
        }
        else
            o[4] = "";
        
        ((DefaultTableModel)mVisual.mTraceTable.getModel()).addRow(o);
        
    }//fillTrace
    
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
    
}//MediatorTasp
