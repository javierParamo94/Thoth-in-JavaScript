package algorithms.regExp.ahosethi;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;

import algorithms.VisualAlgorithm;

import view.application.Application;
import view.utils.Messages;

/**
 * <b>Descripci�n</b><br>
 * Visualizaci�n del algoritmo de la obtenci�n de un aut�mata finito a partir de una
 * expresi�n regular mediante el m�todo de Aho-Sethi-Ullman.
 * <p>
 * <b>Detalles</b><br>
 * Visualiza la obtenci�n de un aut�mata finito.<br>
 * Ayuda a la comprensi�n del algoritmo mediante la construcci�n del �rbol con la 
 * expresi�n regular expandida.
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
public class VisualAho extends VisualAlgorithm implements MouseListener, MouseMotionListener{
    
    // Attributes -----------------------------------------------------------------
    
    
    /**
     * Representa el punto sobre el que ha pulsado el usuario.<br>
     * Se utiliza para mover la posici�n del panel.
     */
    public Point mPoint;
    
    /**
     * Tabla donde se indica las particiones del m�todo de minimizaci�n.
     */
    public JTable mTable1;
    
    /**
     * Tabla donde aparecer� la funci�n de transici�n del nuevo aut�mata. 
     */
    public JTable mTable2;
    
    /**
     * Determina si va a ser mostrado o no el panel.
     */
    public boolean mVisible = true;
    
    // Methods --------------------------------------------------------------------
        
    /**
     * Constructor de la pantalla que va a mostrar el m�todo de Aho-Sethi-Ullman.
     * 
     * @param frame Ventana a la que est� asociada el panel.
     * @param tree �rbol construido por el parser de JavaCC.
     * @param exp Expresi�n regular introducida por el usuario.
     */
    public VisualAho (AhoTree tree, String exp) {
        super(Messages.AHOSETHI,true);
        setShowingPane(new ShowingTree(tree));
        mShowing.addMouseListener(this);
        mShowing.addMouseMotionListener(this);
        mMediator = new MediatorAho(this, tree, exp);
        setSize(Application.getInstance().getSize());
        setLocationRelativeTo(Application.getInstance());
        add(getCentralComponent(),BorderLayout.CENTER);
        setVisible(mVisible);
    
    }//VisualAho

    public void mouseClicked(MouseEvent arg0) { }

    /**
     * M�todo que es llamado cuando se presiona un bot�n del rat�n.
     * 
     * @param arg0 Evento del rat�n.
     */
    public void mousePressed (MouseEvent arg0) {
        mPoint = arg0.getPoint();
        
    }//mousePressed

    public void mouseReleased(MouseEvent arg0) { }

    /**
     * M�todo que es llamado cuando se entra en el panel.
     */
    public void mouseEntered(MouseEvent arg0) {}

    public void mouseExited(MouseEvent arg0) { }
    
    /**
     * M�todo que es llamado cuando se arrastra con el rat�n.<br>
     * Se encarga de arrastrar la vista cuando se arrastra el rat�n.
     * 
     * @param arg0 Evento del rat�n.
     */
    public void mouseDragged(MouseEvent arg0) {
        int x = (int)(arg0.getX() - mPoint.getX());
        int y = (int)(arg0.getY() - mPoint.getY());
        int height = mScroll.getViewport().getView().getHeight();
        int weight = mScroll.getViewport().getView().getWidth();
        Point temp = new Point((int)(mScroll.getViewport().getViewPosition().getX() - x),
                               (int)(mScroll.getViewport().getViewPosition().getY() - y));
        
        if(temp.getX() > weight - mScroll.getViewport().getSize().getWidth())
            temp.x = (int)(weight - mScroll.getViewport().getSize().getWidth());
        
        else if (temp.getX() < 0)
            temp.x = 0;
        
        if(temp.getY() > height - mScroll.getViewport().getHeight())
            temp.y = (int)(height - mScroll.getViewport().getHeight());
        
        else if (temp.getY() < 0)
            temp.y = 0;
        
        mScroll.getViewport().setViewPosition(temp);
        
    }//mouseDragged

    public void mouseMoved (MouseEvent arg0) { }

	public Component getCentralComponent() {
		JSplitPane panel;
        JPanel left = new JPanel(new BorderLayout()),
               right = new JPanel(new BorderLayout());
        JScrollPane scrollTable1, scrollTable2;
        
        mScroll = buildBorder(mScroll, Messages.AHO_TREE);
        mScroll.setSize(6000, 6000);
        mScroll.getViewport().setViewPosition(new Point(6000, 0));
        left.setBorder(BorderFactory.createEmptyBorder(3, 6, 6, 6));
        left.add(mScroll);
        
        scrollTable1 = new JScrollPane(mTable1);
        scrollTable1 = buildBorder(scrollTable1, Messages.NEXT_POS);
        scrollTable1.setPreferredSize(new Dimension(300, 200));
        scrollTable2 = new JScrollPane(mTable2);
        scrollTable2 = buildBorder(scrollTable2, Messages.TRANS_FUN);
        scrollTable2.setPreferredSize(new Dimension(300, 500));
        right.setBorder(BorderFactory.createEmptyBorder(3, 6, 6, 6));
        right.add(scrollTable1, BorderLayout.PAGE_START);
        right.add(scrollTable2, BorderLayout.CENTER);
        
        panel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, right);
        panel.setDividerLocation(getWidth() * 3/5);
        return panel;
	}
    
}//VisualAho
