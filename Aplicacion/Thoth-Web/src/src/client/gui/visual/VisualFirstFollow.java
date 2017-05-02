package src.client.gui.visual;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
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
 * Está formada por tres paneles; uno con la gramática y dos más que muestran el
 * first y el follow respectivamente.
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

	// Attributes
	// --------------------------------------------------------------------
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
	public VerticalPanel panelFirstFollow = new VerticalPanel();

	/**
	 * Cabecera de las tablas first y follow.
	 */
	public String[] mHeader;

	/**
	 * Panel que contiene la tabla del first.
	 */
	public VerticalPanel panelFirst;

	/**
	 * JTextPane donde se encuentra el cálculo del first.
	 */
	public Object[][] mFirst;

	/**
	 * Tabla que almacenará el first.
	 */
	public FlexTable mTableFirst = new FlexTable();

	/**
	 * Panel que contiene la tabla del follow.
	 */
	private VerticalPanel panelFollow;

	/**
	 * JTextPane donde se encuentra el cálculo del follow.
	 */
	public Object[][] mFollow;

	/**
	 * Tabla que almacenará el follow.
	 */
	public FlexTable mTableFollow = new FlexTable();

	/**
	 * Botones de cancelar, siguiente paso, todos los pasos y aceptar.
	 */
	public Button mExit = new Button(sms.exit());
	public Button mObtainFirst = new Button("First");
	public Button mObtainFollow = new Button("Follow");
	public Button mTasp = new Button("TASP");

	// Methods
	// -----------------------------------------------------------------------

	/**
	 * Constructor completo del algoritmo visual del cálculo del First y el
	 * Follow.
	 * 
	 * @param frame
	 *            Frame al que está asociado el diálogo.
	 * @param grammar
	 *            Gramática de la que se va a obtener el First y el Follow.
	 */
	public VisualFirstFollow(Grammar grammar) {

		mGrammar.setEnabled(false);
		mVisible = true;
		mMediator = new MediatorFirstFollow(this, grammar);

		// mFirstFollow.add(stocksFlexTable);

		// setLayout(new BorderLayout());
		// setSize(new Dimension(750, 550));
		buildPanels();
		// setLocationRelativeTo(frame);
		// setResizable(false);
		setVisible(mVisible);

	}// VisualFirstFollow

	/**
	 * Construye los paneles y botones de este pantalla.<br>
	 * Asigna la funcionalidad de los botones.
	 */
	private void buildPanels() {

		DockPanel dockPanel = new DockPanel();
		dockPanel.setStyleName("dockpanel");
		dockPanel.setSpacing(4);
		dockPanel.setHorizontalAlignment(DockPanel.ALIGN_CENTER);

		mGrammar.setCharacterWidth(80);
		mGrammar.setVisibleLines(20);
		panelGrammar.add(new HTML(sms.grammar()));
		panelGrammar.add(mGrammar);
		panelGrammar.setStyleName("gwt-Big-Text");

		panelFirst = new VerticalPanel();
		panelFirst.add(new HTML("First"));
		//panelFirst.setSpacing(10);

		panelFollow = new VerticalPanel();
		panelFollow.add(new HTML("Follow"));
		//panelFollow.setSpacing(10);

		panelFirstFollow.setPixelSize(500, 500);
		panelFirstFollow.setStyleName("gwt-Big-Text");
		// panelFirstFollow.add(mFirstFollow);

		// Botones
		hPanel.add(mExit);
		hPanel.add(mObtainFirst);
		hPanel.add(mObtainFollow);
		mObtainFollow.setEnabled(false);
		hPanel.add(mTasp);
		mTasp.setEnabled(false);
		buildListeners();

		dockPanel.add(new HTML(sms.calculateff()), DockPanel.NORTH);
		dockPanel.add(hPanel, DockPanel.SOUTH);
		dockPanel.add(panelFirstFollow, DockPanel.EAST);
		dockPanel.add(panelGrammar, DockPanel.WEST);

		vPanel.add(dockPanel);

		RootPanel.get().add(vPanel);

	}// buildPanels

	/**
	 * Asigna la funcionalidad de los botones.
	 */
	public void buildListeners() {
		// Pulsar sobre Cancelar
		mExit.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				mMediator.exit();
			}
		});
		// Pulsar sobre FIRST
		mObtainFirst.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (createTableFirst()) {
					panelFirst.add(mTableFirst);
					panelFirst.setStyleName("gwt-Big-Text");
					panelFirstFollow.add(panelFirst);
					mObtainFirst.setEnabled(false);
					mObtainFollow.setEnabled(true);
				} else {
					// ShowDialog.nonFirstFollow();
					mMediator.exit();
				}
			}
		});
		// Pulsar sobre FOLLOW
		mObtainFollow.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				createTableFollow();

				panelFollow.add(mTableFollow);
				panelFollow.setStyleName("gwt-Big-Text");
				panelFirstFollow.add(panelFollow);
				mObtainFollow.setEnabled(false);
				mTasp.setEnabled(true);

			}
		});
		// Pulsar sobre TASP
		mTasp.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				mMediator.tasp();

			}
		});

	}// buildListeners

	/**
	 * Crea una Jtable nueva para el First.
	 * 
	 * @return True si ha podido obtener la tabla first.
	 */
	private boolean createTableFirst() {
		if (mMediator.first()) {
			mTableFirst.setHeight("180px");
			mTableFirst.setWidth("400px");

			for (int i = 0; i < mHeader.length; i++) {
				mTableFirst.setText(0, i, mHeader[i]);
				mTableFirst.getCellFormatter().addStyleName(0,i,"header");
			}
			
			for (int i = 0; i < (mFirst).length; i++) {
				for (int j = 0; j < mHeader.length; j++) {
					if (mFirst[i][j] == null)
						mTableFirst.setText(i + 1, j, " ");
					else
						mTableFirst.setText(i + 1, j, mFirst[i][j].toString());
				}
			}
			return true;
		}

		return false;
	}// createTableFirst

	/**
	 * Crea una Jtable nueva para el follow
	 * 
	 * @return True si ha podido obtener la tabla del follow.
	 */

	private boolean createTableFollow() {		
		if (mMediator.follow()) {
			mTableFollow.setHeight("180px");
			mTableFollow.setWidth("400px");

			for (int i = 0; i < mHeader.length; i++) {
				mTableFollow.setText(0, i, mHeader[i]);
				mTableFollow.getCellFormatter().addStyleName(0,i,"header");
			}

			for (int i = 0; i < (mFollow).length; i++) {
				for (int j = 0; j < mHeader.length; j++) {
					if (mFollow[i][j] == null)
						mTableFollow.setText(i + 1, j, " ");
					else
						mTableFollow
								.setText(i + 1, j, mFollow[i][j].toString());
				}
			}
			return true;
		}

		return false;
	}// createTableFollow

	/**
	 * Asigna al panel que se le pasa un borde con un título.
	 * 
	 * @param scroll
	 *            Panel al que se le asigna el borde.
	 * 
	 * @param title
	 *            Título del borde.
	 * 
	 * @return Panel con el borde asignado.
	 */
	/*
	 * private JScrollPane buildBorder (JScrollPane scroll, String title) {
	 * scroll.setBorder( BorderFactory.createCompoundBorder(
	 * BorderFactory.createCompoundBorder(
	 * BorderFactory.createTitledBorder(title),
	 * BorderFactory.createEmptyBorder(3, 6, 6, 6)), scroll.getBorder()));
	 * 
	 * return scroll; }//buildBorder
	 */

}// VisualFirstFollow
