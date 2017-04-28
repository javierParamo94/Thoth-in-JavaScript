package src.client.gui.visual;


import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

import src.client.core.grammar.Grammar;
import src.client.gui.MessageMessages;
import src.client.gui.mediator.MediatorFirstFollow;

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
public class VisualFirstFollow extends Composite {

    // Attributes --------------------------------------------------------------------
	/**
	 * 
	 */
	private MessageMessages sms = GWT.create(MessageMessages.class);
    
    /**
     * Mediador asociado al panel.
     */
    public MediatorFirstFollow mMediator;
    
    /**
     * Indica si debe mostrarse o no el panel.
     */
    public boolean mVisible;
	/**
	 * Panel donde irán colocados los botones
	 */
	private HorizontalPanel hPanel = new HorizontalPanel();
    
	/**
	 * Panel vertical donde ira la visualizacion de las áreas
	 */
	public VerticalPanel vPanel = new VerticalPanel();
	
    /**
     * Area donde se encuentra la gramática que va a ser analizada.
     */
    public TextArea mGrammar = new TextArea();
    
    /**
     * Panel donde se encuentra la gramática que va a ser analizada.
     */
    public VerticalPanel panelGrammar = new VerticalPanel();
    /**
     * Panel que contiene las tablas del first y el follow.
     */
    public VerticalPanel mFirstFollow = new VerticalPanel();
    
    /**
     * Cabecera de las tablas first y follow.
     */
    public String[] mHeader;
    
    /**
     * Panel que contiene la tabla del first.
     */
   // public JScrollPane mScrollRightUp;
    
    /**
     * JTextPane donde se encuentra el cálculo del first.
     */
    public Object[][] mFirst;
    
    /**
     * Tabla que almacenará el first.
     */
    //public JTable mTableFirst;
    
    /**
     * Panel que contiene la tabla del follow.
     */
    //private JScrollPane mScrollRightDown;
    
    /**
     * JTextPane donde se encuentra el cálculo del follow.
     */
    public Object[][] mFollow;
    
    /**
     * Tabla que almacenará el follow.
     */
    //public JTable mTableFollow;
    
	/**
	 * Botones de cancelar, siguiente paso, todos los pasos y aceptar.
	 */
	public PushButton mExit = new PushButton(sms.exit());
	public PushButton mObtainFirst = new PushButton("First");
	public PushButton mObtainFollow = new PushButton("Follow");
	public PushButton mTasp = new PushButton("TASP");
	
	private FlexTable stocksFlexTable = new FlexTable();
    
    // Methods -----------------------------------------------------------------------
    
    /**
     * Constructor completo del algoritmo visual del cálculo del First y el Follow.
     * 
     * @param frame Frame al que está asociado el diálogo.
     * @param grammar Gramática de la que se va a obtener el First y el Follow.
     */
    public VisualFirstFollow(Grammar grammar) {


        mGrammar.setEnabled(false);
        mVisible = true;
        mMediator = new MediatorFirstFollow(this, grammar);
        
        
        //mFirstFollow.add(stocksFlexTable);
        
        //setLayout(new BorderLayout());
        //setSize(new Dimension(750, 550));
        buildPanels();
        //setLocationRelativeTo(frame);
        //setResizable(false);
        setVisible(mVisible);
        
    }//VisualFirstFollow
    
    /**
     * Construye los paneles y botones de este pantalla.<br>
     * Asigna la funcionalidad de los botones.
     */
    private void buildPanels () {
    	
    	DockPanel dockPanel = new DockPanel();
		dockPanel.setStyleName("dockpanel");
		dockPanel.setSpacing(4);
		dockPanel.setHorizontalAlignment(DockPanel.ALIGN_CENTER);

		mGrammar.setCharacterWidth(80);
		mGrammar.setVisibleLines(20);
		panelGrammar.add(new HTML(sms.grammar()));
		panelGrammar.add(mGrammar);
		
		mFirstFollow.add(new HTML(sms.oldgrammar()));
		mFirstFollow.add(stocksFlexTable);
		
		// Botones
		hPanel.add(mExit);
		hPanel.add(mObtainFirst);
		hPanel.add(mObtainFollow);
		hPanel.add(mTasp);
		//hPanel.add(btnAcept);
		buildListeners();

		// Add text all around
		dockPanel.add(hPanel, DockPanel.SOUTH);
		dockPanel.add(mFirstFollow, DockPanel.EAST);
		dockPanel.add(panelGrammar, DockPanel.WEST);
		dockPanel.add(new HTML("This is the second north component."),DockPanel.NORTH);

		vPanel.add(dockPanel);

		RootPanel.get().add(vPanel);
            //Construccion de botones
       /* mExit = new JButton(Messages.EXIT);
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
        
        buildListeners();*/
        
    }//buildPanels
        
    /**
     * Asigna la funcionalidad de los botones.
     */
    public void buildListeners () {
            //Pulsar sobre Cancelar
        mExit.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
                mMediator.exit();
            }
        });
            //Pulsar sobre FIRST
        mObtainFirst.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
                if(createTableFirst()){
                    mFirstFollow.setVisible(false);
                    mFirstFollow.remove(0);
                    /*mScrollRightUp = new JScrollPane(mTableFirst);
                    mScrollRightUp = buildBorder(mScrollRightUp, "First");
                    mScrollRightUp.getViewport().setBackground(Color.white);
                    mFirstFollow.setVisible(true);
                    mFirstFollow.add(mScrollRightUp, 0);
                    mTableFirst.getSelectionModel().setSelectionMode(0);
                    mTableFirst.setRowSelectionAllowed(false);
                    mTableFirst.setColumnSelectionAllowed(true);
                    mTableFirst.setSelectionBackground(Colors.yellow());
                    */
                    mObtainFirst.setEnabled(false);
                    mObtainFollow.setEnabled(true);
                }
                else{
                    //ShowDialog.nonFirstFollow();
                    mMediator.exit();
                }
            } 
        });
            //Pulsar sobre FOLLOW
        mObtainFollow.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
                //createTableFollow();
                    
               /* mFirstFollow.setVisible(false);
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
                mTasp.setEnabled(true);*/
            } 
        });
            //Pulsar sobre TASP
        mTasp.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
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
            CellTable<String> mTableFirst = new CellTable<String>();
            mTableFirst.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
           /* mTableFirst = new JTable(mFirst, mHeader){
                public boolean isCellEditable (int i, int j){
                    return false;
                }
            };*/
           /* mTableFirst.setShowHorizontalLines(false);
            mTableFirst.setShowGrid(false);
            mTableFirst.setRowMargin(5);
            mTableFirst.setRowHeight(25);*/
            /*((DefaultTableCellRenderer)mTableFirst.getDefaultRenderer(Object.class)).
                            setHorizontalAlignment(DefaultTableCellRenderer.CENTER);*/
            return true;
        }
        
        return false;
    }//createTableFirst
    
    /**
     * Crea una Jtable nueva para el follow
     * 
     * @return True si ha podido obtener la tabla del follow.
     */
   /* private boolean createTableFollow () {
        
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
    /*private JScrollPane buildBorder (JScrollPane scroll, String title) {
        scroll.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createTitledBorder(title),
                                BorderFactory.createEmptyBorder(3, 6, 6, 6)),
                        scroll.getBorder()));
        
        return scroll;
    }//buildBorder*/

}//VisualFirstFollow
