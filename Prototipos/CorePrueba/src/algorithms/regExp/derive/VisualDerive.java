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
 * <b>Descripci�n</b><br>
 * Visualizaci�n del algoritmo de la obtenci�n de un aut�mata finito a partir de una
 * expresi�n regular mediante el m�todo de derivadas.
 * <p>
 * <b>Detalles</b><br>
 * Visualiza la obtenci�n de un aut�mata finito.<br>
 * Ayuda a la comprensi�n del algoritmo mediante la construcci�n del aut�mata en cada
 * paso a paso y la visualizaci�n de las derivadas sucesivas.
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
     * @param frame Ventana a la que est� asociada el di�logo.
     * @param regExp Expresi�n regular en forma de �rbol.
     * @param exp Expresi�n regular introduciada por el usuario.
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
