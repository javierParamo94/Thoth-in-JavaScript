package algorithms.convert;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;

import algorithms.VisualAlgorithm;

import core.grammar.Grammar;
import view.utils.Messages;

/**
 * <b>Descripción</b><br>
 * Visualización del algoritmo de la obtención de un autómata finito a partir de una
 * gramática regular.
 * <p>
 * <b>Detalles</b><br>
 * Visualiza la obtención de un autómata finito.<br>
 * Ayuda a la comprensión del algoritmo mediante la iluminación de las producciones
 * a medida que se crean los estados.
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
public class VisualObtainFA extends VisualAlgorithm {
    
    // Attributes -----------------------------------------------------------------
    
    /**
     * Cuadro de texto donde se muestra la gramática.
     */
    public JTextPane mTextGrammar;
    
    /**
     * Determina si va a ser mostrado o no el panel.
     */
    public boolean mVisible;
    
    // Methods --------------------------------------------------------------------
        
    /**
     * Constructor de la pantalla que va a mostrar el método de obtener un
     * autómata finito a partir de una gramática regular.
     * 
     * @param frame Frame al que está asociado el diálogo.
     * @param gra Gramática sobre la que se va a aplicar el algoritmo.
     */
    public VisualObtainFA (Frame frame, Grammar gra) {
        super(Messages.OBTAIN_FA, true);
        mTextGrammar = new JTextPane();
        mVisible = true;
        mMediator = new MediatorObtainFA(this, gra);
        setSize(new Dimension(750, 550));
        setLocationRelativeTo(frame);
        add(getCentralComponent(),BorderLayout.CENTER);
        setVisible(mVisible);
        
    }//VisualObtainFA

	public Component getCentralComponent() {
		JSplitPane panel;
        JPanel left = new JPanel(new BorderLayout()),
               right = new JPanel(new BorderLayout());
        JScrollPane scrollGrammar;
        
        mScroll = buildBorder(mScroll, Messages.NEW_AUTOMATON);
        left.setBorder(BorderFactory.createEmptyBorder(3, 6, 6, 6));
        left.add(mScroll);
        
        mTextGrammar.setEditable(false);
        mTextGrammar.setFont(new Font("Comic Sans MS negrita", Font.BOLD, 12));
        scrollGrammar = new JScrollPane(mTextGrammar);
        scrollGrammar = buildBorder(scrollGrammar, Messages.GRAMMAR);
        right.setBorder(BorderFactory.createEmptyBorder(3, 6, 6, 6));
        right.add(scrollGrammar, BorderLayout.CENTER);
        
        panel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, right);
        panel.setDividerLocation(getWidth() * 3/4);
        return panel;
	}

}//VisualObtainFA
