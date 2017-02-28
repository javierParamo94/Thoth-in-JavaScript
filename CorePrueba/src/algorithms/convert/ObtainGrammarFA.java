package algorithms.convert;

import java.util.Vector;

import algorithms.Algorithm;
import algorithms.InitializeException;

import core.*;
import core.grammar.Grammar;

/**
 * <b>Descripción</b><br>
 * Se encarga de transformar un autómata finito a una gramática.
 * <p>
 * <b>Detalles</b><br>
 * Desde el estado inicial va a ir recorriendo todos los estados y por cada transición
 * va a crear una producción en la gramática.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Desarrolla el algoritmo de transformación de autómata finto a gramática.<br>
 * Posibilita el paso a paso.
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @version 1.0
 */
public class ObtainGrammarFA extends Algorithm {

    // Attributes ------------------------------------------------------------------
    
    /**
     * Gramática que va a ser creada a partir del autómata finito.
     */
    private Grammar mGrammar;
    
    /**
     * Caracter que designa al axioma de la gramática.
     */
    private final Character AXIOM = 'S';
    
    /**
     * Caracter que designa la letra por la que empezará cada no terminal;
     */
    private final Character NON_TERMINAL = 'P';
    
    /**
     * Almacena los estados por los que va a ir pasando el autómata para construir la
     * gramática.<br>
     * Esta lista se va actualizando segun se va recorriendo el autómata.
     */
    private Vector<State> mStates;
    
    /**
     * Indica la posición del estado actual.
     */
    private int mCount;
    
    //  Methods ---------------------------------------------------------------------
    
    /**
     * Constructor básico.
     * 
     * @param automaton Autómata finito del que vamos a obtener la gramática.
     * @throws Lanza InitializeException cuando el autómata del que queremos obtener
     * la gramática no tiene estado inicial y/o final.
     */
    public ObtainGrammarFA(Automaton automaton) throws InitializeException {
        super(automaton);
        mGrammar = new Grammar();
        mStates = new Vector<State>();
        mCount = 0;
        
    }//ObtainGrammarFA

    /**
     * Primer paso del algoritmo.<br>
     * Etiqueta cada estado con el no terminal asociado a él.
     * 
     * @return True si se puede seguir ejecutando el algoritmo, false en caso contrario.
     */
    public boolean firstStep () {
            //Inicializa la lista de estados recorridos con el inicial
        mStates.add(mAutomaton.getCurrentStates().firstElement());
        
        for(State st : mAutomaton.getStates()){
                //Etiquetamos el estado inicial (Axioma)
            if(st.equals(mAutomaton.getInitialState()))
                mAutomaton.getInitialState().setLabel(AXIOM.toString());
            else    //Etiquetamos los demás estados empezando por A1
                st.setLabel(NON_TERMINAL.toString() + st.getName());
        }//for

        return true;
    }//firstStep

    /**
     * Siguiente paso del algoritmo.<br>
     * Recorre las transiciones del estado actual creando una producción por cada una
     * de ellas.<br>
     * En caso de que el estado sea final creamos una producción compresora, es decir,
     * una producción cuyo consecuente es épsilon. 
     * 
     * @return True si se puede seguir ejecutando el algoritmo, false en caso contrario.
     */
    public boolean nextStep () {
        Vector<Symbol> left = new Vector<Symbol>(1, 0),
                       right = new Vector<Symbol>(2, 0);
        State current = mStates.elementAt(mCount);
        
            //Creamos el nombre del no terminal, es decir, el antecedente
        left.add(new NonTerminal(current.getLabel()));
            //Creamos las producciones
        for(Transition trans : current.getTransitionsOut()){
            right = new Vector<Symbol>(2, 0);
                //Añadimos el teminal si no es épsilon
            if(!trans.getIn().equals(new TerminalEpsilon()))
                right.add(trans.getIn());
                //Creamos el no terminal y le añadimos
            right.add(new NonTerminal(trans.getNextState().getLabel()));
                //Creamos la producción
            mGrammar.createProduction(left, right);
            
            if(!mStates.contains(trans.getNextState()))
                mStates.add(trans.getNextState());
        }
            //Si es final añadimos una producción épsilon
        if(current.isFinal())
            mGrammar.createProduction(left);
        
            //El estado actual pasa a ser el siguiente del vector            
        if(mCount + 1 == mStates.size()){
                //Si no quedan más estados que recorrer retornamos false
            mGrammar.setAxiom(new NonTerminal(AXIOM.toString()));
            mGrammar.calculateType();
            return false;
        }

        mCount++;
        return true;
    }//nextStep
    
    /**
     * Ejecuta todos los pasos del algoritmo.<br>
     * 
     * @return True si la gramática ha sido creada con éxito y false en caso contrario.
     */
    public boolean allSteps () {
        if(firstStep())
            while(nextStep());
        
        if(mGrammar.getSize() > 0)
            return true;
        
        return false;
    }//allSteps
    
    /**
     * Estado actual en el que se encuentra el autómata.
     * 
     * @return Lista de los estados recorridos.
     */
    public State getCurrentState () {
        
        if(mCount != -1 && mCount < mStates.size())
            return mStates.elementAt(mCount);
        
        return null;
    }//getCurrentState
    
    /**
     * Devuelve el autómata sobre el que se ejecuta el algoritmo.<br>
     * Este autómata contiene nuevas etiquetas en cada estado.
     */
    public Automaton getAutomaton () {
        
        return mAutomaton;
    }//getAutomaton
    
    /**
     * Devuelve la gramática creada a partir del autómata.
     * 
     * @return Gramática resultante.
     */
    public Grammar getSolution () {
        
        return mGrammar;
    }//getSolution
    
}//ObtainGrammarFA
