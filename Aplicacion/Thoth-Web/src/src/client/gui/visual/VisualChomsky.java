package src.client.gui.visual;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import src.client.gui.mediator.MediatorChomsky;
import src.client.gui.utils.MessageMessages;
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
	/**
	 * Variable para la internacionalización de los textos
	 */
	private MessageMessages sms = GWT.create(MessageMessages.class);

	/**
	 * Texto el cuál muestra la gramática original.
	 */
	public HTML mNew = new HTML();

	/**
	 * Texto que muestra la gramática final.
	 */
	public HTML mOld = new HTML();
	
	/**
	 * Texto que muestra los simbolos unitarios.
	 */
	public HTML mAux = new HTML();
	
	/**
	 * Panel que contiene los botones.
	 */
	private HorizontalPanel buttonPanel = new HorizontalPanel();

	/**
	 * Panel general de la vista.
	 */
	public VerticalPanel generalPanel = new VerticalPanel();
	
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

	/**
	 * Botones de cancelar, siguiente paso, todos los pasos y aceptar.
	 */
	public Button btnCancel = new Button(sms.cancel());
	public Button btnOneStep = new Button(sms.nextstep());
	public Button btnAllSteps = new Button(sms.allsteps());
	public Button btnAcept = new Button(sms.accept());

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

		mOld.setText(grammar.completeToString());
		mAux.setSize("500px", "30px");

		mOld.setStyleName("Grammar-Text");
		mNew.setStyleName("Grammar-Text");
		mAux.setStyleName("Grammar-Text");

		mVisible = true;
		mMediator = new MediatorChomsky(this, grammar);

		//Si cumple las condiciones, construye los paneles
		if (mVisible)
			buildPanels();

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
		HTML newGrammar = new HTML(sms.newgrammar());
		newGrammar.setStyleName("Panel-Text");
		vPanelNew.add(newGrammar);
		ScrollPanel sPanelNew = new ScrollPanel(mNew);
		sPanelNew.setSize("600px", "450px");
		vPanelNew.setSpacing(10);
		vPanelNew.add(sPanelNew);
		vPanelNew.setStyleName("gwt-Big-Text");

		HTML oldGramar = new HTML(sms.oldgrammar());
		oldGramar.setStyleName("Panel-Text");
		vPanelOld.add(oldGramar);
		ScrollPanel sPanelOld = new ScrollPanel(mOld);
		sPanelOld.setSize("600px", "380px");
		vPanelOld.setSpacing(10);
		vPanelOld.add(sPanelOld);
		HTML transProd = new HTML(sms.transprod());
		transProd.setStyleName("Panel-Text");
		vPanelOld.add(transProd);
		vPanelOld.add(mAux);
		vPanelOld.setStyleName("gwt-Big-Text");

		// Botones
		buttonPanel.add(btnCancel);
		buttonPanel.add(btnOneStep);
		buttonPanel.add(btnAllSteps);
		buttonPanel.add(btnAcept);
		buildListeners();

		HTML chomskyAlgorithm = new HTML(sms.chomskyalgorithm());
		chomskyAlgorithm.setStyleName("Panel-Text");
		dockPanel.add(chomskyAlgorithm, DockPanel.NORTH);
		dockPanel.add(buttonPanel, DockPanel.SOUTH);
		dockPanel.add(vPanelNew, DockPanel.EAST);
		dockPanel.add(vPanelOld, DockPanel.WEST);

		generalPanel.add(dockPanel);

		RootPanel.get().add(generalPanel);

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
