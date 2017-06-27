package core.pushdownautomaton;

import core.*;

import java.util.Vector;

/**
 * <b>Descripci�n</b><br>
 * Proporciona la funcionalidad que deben tener los estados de Aut�mata de Pila.
 * <p>
 * <b>Detalles</b><br>
 * Implementa los estados abstractos heredados de State y aumenta con nuevos
 * m�todos la funcionalidad de �stos.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Implementa el tipo de datos Estado de Aut�mata de Pila.
 * </p>
 * 
 * @author Javier Jimeno Visitaci�n, I�igo Mediavilla S�iz
 * @version 2.0
 */
public class StatePDA extends State implements Cloneable{
    
    //  Methods ---------------------------------------------------------------------
    
    /**
     * Constructor b�sico.
     * 
     * @param name Nombre del estado del aut�mata de pila.
     */
    public StatePDA (String name){
        super(name);

    }//StatePDA
    
    /**
     * Constructor completo.
     * 
     * @param name Nombre del estado del aut�mata de pila.
     * @param fin Es final o no.
     */
    public StatePDA (String name, boolean fin){
        super(name, fin);

    }//StatePDA
    
    /**
     * Crea una nueva transici�n de entrada y la a�ade.
     * 
     * @param in Terminal de entrada.
     * @param inPush Cadena de lectura de pila.
     * @param prevState Estado origen de la transici�n.
     * @param outPush Cadena de escritura en pila.
     * @return La transici�n creada.
     */
    public Transition createTransitionIn (Terminal in, String inPush, State prevState, String outPush){
        
        return new TransitionPDA(in, prevState, inPush, this, outPush);
    }//createTransitionIn
    
    /**
     * Crea una nueva transici�n de salida y la a�ade.
     * 
     * @param in Terminal de entrada.
     * @param inPush Cadena de lectura de pila.
     * @param nextState Estado destino de la transici�n.
     * @param outPush Cadena de escritura en pila.
     * @return La transici�n creada.
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
     * Clona las transiciones del estado que se le pasa por par�metro.
     * 
     * @param state Estado del que queremos clonar sus transiciones.
     * @param vState Vector de estados del aut�mata clonado.
     */
    public void cloneTransitions (State state, Vector<State> vState){
        TransitionPDA temp;
        
        for(Transition tran : state.getTransitionsOut()){
            temp = ((TransitionPDA)tran).clone();
            createTransitionOut(temp.getIn(), temp.getInPush(),vState.elementAt(vState.indexOf(temp.getNextState())),temp.getOutPush());
        }
        
    }//cloneTransitions

    /**
     * Devuelve un String con el tipo de la transici�n. Debe concordar con uno de los
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
