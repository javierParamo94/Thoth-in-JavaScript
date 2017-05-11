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

import src.client.gui.mediator.MediatorDirectRecursive;
import src.client.gui.utils.MessageMessages;
import src.client.core.grammar.Grammar;

/**
 * <b>Descripción</b><br>
 * Visualización del algoritmo de eliminación de recursividad directa.
 * <p>
 * <b>Detalles</b><br>
 * Visualiza paso a paso el algoritmo de eliminación de recursividad directa.<br>
 * Está formada por tres paneles de texto, uno contiene la vieja gramática, otro
 * la nueva y el último y más pequeño la producción que será borrada. paso.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Visualiza la eliminación de recursividad directa.<br>
 * Posibilita el paso a paso.
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @version 1.0
 */
public class VisualDirectRecursive extends Composite {

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
	 * Texto que muestra las producciones recursivas.
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
	 * Mediador de limpieza asociado al panel.
	 */
	public MediatorDirectRecursive mMediator;

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
	 * Constructor completo del algoritmo visual de limpieza de símbolos no
	 * terminables.
	 * 
	 * @param frame
	 *            Frame al que está asociado el diálogo.
	 * @param grammar
	 *            Gramática a limpiar.
	 */
	public VisualDirectRecursive(Grammar grammar) {

		mOld.setText(grammar.completeToString());
		mOld.setStyleName("Grammar-Text");
		mNew.setStyleName("Grammar-Text");
		mAux.setSize("500px", "20px");
		mAux.setStyleName("Grammar-Text");
		
		mVisible = true;
		mMediator = new MediatorDirectRecursive(this, grammar);

		//Si cumple las condiciones, construye los paneles
		if (mVisible)
			buildPanels();

	}// VisualDirectRecursive

	/**
	 * Construye los paneles y botones.<br>
	 * Asigna la funcionalidad de los botones.
	 */
	private void buildPanels() {
		DockPanel dockPanel = new DockPanel();
		dockPanel.setStyleName("dockpanel");
		dockPanel.setSpacing(4);
		dockPanel.setHorizontalAlignment(DockPanel.ALIGN_CENTER);

		//Paneles
		vPanelNew.add(new HTML(sms.newgrammar()));
		ScrollPanel sPanelNew = new ScrollPanel(mNew);
		sPanelNew.setSize("600px", "450px");
		vPanelNew.setSpacing(10);
		vPanelNew.add(sPanelNew);
		vPanelNew.setStyleName("gwt-Big-Text");

		vPanelOld.add(new HTML(sms.oldgrammar()));
		ScrollPanel sPanelOld = new ScrollPanel(mOld);
		sPanelOld.setSize("600px", "380px");
		vPanelOld.setSpacing(10);
		vPanelOld.add(sPanelOld);
		vPanelOld.add(new HTML(sms.recprods()));
		vPanelOld.add(mAux);
		vPanelOld.setStyleName("gwt-Big-Text");

		// Botones
		buttonPanel.add(btnCancel);
		buttonPanel.add(btnOneStep);
		buttonPanel.add(btnAllSteps);
		buttonPanel.add(btnAcept);
		buildListeners();

		dockPanel.add(new HTML(sms.directrecursive()), DockPanel.NORTH);
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
}// VisualDirectRecursive

