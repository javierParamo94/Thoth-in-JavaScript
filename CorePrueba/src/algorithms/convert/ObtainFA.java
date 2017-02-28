package algorithms.convert;

import java.util.Vector;

import algorithms.Algorithm;

import core.*;
import core.finiteautomaton.FiniteAutomaton;
import core.finiteautomaton.StateFA;
import core.grammar.*;

/**
 * <b>Descripción</b><br>
 * Se encarga de transformar la gramática regular a un autómata finito.
 * <p>
 * <b>Detalles</b><br>
 * Desde la producción inicial va a ir recorriéndolas creando los estados
 * y transiciones correspondientes.<br>
 * Este algoritmo sólo transforma en autómata las gramáticas cuyas producciones
 * sean lineales a derechas y regulares.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Desarrolla el algoritmo de transformación de gramática regular a autómata finito.
 * Posibilita el paso a paso.
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @version 1.0
 */
public class ObtainFA extends Algorithm implements TypeHandler {

    // Attributes ------------------------------------------------------------------
    
    /**
     * Gramática de la cual vamos a obtener el autómata.
     */
    private Grammar mGrammar;
    
    /**
     * Producción actual que vamos a recorrer.
     */
    private Production mCurrent;
    
    //  Methods ---------------------------------------------------------------------
    
    /**
     * Constructor básico.
     * 
     * @param grammar Gramática de la cual obtendremos un autómata finito.
     */
    public ObtainFA (Grammar grammar) {
        super();
        mGrammar = grammar;
        mAutomaton = new FiniteAutomaton("FA");
                
    }//ObtainFA
    
    /**
     * Devuelve la gramática de la que estamos obteniendo el autómata.
     * 
     * @return La gramática del algoritmo
     */
    public Grammar getGrammar () {
        
        return mGrammar;
    }//getGrammar

    /**
     * Primer paso del algoritmo.<br>
     * Comprueba que la gramática sea regular y sea lineal a derechas.<br>
     * También se encarga de inicializar la producción actual.
     * 
     * @return True si se puede seguir ejecutando el algoritmo, false en caso contrario.
     */
    public boolean firstStep () {
        
        if(mGrammar.getType() == CHOMSKY || mGrammar.getType() == DEPENDENT ||
                mGrammar.getType() == INDEPENDENT || mGrammar.getSize() <= 0)
            return false;
        
            //Si no es lineal a derechas no podemos ejecutar el algoritmo
        for(Production prod : mGrammar.getProductions())
            if(prod.getRight().firstElement().getClass().equals(NonTerminal.class))
                return false;

        mCurrent = mGrammar.getProductions().firstElement();
        
        return true;
    }//firstStep

    /**
     * Siguiente paso del algoritmo.<br>
     * Creamos los estados necesarios para cada producción y la transición entre ellos.
     * 
     * @return True si se puede seguir ejecutando el algoritmo, false en caso contrario.
     */
    public boolean nextStep () {
        Vector<Symbol> left = mCurrent.getLeft(),
                       right = mCurrent.getRight();
        State temp, st;
        Terminal ter;
        int next;
        
        temp = mAutomaton.createState(left.firstElement().toString());
            //Si es el axioma le ponemos como inicial
        if(left.firstElement().equals(mGrammar.getAxiom()))
            mAutomaton.setInitialState(temp);
            //Si la parte derecha tiene sólo un terminal ó épsilon
        if(right.size()==1)
            if(right.firstElement().getClass().equals(TerminalEpsilon.class))
                temp.setFinal(true);
            else{
                ter = new Terminal(right.firstElement().toString().charAt(0));
                st = mAutomaton.createState(temp.getName()+"'", true);
                ((StateFA)st).createTransitionIn(ter, temp);
                mAutomaton.addAlphabetToken(ter);
            }
        else{
                //Si la parte derecha tiene un terminal y un no terminal
            ter = new Terminal(right.firstElement().toString().charAt(0));
            st = mAutomaton.createState(right.lastElement().toString());
            ((StateFA)st).createTransitionIn(ter, temp);
            mAutomaton.addAlphabetToken(ter);
        }
        
        next = mGrammar.getProductions().indexOf(mCurrent) + 1;
            //Si nos quedan producciones por analizar
        if(mGrammar.getSize() > next){
            mCurrent = mGrammar.getProductions().elementAt(next);
            return true;
        }
        else{
                //Algoritmo finalizado
            mAutomaton.isDeterministic(new Vector<State>());
            mCurrent = null;
            return false;
        }
        
    }//nextStep
    
    /**
     * Ejecuta todos los pasos del algoritmo.<br>
     * 
     * @return True si el algoritmo ha sido completado con éxito.
     */
    public boolean allSteps () {
        if(firstStep())
            while(nextStep());
        
        if(mAutomaton.getStates().size() > 0)
            return true;
        
        return false;
    }//allSteps

    /**
     * Devuelve el autómata creado a partir de la gramática.
     * 
     * @return Autómata resultante.
     */
    public FiniteAutomaton getSolution () {
        
        return (FiniteAutomaton)mAutomaton;
    }//getSolution
    
}//ObtainFA
