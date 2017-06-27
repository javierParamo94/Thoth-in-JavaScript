package algorithms.convert;

import java.util.Vector;

import algorithms.Algorithm;
import algorithms.InitializeException;

import core.*;
import core.grammar.Grammar;

/**
 * <b>Descripci�n</b><br>
 * Se encarga de transformar un aut�mata finito a una gram�tica.
 * <p>
 * <b>Detalles</b><br>
 * Desde el estado inicial va a ir recorriendo todos los estados y por cada transici�n
 * va a crear una producci�n en la gram�tica.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Desarrolla el algoritmo de transformaci�n de aut�mata finto a gram�tica.<br>
 * Posibilita el paso a paso.
 * </p>
 * 
 * @author �lvar Arn�iz Gonz�lez, Andr�s Arn�iz Moreno
 * @version 1.0
 */
public class ObtainGrammarFA extends Algorithm {

    // Attributes ------------------------------------------------------------------
    
    /**
     * Gram�tica que va a ser creada a partir del aut�mata finito.
     */
    private Grammar mGrammar;
    
    /**
     * Caracter que designa al axioma de la gram�tica.
     */
    private final Character AXIOM = 'S';
    
    /**
     * Caracter que designa la letra por la que empezar� cada no terminal;
     */
    private final Character NON_TERMINAL = 'P';
    
    /**
     * Almacena los estados por los que va a ir pasando el aut�mata para construir la
     * gram�tica.<br>
     * Esta lista se va actualizando segun se va recorriendo el aut�mata.
     */
    private Vector<State> mStates;
    
    /**
     * Indica la posici�n del estado actual.
     */
    private int mCount;
    
    //  Methods ---------------------------------------------------------------------
    
    /**
     * Constructor b�sico.
     * 
     * @param automaton Aut�mata finito del que vamos a obtener la gram�tica.
     * @throws Lanza InitializeException cuando el aut�mata del que queremos obtener
     * la gram�tica no tiene estado inicial y/o final.
     */
    public ObtainGrammarFA(Automaton automaton) throws InitializeException {
        super(automaton);
        mGrammar = new Grammar();
        mStates = new Vector<State>();
        mCount = 0;
        
    }//ObtainGrammarFA

    /**
     * Primer paso del algoritmo.<br>
     * Etiqueta cada estado con el no terminal asociado a �l.
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
            else    //Etiquetamos los dem�s estados empezando por A1
                st.setLabel(NON_TERMINAL.toString() + st.getName());
        }//for

        return true;
    }//firstStep

    /**
     * Siguiente paso del algoritmo.<br>
     * Recorre las transiciones del estado actual creando una producci�n por cada una
     * de ellas.<br>
     * En caso de que el estado sea final creamos una producci�n compresora, es decir,
     * una producci�n cuyo consecuente es �psilon. 
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
                //A�adimos el teminal si no es �psilon
            if(!trans.getIn().equals(new TerminalEpsilon()))
                right.add(trans.getIn());
                //Creamos el no terminal y le a�adimos
            right.add(new NonTerminal(trans.getNextState().getLabel()));
                //Creamos la producci�n
            mGrammar.createProduction(left, right);
            
            if(!mStates.contains(trans.getNextState()))
                mStates.add(trans.getNextState());
        }
            //Si es final a�adimos una producci�n �psilon
        if(current.isFinal())
            mGrammar.createProduction(left);
        
            //El estado actual pasa a ser el siguiente del vector            
        if(mCount + 1 == mStates.size()){
                //Si no quedan m�s estados que recorrer retornamos false
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
     * @return True si la gram�tica ha sido creada con �xito y false en caso contrario.
     */
    public boolean allSteps () {
        if(firstStep())
            while(nextStep());
        
        if(mGrammar.getSize() > 0)
            return true;
        
        return false;
    }//allSteps
    
    /**
     * Estado actual en el que se encuentra el aut�mata.
     * 
     * @return Lista de los estados recorridos.
     */
    public State getCurrentState () {
        
        if(mCount != -1 && mCount < mStates.size())
            return mStates.elementAt(mCount);
        
        return null;
    }//getCurrentState
    
    /**
     * Devuelve el aut�mata sobre el que se ejecuta el algoritmo.<br>
     * Este aut�mata contiene nuevas etiquetas en cada estado.
     */
    public Automaton getAutomaton () {
        
        return mAutomaton;
    }//getAutomaton
    
    /**
     * Devuelve la gram�tica creada a partir del aut�mata.
     * 
     * @return Gram�tica resultante.
     */
    public Grammar getSolution () {
        
        return mGrammar;
    }//getSolution
    
}//ObtainGrammarFA
