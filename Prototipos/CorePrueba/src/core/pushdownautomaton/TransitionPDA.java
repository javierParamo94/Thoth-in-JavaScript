package core.pushdownautomaton;

import java.util.Iterator;
import java.util.Vector;

import core.*;

/**
 * <b>Descripción</b><br>
 * Proporciona la funcionalidad que deben tener todas las transiciones de Autómata
 * de Pila.
 * <p>
 * <b>Detalles</b><br>
 * Implementa los métodos abstractos heredados de Transition.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Implementa el tipo de datos Transición de Autómata de Pila.
 * </p>
 * 
 * @author Javier Jimeno Visitación, Iñigo Mediavilla Sáiz
 * @version 2.0
 */
public class TransitionPDA extends Transition implements Cloneable{
    
    //Attributes ------------------------------------------------------------------
        
    /**
     * Cadena de lectura de la pila.
     */
    protected String mInPush;
    
    /**
     * Cadena de escritura en la pila.
     */
    protected String mOutPush;
    
    
    //  Methods ---------------------------------------------------------------------
            
    /**
     * Constructor completo sin ID.
     * 
     * @param in Terminal de entrada de la transición.
     * @param prevState Estado origen. 
     * @param inPush Cadena de lectura de pila.
     * @param nextState Siguiente estado.
     * @param outPush Cadena de escritura en pila.
     */
    public TransitionPDA (Terminal in, State prevState, String inPush, State nextState, String outPush){
        super(in,prevState, nextState);
        mInPush = inPush;
        mOutPush = outPush;
        prevState.addTransitionOut(this);
        nextState.addTransitionIn(this);
        
    }//TransitionPDA
    
    /**
     * Constructor completo.
     * 
     * @param in Terminal de entrada de la transición.
     * @param prevState Estado origen. 
     * @param inPush Cadena de lectura de pila.
     * @param nextState Siguiente estado.
     * @param outPush Cadena de escritura en pila.
     * @param id Identificador de la transición.
     */
    public TransitionPDA (Terminal in, State prevState, String inPush, State nextState, String outPush, int id){
        super(in,prevState, nextState,id);
        mInPush = inPush;
        mOutPush = outPush;
        prevState.addTransitionOut(this);
        nextState.addTransitionIn(this);
    }//TransitionPDA
        
    /**
     * Devuelve la cadena de lectura de pila.
     * 
     * @return cadena de lectura de pila.
     */
    public String getInPush (){
        
        return mInPush;
    }//getInPush
    
    /**
     * Devuelve la cadena de escritura en pila.
     * 
     * @return cadena de escritura en pila.
     */
    public String getOutPush (){        
        return mOutPush;
    }//getOutPush
    
    /**
     * Método que modifica el valor del Terminal de entrada.
     * 
     * @param in Terminal de entrada.
     */
    public void setIn (Terminal in){
    	this.mIn=in;
    }//getOutPush
    
    /**
     * Método que modifica el valor de la cadena de lectura de pila.
     * 
     * @param inPush Cadena de lectura de pila.
     */
    public void setInPush (String inPush){
        this.mInPush=inPush;
    }//getInPush
    
    /**
     * Método que modifica el valor de la cadena de escritura en pila.
     * 
     * @param inPush Cadena de escritura en pila.
     */
    public void setOutPush (String outPush){
    	this.mOutPush=outPush;
    }//getOutPush
    
    /**
     * Devuelve true si la transicion que se le pasa es igual.<br>
     * En función de la entrada, de la lectura de pila, de la escritura en pila
     * y de los estados siguiente y anterior.
     * 
     * @return True si es igual y false en caso contrario.
     */
    public boolean equals (Object transition){
    		
        if(mIn.equals(((TransitionPDA)transition).getIn())
            && mNextState.equals(((TransitionPDA)transition).getNextState())
            && mPrevState.equals(((TransitionPDA)transition).getPrevState())
            && mInPush.equals(((TransitionPDA)transition).getInPush())
            && mOutPush.equals(((TransitionPDA)transition).getOutPush()) )
            return true;
        
        return false;
    }//equals
    
    /**
     * Clona la transición.
     * 
     * @return Transición clonada.
     */
    public TransitionPDA clone (){
        
        return new TransitionPDA(mIn, mPrevState, mInPush, mNextState, mOutPush);
    }//clone
    
    /**
     * Devuelve un String con el tipo de la transición. Debe concordar con uno de los
     * tipos definidos en la clase Automaton.
     * 
     * @return El tipo autómata de pila
     */
    
    public String getTransitionType(){
    	return Automaton.PUSH_DOWN_AUTOMATON;
    }//getTransitionType
    
    /**
     * Devuelve un vector de Strings con los datos de la transición
     * 
     * @return Datos de la transicion
     */
    
    public Vector<String> getTransitionData(){
    	Vector<String> data = new Vector<String>(3);
    	data.add(this.getIn().toString());
    	data.add(this.getInPush());
    	data.add(this.getOutPush());
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
    	TransitionPDA temp=(TransitionPDA)ite.next();
    	Terminal in=new Terminal(data.elementAt(0).charAt(0));
    	String inPush=data.elementAt(1);
    	String outPush=data.elementAt(2);
    	while(ite.hasNext()){
    		if(temp.mIn.equals(in) && temp.mNextState.equals(mNextState)
    				&& temp.mInPush.equals(inPush) && temp.mOutPush.equals(outPush)){
                freeStates();
                return;
    		}
    	}
    	mIn = in;
    	mInPush=inPush;
    	mOutPush=outPush;
    }//setTransitionData
    
    /**
     * Devuelve la cadena que representa los datos de la transición.
     * 
     * @return String con los datos del terminal de netrada,la cadena de lectura de pila y la cadena de escritura en pila.
     */
    public String toString(){
    	String s=this.getIn().toString()+" , "+mInPush+" ; "+mOutPush;
    	return s;
    }//toString
    
}//TransitionPDA
