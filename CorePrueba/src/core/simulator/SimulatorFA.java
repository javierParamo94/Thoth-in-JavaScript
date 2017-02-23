package core.simulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import core.*;
import core.finiteautomaton.*;
import core.simulatorobject.SimulatorObjectFA;

/**
 * <b>Descripción</b><br>
 * Se encarga de controlar la forma de simular el proceso de validación de una palabra en un automata finito.
 * <p>
 * <b>Detalles</b><br>
 * Se va calculando el estado siguiente al que el autómata pasará.
 * Siempre va un paso por delante.
 * Contiene métodos de acceso para facilitar a la parte gráfica la obtención de los datos.
 * </p>
 * 
 * @author Javier Jimeno Visitación, Iñigo Mediavilla Sáiz
 * @version 2.0
 */
public class SimulatorFA extends Simulator implements Cloneable{
    
	//  Methods ---------------------------------------------------------------------
    
    /**
     * Constructor.
     * 
     * @param sWord Cadena de entrada.
     * @param sInitial Estado inicial del autómata finito.
     */
    public SimulatorFA (String sWord, State sInitial) {
        super(sWord,sInitial);
        this.id=0;
        initialize();
    }//SimulatorFA
    
    /**
     * Método que inicializa la estructura donde se almacenará todo los objetos de la simulación.
     * y que calcula el estado inicial de simulación y el siguiente estado.
     *  
     */
    private void initialize(){
    	
    	HashMap<Integer,SimulatorObject> map=firstStep();
    	if(word.size()==0){ //si la cadena de entrada está vacia.
    		for(Integer j=1;map.containsKey(j);j++){    			
    			SimulatorObject so=map.get(j);
    			so.notHasNext();
    			if(so.getState().isFinal())
    				so.validTrue();
    		}
    	}
        this.struct.put(id,map);
        nextStep();        
    }//initialize	
    
    /**
     * Método que se encarga de calcular el primer paso de la simulación del automata,
     * este paso se calcula partiendo del estado inicial y haciendo la cerradura epsilon de este.
     * 
     * @return el primer map con los estados resultantes de la cerradura epsilon del estado inicial.
     */
    private HashMap<Integer,SimulatorObject> firstStep(){
    	
        HashMap<Integer,SimulatorObject> map= new HashMap<Integer,SimulatorObject>(); 
        Integer positionMap=1;
        map.put(positionMap,new SimulatorObjectFA(this.initial,null,null,this.getWordString()));
        ArrayList<State> states=new ArrayList<State>();
        states.add(this.initial);
        for (Integer j=1;map.containsKey(j);j++){
        	Iterator<State> i= this.lockEpsilon(map.get(j).getState()).iterator();
        	while (i.hasNext()){
        		State st=i.next();
        		if(!states.contains(st)){
        			positionMap++;
        			map.put(positionMap,new SimulatorObjectFA(st,null,map.get(j),this.getWordString()));
        			states.add(st);
        		}
        	}
        }
        return map;
    }//firstStep
        
    /**
     * Método que devuelve los objetos de la simulación siguientes al estado visualizado.
     * En caso de no tener lo calcula teniendo en cuenta los estados de los que se parte y las transiciones.
     * 
     * @return devuelve el map que contiene el estado siguiente.
     */
    public HashMap<Integer,SimulatorObject> nextStep () { 
    	
    	if((struct.size()-this.currentMap)==2){
    		if(!this.hasNotMoreSteps()){
    			HashMap<Integer,SimulatorObject> map= new HashMap<Integer,SimulatorObject>();
    			Integer positionMap=0; //posición que ocupara en el map.
    			HashMap<Integer,SimulatorObject> lastMap=struct.get(id);
    			for(Integer j=1; lastMap.containsKey(j);j++){ //para cada objeto de simulación del map
    				Integer cont=positionMap;
    				SimulatorObjectFA so=(SimulatorObjectFA)lastMap.get(j);
    				if(!so.isValid() && !so.isWrong()){ //si no es valida ni incorrecta  				
    					if(!so.hasNext()){ //si no tiene siguiente
    						SimulatorObject simObj=new SimulatorObjectFA(so.getState(),so.getWordRead(),so,this.getWordString());
    						simObj.notHasNext();
    						simObj.wrongTrue();
    						positionMap++;
    						map.put(positionMap,simObj);
    					}else{ //si tiene siguiente					    					
    						State state= so.getState();
    						Iterator<Transition> i= state.getTransitionsOut().iterator();    					
    						while(i.hasNext()){ //para cada transición
    							TransitionFA tran =(TransitionFA) i.next();
    							Character letter=characterToRead(this.word,so.getWordRead());    						
    							if(letter!=null){//si queda alguna letra por leer
    								ArrayList<Character> w=new ArrayList<Character>();
    								if((so.getWordRead()!=null)){
    									w=(ArrayList<Character>)so.getWordRead().clone();    								
    								}    							
    								if(tran.getIn().getToken()==letter){ 
    									w.add(letter);   								
    									positionMap++;
    									map.put(positionMap,new SimulatorObjectFA(tran.getNextState(),w,so,this.getWordString()));
    								}
    							}else{ //si no queda ninguna letra por leer
    								if(so.getState().isFinal()){    								
    									so.notHasNext();								
    									so.validTrue();
    								}					
    							}//else
    						}//while
    					}//else
    				}//if
    				if(cont.equals(positionMap) && so.hasNext()){
    					Character let=characterToRead(this.word,so.getWordRead());
    					if(let==null && so.getState().isFinal()){//si no queda ninguna letra por leer y el estado es final
    						so.notHasNext();								
    						so.validTrue();
    					}else{
    						SimulatorObjectFA simu=new SimulatorObjectFA(so.getState(),so.getWordRead(),so,this.getWordString());
    						simu.notHasNext();
    						simu.wrongTrue();
    						positionMap++;
    						map.put(positionMap,simu);
    					}
    				}//if
    			}//for
      
    			for (Integer j=1;map.containsKey(j);j++){
        			ArrayList<State> states=new ArrayList<State>();
    				if(!(map.get(j).isWrong() && !(map.get(j).isValid()))){
    					Iterator<State> i= this.lockEpsilon(map.get(j).getState()).iterator();
    					states.add(map.get(j).getState());
    					while (i.hasNext()){
    						State st=i.next();
    						if(!states.contains(st)){
    							positionMap++;
    							map.put(positionMap,new SimulatorObjectFA(st,map.get(j).getWordRead(),map.get(j),this.getWordString()));
    							states.add(st);
    						}
    					}
    				}
    			}
    			if(!map.isEmpty()){
    				this.id++;
    				struct.put(id,map);    			
    			}else
    				this.noMoreStepsTrue();  
    			this.currentMap++;
    			return lastMap;
    		}else{
    			if(this.struct.size()!=this.currentMap){
        			this.currentMap++;
        			return this.struct.get(this.currentMap);
    			}else{
    				return null;
    			}
    		}
    	}else{
    		if(this.struct.size()!=this.currentMap)
    			this.currentMap++;
    		return this.struct.get(this.currentMap);
    	}
    }//nextStep
    
    /**
     * Método para calcular la cerradura epsilon de un estado.
     * 
     * @param state Estado del que se va a hacer la cerradura epsilon.
     * @return Vector de estados a los que se llega consumiendo epsilon.
     */
    private Vector<State> lockEpsilon (State state) {
        
    	Iterator<Transition> i= state.getTransitionsOut().iterator();
    	Vector<State> vector= new Vector<State>();
		while(i.hasNext()){
			TransitionFA tran=(TransitionFA)i.next();
			if(tran.getIn().equals(new TerminalEpsilon()))
				vector.add(tran.getNextState());
		}
		return vector;
    }//lockepsilon
    
    /**
     * Método para saber el primer caracter de la cadena que queda por leer.
     * 
     * @param wordToRead Palabra de entrada.
     * @param read Palabra que se lleva leida.
     * @return Caracter que debe leer.
     */
    private Character characterToRead(ArrayList<Character> wordToRead,ArrayList<Character> read){
    	
    	if(read!=null){
    		if(wordToRead.equals(read))
    			return null;
    		else{
    			Iterator<Character> i=wordToRead.iterator();
    			Iterator<Character> j=read.iterator();
    			while(j.hasNext()){
    				i.next();
    				j.next();
    			}
    			return i.next();
    		}    		
    	}else{
    		return wordToRead.get(0);
    	}
    }//characterToRead
    
    /**
     * Método que obtiene la traza completa de 
     * 
     * @param simObj Objeto de simulación del que se quiere obtener la traza.
     * @return devuelve el map que contiene los objetos de simulación que le preceden.
     */
    public HashMap<Integer,SimulatorObject> getTrace (SimulatorObject simObj) {
    	HashMap<Integer,SimulatorObject> map=new HashMap<Integer,SimulatorObject>();
    	Integer positionMap=0;
    	ArrayList<Character> epsilon=new ArrayList<Character>();
    	epsilon.add(new TerminalEpsilon().getToken());
    	SimulatorObjectFA so=(SimulatorObjectFA)simObj.clone();
    	positionMap++;
		so.setWordRead(null);
		map.put(positionMap, so);
		
		if(so.isWrong() && so.getPrev()!=null){ //para que en la traza no salga el mismo objeto repetido
			simObj=(SimulatorObjectFA)simObj.getPrev();
		}
		
    	while(simObj.getPrev()!=null){
    		so=(SimulatorObjectFA)simObj.clone();
    		SimulatorObjectFA soPrev=(SimulatorObjectFA)simObj.getPrev().clone();
    		if(soPrev.getWordRead()!=null &&
    				so.getWordRead().size()==soPrev.getWordRead().size()){
    			soPrev.setWordRead(epsilon);
    		}else{    			
    			ArrayList<Character> array=new ArrayList<Character>();
    			if(so.getWordRead()!=null)
    				array.add(so.getWordRead().get(so.getWordRead().size()-1));
    			else
    				array.add(new TerminalEpsilon().getToken());
    			soPrev.setWordRead(array);
    		}
    		positionMap++;
			map.put(positionMap, soPrev);    		
    		simObj=simObj.getPrev();
    	}
    	return map;
    }//getTrace
    
}//SimulatorFA
        