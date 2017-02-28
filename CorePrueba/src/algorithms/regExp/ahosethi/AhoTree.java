package algorithms.regExp.ahosethi;

import java.util.Vector;

import algorithms.tree.*;


/**
 * <b>Descripción</b><br>
 * Representa el árbol sintáctico ampliado para el algoritmo de Aho-Sethi-Ullman.
 * <p>
 * <b>Detalles</b><br>
 * Implementa un árbol binario formado por nodos. Aumenta la funcionalidad con
 * varias funciones de acceso a datos del árbol, como puede ser el cálculo de la
 * siguiente-pos.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Representación del árbol sintáctico.
 * Calculo de la función siguiente-pos.
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @version 1.0
 * @see Tree
 * @see AhoNode
 */
public class AhoTree extends Tree{

    // Methods ---------------------------------------------------------------------
	
	/**
	 * Constructor del árbol.<br>
	 * Debemos pasarle un nodo que va a ser la raíz del árbol. 
	 * 
	 * @param root Nodo raíz del árbol.
	 */
	public AhoTree (AhoNode root) {
        super(root);
		
	}//Tree
    
    /**
     * Devuelve el nodo raíz del árbol.
     * 
     * @return Nodo raíz del árbol.
     */
    public AhoNode getRoot (){
        
        return (AhoNode)mRoot;
    }//getRoot
    
    /**
     * Devuelve las siguiente-pos de un carácter determinado. 
     * 
     * @param car Carácter del que queremos saber la siguiente-pos.
     * @param num Lista de posiciones de los caracteres que queremos obtener la primera-pos.
     * @return Vector con la siguiente-pos.
     */
    public Vector<Integer> nextPosition (Character car, Vector<Integer> num){
        Vector<Node> pre = preOrden();
        Vector<Integer> temp, pos = new Vector<Integer>(3, 3);
        
            //Recorremos el árbol y si car es igual al carácter del nodo ->
            // -> Hallamos la siguiente pos para el índice de ese nodo.
        for (int i=0; i<pre.size(); i++)
            if(pre.elementAt(i).getToken().equals(car) && 
                    num.contains(((AhoNode)pre.elementAt(i)).getFirst().firstElement())){
                temp = nextPos(((AhoNode)pre.elementAt(i)).getFirst().firstElement());
                pos.removeAll(temp);
                pos.addAll(temp);
            }

        return pos;
    }//nextPosition
    
    /**
     * Devuelve las siguientes pos del índice de una hoja. 
     * 
     * @param i La posición de la que queremos saber las siguientes pos.
     * @return Las posiciones que pueden seguir a la posición i.
     */
    private Vector<Integer> nextPos (Integer pos){
        Vector<Node> pre = preOrden();
        Vector<Integer> temp, positions = new Vector<Integer>(3, 3);
        
        for (int i=0; i<pre.size(); i++)
            if(!pre.elementAt(i).isLeaf() && 
                    ((AhoNode)pre.elementAt(i).getLeft()).getLast().contains(pos))
                switch(pre.elementAt(i).getToken()){
                    case '.':
                        temp = ((AhoNode)pre.elementAt(i).getRight()).getFirst();
                        positions.removeAll(temp);
                        positions.addAll(temp);
                        break;
                    case '*':
                        temp = ((AhoNode)pre.elementAt(i).getLeft()).getFirst();
                        positions.removeAll(temp);
                        positions.addAll(temp);
                        break;
                }
        
        return positions;
    }//nextPos

}//AhoTree
