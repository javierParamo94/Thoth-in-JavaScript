package algorithms.regExp.ahosethi;

import java.util.Vector;

import algorithms.tree.*;


/**
 * <b>Descripci�n</b><br>
 * Representa el �rbol sint�ctico ampliado para el algoritmo de Aho-Sethi-Ullman.
 * <p>
 * <b>Detalles</b><br>
 * Implementa un �rbol binario formado por nodos. Aumenta la funcionalidad con
 * varias funciones de acceso a datos del �rbol, como puede ser el c�lculo de la
 * siguiente-pos.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Representaci�n del �rbol sint�ctico.
 * Calculo de la funci�n siguiente-pos.
 * </p>
 * 
 * @author �lvar Arn�iz Gonz�lez, Andr�s Arn�iz Moreno
 * @version 1.0
 * @see Tree
 * @see AhoNode
 */
public class AhoTree extends Tree{

    // Methods ---------------------------------------------------------------------
	
	/**
	 * Constructor del �rbol.<br>
	 * Debemos pasarle un nodo que va a ser la ra�z del �rbol. 
	 * 
	 * @param root Nodo ra�z del �rbol.
	 */
	public AhoTree (AhoNode root) {
        super(root);
		
	}//Tree
    
    /**
     * Devuelve el nodo ra�z del �rbol.
     * 
     * @return Nodo ra�z del �rbol.
     */
    public AhoNode getRoot (){
        
        return (AhoNode)mRoot;
    }//getRoot
    
    /**
     * Devuelve las siguiente-pos de un car�cter determinado. 
     * 
     * @param car Car�cter del que queremos saber la siguiente-pos.
     * @param num Lista de posiciones de los caracteres que queremos obtener la primera-pos.
     * @return Vector con la siguiente-pos.
     */
    public Vector<Integer> nextPosition (Character car, Vector<Integer> num){
        Vector<Node> pre = preOrden();
        Vector<Integer> temp, pos = new Vector<Integer>(3, 3);
        
            //Recorremos el �rbol y si car es igual al car�cter del nodo ->
            // -> Hallamos la siguiente pos para el �ndice de ese nodo.
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
     * Devuelve las siguientes pos del �ndice de una hoja. 
     * 
     * @param i La posici�n de la que queremos saber las siguientes pos.
     * @return Las posiciones que pueden seguir a la posici�n i.
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
