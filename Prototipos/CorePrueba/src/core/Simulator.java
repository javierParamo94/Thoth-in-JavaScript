package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * <b>Descripci�n</b><br>
 * Se encarga de controlar la forma de simular la validaci�n de una palabra.
 * <p>
 * <b>Detalles</b><br>
 * Define la estructura a seguir por todos los simuladores de los automatas.
 * </p>
 * 
 * @author Javier Jimeno Visitaci�n, I�igo Mediavilla S�iz
 * @version 2.0
 */
public abstract class Simulator{
    
	// Attributes ------------------------------------------------------------------
    		
	/** 
	 * Cadena de entrada.
	 */
	protected ArrayList<Character> word;
	
	/** 
	 * Estado inicial.
	 */
	protected State initial;
	
	/** 
	 * Id de cada estructura.
	 */
	protected Integer id;
	
	/** 
	 * map actualmente visualizado.
	 */
	protected Integer currentMap;
	
	/**
	 * Estructura para guardar los objetos simulador.
	 */
	protected HashMap<Integer,HashMap<Integer,SimulatorObject>> struct;
	
	/** 
	 * Indica que no hay m�s pasos.
	 */
	protected boolean noMoreSteps;
	
    //  Methods ---------------------------------------------------------------------
    
	public Simulator (State sInitial) {        
        this.initial=sInitial;
        this.struct=new HashMap<Integer,HashMap<Integer,SimulatorObject>>();        
        this.noMoreSteps=false;
        this.currentMap=-1;
    }//Simulator    
	
	/**
     * Construye un simulador.
     * 
     * @param sWord Cadena de entrada.
     * @param sInitial Estado inicial del aut�mata finito.
     */
    public Simulator (String sWord, State sInitial) {        
        this.initial=sInitial;
        this.struct=new HashMap<Integer,HashMap<Integer,SimulatorObject>>();
        char[] ch=sWord.toCharArray();
        this.word=new ArrayList<Character>();
        for(int a=0;a<ch.length;a++){        	
        	this.word.add(ch[a]);
        }
        this.noMoreSteps=false;
        this.currentMap=-1;
    }//Simulator    
    
    /**
     * M�todo de acceso a la palabra que hay que reconocer.
     * 
     * @return Palabra.
     */
    public ArrayList<Character> getWord () {
    	return this.word;
    }//getWord    
    
    /**
     * M�todo de acceso a la palabra que hay que reconocer.
     * 
     * @return Palabra.
     */
    public String getWordString () {
    	if(this.word==null)
    		return null;
    	else{
        	String s="";
    		Iterator<Character> i=word.iterator();
    		while(i.hasNext()){
    			s+=i.next();
    		}
    		return s;
    	}
    }//getWordString
    
    /**
     * M�todo de acceso al id.
     * 
     * @return Id.
     */
    public Integer getId () {
    	return this.id;
    }//getId
    
    /**
     * M�todo que devuelve los objetos de la simulaci�n siguientes al estado visualizado.
     * En caso de no tener lo calcula teniendo en cuenta los estados de los que se parte y las transiciones.
     * 
     * @return devuelve el map que contiene el eatdo siguiente.
     */
    public abstract HashMap<Integer,SimulatorObject> nextStep ();
    
    /**
     * M�todo que devuelve los objetos de la simulaci�n anteriores al estado visualizado.
     * 
     * @return devuelve el map que contiene el estado anterior.
     */
    public HashMap<Integer,SimulatorObject> prevStep (){
    	if(this.currentMap!=0){
    		this.currentMap--;
    	}
    	return struct.get(this.currentMap);
    }//prevStep
    
    /**
     * M�todo que devuelve la traza seguida si la palabra es reconocida
     * o null si la palabra no es reconocida.
     * 
     * @return devuelve el map que contiene la traza.
     */
    public HashMap<Integer,SimulatorObject> allSteps (){
    	int contador=100;
    	HashMap<Integer,SimulatorObject> map=getInitialMap();
    	while(map!=null){
    		for(Integer j=1;map.containsKey(j);j++){
    			SimulatorObject so=map.get(j);
    			if(so.isValid())
    				return getTrace(so);
    		}
    		if(contador<0)
    			return null;
    		map=nextStep();
    		contador--;
    	}
    	return null;
    }
    
    /**
     * M�todo de acceso al map inicial.
     * 
     * @return map de inicio.
     */
    public HashMap<Integer,SimulatorObject> getInitialMap () {
    	this.currentMap=0;
    	return struct.get(0);
    }//getInitialMap
    
    /**
     * M�todo para saber si el simulador tiene m�s pasos.
     * 
     * @return true si no tiene m�s pasos, false si tiene.
     */
    public boolean hasNotMoreSteps () {
    	return this.noMoreSteps;
    }//HasNotMoreSteps
    
    /**
     * M�todo para indicar que el simulador no tiene m�s pasos.
     * 
     */
    public void noMoreStepsTrue () {
    	this.noMoreSteps=true;
    }//noMoreStepsTrue
    
    /**
     * M�todo que devuelve el n�mero del mapa visualizado actualmente.
     * 
     */
    public int getNumberMap () {
    	return this.currentMap;
    }//noMoreStepsTrue
    
    /**
     * M�todo que obtiene la traza completa de 
     * 
     * @param simObj Objeto de simulaci�n del que se quiere obtener la traza.
     * @return devuelve el map que contiene los objetos de simulaci�n que le preceden.
     */
    public abstract HashMap<Integer,SimulatorObject> getTrace (SimulatorObject simObj);
    
}//Simulator
        