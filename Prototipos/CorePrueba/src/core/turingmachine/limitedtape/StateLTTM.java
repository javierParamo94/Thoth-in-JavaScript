package core.turingmachine.limitedtape;

import java.util.Vector;

import core.Automaton;
import core.State;
import core.Terminal;
import core.Transition;
import core.turingmachine.StateTM;



/**
 * <b>Descripción</b><br>
 * Proporciona la funcionalidad que deben tener los estados de una maquina de turing con cinta limitada.
 * <p>
 * <b>Detalles</b><br>
 * Implementa los estados abstractos heredados de State y aumenta con nuevos
 * métodos la funcionalidad de éstos.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Implementa el tipo de datos Estado de Maquina de Turing con cinta limitada.
 * </p>
 * 
 * @author Javier Jimeno Visitación, Iñigo Mediavilla Sáiz
 * @version 2.0
 */
public class StateLTTM extends StateTM implements Cloneable{
    
    //  Methods ---------------------------------------------------------------------
    
    /**
     * Constructor básico.
     * 
     * @param name Nombre del estado de la maquina de turing con cinta limitada.
     */
    public StateLTTM (String name){
        super(name);

    }//StateLTTM
    
    /**
     * Constructor completo.
     * 
     * @param name Nombre del estado de la maquina de turing con cinta limitada.
     * @param fin Es final o no.
     */
    public StateLTTM (String name, boolean fin){
        super(name, fin);

    }//StateLTTM
    
    /**
     * Crea una nueva transición de entrada y la añade.
     * 
     * @param in Terminal de lectura de cinta.
     * @param prevState Estado origen de la transición.
     * @param write Terminal de escritura en cinta.
     * @param move Carácter que indica el movimiento de la cinta.
     * @return La transición creada.
     */
    public Transition createTransitionIn (Terminal in, State prevState, Terminal write, Character move){
        
        return new TransitionLTTM(in, prevState, write, this, move);
    }//createTransitionIn
    
    /**
     * Crea una nueva transición de salida y la añade.
     * 
     * @param in Terminal de lectura de cinta.
     * @param nextState Estado destino de la transición.
     * @param write Terminal de escritura en cinta.
     * @param move Carácter que indica el movimiento de la cinta.
     * @return La transición creada.
     */
    public Transition createTransitionOut (Terminal in, State nextState, Terminal write, Character move){
        return new TransitionLTTM(in, this, write, nextState, move);
    }//createTransitionOut    
    
    /**
     * Clona el estado y lo devuelve.
     * 
     * @return Estado clonado.
     */
    public State clone (){
        StateLTTM state;
        
        state = new StateLTTM(mName, mFinal);
        state.setLabel(mLabel);
        state.setInitial(this.mInitial);
        
        return state;
    }//clone    
    
    /**
     * Clona las transiciones del estado que se le pasa por parámetro.
     * 
     * @param state Estado del que queremos clonar sus transiciones.
     * @param vState Vector de estados de la maquina de turing con cinta limitada clonada.
     */
    public void cloneTransitions (State state, Vector<State> vState){
        TransitionLTTM temp;
        
        for(Transition tran : state.getTransitionsOut()){
            temp = (TransitionLTTM)tran.clone();
            createTransitionOut(temp.getIn(), vState.elementAt(vState.indexOf(temp.getNextState())),temp.getWriteTape(),temp.getMoveTape());
        }
        
    }//cloneTransitions
    
    /**
     * Devuelve un String con el tipo de la Estado. Debe concordar con uno de los
     * tipos definidos en la clase Automaton.
     * 
     * @see core.Automaton
     * 
     * @return Tipo del estado
     */ 
	public String getStateType() {
		return Automaton.LIMITED_TAPE_TURING_MACHINE;
	}//getStateType
    
}//StateLTTM
