package core.finiteautomaton;

import java.util.Vector;

import core.*;

/**
 * <b>Descripci�n</b><br>
 * Proporciona la funcionalidad que deben tener todas las transiciones de Aut�mata
 * Finito.
 * <p>
 * <b>Detalles</b><br>
 * Implementa los m�todos abstractos heredados de Transition.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Implementa el tipo de datos Transici�n de Aut�mata Finito.
 * </p>
 * 
 * @author �lvar Arn�iz Gonz�lez, Andr�s Arn�iz Moreno
 * @version 1.0
 * 
 * @author Javier Jimeno Visitaci�n, I�igo Mediavilla Saiz
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
     * @param in Terminal de entrada de la transici�n.
     * @param prevState Estado origen.
     * @param nextState Siguiente estado.
     */
    public TransitionFA (Terminal in, State prevState, State nextState){
        super(in, prevState, nextState);
        prevState.addTransitionOut(this);
        nextState.addTransitionIn(this);
        
    }//TransitionFA
    
    /**
     * Constructor completo con asignaci�n de id.
     * 
     * @param in Terminal de entrada de la transici�n.
     * @param prevState Estado origen.
     * @param nextState Siguiente estado.
     * @param id Identificador de la transici�n.
     */
    public TransitionFA (Terminal in, State prevState, State nextState, int id){
        super(in, prevState, nextState, id);
        prevState.addTransitionOut(this);
        nextState.addTransitionIn(this);
        
    }//TransitionFA
    
    /**
     * Devuelve true si la transicion que se le pasa es igual.<br>
     * En funci�n de la entrada y de los estados siguiente y anterior.
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
     * Clona la transici�n.
     * 
     * @return Transici�n clonada.
     */
    public TransitionFA clone (){
        
        return new TransitionFA(mIn, mPrevState, mNextState);
    }//clone
    
    /**
     * Devuelve un String con el tipo de la transici�n. Debe concordar con uno de los
     * tipos definidos en la clase Automaton.
     * 
     * @return El tipo aut�mata finito
     */
    
    public String getTransitionType(){
    	return Automaton.FINITE_AUTOMATON;
    }//getTransitionType
    
    /**
     * Devuelve un vector de Strings con los datos de la transici�n
     * 
     * @return Datos de la transicion
     */
    
    public Vector<String> getTransitionData(){
    	Vector<String> data = new Vector<String>(1);
    	data.add(this.getIn().toString());
    	return data;
    }//getTransitionData
    
    /**
     * Asigna el valor de los datos de la transici�n
     * 
     * @param Datos de la transicion
     */
    
    public void setTransitionData(Vector<String> data){
    	super.setIn(new Terminal(data.elementAt(0).charAt(0)));
    }//setTransitionData
    
    /**
     * Devuelve la cadena que representa los datos de la transici�n.
     * 
     * @return El terminal de la transici�n
     */
    public String toString(){
    	return this.getIn().toString();
    }//toString
    
}//TransitionFA
