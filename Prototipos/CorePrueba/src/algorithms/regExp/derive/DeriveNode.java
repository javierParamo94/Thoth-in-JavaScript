package algorithms.regExp.derive;

import algorithms.tree.Node;

/**
 * <b>Descripción</b><br>
 * Representa cada uno de los nodos del árbol sintáctico usado para el método de las
 * derivadas.
 * <p>
 * <b>Detalles</b><br>
 * Aumenta la funcionalidad del tipo abstracto de datos nodo.<br>
 * Acumula en cada nodo la expresión regular asociada al nodo.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Representación del nodo del árbol sintáctico de Derive.
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @version 1.0
 * @see DeriveTree
 * @see Node
 */public class DeriveNode extends Node {

    // Attributes --------------------------------------------------------------------
    
    /**
     * Expresión regular asociada al nodo del árbol.
     */
    private String mRegExp;
    
    // Methods -----------------------------------------------------------------------
    
    /**
     * Constructor para un nodo hoja.
     * 
     * @param c Carácter del nodo.
     */
    public DeriveNode(Character c) {
        super(c);
        mRegExp = c.toString();
        
    }//DeriveNode

    /**
     * Constructor completo para nodos.<br>
     * Como mínimo se le debe pasar el hijo izquierdo, siendo el derecho opcional.
     * 
     * @param c Carácter del nodo.
     * @param left Hijo izquierdo.
     * @param right Hijo derecho.
     */
    public DeriveNode(Character c, DeriveNode left, DeriveNode right) {
        super(c, left, right);
        
        switch(c){
            case '|':
                mRegExp = left.getRegExp() + c + right.getRegExp();
                break;
            case '.':
                if(left.isLeaf())
                    mRegExp = left.getRegExp();
                else
                    mRegExp = "(" + left.getRegExp() + ")";
                if(right.isLeaf())
                    mRegExp = mRegExp + right.getRegExp();
                else
                    mRegExp = mRegExp + "(" + right.getRegExp() + ")";
                break;
            case '*':
                if(left.isLeaf())
                    mRegExp = left.getRegExp() + c;
                else
                    mRegExp = "(" + left.getRegExp() + ")" + c;
                break;
            default:
                mRegExp = c.toString();
        } 
        
    }//DeriveNode
    
    /**
     * Devuelve la expresión regular del nodo.
     * 
     * @return Expresión regular asociada al nodo.
     */
    public String getRegExp () {
        
        return mRegExp;
    }//getRegExp

}//DeriveNode
