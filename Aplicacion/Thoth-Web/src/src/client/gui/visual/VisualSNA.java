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

import src.client.core.grammar.Grammar;
import src.client.gui.mediator.MediatorSNA;
import src.client.gui.utils.MessageMessages;

/**
 * <b>Descripción</b><br>
 * Visualización de la eliminación de símbolos no alcanzables.
 * <p>
 * <b>Detalles</b><br>
 * Visualiza paso a paso la eliminación de los símbolos no alcanzables.<br>
 * Está formada por dos paneles de texto, uno contiene la vieja gramática y otro
 * la nueva; la nueva gramática va siendo construída desde el axioma hasta
 * finalizar.
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
public class VisualSNA extends Composite {

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
	 * Panel donde irán colocados los botones
	 */
	private HorizontalPanel buttonPanel = new HorizontalPanel();

	/**
	 * Panel vertical donde ira la visualizacion de las áreas
	 */
	public VerticalPanel generalPanel = new VerticalPanel();

	/**
	 * Mediador asociado al panel
	 */
	public MediatorSNA mMediator;

	/**
	 * Indica si debe mostrarse o no el panel.
	 */
	public boolean mVisible;

	/**
	 * Panel vertical que engloba el área de la nueva gramática
	 */
	public VerticalPanel vPanelNew = new VerticalPanel();

	/**
	 * Panel vertical que engloba el área de la vieja gramática
	 */
	public VerticalPanel vPanelOld = new VerticalPanel();

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
	 * Constructor completo del algoritmo visual de limpieza de símbolos no
	 * alcanzables.
	 * 
	 * @param frame
	 *            Frame al que está asociado el diálogo.
	 * @param grammar
	 *            Gramática a limpiar.
	 */
	public VisualSNA(Grammar grammar) {

		mOld.setPixelSize(500, 400);
		mOld.setText(grammar.completeToString());
		mOld.setStyleName("Grammar-Text");

		mNew.setPixelSize(500, 400);
		mNew.setStyleName("Grammar-Text");

		mVisible = true;
		mMediator = new MediatorSNA(this, grammar);
		btnAcept.setEnabled(false);

		//Si cumple las condiciones, construye los paneles
		if (mVisible)
			buildPanels();

	}// VisualElimNonDeterministic

	public void buildPanels() {

		DockPanel dockPanel = new DockPanel();
		dockPanel.setStyleName("dockpanel");
		dockPanel.setSpacing(4);
		dockPanel.setHorizontalAlignment(DockPanel.ALIGN_CENTER);
		
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
		sPanelOld.setSize("600px", "450px");
		vPanelOld.setSpacing(10);
		vPanelOld.add(sPanelOld);
		vPanelOld.setStyleName("gwt-Big-Text");


		// Botones
		buttonPanel.add(btnCancel);
		buttonPanel.add(btnOneStep);
		buttonPanel.add(btnAllSteps);
		buttonPanel.add(btnAcept);
		
		buildListeners();

		// Add text all around
		HTML snaAlgorithm = new HTML(sms.snaalgorithm());
		snaAlgorithm.setStyleName("Panel-Text");
		dockPanel.add(snaAlgorithm, DockPanel.NORTH);
		dockPanel.add(buttonPanel, DockPanel.SOUTH);
		dockPanel.add(vPanelNew, DockPanel.EAST);
		dockPanel.add(vPanelOld, DockPanel.WEST);

		generalPanel.add(dockPanel);

		RootPanel.get().add(generalPanel);
	}

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

}// VisualSNA
