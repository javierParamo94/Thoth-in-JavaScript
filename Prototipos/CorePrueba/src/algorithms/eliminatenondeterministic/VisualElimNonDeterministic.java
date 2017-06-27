package algorithms.eliminatenondeterministic;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

import org.jgraph.JGraph;
import org.jgraph.graph.GraphLayoutCache;

import algorithms.VisualAlgorithm;

import core.finiteautomaton.FiniteAutomaton;

import view.utils.Messages;
import view2.controller.CellsCreatorHelper;

/**
 * <b>Descripci�n</b><br>
 * Visualizaci�n del algoritmo de eliminaci�n de no determinismo.
 * <p>
 * <b>Detalles</b><br>
 * Visualiza la eliminaci�n de no determinismo en aut�matas finitos.<br>
 * Ayuda a la comprensi�n del algoritmo mediante el paso a paso.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Visualiza el algoritmo de obtener aut�mata finito.<br>
 * Posibilita el paso a paso.
 * </p>
 * 
 * @author �lvar Arn�iz Gonz�lez, Andr�s Arn�iz Moreno
 * @version 1.0
 */
public class VisualElimNonDeterministic extends VisualAlgorithm {

	// Attributes
	// -----------------------------------------------------------------

	/**
	 * Tabla donde se indica la funci�n de transici�n del nuevo aut�mata.
	 */
	public JTable mTable;

	/**
	 * Informaci�n sobre el algoritmo.
	 */
	public JTextArea mInformation;

	/**
	 * Determina si va a ser mostrado o no el panel.
	 */
	public boolean mVisible;

	// Methods
	// --------------------------------------------------------------------

	/**
	 * Constructor del algoritmo visual.<br>
	 * Se encarga de mostrar el algoritmo por pantalla y de hacerle comprensible
	 * para el usuario.
	 * 
	 * @param frame
	 *            Ventana a la que est� asociada el di�logo.
	 */
	public VisualElimNonDeterministic(Frame frame, FiniteAutomaton aut,
			GraphLayoutCache gCache) {
		super(Messages.ELIMINATE_NON_DETERMINISTIC, true);
		setSize(new Dimension(750, 550));
		mVisible = true;
		JGraph graph = new JGraph(gCache);
		graph.setEnabled(false);
		graph.setAntiAliased(true);
		CellsCreatorHelper.scaleAndCenterGraph(graph, new Dimension(705, 270));
		setShowingPane(graph);
		mInformation = new JTextArea();
		mMediator = new MediatorElimNonDeterministic(this, aut);
		add(getCentralComponent(),BorderLayout.CENTER);
		buildPanels();
		setLocationRelativeTo(frame);
		setVisible(mVisible);

	}// VisualElimNonDeterministic

	/**
	 * Construye los paneles y botones del algoritmo visual.
	 */
	public void buildPanels() {

		mScroll
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		mScroll
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

	}// buildPanels


	public Component getCentralComponent() {
		JPanel central = new JPanel(new BorderLayout()), up = new JPanel(
				new BorderLayout()), down = new JPanel(new BorderLayout());
		JScrollPane scrollTable, scrollText;

		mScroll = buildBorder(mScroll, Messages.AUTOMATON);
		up.setBorder(BorderFactory.createEmptyBorder(3, 6, 6, 6));
		up.add(mScroll);

		scrollTable = new JScrollPane(mTable);
		scrollTable = buildBorder(scrollTable, Messages.TRANS_TABLE);
		scrollTable.setPreferredSize(new Dimension(300, 100));
		scrollText = new JScrollPane(mInformation);
		scrollText.setPreferredSize(new Dimension(250, 150));
		scrollText = buildBorder(scrollText, Messages.DETAILS);
		down.setBorder(BorderFactory.createEmptyBorder(3, 6, 6, 6));
		down.add(scrollTable, BorderLayout.CENTER);
		down.add(scrollText, BorderLayout.LINE_END);
		down.setPreferredSize(new Dimension(500, 150));

		central.add(up, BorderLayout.CENTER);
		central.add(down, BorderLayout.PAGE_END);

		return central;
	}

}// VisualElimNonDeterministic
