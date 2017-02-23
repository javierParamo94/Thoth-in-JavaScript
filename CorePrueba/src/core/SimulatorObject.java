package core;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * <b>Descripción</b><br>
 * Contiene los datos que se van a proporcionar para simular la validación de una palabra.
 * <p>
 * 
 * @author Javier Jimeno Visitación, Iñigo Mediavilla Sáiz
 * @version 2.0
 */
public class SimulatorObject implements Cloneable{
    
	// Attributes ------------------------------------------------------------------
	
	/** 
	 * Estado.
	 */
	protected State state;
	
	/** 
	 * Cadena para comprobar.
	 */
	protected String word;
	
	/** 
	 * Cadena leida.
	 */
	protected ArrayList<Character> wordRead;
	
	/** 
	 * Objeto de simulación anterior.
	 */
	protected SimulatorObject prev;
	
	/** 
	 * Si tiene algún objeto de simulación posterior.
	 */
	protected boolean hasNext;
	
	/** 
	 * Si acepta la palabra.
	 */
	protected boolean valid;
	
	/** 
	 * Si no acepta la palabra.
	 */
	protected boolean wrong;
	
    //  Methods ---------------------------------------------------------------------
    
    /**
     * Construye un objeto de simulación.
     * 
     * @param oState Estado.
     * @param oWordRead Cadena que se lleva leída.
     * @param oPrev Objeto de simulación que le precede.
     */
    public SimulatorObject (State oState, ArrayList<Character> oWordRead, SimulatorObject oPrev, String oWord){
    	this.state=oState;
    	this.word=oWord;
    	this.wordRead=oWordRead;
    	this.prev=oPrev;
    	this.hasNext=true;
    	this.valid=false;
    	this.wrong=false;
    }//SimulatorObject
    
    /**
     * Método de acceso al estado.
     * 
     * @return Estado.
     */
    public State getState () {
    	return this.state;    	
    }//get State
    
    /**
     * Método que devuelve la cadena leída.
     * 
     * @return Cadena leída.
     */
    public ArrayList<Character> getWordRead () {
    	return this.wordRead;	
    }//getWordRead
    
    /**
     * Método que modifica la cadena leída.
     * 
     * @param Nueva cadena leída.
     */
    public void setWordRead (ArrayList<Character> word) {
    	this.wordRead=word;    	
    }//setWordRead
    
    /**
     * Método de acceso al objeto de simulación anterior.
     * 
     * @return SimulatorObject Objeto de simulación.
     */
    public SimulatorObject getPrev () {
    	return this.prev;    	
    }//getPrev
    
    /**
     * Método para saber si tiene un objeto de simulación posterior.
     * 
     * @return true si tiene, false si no.
     */
    public boolean hasNext () {
    	return this.hasNext;    	
    }//hasNext
    
    /**
     * Método para para indicar que no tiene un objeto de simulación posterior.
     * 
     */
    public void notHasNext () {
    	this.hasNext=false;    	
    }//notHasNext
    
    /**
     * Método para saber si la palabra es aceptada.
     * 
     * @return true si es aceptada, false si no.
     */
    public boolean isValid () {
    	return this.valid;    	
    }//isValid
    
    /**
     * Método para indicar que la palabra es aceptada.
     * 
     */
    public void validTrue () {
    	this.valid=true;    	
    }//validTrue
    
    /**
     * Método para saber si la palabra no es aceptada.
     * 
     * @return true si no es aceptada, false si no.
     */
    public boolean isWrong () {
    	return this.wrong;    	
    }//isWrong
    
    /**
     * Método para indicar que la palabra no es aceptada.
     * 
     */
    public void wrongTrue () {
    	this.wrong=true;    	
    }//WrongTrue
    
    /**
     * Método que devuelve la palabra para validar.
     * 
     */
    public String getWord () {
    	return this.word;    	
    }//getWord
    
    /**
     * Método de acceso a la cadena leida.
     * 
     * @return String Cadena leida.
     */
    public String stringRead () {
    	
    	if(this.wordRead==null)
    		return null;
    	else{
        	String s="";
    		Iterator<Character> i=wordRead.iterator();
    		while(i.hasNext()){
    			s+=i.next();
    		}
    		return s;
    	}
    }//stringRead
    
    /**
     * Método que devuelve la cadena con los datos del objeto de simulación.
     * 
     * @return String Cadena.
     */
    public String toString () {
    	String s=this.state.toString();
    	if(this.wordRead==null)
    		return s;
    	else{
    		return s+" "+this.wordRead;
    	}
    }//toString
    
    /**
     * Clona el objeto de simulación y lo devuelve.
     * 
     * @return Objeto de simulación clonado.
     */
    public SimulatorObject clone (){
    	ArrayList<Character> readWord;
    	if(wordRead!=null)
    		readWord=(ArrayList<Character>)wordRead.clone();
    	else
    		readWord=null;
    	return new SimulatorObject(state,readWord,prev,word);
    }//clone    
    
}//SimulatorObject 

    