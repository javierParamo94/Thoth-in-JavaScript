package core.simulatorobject;

import java.util.ArrayList;
import java.util.Iterator;

import core.*;

/**
 * <b>Descripci�n</b><br>
 * Contiene los datos que se van a proporcionar para simular la validaci�n de una palabra en una m�quina de Turing.
 * <p>
 * 
 * @author Javier Jimeno Visitaci�n, I�igo Mediavilla S�iz
 * @version 2.0
 */
public class SimulatorObjectTM extends SimulatorObject implements Cloneable{
   	
	//Attributes ------------------------------------------------------------------
    
    /**
     * Estado de la cinta.
     */
    protected ArrayList<Terminal> tape;
    
    /** 
	 * Posici�n del cabezal.
	 */
	protected int head;
	
    //  Methods ---------------------------------------------------------------------
    
    /**
     * Constructor.
     * 
     * @param oState Estado.
     * @param oPrev Objeto de simulaci�n que le precede.
     * @param oTape Estado de la cinta. 
     * @param oHead Posici�n del cabezal.
     */
	public SimulatorObjectTM (State oState, SimulatorObject oPrev,ArrayList<Terminal> oTape,int oHead, String oWord){
    	super(oState,null,oPrev,oWord);
    	this.tape=oTape;
    	this.head=oHead;
    }//SimulatorObjectTM
    
    /**
     * M�todo de acceso a la cinta.
     * 
     * @return El estado de la cinta.
     */
    public ArrayList<Terminal> getTape () {
    	return this.tape;    	
    }//getTape
    
    /**
     * M�todo que modifica la pila.
     * 
     * @param tp Nuevo estado de la cinta.
     */
    public void setTape (ArrayList<Terminal> tp) {
    	this.tape=tp;
    }//setTape
    
    /**
     * M�todo de acceso a la posici�n del cabezal.
     * 
     * @return La posici�n del cabezal.
     */
    public int getHead () {
    	return this.head;
    }//getHead
    
    /**
     * M�todo que modifica la posici�n del cabezal.
     * 
     * @param oHead Nueva Posici�n del cabezal.
     */
    public void setHead (int oHead) {
    	this.head=oHead;    	
    }//setHead
    
    /**
     * M�todo de acceso a la cadena con el estado de la cinta.
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
     * Clona el objeto de simulaci�n y lo devuelve.
     * 
     * @return Objeto de simulaci�n clonado.
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

    