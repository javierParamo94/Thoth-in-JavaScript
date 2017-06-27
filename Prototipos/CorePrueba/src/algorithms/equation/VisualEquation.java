package algorithms.equation;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.jgraph.JGraph;

import algorithms.VisualAlgorithm;

import core.finiteautomaton.FiniteAutomaton;

import view.utils.Images;
import view.utils.Messages;

/**
 * <b>Descripción</b><br>
 * Visualización del algoritmo de la obtención de una expresión regular a partir
 * de un autómata finito mediante el método de las ecuaciones características.
 * <p>
 * <b>Detalles</b><br>
 * Visualiza la obtención de un autómata finito.<br>
 * Posibilita al usuario comparar la expresión regular que él ha calculado con
 * la que el programa ha obtenido y así servir de apoyo al aprendizaje del
 * método.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Visualiza el método de las ecuaciones características.<br>
 * Posibilita el paso a paso.
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @version 1.0
 */
public class VisualEquation extends VisualAlgorithm {

	// Attributes
	// -----------------------------------------------------------------

	/**
	 * Tabla donde se indica las particiones del método de minimización.
	 */
	public JTable mTable;

	/**
	 * Determina si va a ser mostrado o no el panel.
	 */
	public boolean mVisible;

	/**
	 * Botón Comprobar
	 */
	public JButton mCheck;

	/**
	 * Representa el campo de texto en el que se imprimirá la información
	 * relativa al algoritmo de ecuaciones características.
	 */
	public JTextArea mText;

	/**
	 * Representa el campo de texto en el que se imprimirá la solución.
	 */
	public JTextField mSolution;

	/**
	 * Representa el campo de texto donde se puede introducir una ecuación
	 * característica para comprobar si es equivalente a la solución.
	 */
	public JTextField mValidate;

	// Methods
	// --------------------------------------------------------------------

	/**
	 * Constructor del algoritmo visual.<br>
	 * Se encarga de mostrar el algoritmo por pantalla y de hacerle comprensible
	 * para el usuario.
	 * 
	 * @param frame Ventana a la que está asociada el diálogo.
	 * @param aut Automata
	 * @param JGraph graph
	 */
	public VisualEquation(Frame frame, FiniteAutomaton aut, JGraph graph) {
		super(Messages.EQUATIONS_CARAC, true);
		setSize(new Dimension(750, 550));
		mVisible = true;
		add(getCentralComponent(),BorderLayout.CENTER);
		buildListeners();
		mMediator = new MediatorEquation(this, aut, graph.getGraphLayoutCache());
		setLocationRelativeTo(frame);
		setVisible(mVisible);

	}// VisualEquation

	/**
	 * Asigna la funcionalidad de los botones.
	 */
	private void buildListeners() {
		// Pulsar sobre Chequear
		mCheck.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((MediatorEquation) mMediator).check(mValidate.getText());

			}
		});

	}// buildListeners


	public Component getCentralComponent() {
		JPanel central = new JPanel(new BorderLayout()), 
			up = new JPanel(new BorderLayout()), 
			down = new JPanel(new BorderLayout()), 
			solution = new JPanel(new GridLayout(2, 1, 0, 0)), 
			sol = new JPanel(), validate = new JPanel();
		JScrollPane scrollText;

		mText = new JTextArea();
		mText.setEditable(false);
		mSolution = new JTextField(20);
		mSolution.setEnabled(false);
		mValidate = new JTextField(15);
		mValidate.setEnabled(false);


		mScroll.getViewport().setPreferredSize(new Dimension(650, 200));
		mScroll = (JScrollPane) buildBorder(mScroll, Messages.AUTOMATON);
		up.setBorder(BorderFactory.createEmptyBorder(3, 6, 6, 6));
		up.add(mScroll);

		scrollText = new JScrollPane(mText);
		scrollText = (JScrollPane) buildBorder(scrollText, Messages.EQUATIONS);
		scrollText.setPreferredSize(new Dimension(400, 150));
		sol.add(mSolution);
		sol = (JPanel) buildBorder(sol, Messages.SOLUTION);
		validate.add(mValidate);
		validate.add(mCheck = new JButton(Images.getTickIcon()));
		mCheck.setEnabled(false);
		validate = (JPanel) buildBorder(validate, Messages.VALIDATE);
		solution.add(sol);
		solution.add(validate);

		down.setBorder(BorderFactory.createEmptyBorder(3, 6, 6, 6));
		down.add(scrollText, BorderLayout.CENTER);
		down.add(solution, BorderLayout.LINE_END);

		central.add(up, BorderLayout.CENTER);
		central.add(down, BorderLayout.PAGE_END);

		return central;
	}

}// VisualEquation
