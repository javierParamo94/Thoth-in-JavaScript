package core.turingmachine.writemove;

import java.util.Iterator;
import java.util.Vector;

import core.Automaton;
import core.Transition;
import core.State;
import core.Terminal;
import core.TerminalTuring;
import core.turingmachine.TransitionTM;

/**
 * <b>Descripción</b><br>
 * Proporciona la funcionalidad que deben tener todas las transiciones de la Maquina de Turing "escribir o mover".
 * <p>
 * <b>Detalles</b><br>
 * Implementa los métodos abstractos heredados de Transition.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Implementa el tipo de datos Transición de Maquina de Turing "escribir o mover".
 * </p>
 * 
 * @author Javier Jimeno Visitación, Iñigo Mediavilla Sáiz
 * @version 2.0
 */
public class TransitionWMTM extends TransitionTM implements Cloneable{
        
    //  Methods ---------------------------------------------------------------------
    		       
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
    public TransitionWMTM (Terminal in, State prevState, Terminal write, State nextState, Character move){
        super(in, prevState, write, nextState, move);
    }//TransitionWMTM
    
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
    public TransitionWMTM (Terminal in, State prevState,State nextState, Character move){
        super(in, prevState, new TerminalTuring(), nextState, move);
    }//TransitionWMTM
        
    
    
    /**
     * Método que modifica el valor del terminal de escritura en cinta.
     * 
     * @param write terminal de escritura en cinta.
     */
    public void setWriteTape (Terminal write){
    	if(write==null){
    		this.writeTape=null;
    		this.moveTape='R';
    	}else{
    		this.writeTape=write;
			this.moveTape=null;
    	}
    }//setWriteTape
    
    /**
     * Método que modifica el caracter de movimiento de la cinta.
     * 
     * @param move Carácter que indica el movimiento de la cinta.
     */
    public void setMoveTape (Character move){
    	if(move!=null){
    		this.writeTape=null;
    		this.moveTape=move;
    	}else{
    		this.writeTape=new TerminalTuring();
			this.moveTape=null;
    	}
    }//setMoveTape
    
    /**
     * Devuelve true si la transicion que se le pasa es igual.<br>
     * En función de la lectura de cinta, de la escritura en cinta,
     * del movimiento de la cinta y de los estados siguiente y anterior.
     * 
     * @return True si es igual y false en caso contrario.
     */
    public boolean equals (Object transition){
        if(mIn.toString().equals(((TransitionWMTM)transition).getIn().toString())
            && mNextState.equals(((TransitionWMTM)transition).getNextState())
            && mPrevState.equals(((TransitionWMTM)transition).getPrevState())){
        	if(writeTape!=null){
        		if(((TransitionWMTM)transition).getWriteTape()!=null){
        			if(writeTape.equals(((TransitionWMTM)transition).getWriteTape()))
        				return true;
        		}else{
        			return false;
        		}
        	}else{
        		if(moveTape==(((TransitionWMTM)transition).getMoveTape()) )
        			return true;
        	}
        }
        return false;
    }//equals
    
    /**
     * Clona la transición.
     * 
     * @return Transición clonada.
     */
    public TransitionWMTM clone (){
        	return new TransitionWMTM(mIn, mPrevState, writeTape, mNextState, moveTape);
    }//clone
    
    /**
     * Devuelve un String con el tipo de la transición. Debe concordar con uno de los
     * tipos definidos en la clase Automaton.
     * 
     * @return El tipo maquina de turing  "escribir o mover"
     */
    
    public String getTransitionType(){
    	return Automaton.WRITE_MOVE_TURING_MACHINE;
    }//getTransitionType
    
    /**
     * Devuelve un vector de Strings con los datos de la transición
     * 
     * @return Datos de la transicion
     */
    
    public Vector<String> getTransitionData(){
    	Vector<String> data = new Vector<String>(3);
    	data.add(this.mIn.toString());
    	if(writeTape!=null){
    		data.add(this.writeTape.toString());
    		data.add("null");
    	}else{
    		data.add("null");
    		data.add(moveTape.toString());
    	}
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
    	TransitionTM temp=(TransitionWMTM)ite.next();
    	Terminal in=new Terminal(data.elementAt(0).charAt(0));
    	Terminal write;
    	Character move;
    	if(!data.elementAt(1).equals("null")){
    		write=new Terminal(data.elementAt(1).charAt(0));
    		move=null;
    	}else{
    		write=null;
    		move=data.elementAt(2).charAt(0);
    	}
    	while(ite.hasNext()){
    		if(temp.getIn().equals(in) && temp.getNextState().equals(mNextState)){
    			if(temp.getWriteTape()!=null){
    				if(temp.getWriteTape().equals(write)){
    					freeStates();
    	                return;
    				}
    			}else if(temp.getMoveTape()==move){
    				freeStates();
	                return;
    			}                
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
    	String s=this.getIn().toString();
    	if(writeTape!=null)
    		s+=" ; "+this.writeTape.toString();
    	else
    		s+=" , "+moveTape;
    	return s;
    }//toString
    
}//TransitionWMTM
