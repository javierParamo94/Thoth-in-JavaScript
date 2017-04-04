package src.client.gui;


import src.client.algorithms.MediatorAlgorithm;
import src.client.algorithms.eliminatenondeterministic.MediatorElimNonDeterministic;
import src.client.core.finiteautomaton.FiniteAutomaton;
//import algorithms.VisualAlgorithm;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
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
public class VisualElimNonDeterministic extends MyComposite {

	private TextArea textA = new TextArea();
	private TextArea textA1 = new TextArea();
	private HorizontalPanel hPanel = new HorizontalPanel();
	protected MediatorAlgorithm mMediator;

	public PushButton btnCancel = new PushButton(new Image(
			"images/cancelAlgorithm.png"));
	public PushButton btnOneStep = new PushButton(new Image(
			"images/oneStep.png"));
	public PushButton btnAllSteps = new PushButton(new Image(
			"images/allSteps.png"));
	public PushButton btnAcept = new PushButton(new Image(
			"images/acceptAlgorithm.png"));

	public VisualElimNonDeterministic(/*Frame frame,*/ /*FiniteAutomaton aut/*,GraphLayoutCache gCache*/) {

		//mMediator = new MediatorElimNonDeterministic(this, aut);
		//add(getCentralComponent(), BorderLayout.CENTER);
		buildPanels();
		//setLocationRelativeTo(frame);
		//setVisible(mVisible);

	}// VisualElimNonDeterministic

	public void buildPanels() {
		textA.setCharacterWidth(80);
		textA.setVisibleLines(20);
		textA1.setCharacterWidth(80);
		textA1.setVisibleLines(20);
		DockPanel dockPanel = new DockPanel();
		dockPanel.setStyleName("dockpanel");
		dockPanel.setSpacing(4);
		dockPanel.setHorizontalAlignment(DockPanel.ALIGN_CENTER);

		// Botones
		hPanel.add(btnCancel);
		hPanel.add(btnOneStep);
		hPanel.add(btnAllSteps);
		hPanel.add(btnAcept);
		buildCommonListeners();

		// Add text all around
		dockPanel.add(new HTML("This is the first north component."),
				DockPanel.NORTH);
		dockPanel.add(hPanel, DockPanel.SOUTH);
		dockPanel.add(textA, DockPanel.EAST);
		dockPanel.add(textA1, DockPanel.WEST);

		dockPanel.add(new HTML("This is the second north component."),
				DockPanel.NORTH);
		dockPanel.add(new HTML("This is the second south component"),
				DockPanel.SOUTH);

		VerticalPanel vPanel = new VerticalPanel();
		vPanel.add(dockPanel);

		// Add the widgets to the root panel.
		RootPanel.get().add(vPanel);
	}

	@Override
	public void handleError(String error) {
		// TODO Auto-generated method stub

	}

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
