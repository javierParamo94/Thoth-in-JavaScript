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
import src.client.gui.mediator.MediatorFirstFollow;
import src.client.gui.utils.MessageMessages;

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
	private HorizontalPanel buttonPanel = new HorizontalPanel();

	/**
	 * Panel vertical donde ira la visualizacion de las áreas
	 */
	public VerticalPanel generalPanel = new VerticalPanel();

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
		if (mVisible)
			buildPanels();

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
		HTML gramar = new HTML(sms.grammar());
		gramar.setStyleName("Panel-Text");
		panelGrammar.add(gramar);
		panelGrammar.add(mGrammar);
		panelGrammar.setStyleName("gwt-Big-Text");

		panelFirst = new VerticalPanel();
		HTML first = new HTML("First");
		first.setStyleName("Panel-Text");
		panelFirst.add(first);

		panelFollow = new VerticalPanel();
		HTML follow = new HTML("Follow");
		follow.setStyleName("Panel-Text");
		panelFollow.add(follow);

		panelFirstFollow.setPixelSize(500, 500);
		panelFirstFollow.setStyleName("gwt-Big-Text");

		// Botones
		buttonPanel.add(mExit);
		buttonPanel.add(mObtainFirst);
		buttonPanel.add(mObtainFollow);
		mObtainFollow.setEnabled(false);
		buttonPanel.add(mTasp);
		mTasp.setEnabled(false);
		buildListeners();

		HTML calculateFF = new HTML(sms.calculateff());
		calculateFF.setStyleName("Panel-Text");
		dockPanel.add(calculateFF, DockPanel.NORTH);
		dockPanel.add(buttonPanel, DockPanel.SOUTH);
		dockPanel.add(panelFirstFollow, DockPanel.EAST);
		dockPanel.add(panelGrammar, DockPanel.WEST);

		generalPanel.add(dockPanel);

		RootPanel.get().add(generalPanel);

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
				generalPanel.clear();
				mMediator.tasp();

			}
		});

	}// buildListeners

	/**
	 * Crea una tabla nueva para el First.
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
	 * Crea una tabla nueva para el follow
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
}// VisualFirstFollow
