package core.simulatorobject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

import core.*;

/**
 * <b>Descripci�n</b><br>
 * Contiene los datos que se van a proporcionar para simular la validaci�n de una palabra en un aut�mata de pila.
 * <p>
 * 
 * @author Javier Jimeno Visitaci�n, I�igo Mediavilla S�iz
 * @version 2.0
 */
public class SimulatorObjectPDA extends SimulatorObject implements Cloneable{
   	
// Attributes ------------------------------------------------------------------
		
    /**
     * Estado de la pila.
     */
    protected Stack<Character> stack;
    
    /** 
	 * Cadena de pila leida.
	 */
	protected String readStack;
	
    //  Methods ---------------------------------------------------------------------
    
    /**
     * Constructor.
     * 
     * @param oState Estado.
     * @param oWordRead Cadena que se lleva le�da.
     * @param oPrev Objeto de simulaci�n que le precede.
     * @param oStack Estado de la pila.
     * @param oReadStack Cadena de pila leida.
     */
    public SimulatorObjectPDA (State oState, ArrayList<Character> oWordRead, SimulatorObject oPrev,Stack<Character> oStack, String oReadStack, String oWord){
    	super(oState,oWordRead,oPrev,oWord);
    	this.stack=oStack;
    	this.readStack=oReadStack;
    }//SimulatorObjectPDA
    
    /**
     * M�todo de acceso a la pila.
     * 
     * @return La pila.
     */
    public Stack<Character> getStack () {
    	return this.stack;    	
    }//getStack
    
    /**
     * M�todo que modifica la pila.
     * 
     * @param st Nueva pila.
     */
    public void setStack (Stack<Character> st) {
    	this.stack=st;    	
    }//setStack
    
    /**
     * M�todo de acceso a la cadena de pila leida.
     * 
     * @return String Cadena leida.
     */
    public String stringPushRead () {     
    	return this.readStack;    	
    }//stringPushRead
    
    /**
     * M�todo de acceso a la cadena con el estado de la pila.
     * 
     * @return String Cadena con el estado de la pila.
     */
    public String pushToString () {
    	
    	Iterator<Character> i=this.stack.iterator();
    	String s="";
    	while(i.hasNext())
    		s+=i.next();
    	return s;    	
    }//stringPushRead
    
    /**
     * Clona el objeto de simulaci�n y lo devuelve.
     * 
     * @return Objeto de simulaci�n clonado.
     */
    public SimulatorObject clone (){
    	ArrayList<Character> readWord;
    	if(wordRead!=null)
    		readWord=(ArrayList<Character>)wordRead.clone();
    	else
    		readWord=null;
    	Stack<Character> st=(Stack<Character>)stack.clone();
    	SimulatorObject so=new SimulatorObjectPDA(state,readWord,prev,st,readStack,word);
    	if(this.valid)
    		so.validTrue();
    	if(this.wrong)
    		so.wrongTrue();
    	return so;
    }//clone
    
}//SimulatorObjectPDA

    