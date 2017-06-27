package core.turingmachine.multitrack;

import java.util.Iterator;
import java.util.Vector;

import core.Automaton;
import core.Transition;
import core.State;
import core.Terminal;
import core.turingmachine.TransitionTM;
import core.turingmachine.multihead.TransitionMHTM;

/**
 * <b>Descripción</b><br>
 * Proporciona la funcionalidad que deben tener todas las transiciones de la Maquina de Turing multipista.
 * <p>
 * <b>Detalles</b><br>
 * Implementa los métodos abstractos heredados de Transition.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Implementa el tipo de datos Transición de Maquina de Turing multipista.
 * </p>
 * 
 * @author Javier Jimeno Visitación, Iñigo Mediavilla Sáiz
 * @version 2.0
 */
public class TransitionMTRTM extends TransitionTM implements Cloneable{
    
    //Attributes ------------------------------------------------------------------
    
	/**
     * Vector de terminales de lectura de cinta.
     */
    protected Vector<Terminal> readings;
	
    /**
     * Vector de terminales de escritura en cinta.
     */
    protected Vector<Terminal> writings;
    
    
    //  Methods ---------------------------------------------------------------------
           
    /**
     * Constructor con los estados origen y destino, 
     * el vector de terminales de lectura de cinta,
     * el vector de terminales de escritura en cinta
     * y el vector de caracteres de movimiento de los cabezales de las cintas.
     * Constructor completo sin ID.
     * 
     * @param in Vector de terminales de lectura de cinta.
     * @param prevState Estado origen. 
     * @param write Vector de terminales de escritura en cinta.
     * @param nextState Siguiente estado.
     * @param move Carácter que indica el movimiento de la cinta.
     */
    public TransitionMTRTM (Vector<Terminal> in, State prevState, Vector<Terminal> write, State nextState, Character move){
        mPrevState=prevState;
        mNextState=nextState;
        readings= in;
    	writings = write;
        moveTape = move;
        prevState.addTransitionOut(this);
        nextState.addTransitionIn(this);
        
    }//TransitionMTRTM        
       
    /**
     * Constructor completo.
     * 
     * @param in Vector de terminales de lectura de cinta.
     * @param prevState Estado origen. 
     * @param write Vector de terminales de escritura en cinta.
     * @param nextState Siguiente estado.
     * @param move Carácter que indica el movimiento de la cinta.
     * @param id Identificador de la transición.
     */
    public TransitionMTRTM (Vector<Terminal> in, State prevState, Vector<Terminal> write, State nextState, Character move, int id){
        mPrevState=prevState;
    	mNextState=nextState;
    	readings=in;
        writings = write;
        moveTape = move;
        prevState.addTransitionOut(this);
        nextState.addTransitionIn(this);
    }//TransitionMTRTM
     
    /**
     * Devuelve el vector de terminales de lectura de cinta.
     * 
     * @return Vector de terminales de lectura de cinta.
     */
    public Vector<Terminal> getReadings (){
        
        return readings;
    }//getReadings
    
    /**
     * Devuelve el vector de terminales de escritura en cinta.
     * 
     * @return Vector de terminales de escritura en cinta.
     */
    public Vector<Terminal> getWritings (){
        
        return writings;
    }//getWritings
    
    /**
     * Método que modifica el valor del vector de terminales de lectura de cinta.
     * 
     * @param in Vector de terminales de lectura de cinta.
     */
    public void setReadings (Vector<Terminal> in){
    	this.readings=in;
    }//setReadings
    
    /**
     * Método que modifica el valor del vector de terminales de escritura en cinta.
     * 
     * @param write Vector de terminales de escritura en cinta.
     */
    public void setWritings (Vector<Terminal> write){
        this.writings=write;
    }//setWritings
    
    /**
     * Devuelve true si la transicion que se le pasa es igual.<br>
     * En función del vector de lectura de las cintas, del vector de escritura en las cintas,
     * del movimiento del cabezal de la cinta y de los estados siguiente y anterior.
     * 
     * @return True si es igual y false en caso contrario.
     */
    public boolean equals (Object transition){
        if(compare(readings,((TransitionMTRTM)transition).getReadings())
            && mNextState.equals(((TransitionMTRTM)transition).getNextState())
            && mPrevState.equals(((TransitionMTRTM)transition).getPrevState())
            && writings.equals(((TransitionMTRTM)transition).getWritings())
            && moveTape==(((TransitionMTRTM)transition).getMoveTape()) )
            return true;
        
        return false;
    }//equals
    
    /**
     * Comprueba si los vectores de terminales de entrada son iguales.
     * 
     * @param r1 Vector de terminales
     * @param r2 Vector de terminales
     * @return True si son iguales, false si son diferentes.
     */
    private boolean compare(Vector<Terminal> r1, Vector<Terminal> r2){
    	for(int i=0;i<r1.size();i++){
    		if(!r1.get(i).toString().equals(r2.get(i).toString()))
    			return false;
    	}
    	return true;
    }//compare
    
    /**
     * Clona la transición.
     * 
     * @return Transición clonada.
     */
    public TransitionMTRTM clone (){
        
        return new TransitionMTRTM(readings, mPrevState, writings, mNextState, moveTape);
    }//clone
    
    /**
     * Devuelve un String con el tipo de la transición. Debe concordar con uno de los
     * tipos definidos en la clase Automaton.
     * 
     * @return El tipo maquina de turing multipista
     */
    
    public String getTransitionType(){
    	return Automaton.MULTI_TRACK_TURING_MACHINE;
    }//getTransitionType
    
    /**
     * Devuelve un vector de Strings con los datos de la transición
     * 
     * @return Datos de la transicion
     */
    
    public Vector<String> getTransitionData(){
    	int size=readings.size();
    	Vector<String> data = new Vector<String>(3*size);
    	for(int i=0;i<size;i++){
    		data.add(readings.get(i).toString());
    		data.add(writings.get(i).toString());
    	}
		data.add(moveTape.toString());
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
    	Vector<Terminal> in =new Vector<Terminal>();
    	Vector<Terminal> write =new Vector<Terminal>();
    	Character move=data.get(data.size()-1).charAt(0);
    	for(int i=0;i<data.size();i+=2){
    		in.add(new Terminal(data.elementAt(i).charAt(0)));
    		write.add(new Terminal(data.elementAt(i+1).charAt(0)));
    	}
    	Iterator<Transition> ite=mPrevState.getTransitionsOut().iterator();
    	TransitionMTRTM temp=(TransitionMTRTM)ite.next();
    	while(ite.hasNext()){
    		if(temp.getReadings().equals(in) && temp.mNextState.equals(mNextState)
    			&& temp.getWritings().equals(write) && temp.getMoveTape()==move){
    			freeStates();
    			return;
    		}
    	}
    	
    	setReadings(in);
    	setWritings(write);
    	setMoveTape(move);
    }//setTransitionData
    
    /**
     * Devuelve la cadena que representa los datos de la transición.
     * 
     * @return String con los datos del vector de terminales de lectura de cinta,
     *  del vector de terminales de escritura de cinta 
     *  y el movimiento del cabezal de la cinta.
     */
    public String toString(){
    	String s="";
    	for(int i=0;i<readings.size();i++)
    		s+=readings.get(i).toString()+" ; "+writings.get(i).toString()+" / ";
    	s=s.substring(0, s.length()-3);
    	s+=" , "+moveTape;
    	return s;
    }//toString
    
}//TransitionMTRTM
