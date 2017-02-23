package core;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * <b>Descripci�n</b><br>
 * Contiene los datos que se van a proporcionar para simular la validaci�n de una palabra.
 * <p>
 * 
 * @author Javier Jimeno Visitaci�n, I�igo Mediavilla S�iz
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
	 * Objeto de simulaci�n anterior.
	 */
	protected SimulatorObject prev;
	
	/** 
	 * Si tiene alg�n objeto de simulaci�n posterior.
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
     * Construye un objeto de simulaci�n.
     * 
     * @param oState Estado.
     * @param oWordRead Cadena que se lleva le�da.
     * @param oPrev Objeto de simulaci�n que le precede.
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
     * M�todo de acceso al estado.
     * 
     * @return Estado.
     */
    public State getState () {
    	return this.state;    	
    }//get State
    
    /**
     * M�todo que devuelve la cadena le�da.
     * 
     * @return Cadena le�da.
     */
    public ArrayList<Character> getWordRead () {
    	return this.wordRead;	
    }//getWordRead
    
    /**
     * M�todo que modifica la cadena le�da.
     * 
     * @param Nueva cadena le�da.
     */
    public void setWordRead (ArrayList<Character> word) {
    	this.wordRead=word;    	
    }//setWordRead
    
    /**
     * M�todo de acceso al objeto de simulaci�n anterior.
     * 
     * @return SimulatorObject Objeto de simulaci�n.
     */
    public SimulatorObject getPrev () {
    	return this.prev;    	
    }//getPrev
    
    /**
     * M�todo para saber si tiene un objeto de simulaci�n posterior.
     * 
     * @return true si tiene, false si no.
     */
    public boolean hasNext () {
    	return this.hasNext;    	
    }//hasNext
    
    /**
     * M�todo para para indicar que no tiene un objeto de simulaci�n posterior.
     * 
     */
    public void notHasNext () {
    	this.hasNext=false;    	
    }//notHasNext
    
    /**
     * M�todo para saber si la palabra es aceptada.
     * 
     * @return true si es aceptada, false si no.
     */
    public boolean isValid () {
    	return this.valid;    	
    }//isValid
    
    /**
     * M�todo para indicar que la palabra es aceptada.
     * 
     */
    public void validTrue () {
    	this.valid=true;    	
    }//validTrue
    
    /**
     * M�todo para saber si la palabra no es aceptada.
     * 
     * @return true si no es aceptada, false si no.
     */
    public boolean isWrong () {
    	return this.wrong;    	
    }//isWrong
    
    /**
     * M�todo para indicar que la palabra no es aceptada.
     * 
     */
    public void wrongTrue () {
    	this.wrong=true;    	
    }//WrongTrue
    
    /**
     * M�todo que devuelve la palabra para validar.
     * 
     */
    public String getWord () {
    	return this.word;    	
    }//getWord
    
    /**
     * M�todo de acceso a la cadena leida.
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
     * M�todo que devuelve la cadena con los datos del objeto de simulaci�n.
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
    	return new SimulatorObject(state,readWord,prev,word);
    }//clone    
    
}//SimulatorObject 

    