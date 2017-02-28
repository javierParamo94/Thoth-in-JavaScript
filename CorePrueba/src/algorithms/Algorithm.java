package algorithms;

import core.Automaton;

/**
 * <b>Descripci�n</b><br>
 * Se encarga de establecer la interfaz de todos los algoritmos.
 * <p>
 * <b>Detalles</b><br>
 * Define la estructura a seguir por los algoritmos de todos los aut�matas.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Posibilita el paso a paso.
 * </p>
 * 
 * @author �lvar Arn�iz Gonz�lez, Andr�s Arn�iz Moreno
 * @version 1.0
 */
public abstract class Algorithm {

    // Attributes ------------------------------------------------------------------
    
    /**
     * Aut�mata sobre el que se va a aplicar el algoritmo.
     */
    protected Automaton mAutomaton;
    
    // Methods ---------------------------------------------------------------------
    
    /**
     * Constructor para los algoritmos que construir�n un aut�mata durante su
     * resoluci�n.
     */
    public Algorithm () {
        super();
        
    }//Algorithm
    
    /**
     * Constructor para los algoritmos que necesitan acceder a un aut�mata
     * para su resoluci�n.
     * 
     * @param automaton Automata al que se le va a aplicar el algoritmo.
     * @throws Lanza InitializeException si el aut�mata no tiene estado inicial y/o
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
     * que no puede inicializar el aut�mata.
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
     * que no puede inicializar el aut�mata.
     */
    public abstract boolean allSteps() throws InitializeException;
    
}//Algorithm
