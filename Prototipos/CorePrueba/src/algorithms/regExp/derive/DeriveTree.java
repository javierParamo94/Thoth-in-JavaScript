package algorithms.regExp.derive;

import java.io.StringReader;

import algorithms.regExp.ahosethi.*;
import algorithms.regExp.parserjavacc.ParseException;
import algorithms.regExp.parserjavacc.ParserExpReg;
import algorithms.tree.Tree;

import core.TerminalEnd;
import core.finiteautomaton.FiniteAutomaton;

/**
 * <b>Descripci�n</b><br>
 * Representa el �rbol sint�ctico para el m�todo de las derivadas.
 * <p>
 * <b>Detalles</b><br>
 * Implementa un �rbol binario formado por nodos. Aumenta la funcionalidad con 
 * varias funciones de acceso a datos del �rbol.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Representaci�n del �rbol sint�ctico.
 * </p>
 * 
 * @author �lvar Arn�iz Gonz�lez, Andr�s Arn�iz Moreno
 * @version 1.0
 * @see Tree
 * @see AhoNode
 */
public class DeriveTree extends Tree{

    // Attributes --------------------------------------------------------------------
    
    /**
     * Aut�mata finito asociado al �rbol.
     */
    private FiniteAutomaton mAutomaton;
    
    // Methods -----------------------------------------------------------------------
    
    /**
     * Constructor del �rbol.<br>
     * Debemos pasarle el nodo ra�z.
     * 
     * @param n Nodo ra�z.
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
     * Devuelve el nodo ra�z del �rbol.
     * 
     * @return Nodo ra�z del �rbol.
     */
    public DeriveNode getRoot () {
        
        return (DeriveNode)mRoot;
    }//getRoot
    
    /**
     * Devuelve el aut�mata asociado al �rbol.
     * 
     * @return Aut�mata asociado al �rbol.
     */
    public FiniteAutomaton getAutomaton () {
        
        return mAutomaton;
    }//getAutomaton
    
    /**
     * Devuelve la expresi�n regular ampliada del �rbol.
     * 
     * @return Expresi�n regular ampliada.
     */
    public String getAmpRegExp () {
        
        return ((DeriveNode)mRoot).getRegExp() + TerminalEnd.END_CHARACTER;
    }//getAmpRegExp

}//DeriveTree
