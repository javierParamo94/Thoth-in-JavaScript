package algorithms.regExp.derive;

import java.io.StringReader;

import algorithms.regExp.ahosethi.*;
import algorithms.regExp.parserjavacc.ParseException;
import algorithms.regExp.parserjavacc.ParserExpReg;
import algorithms.tree.Tree;

import core.TerminalEnd;
import core.finiteautomaton.FiniteAutomaton;

/**
 * <b>Descripción</b><br>
 * Representa el árbol sintáctico para el método de las derivadas.
 * <p>
 * <b>Detalles</b><br>
 * Implementa un árbol binario formado por nodos. Aumenta la funcionalidad con 
 * varias funciones de acceso a datos del árbol.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Representación del árbol sintáctico.
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @version 1.0
 * @see Tree
 * @see AhoNode
 */
public class DeriveTree extends Tree{

    // Attributes --------------------------------------------------------------------
    
    /**
     * Autómata finito asociado al árbol.
     */
    private FiniteAutomaton mAutomaton;
    
    // Methods -----------------------------------------------------------------------
    
    /**
     * Constructor del árbol.<br>
     * Debemos pasarle el nodo raíz.
     * 
     * @param n Nodo raíz.
     */
    public DeriveTree(DeriveNode n) {
        super(n);
        ParserExpReg parser = ParserExpReg.getInstance(new StringReader(getAmpRegExp()));
        try{
            AhoTree ahoTree = parser.buildAhoTree();
            AhoSethiUllman aho = new AhoSethiUllman(ahoTree);
            aho.allSteps();
            mAutomaton = aho.getSolution();
        }catch(ParseException e){}
        
    }//DeriveTree
    
    /**
     * Devuelve el nodo raíz del árbol.
     * 
     * @return Nodo raíz del árbol.
     */
    public DeriveNode getRoot () {
        
        return (DeriveNode)mRoot;
    }//getRoot
    
    /**
     * Devuelve el autómata asociado al árbol.
     * 
     * @return Autómata asociado al árbol.
     */
    public FiniteAutomaton getAutomaton () {
        
        return mAutomaton;
    }//getAutomaton
    
    /**
     * Devuelve la expresión regular ampliada del árbol.
     * 
     * @return Expresión regular ampliada.
     */
    public String getAmpRegExp () {
        
        return ((DeriveNode)mRoot).getRegExp() + TerminalEnd.END_CHARACTER;
    }//getAmpRegExp

}//DeriveTree
