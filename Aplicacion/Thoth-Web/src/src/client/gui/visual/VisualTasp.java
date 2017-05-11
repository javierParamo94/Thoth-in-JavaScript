package src.client.gui.visual;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import src.client.core.grammar.Grammar;
import src.client.core.grammar.tasp.FirstFollow;
import src.client.gui.mediator.MediatorTasp;
import src.client.gui.utils.MessageMessages;

/**
 * <b>Descripción</b><br>
 * Visualización del algoritmo de reconocimiento de palabras mediante la tabla
 * de análisis sintáctico predictivo.
 * <p>
 * <b>Detalles</b><br>
 * Visualiza paso a paso el reconocimiento de palabras mediante la TASP.<br>
 * Está formada por tres paneles , uno contiene la TASP otro el campo donde
 * introducimos la palabra a reconocer y por último la traza de reconocimiento.
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
public class VisualTasp extends Composite {

	// Attributes
	// -----------------------------------------------------------------
	/**
     * Variable para imprimir mensajes con internacionalización.
     */
	private MessageMessages sms = GWT.create(MessageMessages.class);
	/**
	 * Mediador asociado al panel.
	 */
	public MediatorTasp mMediator;

	/**
	 * FlexTable donde va a estar la TASP.
	 */
	public FlexTable mTaspTable;

	/**
	 * Si está a true la ventana será mostrada y no se mostrará si es false.
	 */
	public boolean mVisible;

	/**
	 * FlexTable donde va a estar la traza.
	 */
	public FlexTable mTraceTable;

	/**
	 * FlexTable donde van a estar los titulos de las columnas en la traza.
	 */
	public FlexTable mTraceTitle;//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Panel donde va a ir la FlexTable con la traza y tabla TASB.
	 */
	public VerticalPanel panelTrace;
	/**
	 * Panel donde va a ir la FlexTable con la traza.
	 */
	public 	HorizontalPanel panelButton;
	/**
	 * JTextField donde se va a introducir la palabra a reconocer.
	 */
	public TextBox mWord;

	/**
	 * Botón para comprobar si la palabra introducida es válida.
	 */
	public PushButton mCheckWord;

	/**
	 * Botón para borrar la palabra introducida.
	 */
	public PushButton mClear;
	/**
	 * Botón Validar.
	 */
	public Button mCheck;

	/**
	 * Botón Salir.
	 */
	public Button mExit;

	/**
	 * Botón Siguiente.
	 */
	public Button mNext;
	/**
	 * 
	 */
	public Grammar mGrammar;

	// Methods
	// --------------------------------------------------------------------

	/**
	 * Constructor básico del algoritmo visual de TASP.
	 * 
	 * @param frame
	 *            Frame al que está asociado el diálogo.
	 * @param gra
	 *            Gramática de la que vamos a hacer el reconocimiento.
	 */
	public VisualTasp(Grammar gra) {

		mWord = new TextBox();
		mWord.setPixelSize(50, 15);
		mVisible = true;
		mMediator = new MediatorTasp(this, gra);
		if (mVisible)
			buildPanels();

	}// VisualTasp

	/**
	 * Constructor completo del algoritmo visual de TASP.
	 * 
	 * @param frame
	 *            Frame al que está asociado el diálogo.
	 * @param first
	 *            Algoritmo first follow.
	 */
	public VisualTasp(FirstFollow first, Grammar gra) {
		mGrammar = gra;
		mWord = new TextBox();
		mWord.setPixelSize(50, 15);
		mVisible = true;
		mMediator = new MediatorTasp(this, first);
		buildPanels();
		setVisible(mVisible);

	}// VisualTasp

	/**
	 * Construye los paneles y botones.<br>
	 * Asigna la funcionalidad de los botones.
	 */
	private void buildPanels() {
		HorizontalPanel word = new HorizontalPanel();
		VerticalPanel down = new VerticalPanel();
		panelButton = new HorizontalPanel();
		panelTrace = new VerticalPanel();

		// Botonera inferior
		mExit = new Button(sms.cancel());
		mNext = new Button(sms.nextstep());
		mCheck = new Button(sms.allsteps());

		panelButton.add(mExit);
		panelButton.add(mNext);
		panelButton.add(mCheck);
		mNext.setEnabled(false);
		mCheck.setEnabled(false);

		// TASP
		VerticalPanel panelTaspTable = new VerticalPanel();
		panelTaspTable.add(new HTML("TASP"));
		panelTaspTable.setPixelSize(500, 300);
		panelTaspTable.add(mTaspTable);

		// Introducir palabra
		word.setPixelSize(500, 100);
		word.add(mWord);
		word.setStyleName("gwt-Big-Text");

		// Botones validar y borrar palabra
		word.add(new HTML(sms.word()));
		mCheckWord = new PushButton(new Image("images/tick.png"));
		mClear = new PushButton(new Image("images/cross.png"));
		word.add(mCheckWord);
		word.add(mClear);

		// Tabla
		VerticalPanel scrollTraceTable = new VerticalPanel();
		//scrollTraceTable.add(mTraceTitle);
		scrollTraceTable.add(mTraceTable);
		// Pantalla central
		down.setPixelSize(500, 500);
		down.add(new HTML(sms.trace()));
		down.add(word);
		down.add(scrollTraceTable);
		panelTrace.add(panelTaspTable);
		panelTrace.add(down);

		// Principal
		RootPanel.get().add(panelTrace);
		RootPanel.get().add(panelButton);
		buildListeners();

	}// buildPanels

	/**
	 * Asigna la funcionalidad de los botones.
	 */
	public void buildListeners() {
		// Pulsar sobre validar palabra
		mCheckWord.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				mMediator.acceptWord();
			}
		});
		// Pulsar sobre borrar palabra
		mClear.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				mMediator.cancelWord();
			}
		});
		// Pulsar sobre Salir
		mExit.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				mMediator.exit();
			}
		});
		// Pulsar sobre Siguiente paso
		mNext.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				mMediator.next();

			}
		});
		// Pulsar sobre Validar cadena
		mCheck.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				mMediator.check();

			}
		});

	}// buildListeners

	/**
	 * Asigna al panel que se le pasa un borde con un título.
	 * 
	 * @param component
	 *            Panel al que se le asigna el borde.
	 * @param title
	 *            Título del borde.
	 * @return Panel con el borde asignado.
	 */
	/*
	 * private JComponent buildBorder (JComponent component, String title) {
	 * component.setBorder( BorderFactory.createCompoundBorder(
	 * BorderFactory.createCompoundBorder(
	 * BorderFactory.createTitledBorder(title),
	 * BorderFactory.createEmptyBorder(3, 6, 6, 6)), component.getBorder()));
	 * 
	 * return component; }//buildBorder
	 */

}// VisualTasp
