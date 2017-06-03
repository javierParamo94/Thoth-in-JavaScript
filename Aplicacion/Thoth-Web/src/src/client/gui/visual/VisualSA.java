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
import src.client.gui.mediator.MediatorSA;
import src.client.gui.utils.MessageMessages;

/**
 * <b>Descripción</b><br>
 * Visualización del algoritmo de eliminación de símbolos anulables.
 * <p>
 * <b>Detalles</b><br>
 * Visualiza paso a paso la eliminación de símbolos anulables.<br>
 * Está formada por tres paneles de texto, uno contiene la vieja gramática, otro
 * la nueva y el último y más pequeño la producción épsilon que se debe
 * eliminar. paso.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Visualiza el algoritmo de eliminación de símbolos anulables.<br>
 * Posibilita el paso a paso.
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @version 1.0
 */
public class VisualSA extends Composite {

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
	 * Texto que muestra los simbolos anulables.
	 */
	public HTML mAux = new HTML();
	
	/**
	 * Panel donde irán colocados los botones
	 */
	private HorizontalPanel buttonPanel = new HorizontalPanel();

	/**
	 * Panel vertical donde ira la visualizacion de las áreas
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
	 * Mediador asociado al panel
	 */
	public MediatorSA mMediator;

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
	 * Constructor completo del algoritmo visual de limpieza de símbolos
	 * anulables.
	 * 
	 * @param frame
	 *            Frame al que está asociado el diálogo.
	 * @param grammar
	 *            Gramática a limpiar.
	 */
	public VisualSA(Grammar grammar) {

		mOld.setText(grammar.completeToString());
		mOld.setStyleName("Grammar-Text");
		mNew.setStyleName("Grammar-Text");
		
		mAux.setSize("200px", "30px");
		mAux.setStyleName("Grammar-Text");
		
		mVisible = true;
		mMediator = new MediatorSA(this, grammar);
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
		sPanelNew.setSize("600px", "485px");
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
		HTML cancelSymbols = new HTML(sms.cancelsymbols());
		cancelSymbols.setStyleName("Panel-Text");
		vPanelOld.add(cancelSymbols);
		vPanelOld.add(mAux);
		vPanelOld.setStyleName("gwt-Big-Text");

		// Botones
		buttonPanel.add(btnCancel);
		buttonPanel.add(btnOneStep);
		buttonPanel.add(btnAllSteps);
		buttonPanel.add(btnAcept);
		buildListeners();

		HTML saAlgorithm = new HTML(sms.saalgorithm());
		saAlgorithm.setStyleName("Panel-Text");
		dockPanel.add(saAlgorithm, DockPanel.NORTH);
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

}// VisualSA
