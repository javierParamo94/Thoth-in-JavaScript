package core.turingmachine;

import java.util.Iterator;
import java.util.Vector;

import core.Automaton;
import core.Transition;
import core.State;
import core.Terminal;

/**
 * <b>Descripción</b><br>
 * Proporciona la funcionalidad que deben tener todas las transiciones de la Maquina de Turing.
 * <p>
 * <b>Detalles</b><br>
 * Implementa los métodos abstractos heredados de Transition.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Implementa el tipo de datos Transición de Maquina de Turing.
 * </p>
 * 
 * @author Javier Jimeno Visitación, Iñigo Mediavilla Sáiz
 * @version 2.0
 */
public class TransitionTM extends Transition implements Cloneable{
    
    //Attributes ------------------------------------------------------------------
        
    /**
     * Caracter de escritura en la cinta.
     */
    protected Terminal writeTape;
    
    /**
     * Caracter que indica el movimiento de la cinta.
     */
    protected Character moveTape;
    
    
    //  Methods ---------------------------------------------------------------------
            
    /**
     * Constructor para posibilitar otros tipos de transiciones dentro de las máquinas de Turing.
     *  
     */
    public TransitionTM (){
        
    }//TransitionTM
    
    /**
     * Constructor con los estados origen y destino, el terminal de lectura
     * de la cinta y de escritura en cinta y el movimiento del cabezal.
     * Constructor completo sin ID.
     * 
     * @param in Terminal de lectura de cinta.
     * @param prevState Estado origen. 
     * @param write Terminal de escritura en cinta.
     * @param nextState Siguiente estado.
     * @param move Carácter que indica el movimiento del cabezal de la cinta.
     */
    public TransitionTM (Terminal in, State prevState, Terminal write, State nextState, Character move){
        super(in,prevState, nextState);
        writeTape = write;
        moveTape = move;
        prevState.addTransitionOut(this);
        nextState.addTransitionIn(this);
        
    }//TransitionTM        
       
    /**
     * Constructor completo.
     * 
     * @param in Terminal de lectura de cinta.
     * @param prevState Estado origen. 
     * @param write Terminal de escritura en cinta.
     * @param nextState Siguiente estado.
     * @param move Carácter que indica el movimiento del cabezal de la cinta.
     * @param id Identificador de la transición.
     */
    public TransitionTM (Terminal in, State prevState, Terminal write, State nextState, Character move, int id){
        super(in,prevState, nextState,id);
        writeTape = write;
        moveTape = move;
        prevState.addTransitionOut(this);
        nextState.addTransitionIn(this);
    }//TransitionTM
        
    /**
     * Devuelve el terminal de escritura en cinta.
     * 
     * @return terminal de escritura en cinta.
     */
    public Terminal getWriteTape (){
        
        return writeTape;
    }//getWriteTape
    
    /**
     * Devuelve el carácter de movimiento de la cinta.
     * 
     * @return carácter que indica el movimiento de la cinta.
     */
    public Character getMoveTape (){        
        return moveTape;
    }//getMoveTape
    
    /**
     * Método que modifica el valor del Terminal de lectura de cinta.
     * 
     * @param in Terminal de lectura de cinta.
     */
    public void setIn (Terminal in){
    	this.mIn=in;
    }//setIn
    
    /**
     * Método que modifica el valor del terminal de escritura en cinta.
     * 
     * @param write terminal de escritura en cinta.
     */
    public void setWriteTape (Terminal write){
        this.writeTape=write;
    }//setWriteTape
    
    /**
     * Método que modifica el caracter de movimiento de la cinta.
     * 
     * @param move Carácter que indica el movimiento de la cinta.
     */
    public void setMoveTape (Character move){
    	this.moveTape=move;
    }//setMoveTape
    
    /**
     * Devuelve true si la transicion que se le pasa es igual.<br>
     * En función de la lectura de cinta, de la escritura en cinta,
     * del movimiento de la cinta y de los estados siguiente y anterior.
     * 
     * @return True si es igual y false en caso contrario.
     */
    public boolean equals (Object transition){
        if(mIn.toString().equals(((TransitionTM)transition).getIn().toString())
            && mNextState.equals(((TransitionTM)transition).getNextState())
            && mPrevState.equals(((TransitionTM)transition).getPrevState())
            && writeTape.equals(((TransitionTM)transition).getWriteTape())
            && moveTape==(((TransitionTM)transition).getMoveTape()) )
            return true;
        
        return false;
    }//equals
    
    /**
     * Clona la transición.
     * 
     * @return Transición clonada.
     */
    public TransitionTM clone (){
        
        return new TransitionTM(mIn, mPrevState, writeTape, mNextState, moveTape);
    }//clone
    
    /**
     * Devuelve un String con el tipo de la transición. Debe concordar con uno de los
     * tipos definidos en la clase Automaton.
     * 
     * @return El tipo maquina de turing
     */
    
    public String getTransitionType(){
    	return Automaton.TURING_MACHINE;
    }//getTransitionType
    
    /**
     * Devuelve un vector de Strings con los datos de la transición
     * 
     * @return Datos de la transicion
     */
    
    public Vector<String> getTransitionData(){
    	Vector<String> data = new Vector<String>(3);
    	data.add(this.mIn.toString());
    	data.add(this.writeTape.toString());
    	Character c[]={this.moveTape};
    	data.add(c.toString());
    	return data;
    }//getTransitionData
    
    /**
     * Establece el valor de los datos de la transición     
     * Si ya existia una transición igual entre los estados origen y destino
     * esta transición se borrará para evitar repeticiones.
     * 
     * @param data Datos de la transicion
     */
    
    public void setTransitionData(Vector<String> data){
    	Iterator<Transition> ite=mPrevState.getTransitionsOut().iterator();
    	TransitionTM temp=(TransitionTM)ite.next();
    	Terminal in=new Terminal(data.elementAt(0).charAt(0));
    	Terminal write=new Terminal(data.elementAt(1).charAt(0));
    	Character move=data.elementAt(2).charAt(0);
    	while(ite.hasNext()){
    		if(temp.mIn.equals(in) && temp.mNextState.equals(mNextState)
    				&& temp.writeTape.equals(write) && temp.moveTape==move){
                freeStates();
                return;
    		}
    	}
    	mIn = in;
    	writeTape=write;
    	moveTape=move;
    }//setTransitionData
    
    /**
     * Devuelve la cadena que representa los datos de la transición.
     * 
     * @return String con los datos del terminal de lectura de cinta, el terminal de escritura de cinta y el carácter del movimiento de la cinta.
     */
    public String toString(){
    	String s=this.getIn().toString()+" ; "+this.writeTape.toString()+" , "+moveTape;
    	return s;
    }//toString
    
}//TransitionTM
