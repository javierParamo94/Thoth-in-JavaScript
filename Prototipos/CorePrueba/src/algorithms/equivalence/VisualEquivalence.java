package algorithms.equivalence;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.jgraph.JGraph;
import org.jgraph.graph.GraphLayoutCache;

import algorithms.VisualAlgorithm;

import core.finiteautomaton.FiniteAutomaton;

import view.utils.Messages;
import view2.controller.CellsCreatorHelper;

/**
 * <b>Descripción</b><br>
 * Visualización del algoritmo de equivalencia.
 * <p>
 * <b>Detalles</b><br>
 * Visualiza el algoritmo de equivalencia de autómatas finitos.<br>
 * Ayuda a la comprensión del algoritmo mediante la iluminación de las parejas de
 * estados y de su anotación en una tabla.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Visualiza el algoritmo de equivalencia de autómatas.<br>
 * Posibilita el paso a paso.
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @version 1.0
 */
public class VisualEquivalence extends VisualAlgorithm {
    
    // Attributes -----------------------------------------------------------------
    
    /**
     * Panel que contiene el autómata visual 1.<br>
     * Este va a ser redibujado y escalado para que el usuario vea
     * todo el contenido del autómata.
     */
    public JScrollPane mScrollA;
    
    
    /**
     * Panel que contiene el autómata visual 2.<br>
     * Este va a ser redibujado y escalado para que el usuario vea
     * todo el contenido del autómata.
     */
    public JScrollPane mScrollB;
    
    /**
     * Panel donde vamos a mostrar el segundo autómata a comparar.
     */
    private JComponent mShowingB;
    
    /**
     * Tabla donde se indica la equivalencia de autómatas.
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
     * @param autoA Autómata visual que va a compararse.
     * @param autoB Autómata visual que va a compararse.
     */
    public VisualEquivalence (Frame frame, GraphLayoutCache gCacheA,FiniteAutomaton autA,GraphLayoutCache gCacheB, FiniteAutomaton autB) {
        super(Messages.EQUIVALENCE, true);
        setSize(new Dimension(750, 550));
        mVisible = true;
        mMediator = new MediatorEquivalence(this, gCacheA,autA,gCacheB, autB);
        JGraph graphA = new JGraph(gCacheA);
        CellsCreatorHelper.scaleAndCenterGraph(graphA, new Dimension(360, 275));
        graphA.setEnabled(false);
        graphA.setAntiAliased(true);
        setShowingPane(graphA);
        JGraph graphB = new JGraph(gCacheB);
        CellsCreatorHelper.scaleAndCenterGraph(graphB, new Dimension(360, 275));
        graphB.setEnabled(false);
        graphB.setAntiAliased(true);
        mShowingB = graphB;
        buildPanels(autA,autB);
        setLocationRelativeTo(frame);
        setVisible(true);
        
    }//VisualEquivalence
    
    /**
     * Construye los paneles y botones del algoritmo visual.
     */
    public void buildPanels (FiniteAutomaton autA, FiniteAutomaton autB) {
        JPanel central = new JPanel(new BorderLayout()),
               up = new JPanel(new GridLayout(1, 2 , 0, 0)),
               upA = new JPanel(new BorderLayout()),
               upB = new JPanel(new BorderLayout()),
               down = new JPanel(new BorderLayout());
        JScrollPane scrollTable;
        
        mScrollA = new JScrollPane(mShowing);
        mScrollA = buildBorder(mScrollA, autA.getName());
        upA.setBorder(BorderFactory.createEmptyBorder(3, 6, 6, 6));
        upA.add(mScrollA);
        mScrollB = new JScrollPane(mShowingB);
        mScrollB = buildBorder(mScrollB, autB.getName());
        upB.setBorder(BorderFactory.createEmptyBorder(3, 6, 6, 6));
        upB.add(mScrollB);
        up.add(upA);
        up.add(upB);
        
        scrollTable = new JScrollPane(mTable);
        scrollTable = buildBorder(scrollTable, Messages.PAIR_STATES);
        scrollTable.setPreferredSize(new Dimension(150, 150));
        down.setBorder(BorderFactory.createEmptyBorder(3, 6, 6, 6));
        down.add(scrollTable, BorderLayout.CENTER);
        
        central.add(up, BorderLayout.CENTER);
        central.add(down, BorderLayout.PAGE_END);
        
        add(central, BorderLayout.CENTER);
        
        mScrollA.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mScrollA.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        //mScrollA.getViewport().setViewPosition(mShowingA.mPoint);
        mScrollB.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mScrollB.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        //mScrollB.getViewport().setViewPosition(mShowingB.mPoint);
        
    }//buildPanels
    
    /**
     * Asigna el componente que se va a dibujar en el segundo de los paneles
     * en los que se muestran los autómatas a comparar.
     * 
     * @param c
     */
    public void setSecondShowingPane(JComponent c){
    	mShowingB = c;
    	mScrollB.setViewportView(c);
    }
    
}//VisualEquivalence
