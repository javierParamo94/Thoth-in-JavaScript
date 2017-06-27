package core.turingmachine.multitape;

import java.util.Iterator;
import java.util.Vector;

import core.Automaton;
import core.Transition;
import core.State;
import core.Terminal;
import core.turingmachine.TransitionTM;

/**
 * <b>Descripción</b><br>
 * Proporciona la funcionalidad que deben tener todas las transiciones de la Maquina de Turing multicinta.
 * <p>
 * <b>Detalles</b><br>
 * Implementa los métodos abstractos heredados de Transition.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Implementa el tipo de datos Transición de Maquina de Turing multicinta.
 * </p>
 * 
 * @author Javier Jimeno Visitación, Iñigo Mediavilla Sáiz
 * @version 2.0
 */
public class TransitionMTTM extends TransitionTM implements Cloneable{
    
    //Attributes ------------------------------------------------------------------
    
	/**
     * Vector de terminales de lectura de cinta.
     */
    protected Vector<Terminal> readings;
	
    /**
     * Vector de terminales de escritura en cinta.
     */
    protected Vector<Terminal> writings;
    
    /**
     * Vector de caracteres que indican el movimiento de los cabezales de las cintas.
     */
    protected Vector<Character> movements;
    
    
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
     * @param move Vector de caracteres que indican el movimiento de los cabezales de las cintas.
     */
    public TransitionMTTM (Vector<Terminal> in, State prevState, Vector<Terminal> write, State nextState, Vector<Character> move){
        mPrevState=prevState;
        mNextState=nextState;
        readings= in;
    	writings = write;
        movements = move;
        prevState.addTransitionOut(this);
        nextState.addTransitionIn(this);
        
    }//TransitionMTTM        
       
    /**
     * Constructor completo.
     * 
     * @param in Vector de terminales de lectura de cinta.
     * @param prevState Estado origen. 
     * @param write Vector de terminales de escritura en cinta.
     * @param nextState Siguiente estado.
     * @param move Vector de caracteres que indican el movimiento de los cabezales de las cintas.
     * @param id Identificador de la transición.
     */
    public TransitionMTTM (Vector<Terminal> in, State prevState, Vector<Terminal> write, State nextState, Vector<Character> move, int id){
        mPrevState=prevState;
    	mNextState=nextState;
    	readings=in;
        writings = write;
        movements = move;
        prevState.addTransitionOut(this);
        nextState.addTransitionIn(this);
    }//TransitionMTTM
     
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
     * Devuelve el vector de caracteres de movimiento de los cabezales de las cintas.
     * 
     * @return vector de caracteres de movimiento de los cabezales de las cintas.
     */
    public Vector<Character> getMovements (){        
        return movements;
    }//getMovements
    
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
     * Método que modifica el vector de caracteres de movimiento de los cabezales de las cintas.
     * 
     * @param move Vector de caracteres de movimiento de los cabezales de las cintas.
     */
    public void setMovements (Vector<Character> move){
    	this.movements=move;
    }//setMovements
    
    /**
     * Devuelve true si la transicion que se le pasa es igual.<br>
     * En función del vector de lectura de las cintas, del vector de escritura en las cintas,
     * de los movimientos de los cabezales de las cintas y de los estados siguiente y anterior.
     * 
     * @return True si es igual y false en caso contrario.
     */
    public boolean equals (Object transition){
        if(compare(readings,((TransitionMTTM)transition).getReadings())
            && mNextState.equals(((TransitionMTTM)transition).getNextState())
            && mPrevState.equals(((TransitionMTTM)transition).getPrevState())
            && writings.equals(((TransitionMTTM)transition).getWritings())
            && movements.equals((((TransitionMTTM)transition).getMovements())))
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
    public TransitionMTTM clone (){
        
        return new TransitionMTTM(readings, mPrevState, writings, mNextState, movements);
    }//clone
    
    /**
     * Devuelve un String con el tipo de la transición. Debe concordar con uno de los
     * tipos definidos en la clase Automaton.
     * 
     * @return El tipo maquina de turing multicinta
     */
    
    public String getTransitionType(){
    	return Automaton.MULTI_TAPE_TURING_MACHINE;
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
    		data.add(movements.get(i).toString());
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
    	
    	Vector<Terminal> in =new Vector<Terminal>();
    	Vector<Terminal> write =new Vector<Terminal>();
    	Vector<Character> move =new Vector<Character>();
    	for(int i=0;i<data.size();i+=3){
    		in.add(new Terminal(data.elementAt(i).charAt(0)));
    		write.add(new Terminal(data.elementAt(i+1).charAt(0)));
    		move.add(data.elementAt(i+2).charAt(0));
    	}
    	Iterator<Transition> ite=mPrevState.getTransitionsOut().iterator();
    	TransitionMTTM temp=(TransitionMTTM)ite.next();
    	while(ite.hasNext()){
    		if(temp.getReadings().equals(in) && temp.mNextState.equals(mNextState)
    			&& temp.getWritings().equals(write) && temp.getMovements().equals(move)){
    			freeStates();
    			return;
    		}
    	}
    	
    	setReadings(in);
    	setWritings(write);
    	setMovements(move);
    	
    }//setTransitionData
    
    /**
     * Devuelve la cadena que representa los datos de la transición.
     * 
     * @return String con los datos del vector de terminales de lectura de cinta,
     *  del vector de terminales de escritura de cinta 
     *  y del vector de caracteres con los movimientos de los cabezales de las cintas.
     */
    public String toString(){
    	String s="";
    	for(int i=0;i<readings.size();i++)
    		s+=readings.get(i).toString()+" ; "+writings.get(i).toString()+" , "+movements.get(i)+" / ";
    	s=s.substring(0, s.length()-3);
    	return s;
    }//toString
    
}//TransitionMTTM
