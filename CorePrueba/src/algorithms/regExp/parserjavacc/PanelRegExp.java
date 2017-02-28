package algorithms.regExp.parserjavacc;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;


import core.TerminalEnd;
import view.application.Application;
import view.utils.Messages;

/**
 * <b>Descripción</b><br>
 * Panel para introducir una expresión regular.
 * <p>
 * <b>Detalles</b><br>
 * Dispone de un campo de texto donde se introduce la palabra y los botones para
 * acceder directamente a los métodos de Thompson, Aho-Sethi-Ullman y derivadas.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Permite introducir una expresión regular.
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @version 1.0
 */
public class PanelRegExp  extends JPanel {

    // Attributes ---------------------------------------------------------------------
    
    /**
     * Campo de texto donde se va a introducir la expresión regular.
     */
    public JTextField mTextExp;
    
    /**
     * Botón para ejecutar el método de Thompson.
     */
    public JButton mThompson;
    
    /**
     * Botón para ejecutar al método de Aho-Sethi-Ullman.
     */
    public JButton mAho;
    
    /**
     * Botón para ejecutar el método de las derivadas.
     */
    public JButton mDerive;
    
    /**
     * Mediador que ejecutará el parser JavaCC para analizar la expresión
     * regular introducida por el usuario.
     */
    public MediatorRegExp mMediator;
    
    // Methods ------------------------------------------------------------------------
    
    /**
     * Constructor del panel.<br>
     * Consiste en un panel para introducir una expresión regular.
     */
    public PanelRegExp () {
        super(new GridLayout(1, 2));
        buildPanel();
        buildListeners();
        
    }//PanelRegExp
    
    /**
     * Construye los paneles que componen el panel de expresión regular.
     */
    private void buildPanel () {
        JPanel central = new JPanel(),
               expression = new JPanel(new FlowLayout(0, 15, 0)),
               buttons = new JPanel(new GridLayout(3, 1, 0, 10));

        mTextExp = new JTextField(30);
        mThompson = new JButton("THOMPSON");
        mAho = new JButton("AHO SETHI ULLMAN");
        mDerive = new JButton(Messages.DERI);
        
        buttons.add(mThompson);
        buttons.add(mAho);
        buttons.add(mDerive);
        
        expression.add(new JLabel(Messages.REG_EXP + ":"));
        expression.add(mTextExp);
        
            //Añadimos los componentes
        central.add(expression);
        central.add(buttons);
        central.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        add(new JScrollPane(central), BorderLayout.CENTER);
        
    }//buildPanel
    
    /**
     * Asigna las acciones de los botones. 
     */
    private void buildListeners () {
            //Método de Thompson
        mThompson.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                endWithDollar();
                new MediatorRegExp(mTextExp.getText(), MediatorRegExp.THOMPSON);
            }
        }); //Método de Aho-Sethi-Ullman
        mAho.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                endWithDollar();
                new MediatorRegExp(mTextExp.getText(), MediatorRegExp.AHO);
            }
        }); //Método de las derivadas
        mDerive.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                endWithDollar();
                new MediatorRegExp(mTextExp.getText(), MediatorRegExp.DERIVE);
            }
        });
    }//buildListeners
    
    /**
     * Comprueba que la expresión regular introducida tenga el carácter de
     * fin de entrada.<br>
     * Si no le tiene le añadirá y si le tiene no hará nada.
     */
    private void endWithDollar () {
        if(!mTextExp.getText().endsWith(new TerminalEnd().toString()))
            mTextExp.setText(mTextExp.getText() + new TerminalEnd().toString());
        
    }//endWithDollar
    
    /**
     * Establece la nueva expresión regular del panel.<br>
     * La escribe en el JTextField donde el usuario introduce su E.R.
     * 
     * @param regExp Expresión regular del panel.
     */
    public void setRegExp (String regExp) {
        mTextExp.setText(regExp);
        
    }//setRegExp

}//PanelValidate
