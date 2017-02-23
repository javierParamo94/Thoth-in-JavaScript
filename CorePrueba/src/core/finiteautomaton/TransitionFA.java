package core.finiteautomaton;

import java.util.Vector;

import core.*;

/**
 * <b>Descripción</b><br>
 * Proporciona la funcionalidad que deben tener todas las transiciones de Autómata
 * Finito.
 * <p>
 * <b>Detalles</b><br>
 * Implementa los métodos abstractos heredados de Transition.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Implementa el tipo de datos Transición de Autómata Finito.
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @version 1.0
 * 
 * @author Javier Jimeno Visitación, Iñigo Mediavilla Saiz
 * @version 2.0
 */
public class TransitionFA extends Transition implements Cloneable{
    
    //  Methods ---------------------------------------------------------------------
    
	public TransitionFA (){
        super(); 
    }//Transition
	
    /**
     * Constructor sin terminal de entrada.
     * 
     * @param prevState Estado origen.
     * @param nextState Siguiente estado.
     */
    public TransitionFA (State prevState, State nextState){
        super(prevState, nextState);
        prevState.addTransitionOut(this);
        nextState.addTransitionIn(this);
        
    }//TransitionFA

    /**
     * Constructor completo.
     * 
     * @param in Terminal de entrada de la transición.
     * @param prevState Estado origen.
     * @param nextState Siguiente estado.
     */
    public TransitionFA (Terminal in, State prevState, State nextState){
        super(in, prevState, nextState);
        prevState.addTransitionOut(this);
        nextState.addTransitionIn(this);
        
    }//TransitionFA
    
    /**
     * Constructor completo con asignación de id.
     * 
     * @param in Terminal de entrada de la transición.
     * @param prevState Estado origen.
     * @param nextState Siguiente estado.
     * @param id Identificador de la transición.
     */
    public TransitionFA (Terminal in, State prevState, State nextState, int id){
        super(in, prevState, nextState, id);
        prevState.addTransitionOut(this);
        nextState.addTransitionIn(this);
        
    }//TransitionFA
    
    /**
     * Devuelve true si la transicion que se le pasa es igual.<br>
     * En función de la entrada y de los estados siguiente y anterior.
     * 
     * @return True si es igual y false en caso contrario.
     */
    public boolean equals (Object transition){
        if(mIn.equals(((TransitionFA)transition).getIn())
            && mNextState.equals(((TransitionFA)transition).getNextState())
            && mPrevState.equals(((TransitionFA)transition).getPrevState()))
            return true;
        
        return false;
    }//equals
    
    /**
     * Clona la transición.
     * 
     * @return Transición clonada.
     */
    public TransitionFA clone (){
        
        return new TransitionFA(mIn, mPrevState, mNextState);
    }//clone
    
    /**
     * Devuelve un String con el tipo de la transición. Debe concordar con uno de los
     * tipos definidos en la clase Automaton.
     * 
     * @return El tipo autómata finito
     */
    
    public String getTransitionType(){
    	return Automaton.FINITE_AUTOMATON;
    }//getTransitionType
    
    /**
     * Devuelve un vector de Strings con los datos de la transición
     * 
     * @return Datos de la transicion
     */
    
    public Vector<String> getTransitionData(){
    	Vector<String> data = new Vector<String>(1);
    	data.add(this.getIn().toString());
    	return data;
    }//getTransitionData
    
    /**
     * Asigna el valor de los datos de la transición
     * 
     * @param Datos de la transicion
     */
    
    public void setTransitionData(Vector<String> data){
    	super.setIn(new Terminal(data.elementAt(0).charAt(0)));
    }//setTransitionData
    
    /**
     * Devuelve la cadena que representa los datos de la transición.
     * 
     * @return El terminal de la transición
     */
    public String toString(){
    	return this.getIn().toString();
    }//toString
    
}//TransitionFA
