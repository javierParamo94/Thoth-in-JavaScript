package src.client.gui.visual;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
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
 * @author Francisco Javier Páramo Arnaiz 
 * @version 2.0
 */
public class VisualTasp extends Composite {

	// Attributes
	// -----------------------------------------------------------------
	/**
     * Variable para imprimir mensajes con internacionalización.
     */
	private MessageMessages mMsg = GWT.create(MessageMessages.class);
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
	 * Panel donde va a ir la FlexTable con la traza y tabla TASB.
	 */
	public VerticalPanel mPanelTrace;
	/**
	 * Panel donde va a ir la FlexTable con la traza.
	 */
	public 	HorizontalPanel mPanelButton;
	/**
	 * JTextField donde se va a introducir la palabra a reconocer.
	 */
	public TextArea mWord;

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
	 * Gramática asociada.
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

		mWord = new TextArea();
		mWord.setPixelSize(120, 30);
		mWord.setReadOnly(false);
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
		mWord = new TextArea();
		mWord.setPixelSize(120, 30);
		mWord.setReadOnly(false);
		mVisible = true;
		mMediator = new MediatorTasp(this, first);
		if (mVisible)
			buildPanels();
	}// VisualTasp

	/**
	 * Construye los paneles y botones.<br>
	 * Asigna la funcionalidad de los botones.
	 */
	private void buildPanels() {
		HorizontalPanel word = new HorizontalPanel();
		VerticalPanel down = new VerticalPanel();
		mPanelButton = new HorizontalPanel();
		mPanelTrace = new VerticalPanel();

		// Botonera inferior
		mExit = new Button(mMsg.cancel());
		mNext = new Button(mMsg.nextstep());
		mCheck = new Button(mMsg.allsteps());
		mPanelButton.add(mExit);
		mPanelButton.add(mNext);
		mPanelButton.add(mCheck);
		mNext.setEnabled(false);
		mCheck.setEnabled(false);

		// TASP
		VerticalPanel panelTaspTable = new VerticalPanel();
		HTML tasp = new HTML("TASP");
		tasp.setStyleName("Panel-Text");
		panelTaspTable.add(tasp);
		panelTaspTable.setPixelSize(700, 300);
		mTaspTable.setPixelSize(700, 300);
		panelTaspTable.add(mTaspTable);
		

		// Introducir palabra
		word.add(new HTML(mMsg.word()));
		word.setPixelSize(700, 50);
		word.add(mWord);
		word.setStyleName("gwt-Big-Text");

		// Botones validar y borrar palabra
		mCheckWord = new PushButton(new Image("/images/tick.png"));
		mClear = new PushButton(new Image("/images/cross.png"));
		word.add(mCheckWord);
		word.add(mClear);

		// Tabla de la traza
		VerticalPanel panelTraceTable = new VerticalPanel();
		panelTraceTable.add(mTraceTable);
		mTraceTable.setPixelSize(700, 30);
		HTML trace = new HTML(mMsg.trace());
		trace.setStyleName("Panel-Text");
		down.add(trace);
		down.add(word);
		down.add(panelTraceTable);
		mPanelTrace.add(panelTaspTable);
		down.setSpacing(5);
		mPanelTrace.add(down);

		// Principal
		RootPanel.get().add(mPanelTrace);
		RootPanel.get().add(mPanelButton);
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
}// VisualTasp
