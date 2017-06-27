package core.pushdownautomaton;

import core.*;

import java.util.Vector;

/**
 * <b>Descripción</b><br>
 * Proporciona la funcionalidad que deben tener los estados de Autómata de Pila.
 * <p>
 * <b>Detalles</b><br>
 * Implementa los estados abstractos heredados de State y aumenta con nuevos
 * métodos la funcionalidad de éstos.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Implementa el tipo de datos Estado de Autómata de Pila.
 * </p>
 * 
 * @author Javier Jimeno Visitación, Iñigo Mediavilla Sáiz
 * @version 2.0
 */
public class StatePDA extends State implements Cloneable{
    
    //  Methods ---------------------------------------------------------------------
    
    /**
     * Constructor básico.
     * 
     * @param name Nombre del estado del autómata de pila.
     */
    public StatePDA (String name){
        super(name);

    }//StatePDA
    
    /**
     * Constructor completo.
     * 
     * @param name Nombre del estado del autómata de pila.
     * @param fin Es final o no.
     */
    public StatePDA (String name, boolean fin){
        super(name, fin);

    }//StatePDA
    
    /**
     * Crea una nueva transición de entrada y la añade.
     * 
     * @param in Terminal de entrada.
     * @param inPush Cadena de lectura de pila.
     * @param prevState Estado origen de la transición.
     * @param outPush Cadena de escritura en pila.
     * @return La transición creada.
     */
    public Transition createTransitionIn (Terminal in, String inPush, State prevState, String outPush){
        
        return new TransitionPDA(in, prevState, inPush, this, outPush);
    }//createTransitionIn
    
    /**
     * Crea una nueva transición de salida y la añade.
     * 
     * @param in Terminal de entrada.
     * @param inPush Cadena de lectura de pila.
     * @param nextState Estado destino de la transición.
     * @param outPush Cadena de escritura en pila.
     * @return La transición creada.
     */
    public Transition createTransitionOut (Terminal in, String inPush,State nextState,String outPush){
        return new TransitionPDA(in, this, inPush, nextState, outPush);
    }//createTransitionOut
    
    /**
     * Clona el estado y lo devuelve.
     * 
     * @return Estado clonado.
     */
    public State clone (){
        StatePDA state;
        
        state = new StatePDA(mName, mFinal);
        state.setLabel(mLabel);
        state.setInitial(this.mInitial);
        
        return state;
    }//clone
    
    /**
     * Clona las transiciones del estado que se le pasa por parámetro.
     * 
     * @param state Estado del que queremos clonar sus transiciones.
     * @param vState Vector de estados del autómata clonado.
     */
    public void cloneTransitions (State state, Vector<State> vState){
        TransitionPDA temp;
        
        for(Transition tran : state.getTransitionsOut()){
            temp = ((TransitionPDA)tran).clone();
            createTransitionOut(temp.getIn(), temp.getInPush(),vState.elementAt(vState.indexOf(temp.getNextState())),temp.getOutPush());
        }
        
    }//cloneTransitions

    /**
     * Devuelve un String con el tipo de la transición. Debe concordar con uno de los
     * tipos definidos en la clase Automaton.
     * 
     * @see core.Automaton
     * 
     * @return Tipo del estado
     */ 
	public String getStateType() {
		return Automaton.PUSH_DOWN_AUTOMATON;
	}//getStateType
    
}//StatePDA
