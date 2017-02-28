package algorithms.minimize;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import algorithms.VisualAlgorithm;

import core.finiteautomaton.FiniteAutomaton;

import view.utils.Messages;

/**
 * <b>Descripción</b><br>
 * Visualización del algoritmo de minimización de autómata finito.
 * <p>
 * <b>Detalles</b><br>
 * Visualiza la minimización de un autómata finito.<br>
 * Ayuda a la comprensión del algoritmo mediante una tabla en la que se van mostrando
 * las particiones de equivalencia y los estados que engloba cada una.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Visualiza el algoritmo de minimización de autómata finito.<br>
 * Posibilita el paso a paso.
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @version 1.0
 */
public class VisualMinimize extends VisualAlgorithm {
    
    // Attributes -----------------------------------------------------------------
    
    /**
     * Tabla donde se indica las particiones del método de minimización.
     */
    public JTable mTable;
    
    /**
     * Determina si va a ser mostrado o no el panel.
     */
    public boolean mVisible;
    
    // Methods --------------------------------------------------------------------
    
    /**
     * Constructor del algoritmo visual.<br>
     * Se encarga de mostrar el algoritmo por pantalla y de hacerle comprensible para
     * el usuario.
     * 
     * @param frame Ventana a la que está asociada el diálogo.
     * @param panel Panel de Autómata.
     */
    public VisualMinimize (Frame frame, FiniteAutomaton aut) {
        super(Messages.MINIMIZE, true);
        setSize(new Dimension(750, 550));
        mVisible = true;
        buildPanels();
        mMediator = new MediatorMinimize(this, aut);
        add(getCentralComponent(),BorderLayout.CENTER);
        setLocationRelativeTo(frame);
        setVisible(mVisible);
        
    }//VisualMinimize
    
    /**
     * Construye los paneles y botones del algoritmo visual.
     * 
     * @param auto Autómata visual.
     */
    public void buildPanels () {

        if(mVisible == true){
            mScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            mScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            //mScroll.getViewport().setViewPosition(mShowing.mPoint);
        }
        
    }//buildPanels
    
	public Component getCentralComponent() {
		JPanel central = new JPanel(new BorderLayout()),
        left = new JPanel(new BorderLayout()),
        right = new JPanel(new BorderLayout());
		JScrollPane scrollTable;

		
		mScroll.getViewport().setPreferredSize(new Dimension(500, 400));
		mScroll = buildBorder(mScroll, Messages.MINIMIZED_AUTOMATON);
		left.setBorder(BorderFactory.createEmptyBorder(3, 6, 6, 6));
		left.add(mScroll);

		scrollTable = new JScrollPane(mTable);
		scrollTable = buildBorder(scrollTable, Messages.PARTITION_TABLE);
		scrollTable.setPreferredSize(new Dimension(300, 400));
		right.setBorder(BorderFactory.createEmptyBorder(3, 6, 6, 6));
		right.add(scrollTable, BorderLayout.CENTER);

		central.add(left, BorderLayout.CENTER);
		central.add(right, BorderLayout.LINE_END);

		return central;
	}

}//VisualMinimize
