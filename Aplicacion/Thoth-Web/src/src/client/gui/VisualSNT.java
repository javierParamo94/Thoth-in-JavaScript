package src.client.gui;




import src.client.GrammarServiceClientImp;

//import algorithms.VisualAlgorithm;

import src.client.core.grammar.Grammar;


import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
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
public class VisualSNT extends Composite {

	private TextArea mNew;
	private TextArea mOld;
	private HorizontalPanel hPanel = new HorizontalPanel();

	public VerticalPanel vPanel = new VerticalPanel();
	public GrammarServiceClientImp  serviceImp;
	private Composite currentPage;

    /**
     * Mediador asociado al panel
     */
    public MediatorSNT mMediator;
    
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

	public VisualSNT(/*Frame frame,*/Grammar grammar /*,FiniteAutomaton aut /*,GraphLayoutCache gCache*/) {

		//add(getCentralComponent(), BorderLayout.CENTER);
		
		buildPanels();
		//setLocationRelativeTo(frame);
		//setVisible(mVisible);

	    
        mOld = new TextArea();
        mOld.setCharacterWidth(80);
        mOld.setVisibleLines(20);
        mOld.setEnabled(false);
		mOld.setText(grammar.completeToString());

        mNew = new TextArea();
        mNew.setCharacterWidth(80);
        mNew.setVisibleLines(20);
        mNew.setEnabled(false);

        mVisible = true;
        mMediator = new MediatorSNT(this, grammar);

        buildPanels();
//        setLocationRelativeTo(frame);
//        setResizable(false);
        setVisible(mVisible);
        
	}// VisualElimNonDeterministic

	public void buildPanels() {
		

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
		dockPanel.add(mNew, DockPanel.EAST);
		dockPanel.add(mOld, DockPanel.WEST);
		dockPanel.add(new HTML("This is the second north component."),
				DockPanel.NORTH);
		dockPanel.add(new HTML("This is the second south component"),
				DockPanel.SOUTH);


		vPanel.add(dockPanel);

		RootPanel.get().add(vPanel);
	}

	/*@Override
	public void handleError(String error) {
		// TODO Auto-generated method stub

	}*/

	private void buildCommonListeners() {
		// Pulsar sobre Cancelar
		btnCancel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				//mMediator.exit();
				/*this.vPanel.clear();

				this.currentPage = new VisualElimNonDeterministic(mGrammar);
				this.vPanel.add(this.currentPage);
				
				mainGui currentPage = new mainGui(serviceImp);
				vPanel.add(currentPage);*/
                //mMediator.exit();
                vPanel.clear();
                vPanel = null;
                currentPage = new mainGui(serviceImp);
                vPanel.add(currentPage);
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
	/*public void openMainGui() {
		this.vPanel.clear();
		hPanel.clear();
		this.currentPage = new mainGui(serviceImp);
		this.vPanel.add(this.currentPage);
		//p.addEast(this.currentPage, 100);
		//this.vPanel.setCellHorizontalAlignment(this.currentPage, HasHorizontalAlignment.ALIGN_CENTER);
	}*/

}// VisualElimNonDeterministic
