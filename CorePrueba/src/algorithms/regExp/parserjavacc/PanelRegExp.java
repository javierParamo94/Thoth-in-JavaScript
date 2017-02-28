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
 * <b>Descripci�n</b><br>
 * Panel para introducir una expresi�n regular.
 * <p>
 * <b>Detalles</b><br>
 * Dispone de un campo de texto donde se introduce la palabra y los botones para
 * acceder directamente a los m�todos de Thompson, Aho-Sethi-Ullman y derivadas.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Permite introducir una expresi�n regular.
 * </p>
 * 
 * @author �lvar Arn�iz Gonz�lez, Andr�s Arn�iz Moreno
 * @version 1.0
 */
public class PanelRegExp  extends JPanel {

    // Attributes ---------------------------------------------------------------------
    
    /**
     * Campo de texto donde se va a introducir la expresi�n regular.
     */
    public JTextField mTextExp;
    
    /**
     * Bot�n para ejecutar el m�todo de Thompson.
     */
    public JButton mThompson;
    
    /**
     * Bot�n para ejecutar al m�todo de Aho-Sethi-Ullman.
     */
    public JButton mAho;
    
    /**
     * Bot�n para ejecutar el m�todo de las derivadas.
     */
    public JButton mDerive;
    
    /**
     * Mediador que ejecutar� el parser JavaCC para analizar la expresi�n
     * regular introducida por el usuario.
     */
    public MediatorRegExp mMediator;
    
    // Methods ------------------------------------------------------------------------
    
    /**
     * Constructor del panel.<br>
     * Consiste en un panel para introducir una expresi�n regular.
     */
    public PanelRegExp () {
        super(new GridLayout(1, 2));
        buildPanel();
        buildListeners();
        
    }//PanelRegExp
    
    /**
     * Construye los paneles que componen el panel de expresi�n regular.
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
        
            //A�adimos los componentes
        central.add(expression);
        central.add(buttons);
        central.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        add(new JScrollPane(central), BorderLayout.CENTER);
        
    }//buildPanel
    
    /**
     * Asigna las acciones de los botones. 
     */
    private void buildListeners () {
            //M�todo de Thompson
        mThompson.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                endWithDollar();
                new MediatorRegExp(mTextExp.getText(), MediatorRegExp.THOMPSON);
            }
        }); //M�todo de Aho-Sethi-Ullman
        mAho.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                endWithDollar();
                new MediatorRegExp(mTextExp.getText(), MediatorRegExp.AHO);
            }
        }); //M�todo de las derivadas
        mDerive.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                endWithDollar();
                new MediatorRegExp(mTextExp.getText(), MediatorRegExp.DERIVE);
            }
        });
    }//buildListeners
    
    /**
     * Comprueba que la expresi�n regular introducida tenga el car�cter de
     * fin de entrada.<br>
     * Si no le tiene le a�adir� y si le tiene no har� nada.
     */
    private void endWithDollar () {
        if(!mTextExp.getText().endsWith(new TerminalEnd().toString()))
            mTextExp.setText(mTextExp.getText() + new TerminalEnd().toString());
        
    }//endWithDollar
    
    /**
     * Establece la nueva expresi�n regular del panel.<br>
     * La escribe en el JTextField donde el usuario introduce su E.R.
     * 
     * @param regExp Expresi�n regular del panel.
     */
    public void setRegExp (String regExp) {
        mTextExp.setText(regExp);
        
    }//setRegExp

}//PanelValidate
