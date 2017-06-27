package algorithms.regExp.derive;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import algorithms.VisualAlgorithm;

import view.application.Application;
import view.utils.Messages;

/**
 * <b>Descripción</b><br>
 * Visualización del algoritmo de la obtención de un autómata finito a partir de una
 * expresión regular mediante el método de derivadas.
 * <p>
 * <b>Detalles</b><br>
 * Visualiza la obtención de un autómata finito.<br>
 * Ayuda a la comprensión del algoritmo mediante la construcción del autómata en cada
 * paso a paso y la visualización de las derivadas sucesivas.
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
public class VisualDerive extends VisualAlgorithm {
    
    // Attributes -----------------------------------------------------------------
    
    /**
     * Donde vamos a ir mostrando las sucesivas derivadas.
     */
    public JTextArea mInfo;
    
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
     * @param regExp Expresión regular en forma de árbol.
     * @param exp Expresión regular introduciada por el usuario.
     */
    public VisualDerive (DeriveTree regExp, String exp) {
        super(Messages.DERIVE, true);
        setSize(new Dimension(750, 550));
        mVisible = true;
        setLocationRelativeTo(Application.getInstance());
        mMediator = new MediatorDerive(this, regExp, exp);
        add(getCentralComponent(),BorderLayout.CENTER);
        setVisible(mVisible);
        
    }//VisualDerive
    

	public Component getCentralComponent() {
		JPanel central = new JPanel(new BorderLayout()),
		up = new JPanel(new BorderLayout()),
		down = new JPanel(new BorderLayout());
		JScrollPane scrollInfo;

		mScroll = buildBorder(mScroll, Messages.NEW_AUTOMATON + ":");
		up.setBorder(BorderFactory.createEmptyBorder(3, 6, 6, 6));
		up.add(mScroll);

		mInfo = new JTextArea();
		mInfo.setEditable(false);
		scrollInfo = new JScrollPane(mInfo);
		scrollInfo = buildBorder(scrollInfo, Messages.DERIVE_INFO + ":");
		scrollInfo.setPreferredSize(new Dimension(150, 150));
		down.setBorder(BorderFactory.createEmptyBorder(3, 6, 6, 6));
		down.add(scrollInfo, BorderLayout.CENTER);

		central.add(up, BorderLayout.CENTER);
		central.add(down, BorderLayout.PAGE_END);

		return central;
	}

}//VisualDerive
