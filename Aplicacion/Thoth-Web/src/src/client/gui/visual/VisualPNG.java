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
import src.client.gui.mediator.MediatorPNG;
import src.client.gui.utils.MessageMessages;

/**
 * <b>Descripción</b><br>
 * Visualización de la eliminación de producciones no generativas.
 * <p>
 * <b>Detalles</b><br>
 * Visualiza paso a paso la eliminación de producciones no generativas.<br>
 * Está formada por tres paneles de texto, uno contiene la vieja gramática, otro
 * la nueva y el último y más pequeño el conjunto UNITARIO(A), es decir, los
 * terminales a los que se pueden llegar siguiendo sólo producciones unitarias.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Visualiza la eliminación de PNGs.<br>
 * Posibilita el paso a paso.
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @author Francisco Javier Páramo Arnaiz
 * @version 2.0
 */
public class VisualPNG extends Composite {

	// Attributes
	// --------------------------------------------------------------------
	/**
	 * Variable para la internacionalización de los textos
	 */
	private MessageMessages mMsg = GWT.create(MessageMessages.class);

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
	 * Panel donde irán colocados los botones
	 */
	private HorizontalPanel buttonPanel = new HorizontalPanel();

	/**
	 * Panel vertical donde ira la visualizacion de las áreas
	 */
	public VerticalPanel mGeneralPanel = new VerticalPanel();
	
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
	public MediatorPNG mMediator;

	/**
	 * Indica si debe mostrarse o no el panel.
	 */
	public boolean mVisible;

	/**
	 * Botones de cancelar, siguiente paso, todos los pasos y aceptar.
	 */
	public Button mBtnCancel = new Button(mMsg.cancel());
	public Button mBtnOneStep = new Button(mMsg.nextstep());
	public Button mBtnAllSteps = new Button(mMsg.allsteps());
	public Button mBtnAcept = new Button(mMsg.accept());

	// Methods
	// -----------------------------------------------------------------------

	/**
	 * Constructor completo del algoritmo visual de limpieza de producciones no
	 * generativas
	 * 
	 * @param frame
	 *            Frame al que está asociado el diálogo.
	 * @param grammar
	 *            Gramática a limpiar.
	 */
	public VisualPNG(Grammar grammar) {

		mOld.setText(grammar.completeToString());
		mOld.setStyleName("Grammar-Text");
		mNew.setStyleName("Grammar-Text");
	
		mAux.setSize("500px", "20px");
		mAux.setStyleName("Grammar-Text");
		
		mVisible = true;
		mMediator = new MediatorPNG(this, grammar);
		mBtnAcept.setEnabled(false);
		
		//Si cumple las condiciones, construye los paneles
		if (mVisible)
			buildPanels();
	}// VisualPNG

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
		HTML newGrammar = new HTML(mMsg.newgrammar());
		newGrammar.setStyleName("Panel-Text");
		vPanelNew.add(newGrammar);
		ScrollPanel sPanelNew = new ScrollPanel(mNew);
		sPanelNew.setSize("600px", "450px");
		vPanelNew.setSpacing(10);
		vPanelNew.add(sPanelNew);
		vPanelNew.setStyleName("gwt-Big-Text");

		HTML oldGramar = new HTML(mMsg.oldgrammar());
		oldGramar.setStyleName("Panel-Text");
		vPanelOld.add(oldGramar);
		ScrollPanel sPanelOld = new ScrollPanel(mOld);
		sPanelOld.setSize("600px", "380px");
		vPanelOld.setSpacing(10);
		vPanelOld.add(sPanelOld);
		HTML uniSymbols = new HTML(mMsg.unisymbols());
		uniSymbols.setStyleName("Panel-Text");
		vPanelOld.add(uniSymbols);
		vPanelOld.add(mAux);
		vPanelOld.setStyleName("gwt-Big-Text");

		// Botones
		buttonPanel.add(mBtnCancel);
		buttonPanel.add(mBtnOneStep);
		buttonPanel.add(mBtnAllSteps);
		buttonPanel.add(mBtnAcept);
		buildListeners();

		HTML pngAlgorithm = new HTML(mMsg.pngalgorithm());
		pngAlgorithm.setStyleName("Panel-Text");
		dockPanel.add(pngAlgorithm, DockPanel.NORTH);
		dockPanel.add(buttonPanel, DockPanel.SOUTH);
		dockPanel.add(vPanelNew, DockPanel.EAST);
		dockPanel.add(vPanelOld, DockPanel.WEST);

		mGeneralPanel.add(dockPanel);

		RootPanel.get().add(mGeneralPanel);

	}// buildPanels

	/**
	 * Asigna la funcionalidad de los botones.
	 */
	public void buildListeners() {
		// Pulsar sobre Cancelar
		mBtnCancel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				mMediator.exit();

			}
		});
		// Pulsar sobre Siguiente
		mBtnOneStep.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				mMediator.next();

			}
		});
		// Pulsar sobre Todos los pasos
		mBtnAllSteps.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				mMediator.all();

			}
		});
		// Pulsar sobre Aceptar
		mBtnAcept.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				mMediator.accept();
			}
		});

	}// buildListeners
}// VisualPNG
