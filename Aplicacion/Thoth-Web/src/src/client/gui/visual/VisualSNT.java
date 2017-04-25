package src.client.gui.visual;

import src.client.GrammarServiceClientImp;
import src.client.core.grammar.Grammar;
import src.client.gui.MessageMessages;
import src.client.gui.mediator.MediatorSNT;

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

/**
 * <b>Descripción</b><br>
 * Visualización del algoritmo de eliminación de no determinismo.
 * <p>
 * <b>Detalles</b><br>
 * Visualiza la eliminación de no determinismo en autómatas finitos.<br>
 * Ayuda a la comprensión del algoritmo mediante el paso a paso.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Visualiza el algoritmo de obtener autómata finito.<br>
 * Posibilita el paso a paso.
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @version 1.0
 */
public class VisualSNT extends Composite {

	private MessageMessages sms = GWT.create(MessageMessages.class);
	
	/**
	 * Área de texto donde se muetra la gramática original.
	 */
	public RichTextArea mNew = new RichTextArea();

	/**
	 * Área de texto donde aparecerá la gramática final.
	 */
	public RichTextArea mOld = new RichTextArea();

	/**
	 * 
	 */
	private HorizontalPanel hPanel = new HorizontalPanel();

	/**
	 * 
	 */
	public VerticalPanel vPanel = new VerticalPanel();
	
	/**
	 * 
	 */
	public VerticalPanel vPanelNew = new VerticalPanel();
	/**
	 * 
	 */
	public VerticalPanel vPanelOld = new VerticalPanel();
	/**
	 * 
	 */
	public GrammarServiceClientImp serviceImp;

	/**
	 * Mediador asociado al panel
	 */
	public MediatorSNT mMediator;

	/**
	 * Indica si debe mostrarse o no el panel.
	 */
	public boolean mVisible;

	/**
	 * Botones de cancelar, siguiente paso, todos los pasos y aceptar.
	 */
	public PushButton btnCancel = new PushButton(new Image(
			"images/cancelAlgorithm.png"));
	public PushButton btnOneStep = new PushButton(new Image(
			"images/oneStep.png"));
	public PushButton btnAllSteps = new PushButton(new Image(
			"images/allSteps.png"));
	public PushButton btnAcept = new PushButton(new Image(
			"images/acceptAlgorithm.png"));

	public VisualSNT(Grammar grammar) {

		mOld.setPixelSize(500, 400);
		mOld.setText(grammar.completeToString());

		mNew.setPixelSize(500, 400);

		mVisible = true;
		mMediator = new MediatorSNT(this, grammar);

		buildPanels();
		setVisible(mVisible);

	}// VisualElimNonDeterministic

	/**
	 * Construye los paneles y botones.<br>
	 */
	public void buildPanels() {

		DockPanel dockPanel = new DockPanel();
		dockPanel.setStyleName("dockpanel");
		dockPanel.setSpacing(4);
		dockPanel.setHorizontalAlignment(DockPanel.ALIGN_CENTER);

		vPanelNew.add(new HTML(sms.newgrammar()));
		vPanelNew.add(mNew);
		
		vPanelOld.add(new HTML(sms.oldgrammar()));
		vPanelOld.add(mOld);
		
		// Botones
		hPanel.add(btnCancel);
		hPanel.add(btnOneStep);
		hPanel.add(btnAllSteps);
		hPanel.add(btnAcept);
		buildCommonListeners();

		// Add text all around
		//dockPanel.add(new HTML("This is the first north component."),DockPanel.NORTH);
		dockPanel.add(hPanel, DockPanel.SOUTH);
		//dockPanel.add(new HTML(sms.newgrammar()), DockPanel.EAST);
		dockPanel.add(vPanelNew, DockPanel.EAST);
		//dockPanel.add(new HTML(sms.oldgrammar()), DockPanel.WEST);
		dockPanel.add(vPanelOld, DockPanel.WEST);
		dockPanel.add(new HTML("This is the second north component."),DockPanel.NORTH);
		//dockPanel.add(new HTML("This is the second south component"),DockPanel.SOUTH);

		vPanel.add(dockPanel);

		RootPanel.get().add(vPanel);
	}

	/**
	 * Asigna la funcionalidad de los botones.
	 */
	private void buildCommonListeners() {

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
}// VisualElimNonDeterministic
