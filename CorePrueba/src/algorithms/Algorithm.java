package algorithms;

import core.Automaton;

/**
 * <b>Descripción</b><br>
 * Se encarga de establecer la interfaz de todos los algoritmos.
 * <p>
 * <b>Detalles</b><br>
 * Define la estructura a seguir por los algoritmos de todos los autómatas.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Posibilita el paso a paso.
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @version 1.0
 */
public abstract class Algorithm {

    // Attributes ------------------------------------------------------------------
    
    /**
     * Autómata sobre el que se va a aplicar el algoritmo.
     */
    protected Automaton mAutomaton;
    
    // Methods ---------------------------------------------------------------------
    
    /**
     * Constructor para los algoritmos que construirán un autómata durante su
     * resolución.
     */
    public Algorithm () {
        super();
        
    }//Algorithm
    
    /**
     * Constructor para los algoritmos que necesitan acceder a un autómata
     * para su resolución.
     * 
     * @param automaton Automata al que se le va a aplicar el algoritmo.
     * @throws Lanza InitializeException si el autómata no tiene estado inicial y/o
     * final.
     */
    public Algorithm(Automaton automaton) throws InitializeException {
        mAutomaton = automaton;
        
        if(!mAutomaton.initialize() && !mAutomaton.hasFinal())
            throw new InitializeException(InitializeException.mError);
        if(!mAutomaton.hasInitial())
            throw new InitializeException(InitializeException.mInitialError);
        if(!mAutomaton.hasFinal())
            throw new InitializeException(InitializeException.mFinalError);
    }//Algorithm
    
    /**
     * Primer paso del algoritmo.<br>
     * Se encarga, en caso de que sea necesario, de inicializar ciertos valores
     * o comprobar las precondiciones necesarias para ejecutar el algoritmo.
     * 
     * @return Devuelve falso si el algoritmo ha fallado y no debe continuar.
     * @throws Lanza InitializeException si se ha encontrado con algun algoritmo
     * que no puede inicializar el autómata.
     */
    public abstract boolean firstStep() throws InitializeException;
    
    /**
     * Siguiente paso del algoritmo.
     * 
     * @return Devuelve falso si no hay mas pasos que realizar.
     */
    public abstract boolean nextStep();
    
    /**
     * Realiza todos los pasos del algoritmo.
     * 
     * @return Devuelve falso cuando el algoritmo ha encontrado fallos.
     * @throws Lanza InitializeException si se ha encontrado con algun algoritmo
     * que no puede inicializar el autómata.
     */
    public abstract boolean allSteps() throws InitializeException;
    
}//Algorithm
