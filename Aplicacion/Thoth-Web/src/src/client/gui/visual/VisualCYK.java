package view.grammar.visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import view.grammar.mediator.MediatorCYK;
import view.utils.Images;
import view.utils.Messages;
import core.grammar.Grammar;

/**
 * <b>Descripción</b><br>
 * Visualización del algoritmo de Cocke-Younger-Kasami.
 * <p>
 * <b>Detalles</b><br>
 * Visualiza el algoritmo de CYK.<br>
 * Está formado por dos paneles uno contiene la vieja gramática y en el otro se encuentra
 * la tabla de CYK y un campo de texto para introducir la palabra a reconocer, también
 * tiene un campo de texto para mostrar información adicional.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Visualiza el algoritmo de CYK.<br>
 * Posibilita el paso a paso.
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @version 1.0
 */
public class VisualCYK extends JDialog {
    
    // Attributes --------------------------------------------------------------------
    
    /**
     * Mediador de limpieza asociado al panel.
     */
    public MediatorCYK mMediator;
    
    /**
     * Indica si debe mostrarse o no el panel.
     */
    public boolean mVisible;
    
    /**
     * JTextPane donde se encuentra la gramática de la que vamos a obtener CYK.
     */
    public JTextPane mTextGrammar;
    
    /**
     * JTable donde va a ir construyéndose gracias al algoritmo CYK.
     */
    public JTable mTable;
    
    /**
     * Panel donde va a ir la JTable con la tabla CYK.
     */
    public JPanel mPanelTable;
    
    /**
     * JTextField donde se va a introducir la palabra a reconocer.
     */
    public JTextField mWord;
    
    /**
     * JTextArea donde vamos a mostrar información útil al usuario.
     */
    public JTextArea mInformation;
    
    /**
     * Botón para comprobar si la palabra introducida es válida.
     */
    public JButton mCheckWord;
    
    /**
     * Botón para borrar la palabra introducida.
     */
    public JButton mClear;
    /**
     * Botón Validar.
     */
    public JButton mCheck;
    
    /**
     * Botón Salir.
     */
    public JButton mExit;
    
    /**
     * Botón Siguiente.
     */
    public JButton mNext;
    
    // Methods -----------------------------------------------------------------------
    
    /**
     * Constructor completo del algoritmo visual de CYK.
     * 
     * @param frame Frame al que está asociado el diálogo.
     * @param grammar Gramática de la que vamos a obtener la tabla de CYK.
     */
    public VisualCYK (Frame frame, Grammar grammar) {
        super(frame, Messages.CYK, true);
        mTextGrammar = new JTextPane();
        mTextGrammar.setEditable(false);
        mWord = new JTextField(20);
        mInformation = new JTextArea();
        mInformation.setEditable(false);
        mVisible = true;
        mMediator = new MediatorCYK(this, grammar);
        setLayout(new BorderLayout());
        setSize(new Dimension(750, 550));
        buildPanels();
        setLocationRelativeTo(frame);
        setResizable(false);
        setVisible(mVisible);
        
    }//VisualCYK
    
    /**
     * Construye los paneles y botones.<br>
     * Asigna la funcionalidad de los botones.
     */
    private void buildPanels () {
        JPanel button = new JPanel(new GridLayout(1, 3, 30, 0)),
               sup = new JPanel(new BorderLayout()),
               central = new JPanel(new BorderLayout()),
               word = new JPanel(new FlowLayout());
        JScrollPane scrollLeft, scrollTable, scrollInf;
        
            //Botonera inferior
        button.setBorder(BorderFactory.createEmptyBorder(5, 62, 10, 65));
        
        mExit = new JButton(Messages.EXIT, Images.getCancelAlgorithmIcon());
        mNext = new JButton(Messages.NEXT_STEP, Images.getOneStepIcon());
        mCheck= new JButton(Messages.ALL_STEPS, Images.getAllStepsIcon());
        mExit.setToolTipText(Messages.EXIT);
        mNext.setToolTipText(Messages.NEXT_STEP);
        mCheck.setToolTipText(Messages.ALL_STEPS);
        button.add(mExit);
        button.add(mNext);
        button.add(mCheck);
        mNext.setEnabled(false);
        mCheck.setEnabled(false);
            //Gramática
        mTextGrammar.setFont(new Font("Comic Sans MS negrita", Font.BOLD, 12));
        scrollLeft = new JScrollPane(mTextGrammar);
        scrollLeft.setPreferredSize(new Dimension(225, 600));
        scrollLeft = (JScrollPane)buildBorder(scrollLeft, Messages.GRAMMAR);
            //Introducir palabra
        word.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        word.add(new JLabel(Messages.WORD));
        word.add(mWord);
            // Aceptar palabra y borrarla
        mCheckWord = new JButton(Images.getTickIcon());
        mClear = new JButton(Images.getCrossIcon());
        word.add(mCheckWord);
        word.add(mClear);
            //Tabla
        mPanelTable = new JPanel(new BorderLayout());
        scrollTable = new JScrollPane(mPanelTable);
            //Información
        scrollInf = new JScrollPane(mInformation);
        scrollInf.setPreferredSize(new Dimension(800, 150));
        scrollInf = (JScrollPane)buildBorder(scrollInf, Messages.DETAILS);
            //Pantalla central
        sup = (JPanel)buildBorder(sup, "Cocke-Younger-Kasami:");
        sup.add(word, BorderLayout.PAGE_START);
        sup.add(scrollTable, BorderLayout.CENTER);
        central.add(sup, BorderLayout.CENTER);
        central.add(scrollInf, BorderLayout.PAGE_END);
            //Principal
        add(central, BorderLayout.CENTER);
        add(scrollLeft, BorderLayout.LINE_START);
        add(button, BorderLayout.PAGE_END);
        
        buildListeners();
                
    }//buildPanels
    
    /**
     * Asigna la funcionalidad de los botones.
     */
    public void buildListeners () {
            //Pulsar sobre validar palabra
        mCheckWord.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                mMediator.acceptWord();
            }
        });
            //Pulsar sobre borrar palabra
        mClear.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                mMediator.cancelWord();
            }
        });
            //Pulsar sobre Salir
        mExit.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                mMediator.exit();
                
            }
        });
            //Pulsar sobre Siguiente paso
        mNext.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                mMediator.next();
                
            }
        });
            //Pulsar sobre Validar cadena        
        mCheck.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                mMediator.check();
                
            } 
        });
        
    }//buildListeners
    
    /**
     * Asigna al panel que se le pasa un borde con un título.
     * 
     * @param component Panel al que se le asigna el borde.
     * @param title Título del borde.
     * @return Panel con el borde asignado.
     */
    private JComponent buildBorder (JComponent component, String title) {
        component.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createTitledBorder(title),
                                BorderFactory.createEmptyBorder(3, 6, 6, 6)),
                         component.getBorder()));
        
        return component;
    }//buildBorder
    
}//VisualCYK
