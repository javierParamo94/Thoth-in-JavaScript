package core.grammar.tasp;

import java.util.Vector;

import core.*;
import core.grammar.*;

/**
 * <b>Descripción</b><br>
 * Crea la tabla de análisis sintáctico predictivo (TASP).
 * <p>
 * <b>Detalles</b><br>
 * A partir del first y el follow construye la tabla TASP.<br>
 * Ésta tabla es necesaria para simular el funcionamiento de un autómata de pila a
 * partir de una gramática independiente del contexto.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Implementa la funcionalidad de la tabla TASP.
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @version 1.0
 * @see FirstFollow
 */
public class TaspTable{

    // Attributes ------------------------------------------------------------------
    
    /**
     * Algoritmo FirstFollow de donde recoge los datos.
     */
    private FirstFollow mFirstFollow;

    /**
     * Gramática sobre la que se va a calcular el first.
     */
    private Grammar mGrammar;
    
    /**
     * Lista con los terminales que componen el first para cada no terminal.
     */
    private Vector<Vector<Terminal>> mFirst;
    
    /**
     * Lista con los terminales que componen el follow para cada no terminal.
     */
    private Vector<Vector<Terminal>> mFollow;
    
    /**
     * Producciones que van a formar la tabla tasp.
     */
    private Production mTasp[][];
    
    /**
     * Lista con los terminales de la gramática.
     */
    private Vector<Terminal> mTerminals;
    
    /**
     * Lista con los no terminales de la gramática.
     */
    private Vector<NonTerminal> mNonTerminals;
    
    // Methods ---------------------------------------------------------------------
    
    /**
     * Constructor básico para la tabla TASP.
     * 
     * @param firstFollow Algoritmo de cálculo de first y follow sobre el que se va
     * a calcular la tabla TASP.
     */
    public TaspTable (FirstFollow firstFollow) {
        mFirstFollow = firstFollow;
        mGrammar = firstFollow.getGrammar();
        
        mTerminals = new Vector<Terminal>();
        mTerminals.add(new TerminalEnd());
        
        for(Terminal ter : mGrammar.getTerminals())
            mTerminals.add(ter);
        mTerminals.remove(new TerminalEpsilon());
        
        mNonTerminals = mGrammar.getNonTerminals();
        mTasp= new Production[mTerminals.size()][mNonTerminals.size()];
        
    }//TaspTable
    
    /**
     * Devuelve los terminales de la TASP.
     * 
     * @return Terminales.
     */
    public Vector<Terminal> getTerminals () {
        
        return mTerminals;
    }//getTerminals
    
    /**
     * Devuelve los no terminales de la TASP.
     * 
     * @return No terminales.
     */
    public Vector<NonTerminal> getNonTerminals () {
        
        return mNonTerminals;
    }//getNonTerminals
    
    /**
     * Calcula la tabla tasp a partir del first y el follow.<br>
     * Si no se han podido obtener se detendrá la construcción de la tabla.
     * 
     * @return False si no se ha podido obtener el first y el follow necesarios
     * para obtener la TASP o si la TASP no se ha podido calcular (Gramática ambigua)
     * en otro caso devuelve true.
     */
    public boolean calculateTasp () {
        Vector<Terminal> uConcat;
        Vector<Symbol> right;
        int i, pos;
        
        if(mFirstFollow.getFirst() == null || mFirstFollow.getFollow() == null)
            return false;
        
        mFirst = mFirstFollow.getFirst();
        mFollow = mFirstFollow.getFollow();
        	//Recorremos cada produccion
        for(Production prod : mGrammar.getProductions()){
        	uConcat = new Vector<Terminal>(5, 5);
        	right = prod.getRight();
        	
        		//Calculamos el FIRST de la parte derecha de la produccion
        	i=0;
        	do{
        		uConcat.remove(new TerminalEpsilon());
        			//Si nos encontramos un terminal ya no hace falta seguir buscando
        		if(right.elementAt(i).getClass().equals(Terminal.class) ||
        				right.elementAt(i).getClass().equals(TerminalEpsilon.class)) {
        			uConcat.remove((Terminal)right.elementAt(i));
        			uConcat.add((Terminal)right.elementAt(i));
        			break;
        		}
        		pos = mNonTerminals.indexOf(right.elementAt(i));
        		uConcat.removeAll(mFirst.elementAt(pos));
        		uConcat.addAll(mFirst.elementAt(pos));
        		i++;
        	} while(i<right.size() && uConcat.contains(new TerminalEpsilon()));
        	
        		//Almacenamos la posición del no terminal antecedente de la producción
        	pos = mNonTerminals.indexOf(prod.getLeft().firstElement());
        		//Si el valor anterior contiene la cadena vacía debemos añadir el FOLLOW del antecedente
        	if(uConcat.contains(new TerminalEpsilon())){
        		uConcat.remove(new TerminalEpsilon());
        		uConcat.removeAll(mFollow.elementAt(pos));
        		uConcat.addAll(mFollow.elementAt(pos));
        	}
        		//Se añade la produccion en las celdas de la TASP que sea necesario
        		//Si ya existía un valor en esa celda la gramática será ambigüa
        	for(int j=0, posTerminal; j<uConcat.size(); j++){
        		posTerminal = mTerminals.indexOf(uConcat.elementAt(j));
        		if(mTasp[posTerminal][pos] == null)
                    mTasp[posTerminal][pos] = prod;
                else    //La gramática es ambigua
                    return false;
        	}
        }
        return true;
    }//calculateTasp
    
    /**
     * Devuelve la gramática de la que se ha construído la tabla TASP.
     * 
     * @return Gramática a partir de la cual se ha construído la TASP.
     */
    public Grammar getGrammar () {
        
        return mGrammar;
    }//getGrammar
    
    /**
     * Devuelve la tabla TASP construida.
     * 
     * @return Tabla TASP.
     */
    public Production[][] getSolution () {
        
        return mTasp;
    }//getSolution
    
}//TaspTable
