package src.client.gui.visual;

import src.client.core.grammar.Grammar;
import src.client.core.grammar.tasp.FirstFollow;
import src.client.gui.mediator.MediatorTasp;


/**
 * <b>Descripción</b><br>
 * Visualización del algoritmo de reconocimiento de palabras mediante la tabla
 * de análisis sintáctico predictivo.
 * <p>
 * <b>Detalles</b><br>
 * Visualiza paso a paso el reconocimiento de palabras mediante la TASP.<br>
 * Está formada por tres paneles , uno contiene la TASP otro el campo donde introducimos
 * la palabra a reconocer y por último la traza de reconocimiento.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Visualiza el reconocimiento de palabras mediante la TASP.<br>
 * Posibilita el paso a paso.
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @version 1.0
 */
public class VisualTasp extends JDialog {
    
    // Attributes -----------------------------------------------------------------
    
    /**
     * Mediador asociado al panel.
     */
    public MediatorTasp mMediator;
    
    /**
     * JTable donde va a estar la TASP.
     */
    public JTable mTaspTable;
    
    /**
     * Si está a true la ventana será mostrada y no se mostrará si es false.
     */
    public boolean mVisible;
    
    /**
     * JTable donde va a estar la traza.
     */
    public JTable mTraceTable;
    
    /**
     * Panel donde va a ir la JTable con la traza.
     */
    public JPanel mTracePanel;
    
    /**
     * JTextField donde se va a introducir la palabra a reconocer.
     */
    public JTextField mWord;
    
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
    
    // Methods --------------------------------------------------------------------
    
    /**
     * Constructor básico del algoritmo visual de TASP.
     * 
     * @param frame Frame al que está asociado el diálogo.
     * @param gra Gramática de la que vamos a hacer el reconocimiento.
     */
    public VisualTasp (Frame frame, Grammar gra) {
        super(frame, Messages.TASP, true);
        
        mWord = new JTextField(20);
        mVisible = true;
        mMediator = new MediatorTasp(this, gra);
        setLayout(new BorderLayout());
        setSize(new Dimension(750, 550));
        buildPanels();
        setLocationRelativeTo(frame);
        setResizable(false);
        setVisible(mVisible);
        
    }//VisualTasp
    
    /**
     * Constructor completo del algoritmo visual de TASP.
     * 
     * @param frame Frame al que está asociado el diálogo.
     * @param first Algoritmo first follow.
     */
    public VisualTasp (Frame frame, FirstFollow first) {
        super(frame, Messages.TASP, true);
        
        mWord = new JTextField(20);
        mVisible = true;
        mMediator = new MediatorTasp(this, first);
        setLayout(new BorderLayout());
        setSize(new Dimension(750, 550));
        buildPanels();
        setLocationRelativeTo(frame);
        setResizable(false);
        setVisible(mVisible);
        
    }//VisualTasp
    
    /**
     * Construye los paneles y botones.<br>
     * Asigna la funcionalidad de los botones.
     */
    private void buildPanels () {
        JPanel button = new JPanel(new GridLayout(1, 3, 30, 0)),
               down = new JPanel(new BorderLayout()),
               central = new JPanel(new BorderLayout()),
               word = new JPanel(new FlowLayout());
        JScrollPane scrollTaspTable, scrollTraceTable;
        
            //Botonera inferior
        button.setBorder(BorderFactory.createEmptyBorder(5, 62, 10, 65));
        mExit = new JButton(Messages.EXIT, Images.getCancelAlgorithmIcon());
        mNext = new JButton(Messages.NEXT_STEP, Images.getOneStepIcon());
        mCheck = new JButton(Messages.ALL_STEPS, Images.getAllStepsIcon());
        mExit.setToolTipText(Messages.EXIT);
        mNext.setToolTipText(Messages.NEXT_STEP);
        mCheck.setToolTipText(Messages.ALL_STEPS);
        button.add(mExit);
        button.add(mNext);
        button.add(mCheck);
        mNext.setEnabled(false);
        mCheck.setEnabled(false);
            //TASP
        scrollTaspTable = new JScrollPane(mTaspTable);
        scrollTaspTable.setPreferredSize(new Dimension(550, 200));
        scrollTaspTable = (JScrollPane)buildBorder(scrollTaspTable, "TASP");
            //Introducir palabra
        word.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        word.add(new JLabel(Messages.WORD));
        word.add(mWord);
            //Botones validar y borrar palabra
        mCheckWord = new JButton(Images.getTickIcon());
        mClear = new JButton(Images.getCrossIcon());
        mCheckWord.setToolTipText(Messages.VALIDATE);
        mClear.setToolTipText(Messages.DELETE);
        word.add(mCheckWord);
        word.add(mClear);
            //Tabla
        scrollTraceTable = new JScrollPane(mTraceTable);
        scrollTraceTable.setWheelScrollingEnabled(true);
            //Pantalla central
        down.setPreferredSize(new Dimension(500, 550));
        down = (JPanel)buildBorder(down, Messages.TRACE);
        down.add(word, BorderLayout.PAGE_START);
        down.add(scrollTraceTable, BorderLayout.CENTER);
        central.add(scrollTaspTable, BorderLayout.PAGE_START);
        central.add(down, BorderLayout.CENTER);
        central.setBorder(BorderFactory.createEmptyBorder(3, 6, 6, 6));
            //Principal
        add(central, BorderLayout.CENTER);
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
    
}//VisualTasp
