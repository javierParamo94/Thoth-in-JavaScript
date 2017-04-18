package view.grammar.visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.table.DefaultTableCellRenderer;

import core.grammar.Grammar;
import view.grammar.mediator.MediatorFirstFollow;
import view.utils.Colors;
import view.utils.Messages;
import view.utils.ShowDialog;

/**
 * <b>Descripción</b><br>
 * Visualización del cálculo del First y el Follow.
 * <p>
 * <b>Detalles</b><br>
 * Visualiza la obtención del First y el Follow.<br>
 * Está formada por tres paneles; uno con la gramática y dos más que muestran el first
 * y el follow respectivamente.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Visualiza el cálculo del First y el Follow.
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @version 1.0
 */
public class VisualFirstFollow extends JDialog {

    // Attributes --------------------------------------------------------------------
    
    /**
     * Mediador asociado al panel.
     */
    public MediatorFirstFollow mMediator;
    
    /**
     * Indica si debe mostrarse o no el panel.
     */
    public boolean mVisible;
    
    /**
     * JTextPane donde se encuentra la gramática que va a ser analizada.
     */
    public JTextPane mGrammar;
    
    /**
     * Panel que contiene las tablas del first y el follow.
     */
    public JPanel mFirstFollow;
    
    /**
     * Cabecera de las tablas first y follow.
     */
    public String[] mHeader;
    
    /**
     * Panel que contiene la tabla del first.
     */
    public JScrollPane mScrollRightUp;
    
    /**
     * JTextPane donde se encuentra el cálculo del first.
     */
    public Object[][] mFirst;
    
    /**
     * Tabla que almacenará el first.
     */
    public JTable mTableFirst;
    
    /**
     * Panel que contiene la tabla del follow.
     */
    private JScrollPane mScrollRightDown;
    
    /**
     * JTextPane donde se encuentra el cálculo del follow.
     */
    public Object[][] mFollow;
    
    /**
     * Tabla que almacenará el follow.
     */
    public JTable mTableFollow;
    
    /**
     * Botón Finalizar.
     */
    public JButton mExit;
    
    /**
     * Botón Siguiente.
     */
    public JButton mObtainFirst;
    
    /**
     * Botón Todos los pasos.
     */
    public JButton mObtainFollow;
    
    /**
     * Botón Aceptar
     */
    public JButton mTasp;
    
    // Methods -----------------------------------------------------------------------
    
    /**
     * Constructor completo del algoritmo visual del cálculo del First y el Follow.
     * 
     * @param frame Frame al que está asociado el diálogo.
     * @param grammar Gramática de la que se va a obtener el First y el Follow.
     */
    public VisualFirstFollow(Frame frame, Grammar grammar) {
        super(frame, Messages.FIRST_FOLLOW_ALGORITHM, true);
        mGrammar = new JTextPane();
        mGrammar.setEditable(false);
        mVisible = true;
        mMediator = new MediatorFirstFollow(this, grammar);
        mFirstFollow = new JPanel(new GridLayout(2, 1));
        
        setLayout(new BorderLayout());
        setSize(new Dimension(750, 550));
        buildPanels();
        setLocationRelativeTo(frame);
        setResizable(false);
        setVisible(mVisible);
        
    }//VisualFirstFollow
    
    /**
     * Construye los paneles y botones de este pantalla.<br>
     * Asigna la funcionalidad de los botones.
     */
    private void buildPanels () {
        JPanel button = new JPanel(new GridLayout(1, 4, 30, 0)),
               central = new JPanel(new BorderLayout()),
               left = new JPanel(new BorderLayout()),
               right = new JPanel(new BorderLayout());
        JScrollPane scrollLeft;
        
            //Construccion de botones
        mExit = new JButton(Messages.EXIT);
        mObtainFirst = new JButton("First");
        mObtainFollow = new JButton("Follow");
        mObtainFollow.setEnabled(false);
        mTasp = new JButton("TASP");
        mTasp.setEnabled(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 62, 10, 65));
        button.add(mExit);
        button.add(mObtainFirst);
        button.add(mObtainFollow);
        button.add(mTasp);
        add(button, BorderLayout.PAGE_END);
        
            //Panel principal
        mGrammar.setFont(new Font("Comic Sans MS negrita", Font.BOLD, 12));
        scrollLeft = new JScrollPane(mGrammar);
        scrollLeft = buildBorder(scrollLeft, Messages.GRAMMAR);

        mScrollRightUp = new JScrollPane();
        mScrollRightUp = buildBorder(mScrollRightUp, "First");
        mScrollRightUp.getViewport().setBackground(Color.WHITE);
        
        mScrollRightDown = new JScrollPane();
        mScrollRightDown = buildBorder(mScrollRightDown, "Follow");
        mScrollRightDown.getViewport().setBackground(Color.WHITE);
        
        left.add(scrollLeft, BorderLayout.CENTER);
        left.setPreferredSize(new Dimension (275, 600));
        left.setBorder(BorderFactory.createEmptyBorder(3, 6, 6, 6));
        central.add(left, BorderLayout.LINE_START);
        mFirstFollow.add(mScrollRightUp);
        mFirstFollow.add(mScrollRightDown);
        right.add(mFirstFollow, BorderLayout.CENTER);
        right.setBorder(BorderFactory.createEmptyBorder(3, 6, 6, 6));
        central.add(right, BorderLayout.CENTER);
        add(central, BorderLayout.CENTER);
        
        buildListeners();
        
    }//buildPanels
        
    /**
     * Asigna la funcionalidad de los botones.
     */
    public void buildListeners () {
            //Pulsar sobre Cancelar
        mExit.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                mMediator.exit();
            }
        });
            //Pulsar sobre FIRST
        mObtainFirst.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                if(createTableFirst()){
                    mFirstFollow.setVisible(false);
                    mFirstFollow.remove(0);
                    mScrollRightUp = new JScrollPane(mTableFirst);
                    mScrollRightUp = buildBorder(mScrollRightUp, "First");
                    mScrollRightUp.getViewport().setBackground(Color.white);
                    mFirstFollow.setVisible(true);
                    mFirstFollow.add(mScrollRightUp, 0);
                    mTableFirst.getSelectionModel().setSelectionMode(0);
                    mTableFirst.setRowSelectionAllowed(false);
                    mTableFirst.setColumnSelectionAllowed(true);
                    mTableFirst.setSelectionBackground(Colors.yellow());
                    
                    mObtainFirst.setEnabled(false);
                    mObtainFollow.setEnabled(true);
                }
                else{
                    ShowDialog.nonFirstFollow();
                    mMediator.exit();
                }
            } 
        });
            //Pulsar sobre FOLLOW
        mObtainFollow.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                createTableFollow();
                    
                mFirstFollow.setVisible(false);
                mFirstFollow.remove(1);
                mScrollRightDown = new JScrollPane(mTableFollow);
                mScrollRightDown = buildBorder(mScrollRightDown, "Follow");
                mScrollRightDown.getViewport().setBackground(Color.white);
                mFirstFollow.setVisible(true);
                mFirstFollow.add(mScrollRightDown, 1);
                mTableFollow.setRowSelectionAllowed(false);
                mTableFollow.setColumnSelectionAllowed(true);
                mTableFollow.setSelectionBackground(Colors.yellow());
                mObtainFollow.setEnabled(false);
                mTasp.setEnabled(true);
            } 
        });
            //Pulsar sobre TASP
        mTasp.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) { 
                mMediator.tasp();
                
            } 
        });
        
    }//buildListeners
    
    /**
     * Crea una Jtable nueva para el First.
     * 
     * @return True si ha podido obtener la tabla first.
     */
    private boolean createTableFirst () {
        
        if(mMediator.first()){
            mTableFirst = new JTable(mFirst, mHeader){
                public boolean isCellEditable (int i, int j){
                    return false;
                }
            };
            mTableFirst.setShowHorizontalLines(false);
            mTableFirst.setShowGrid(false);
            mTableFirst.setRowMargin(5);
            mTableFirst.setRowHeight(25);
            ((DefaultTableCellRenderer)mTableFirst.getDefaultRenderer(Object.class)).
                            setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
            return true;
        }
        
        return false;
    }//createTableFirst
    
    /**
     * Crea una Jtable nueva para el follow
     * 
     * @return True si ha podido obtener la tabla del follow.
     */
    private boolean createTableFollow () {
        
        if(mMediator.follow()){
            mTableFollow = new JTable(mFollow, mHeader){
                public boolean isCellEditable (int i, int j){
                    return false;
                }
            };
            mTableFollow.setShowHorizontalLines(false);
            mTableFollow.setShowGrid(false);
            mTableFollow.setRowMargin(5);
            mTableFollow.setRowHeight(25);
            ((DefaultTableCellRenderer)mTableFollow.getDefaultRenderer(Object.class)).
                            setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
            return true;
        }
        
        return false;
    }//createTableFollow

    /**
     * Asigna al panel que se le pasa un borde con un título.
     * 
     * @param scroll Panel al que se le asigna el borde.
     * @param title Título del borde.
     * @return Panel con el borde asignado.
     */
    private JScrollPane buildBorder (JScrollPane scroll, String title) {
        scroll.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createTitledBorder(title),
                                BorderFactory.createEmptyBorder(3, 6, 6, 6)),
                        scroll.getBorder()));
        
        return scroll;
    }//buildBorder

}//VisualFirstFollow
