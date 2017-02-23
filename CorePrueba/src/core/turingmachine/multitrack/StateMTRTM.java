package core.turingmachine.multitrack;

import java.util.Vector;

import core.Automaton;
import core.State;
import core.Terminal;
import core.Transition;
import core.turingmachine.StateTM;



/**
 * <b>Descripci�n</b><br>
 * Proporciona la funcionalidad que deben tener los estados de una maquina de turing multipista.
 * <p>
 * <b>Detalles</b><br>
 * Implementa los estados abstractos heredados de State y aumenta con nuevos
 * m�todos la funcionalidad de �stos.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Implementa el tipo de datos Estado de Maquina de Turing multipista.
 * </p>
 * 
 * @author Javier Jimeno Visitaci�n, I�igo Mediavilla S�iz
 * @version 2.0
 */
public class StateMTRTM extends StateTM implements Cloneable{
    
    //  Methods ---------------------------------------------------------------------
    
    /**
     * Constructor b�sico.
     * 
     * @param name Nombre del estado de la maquina de turing multipista.
     */
    public StateMTRTM (String name){
        super(name);

    }//StateTM
    
    /**
     * Constructor completo.
     * 
     * @param name Nombre del estado de la maquina de turing multipista.
     * @param fin Es final o no.
     */
    public StateMTRTM (String name, boolean fin){
        super(name, fin);

    }//StateTM
    
    /**
     * Crea una nueva transici�n de entrada y la a�ade.
     * 
     * @param in Vector de terminales de lectura de cinta.
     * @param prevState Estado origen de la transici�n.
     * @param write Vector de terminales de escritura en cinta.
     * @param move Car�cter que indica el movimiento de la cinta.
     * @return La transici�n creada.
     */
    public Transition createTransitionIn (Vector<Terminal> in, State prevState, Vector<Terminal> write, Character move){
        
        return new TransitionMTRTM(in, prevState, write, this, move);
    }//createTransitionIn
    
    /**
     * Crea una nueva transici�n de entrada y la a�ade.
     * 
     * @param in Vector de terminales de lectura de cinta.
     * @param prevState Estado origen de la transici�n.
     * @param write Vector de terminales de escritura en cinta.
     * @param move Car�cter que indica el movimiento de la cinta.
     * @param id Identificador de la transici�n.
     * @return La transici�n creada.
     */
    public Transition createTransitionIn (Vector<Terminal> in, State prevState, Vector<Terminal> write, Character move,int id){
        
        return new TransitionMTRTM(in, prevState, write, this, move, id);
    }//createTransitionIn
    
    /**
     * Crea una nueva transici�n de salida y la a�ade.
     * 
     * @param in Vector de terminales de lectura de cinta.
     * @param nextState Estado destino de la transici�n.
     * @param write Vector de terminales de escritura en cinta.
     * @param move Car�cter que indica el movimiento de la cinta.
     * @return La transici�n creada.
     */
    public Transition createTransitionOut (Vector<Terminal> in, State nextState, Vector<Terminal> write, Character move){
        return new TransitionMTRTM(in, this, write, nextState, move);
    }//createTransitionOut
    
    /**
     * Crea una nueva transici�n de salida y la a�ade.
     * 
     * @param in Vector de terminales de lectura de cinta.
     * @param nextState Estado destino de la transici�n.
     * @param write Vector de terminales de escritura en cinta.
     * @param move Car�cter que indica el movimiento de la cinta.
     * @param id Identificador de la transici�n.
     * @return La transici�n creada.
     */
    public Transition createTransitionOut (Vector<Terminal> in, State nextState, Vector<Terminal> write, Character move, int id){
        return new TransitionMTRTM(in, this, write, nextState, move, id);
    }//createTransitionOut        
    
    /**
     * Clona el estado y lo devuelve.
     * 
     * @return Estado clonado.
     */
    public State clone (){
        StateMTRTM state;
        
        state = new StateMTRTM(mName, mFinal);
        state.setLabel(mLabel);
        state.setInitial(this.mInitial);
        
        return state;
    }//clone    
    
    /**
     * Clona las transiciones del estado que se le pasa por par�metro.
     * 
     * @param state Estado del que queremos clonar sus transiciones.
     * @param vState Vector de estados de la maquina de turing multipista clonada.
     */
    public void cloneTransitions (State state, Vector<State> vState){
        TransitionMTRTM temp;
        
        for(Transition tran : state.getTransitionsOut()){
            temp = ((TransitionMTRTM)tran).clone();
            createTransitionOut(temp.getReadings(), vState.elementAt(vState.indexOf(temp.getNextState())),temp.getWritings(),temp.getMoveTape());
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
		return Automaton.MULTI_TRACK_TURING_MACHINE;
	}//getStateType
    
}//StateMTRTM
