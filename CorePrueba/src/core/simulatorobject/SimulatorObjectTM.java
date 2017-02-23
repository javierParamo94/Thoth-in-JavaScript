package core.simulatorobject;

import java.util.ArrayList;
import java.util.Iterator;

import core.*;

/**
 * <b>Descripción</b><br>
 * Contiene los datos que se van a proporcionar para simular la validación de una palabra en una máquina de Turing.
 * <p>
 * 
 * @author Javier Jimeno Visitación, Iñigo Mediavilla Sáiz
 * @version 2.0
 */
public class SimulatorObjectTM extends SimulatorObject implements Cloneable{
   	
	//Attributes ------------------------------------------------------------------
    
    /**
     * Estado de la cinta.
     */
    protected ArrayList<Terminal> tape;
    
    /** 
	 * Posición del cabezal.
	 */
	protected int head;
	
    //  Methods ---------------------------------------------------------------------
    
    /**
     * Constructor.
     * 
     * @param oState Estado.
     * @param oPrev Objeto de simulación que le precede.
     * @param oTape Estado de la cinta. 
     * @param oHead Posición del cabezal.
     */
	public SimulatorObjectTM (State oState, SimulatorObject oPrev,ArrayList<Terminal> oTape,int oHead, String oWord){
    	super(oState,null,oPrev,oWord);
    	this.tape=oTape;
    	this.head=oHead;
    }//SimulatorObjectTM
    
    /**
     * Método de acceso a la cinta.
     * 
     * @return El estado de la cinta.
     */
    public ArrayList<Terminal> getTape () {
    	return this.tape;    	
    }//getTape
    
    /**
     * Método que modifica la pila.
     * 
     * @param tp Nuevo estado de la cinta.
     */
    public void setTape (ArrayList<Terminal> tp) {
    	this.tape=tp;
    }//setTape
    
    /**
     * Método de acceso a la posición del cabezal.
     * 
     * @return La posición del cabezal.
     */
    public int getHead () {
    	return this.head;
    }//getHead
    
    /**
     * Método que modifica la posición del cabezal.
     * 
     * @param oHead Nueva Posición del cabezal.
     */
    public void setHead (int oHead) {
    	this.head=oHead;    	
    }//setHead
    
    /**
     * Método de acceso a la cadena con el estado de la cinta.
     * 
     * @return String Cadena con el estado de la cinta.
     */
    public String tapeToString () {
    	
    	Iterator<Terminal> i=this.tape.iterator();
    	String s="";
    	while(i.hasNext())
    		s+=i.next();
    	return s;    	
    }//tapeToString
    
    /**
     * Clona el objeto de simulación y lo devuelve.
     * 
     * @return Objeto de simulación clonado.
     */
    public SimulatorObject clone (){
    	ArrayList<Terminal> tp=(ArrayList<Terminal>)tape.clone();
    	SimulatorObject so=new SimulatorObjectTM(state,prev,tp,head,word);    	
    	if(this.valid)
    		so.validTrue();
    	if(this.wrong)
    		so.wrongTrue();
    	return so;
    }//clone
    
}//SimulatorObjectTM

    