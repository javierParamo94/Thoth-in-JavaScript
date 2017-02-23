package core.turingmachine.limitedtape;

import core.Automaton;
import core.State;
import core.Terminal;
import core.turingmachine.TransitionTM;

/**
 * <b>Descripción</b><br>
 * Proporciona la funcionalidad que deben tener todas las transiciones de la Maquina de Turing con cinta limitada.
 * <p>
 * <b>Detalles</b><br>
 * Implementa los métodos abstractos heredados de Transition.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Implementa el tipo de datos Transición de Maquina de Turing con cinta limitada.
 * </p>
 * 
 * @author Javier Jimeno Visitación, Iñigo Mediavilla Sáiz
 * @version 2.0
 */
public class TransitionLTTM extends TransitionTM implements Cloneable{
        
    //  Methods ---------------------------------------------------------------------
        
    /**
     * Constructor con los estados origen y destino, 
     * el terminal de lectura de cinta y escritura en cinta
     * y el movimiento del cabezal.
     * Constructor completo sin ID.
     * 
     * @param in Terminal de lectura de cinta.
     * @param prevState Estado origen. 
     * @param write Terminal de escritura en cinta.
     * @param nextState Siguiente estado.
     * @param move Carácter que indica el movimiento de la cinta.
     */
    public TransitionLTTM (Terminal in, State prevState, Terminal write, State nextState, Character move){
        super(in,prevState, write, nextState, move);
        
    }//TransitionLMTM        
       
    /**
     * Constructor completo.
     * 
     * @param in Terminal de lectura de cinta.
     * @param prevState Estado origen. 
     * @param write Terminal de escritura en cinta.
     * @param nextState Siguiente estado.
     * @param move Carácter que indica el movimiento de la cinta.
     * @param id Identificador de la transición.
     */
    public TransitionLTTM (Terminal in, State prevState, Terminal write, State nextState, Character move, int id){
        super(in,prevState, write, nextState, move, id);
    }//TransitionLTTM
    
    /**
     * Clona la transición.
     * 
     * @return Transición clonada.
     */
    public TransitionLTTM clone (){
        
        return new TransitionLTTM(mIn, mPrevState, writeTape, mNextState, moveTape);
    }//clone
    
    /**
     * Devuelve un String con el tipo de la transición. Debe concordar con uno de los
     * tipos definidos en la clase Automaton.
     * 
     * @return El tipo maquina de turing
     */
    
    public String getTransitionType(){
    	return Automaton.LIMITED_TAPE_TURING_MACHINE;
    }//getTransitionType
    
}//TransitionLTTM
