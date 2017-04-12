package src.client.core.grammar.cleaner;

import java.util.Vector;

import src.client.core.Symbol;
import src.client.core.Terminal;
import src.client.core.NonTerminal;
import src.client.core.grammar.*;

/**
 * <b>Descripción</b><br>
 * Ejecuta el algoritmo de Cocke-Younger-Kasami.
 * <p>
 * <b>Detalles</b><br>
 * Permite reconocer una palabra a través de las producciones de la gramática.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Ejecuta el algoritmo de CYK.
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @version 1.0
 */
public class CYKAlgorithm {

    // Attributes ------------------------------------------------------------------
    
    /**
     * Palabra sobre la que vamos a ejecutar el algoritmo.
     */
    private Vector<Terminal> mWord;
    
    /**
     * Gramática utilizada.
     */
    private Grammar mGrammar;
    
    /**
     * Tabla para representar los resultados del algoritmo.
     */
    private CYKTable mCYK;
    
    // Methods ---------------------------------------------------------------------
    
    /**
     * Constructor básico del algoritmo de CYK.
     * 
     * @param grammar Gramática del algoritmo.
     * @param word Palabra del algoritmo.
     */
    public CYKAlgorithm (Grammar grammar, Vector<Terminal> word) {
        mGrammar = grammar;
        mWord = word;
        mCYK = new CYKTable(mWord.size());
        
    }//CYKAlgorithm
    
    /**
     * Ejecuta todos los pasos del algoritmo.<br>
     * Rellena la tabla de CYK a partir de la gramática.
     */
    public void allSteps () {
        Vector<Vector<NonTerminal>> vN;

        first();
        for(int j=1; j<mWord.size(); j++)
            for(int i=0; i<mWord.size()-j; i++)
                for(int k=0; k<j; k++){
                    vN = getCases(mCYK.get(i, k), mCYK.get(i+k+1, j-k-1));
                    for(int l=0; l<vN.size(); l++)
                        mCYK.add(i, j, getAntecedent(vN.elementAt(l)));
                }
        
    }//allSteps
    
    /**
     * Se encarga de inicializar la primera columna de la tabla CYK.<br>
     * Busca las producciones que deriven directamente los terminales de la
     * palabra.
     */
    private void first () {
        Vector<Symbol> right;
        NonTerminal left;
        
        for(int i=0; i<mWord.size(); i++)
            for(int j=0; j<mGrammar.getSize(); j++){
                left = (NonTerminal) mGrammar.getProductions().elementAt(j).getLeft().firstElement();
                right = mGrammar.getProductions().elementAt(j).getRight();
                if(right.size() == 1 && right.firstElement().equals(mWord.elementAt(i)))
                    mCYK.add(i, 0, left);
            }
    }//first
    
    /**
     * Devuelve el producto cartesiano de ambos vectores.<br>
     * Si alguno de los vectores está vacío no introducirá nada.
     * 
     * @param v1 Primer vector del producto cartesiano.
     * @param v2 Segundo vector del producto cartesiano.
     * @return Producto cartesiano.
     */
    private Vector<Vector<NonTerminal>> getCases (Vector<NonTerminal> v1, Vector<NonTerminal> v2) {
        Vector<Vector<NonTerminal>> solution = new Vector<Vector<NonTerminal>>(v1.size()*v2.size(), 0);
        Vector<NonTerminal> temp;
        
        for(int i=0; i<v1.size(); i++)
            for(int j=0; j<v2.size(); j++){
                temp = new Vector<NonTerminal>(2, 0);
                temp.add(0, v1.elementAt(i));
                temp.add(1, v2.elementAt(j));
                solution.add(temp);
            }
        
        return solution;
    }//getCases
    
    /**
     * Devuelve todos los no terminales que puedan derivar en los no terminales que
     * se le pasan.<br>
     * Puede que existan varias producciones que deriven lo mismo, se devolverá el
     * antecedente de cada una de ellas. No se admitirán duplicados.
     * 
     * @param v Consecuente que buscamos en las producciones.
     * @return Antecedentes de las producciones encontradas.
     */
    private Vector<NonTerminal> getAntecedent (Vector<NonTerminal> v) {
        Vector<Symbol> right;
        NonTerminal left;
        Vector<NonTerminal> solution = new Vector<NonTerminal>(3, 3);
        
        for(int i=0; i<mGrammar.getSize(); i++){
            left = (NonTerminal) mGrammar.getProductions().elementAt(i).getLeft().firstElement();
            right = mGrammar.getProductions().elementAt(i).getRight();
            if(right.equals(v) && !solution.contains(left))
                solution.add(left);
        }
        
        return solution;
    }//getAntecedent    
    
    /**
     * Devuelve la tabla ya construida del algoritmo CYK.
     * 
     * @return CYKTable con la solución.
     */
    public CYKTable getTable () {
        
        return mCYK;
    }//getTable
    
    /**
     * Devuelve verdadero si la palabra puede ser producida por la gramática.<br>
     * Será reconocida si la última posición de la tabla contiene el axioma de la
     * gramática.
     * 
     * @return True si es reconocida y false en caso contrario.
     */
    public boolean getAccept () {
        if(mCYK.get(0, mWord.size()-1).contains(mGrammar.getAxiom()))
            return true;
        
        return false;
    }//getAccept

}//CYKAlgorithm
