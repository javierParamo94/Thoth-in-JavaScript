package algorithms.regExp.ahosethi;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import javax.swing.JComponent;


import view.utils.Colors;

/**
 * <b>Descripción</b><br>
 * Dibuja un árbol de derivación sintáctico. 
 * <p>
 * <b>Detalles</b><br>
 * Dibuja un árbol binario con las primera-pos y última-pos de cada nodo del
 * árbol sintáctico.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Dibujar un árbol binario.
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @version 1.0
 */
public class ShowingTree extends JComponent {
    
    // Attributes -----------------------------------------------------------------
    
    /**
     * Árbol asociado al área de dibujo.
     */
    private AhoTree mTree;
    
    /**
     * Algoritmo que va a construir el autómata.
     */
    private AhoSethiUllman mAlgorithm; 
    
    /**
     * Ancho de cada nodo.
     */
    private final int mWeidth = 25;
    
    /**
     * Alto de cada nodo.
     */
    private final int mHeight = 80;
    
    // Methods --------------------------------------------------------------------
    
    /**
     * Constructor para el panel de dibujar un árbol sintáctico para el
     * algoritmo de Aho-Sethi-Ullman.
     * 
     * @param tree Árbol de Aho-Sethi-Ullman que va a ser mostrado por pantalla.
     */
    public ShowingTree (AhoTree tree) {
        super();
        
        mAlgorithm = new AhoSethiUllman(tree);
        mAlgorithm.allSteps();
        mTree = mAlgorithm.getTree();
        setPreferredSize(new Dimension(6000, 6000));
        
        repaint();
    }//ShowingTree
    
    /**
     * Es llamada cuando se debe redibujar el lienzo.
     * 
     * @param g Gráficos a pintar.
     */
    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        update(g);
        
    }//paintComponent
    
    /**
     * Actualiza los gráficos (el árbol).
     * 
     * @param g Gráficos a actualizar.
     */
    public void update (Graphics g) {
        Graphics2D ourGraphics = (Graphics2D) g;
        
        ourGraphics.setColor(Colors.WHITE);
        ourGraphics.clearRect(0, 0, getWidth(), getHeight());
        ourGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        ourGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        g = draw(ourGraphics);
        
    }//update
    
    /**
     * Dibuja el árbol completo.<br>
     * Invoca a la función recursiva drawNode que dibuja cada nodo.
     * 
     * @param g Gráfico donde se va a dibujar.
     * @return Gráfico con el árbol ya dibujado.
     */
    public Graphics2D draw (Graphics2D g) {
            
        return drawNode(g, (AhoNode)mTree.getRoot(),
                6000 - mTree.getRoot().getMaxLeaf() * (mWeidth + calculateSize(mTree.getRoot()) * 6) - 50, 50);
    }//draw
    
    /**
     * Función recursiva que se encarga de dibujar un nodo y a sus hijos.
     * 
     * @param g Gráfico donde se va a dibujar
     * @param node Nodo que debe dibujarse
     * @param x Posición donde aparecerá
     * @param y Posición donde aparecerá.
     * @return Gráfico con el nodo dibujado.
     */
    public Graphics2D drawNode (Graphics2D g, AhoNode node, int x, int y) {
        Ellipse2D circle = new Ellipse2D.Float(x-15, y-15, 30, 30);
        String temp;
        int letter = 8;
        if(node.getRight() != null){
            g.setStroke(new BasicStroke(1f));
            int size = calculateSize(node);
            g.setColor(Colors.BLACK);
            g.drawLine(x, y + 20,  x - node.getMaxLeaf() * (mWeidth + size * 6), y + mHeight - 20);
            g.drawLine(x, y + 20,  x + node.getMaxLeaf() * (mWeidth + size * 6), y + mHeight - 20);
            g = drawNode(g, (AhoNode)node.getLeft(), x - node.getMaxLeaf() * (mWeidth + size * 6), y + mHeight);
            g = drawNode(g, (AhoNode)node.getRight(), x + node.getMaxLeaf() * (mWeidth + size * 6), y + mHeight);
        }
        else{
            if(node.getLeft() != null){
                g.setStroke(new BasicStroke(0.5f));
                g.setColor(Colors.BLACK);
                g.drawLine(x, y + 20,  x, y + mHeight - 20);
                g = drawNode(g, (AhoNode)node.getLeft(), x,  y + mHeight);
            }
        }
        g.setStroke(new BasicStroke(1f));
        g.setColor(Colors.WHITE);
        g.fill(circle);
        g.setColor(Colors.BLACK);
        if(node.isCancel()){
            g.setColor(Colors.RED);
            g.draw(circle);
            letter = 20;
        }
        g.setFont(new Font("Comics Sans", Font.ROMAN_BASELINE, 16));
        switch(node.getToken()){
            case '*':
                g.drawString("*", x - 2, y + 10);
                break;
            case '|':
                g.drawString("|", x - 2, y + 5);
                break;
            case '.':
                g.setFont(new Font("Comics Sans", Font.BOLD, 25));
                g.drawString("·", x - 3, y + 10);
                break;
            default:
                g.drawString(((Character)node.getToken()).toString(), x - 5, y + 8);
        }
        g.setFont(new Font("Lucide Console", Font.CENTER_BASELINE, 10));
        g.setColor(Colors.green());
        temp = node.getFirst().toString().substring(1, node.getFirst().toString().length() - 1);
        g.drawString(temp, x + 4 - letter - 10.75f * node.getFirst().size(), y + 5);
        g.setColor(Colors.blue());
        temp = node.getLast().toString().substring(1, node.getLast().toString().length() - 1);
        g.drawString(temp, x + letter, y + 5);

        return g;
    }//drawNode
    
    /**
     * Calcula el tamaño máximo de los caracteres que van a acompañar a cada nodo,
     * es decir, la primera posición y la última posición.
     * 
     * @param node Nodo del cual queremos obtener el tamaño de las etiquetas.
     * @return Numero máximo de etiquetas que acompañaran a un nodo.
     */
    private int calculateSize (AhoNode node) {
        int size = 0;

        size = ((AhoNode)node.getLeft()).getFirst().size();
        if(((AhoNode)node.getLeft()).getLast().size() > size)
            size = ((AhoNode)node.getLeft()).getLast().size();
        if(((AhoNode)node.getRight()).getFirst().size() > size)
            size = ((AhoNode)node.getRight()).getFirst().size();
        if(((AhoNode)node.getRight()).getLast().size() > size)
        size = ((AhoNode)node.getRight()).getLast().size();
            
        return size;
    }//calculateSize
    
}//ShowingTree
