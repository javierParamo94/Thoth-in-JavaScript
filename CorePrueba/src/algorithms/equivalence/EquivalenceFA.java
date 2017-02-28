package algorithms.equivalence;

import algorithms.Algorithm;
import algorithms.InitializeException;
import algorithms.eliminatenondeterministic.EliminateNonDeterministicFA;
import core.Automaton;
import core.finiteautomaton.FiniteAutomaton;

/**
 * <b>Descripci�n</b><br>
 * Realiza la comparaci�n de dos aut�matas.
 * <p>
 * <b>Detalles</b><br>
 * Recorre el aut�mata desde los estados iniciales de ambos aut�matas consumiendo
 * la misma entrada para cada uno.<br>
 * Si en alg�n momento llegamos a un estado final con uno de los aut�matas y en el
 * otro no los aut�matas no son equivalentes.<br>
 * Se ayuda de la clase EquivalenceTable.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Implementa el algoritmo de equivalencia de aut�matas finitos.<br>
 * Posibilita el paso a paso.<br>
 * </p>
 * 
 * @author �lvar Arn�iz Gonz�lez, Andr�s Arn�iz Moreno
 * @version 1.0
 * @see EquivalenceTable
 */
public class EquivalenceFA extends Algorithm{

    // Attributes ------------------------------------------------------------------
    
    /**
     * El aut�mata a comparar.
     */
    private Automaton mAutomaton2;
    
    /**
     * Aut�mata determinista con el que vamos a trabajar. Es equivalente a
     * mAutomaton1.
     */
    public FiniteAutomaton mAutomatonA;
    
    /**
     * Aut�mata determinista con el que vamos a trabajar. Es equivalente a
     * mAutomaton2.
     */
    public FiniteAutomaton mAutomatonB;
    
    /**
     * Tabla de equivalencia usada por el algoritmo.
     */
    public EquivalenceTable mTable;
    
    /**
     * Indica si los aut�matas son equivalentes, es decir, reconocen el mismo
     * lenguaje.
     */
    private boolean mAccept;
    
    
    // Methods ---------------------------------------------------------------------
    
    /**
     * Constructor completo.
     * 
     * @param automaton1 Aut�mata a comparar.
     * @param automaton2 Segundo aut�mata a comparar.
     * @throws InitializeException Lanza esta excepci�n si alguno de los dos 
     * aut�matas no tiene estado inicial o final.
     */
    public EquivalenceFA(FiniteAutomaton automaton1, FiniteAutomaton automaton2) throws InitializeException {
        super(automaton1);
            
        mAutomaton2 = automaton2;
        mAccept = false;
            //Comprobamos que haya estado inicial y final en mAutomaton2
        if(!mAutomaton2.initialize() && !mAutomaton2.hasFinal())
            throw new InitializeException(InitializeException.mError);
        if(!mAutomaton2.hasInitial())
            throw new InitializeException(InitializeException.mInitialError);
        if(!mAutomaton2.hasFinal())
            throw new InitializeException(InitializeException.mFinalError);
            //Para no modificar los estados iniciales les clona.
        mAutomatonA = (FiniteAutomaton) mAutomaton.clone();
        mAutomatonB = (FiniteAutomaton) mAutomaton2.clone();
        
    }//EquivalenceFA

    /**
     * Primer paso del algoritmo.<br>
     * Se encarga de transformar los aut�matas a deterministas, les completa e
     * inicializa la tabla de equivalencia.<br>
     * Si alguno de los aut�matas no tiene alfabeto de estados o sus alfabetos de
     * entrada son distintos no continuar� con el algoritmo.
     * 
     * @return Devuelve falso si el algoritmo ha fallado y no debe continuar.
     * @throws InitializeException Lanza esta excepci�n si alguno de los dos 
     * aut�matas no tiene estado inicial o final.
     */
    public boolean firstStep() throws InitializeException {
        
        EliminateNonDeterministicFA deterministic1;
        EliminateNonDeterministicFA deterministic2;
        deterministic1 = new EliminateNonDeterministicFA(mAutomatonA);
        deterministic2 = new EliminateNonDeterministicFA(mAutomatonB);
    
            //Si alguno de los aut�matas no tiene estados devuelve false.
        if(mAutomatonA.getStates().size()<=0 || mAutomatonB.getStates().size()<=0)
            return false;
            //Si los alfabetos no son iguales
        if(!mAutomatonA.getAlphabet().containsAll(mAutomatonB.getAlphabet()) ||
                !mAutomatonB.getAlphabet().containsAll(mAutomatonA.getAlphabet()))
            return false;
            //Si el estado inicial de uno es final tambi�n debe serlo del otro y viceversa
        if(mAutomatonA.getInitialState().isFinal() != mAutomatonB.getInitialState().isFinal())
            return false;
        
            //Hacemos determinista el aut�mata1 y le completamos
        deterministic1.allSteps();
        mAutomatonA = deterministic1.getSolution();
        mAutomatonA.completeAutomaton();
            //Hacemos determinista el aut�mata2 y le completamos
        deterministic2.allSteps();
        mAutomatonB = deterministic2.getSolution();
        mAutomatonB.completeAutomaton();
            //Inicializamos la tabla de equivalencia
        mTable = new EquivalenceTable(mAutomatonA.getInitialState(),
                            mAutomatonB.getInitialState());
        
        return true;
    }//firstStep
    
    /**
     * Siguiente paso del algoritmo.<br>
     * Completa la tabla de equivalencia, la compara para ver si debe seguir
     * con el algoritmo o por el contrario ya queda demostrado que no son
     * equivalentes.
     * 
     * @return Devuelve falso si no hay mas pasos que realizar.
     */
    public boolean nextStep(){
            //Si hemos podido seguir completando la tabla miramos a ver si son
            //equivalentes comparando sus pares de estados.
        if(mTable.completeTable(mAutomatonA.getAlphabet())){
            mAccept = false;
            return mTable.compare(mAutomatonA.getAlphabet());
        }
            //Si ya no se puede seguir completando la tabla, finalizamos el algoritmo
            //y almacenamos si son o no equivalentes.
        mAccept = mTable.compare(mAutomatonA.getAlphabet());
        
        return false;
    }//nextStep
    
    /**
     * Realiza todos los pasos del algoritmo.
     * 
     * @return Devuelve true si los aut�matas son equivalentes.
     * @throws InitializeException Lanza esta excepci�n si alguno de los dos 
     * aut�matas no tiene estado inicial o final.
     */
    public boolean allSteps() throws InitializeException {
        try {
            if(firstStep())
                while(nextStep());
        }
        catch (InitializeException e) {
            throw e;
        }

        return mAccept;
    }//allSteps
    
    /**
     * Devuelve verdadero si los aut�matas son equivalentes.
     * 
     * @return True si son equivalentes.
     */
    public boolean getAccept(){
        
        return mAccept;
    }//getAccept
    
}//EquivalenceFA
