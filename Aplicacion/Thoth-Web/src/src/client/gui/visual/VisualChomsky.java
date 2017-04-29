package src.client.gui.visual;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import src.client.GrammarServiceClientImp;
import src.client.gui.MessageMessages;
import src.client.gui.mediator.MediatorChomsky;
import src.client.core.grammar.Grammar;

/**
 * <b>Descripción</b><br>
 * Visualización de la forma normal de Chomsky.
 * <p>
 * <b>Detalles</b><br>
 * Visualiza paso a paso la transformación de una gramática a FNC.<br>
 * Está formada por tres paneles de texto, uno contiene la vieja gramática, otro
 * la nueva y el último y más pequeño la producción que se está transformando en
 * cada paso.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Visualiza la tranformación a FNC.<br>
 * Posibilita el paso a paso.
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @version 1.0
 */
public class VisualChomsky extends Composite {

	// Attributes
	// --------------------------------------------------------------------
	public RichTextArea mNew = new RichTextArea();
	public RichTextArea mOld = new RichTextArea();
	public RichTextArea mAux = new RichTextArea();
	private HorizontalPanel hPanel = new HorizontalPanel();

	public VerticalPanel vPanel = new VerticalPanel();
	public GrammarServiceClientImp serviceImp;

	/**
	 * Variable para la internacionalización de los textos
	 */
	private MessageMessages sms = GWT.create(MessageMessages.class);
	/**
	 * Panel vertical que engloba el área de la nueva gramática
	 */
	public VerticalPanel vPanelNew = new VerticalPanel();
	/**
	 * Panel vertical que engloba el área de la vieja gramática
	 */
	public VerticalPanel vPanelOld = new VerticalPanel();

	/**
	 * Mediador de limpieza asociado al panel.
	 */
	public MediatorChomsky mMediator;

	/**
	 * Indica si debe mostrarse o no el panel.
	 */
	public boolean mVisible;

	public PushButton btnCancel = new PushButton(new Image(
			"images/cancelAlgorithm.png"));
	public PushButton btnOneStep = new PushButton(new Image(
			"images/oneStep.png"));
	public PushButton btnAllSteps = new PushButton(new Image(
			"images/allSteps.png"));
	public PushButton btnAcept = new PushButton(new Image(
			"images/acceptAlgorithm.png"));

	// Methods
	// -----------------------------------------------------------------------

	/**
	 * Constructor completo de la Forma Normal de Chomsky.
	 * 
	 * @param frame
	 *            Frame al que está asociado el diálogo.
	 * @param grammar
	 *            Gramática a transformar.
	 */
	public VisualChomsky(Grammar grammar) {

		mOld.setPixelSize(500, 350);
		mOld.setText(grammar.completeToString());
		mNew.setPixelSize(500, 455);
		mAux.setSize("500px", "30px");

		mOld.setEnabled(false);
		mNew.setEnabled(false);
		mAux.setEnabled(false);

		mVisible = true;
		mMediator = new MediatorChomsky(this, grammar);

		buildPanels();
		setVisible(mVisible);

	}// VisualChomsky

	/**
	 * Construye los paneles y botones.<br>
	 * Asigna la funcionalidad de los botones.
	 */
	private void buildPanels() {
		DockPanel dockPanel = new DockPanel();
		dockPanel.setStyleName("dockpanel");
		dockPanel.setSpacing(4);
		dockPanel.setHorizontalAlignment(DockPanel.ALIGN_CENTER);

		// Paneles
		vPanelNew.add(new HTML(sms.newgrammar()));
		vPanelNew.setSpacing(20);
		vPanelNew.add(mNew);
		vPanelNew.setStyleName("gwt-Big-Text");

		vPanelOld.add(new HTML(sms.oldgrammar()));
		vPanelOld.setSpacing(20);
		vPanelOld.add(mOld);
		vPanelOld.add(new HTML(sms.transprod()));
		vPanelOld.add(mAux);
		vPanelOld.setStyleName("gwt-Big-Text");

		// Botones
		hPanel.add(btnCancel);
		hPanel.add(btnOneStep);
		hPanel.add(btnAllSteps);
		hPanel.add(btnAcept);
		buildListeners();

		dockPanel.add(new HTML(sms.chomskyalgorithm()), DockPanel.NORTH);
		dockPanel.add(hPanel, DockPanel.SOUTH);
		dockPanel.add(vPanelNew, DockPanel.EAST);
		dockPanel.add(vPanelOld, DockPanel.WEST);

		vPanel.add(dockPanel);

		RootPanel.get().add(vPanel);

	}// buildPanels

	/**
	 * Asigna la funcionalidad de los botones.
	 */
	public void buildListeners() {
		// Pulsar sobre Cancelar
		btnCancel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				mMediator.exit();

			}
		});
		// Pulsar sobre Siguiente
		btnOneStep.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				mMediator.next();

			}
		});
		// Pulsar sobre Todos los pasos
		btnAllSteps.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				mMediator.all();

			}
		});
		// Pulsar sobre Aceptar
		btnAcept.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				mMediator.accept();
			}
		});
	}// buildListeners

}// VisualChomsky
