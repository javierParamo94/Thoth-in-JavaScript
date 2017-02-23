package core.simulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import core.*;
import core.turingmachine.*;
import core.simulatorobject.SimulatorObjectTM;

/**
 * <b>Descripción</b><br>
 * Se encarga de controlar la forma de simular el proceso de validación de una palabra en una máquina de Turing.
 * <p>
 * <b>Detalles</b><br>
 * Se va calculando el estado siguiente al que la máquina de Turing pasará.
 * Siempre va un paso por delante.
 * Contiene métodos de acceso para facilitar a la parte gráfica la obtención de los datos.
 * </p>
 * 
 * @author Javier Jimeno Visitación, Iñigo Mediavilla Sáiz
 * @version 2.0
 */
public class SimulatorTM extends Simulator{
    
	//  Methods ---------------------------------------------------------------------
	
	/**
     * Constructor básico.
     * 
     * @param sInitial Estado inicial del autómata finito.
     */
    public SimulatorTM (State sInitial) {
    	super(sInitial);
        this.id=0;
    }//simulatorTM
    
    /**
     * Constructor completo.
     * 
     * @param sWord Cadena de entrada.
     * @param sInitial Estado inicial de la máquina de Turing.
     */
    public SimulatorTM (String sWord, State sInitial) {
    	super(sWord,sInitial);
        this.id=0;
        initialize();
    }//simulatorTM
    
    /**
     * Método que inicializa la estructura donde se almacenará todos los objetos de la simulación.
     * y que calcula el estado inicial de simulación y el siguiente estado.
     *  
     */
    protected void initialize(){
    	
    	HashMap<Integer,SimulatorObject> map=firstStep();
    	SimulatorObject so=map.get(1);
    	if(so.getState().isFinal()){
        	so.notHasNext();
    		so.validTrue();
    		this.noMoreStepsTrue();   	
    	}
        this.struct.put(id,map);
        nextStep();        
    }//initialize
    
    /**
     * Método que se encarga de calcular el primer paso de la simulación de la máquina de Turing,
     * este paso se calcula partiendo del estado inicial.
     * 
     * @return el primer map con el estado inicial.
     */
    protected HashMap<Integer,SimulatorObject> firstStep(){
    	
        HashMap<Integer,SimulatorObject> map= new HashMap<Integer,SimulatorObject>();
        Integer positionMap=1; //posición que ocupara en el map.
        ArrayList<Terminal> tape=new ArrayList<Terminal>();
        if(word.size()!=0){
        	Iterator<Character> ite=this.word.iterator();
        	while(ite.hasNext())
        		tape.add(new Terminal(ite.next()));
        }else
        	tape.add(new TerminalTuring());
        
        map.put(positionMap,new SimulatorObjectTM(this.initial,null,tape,0,this.getWordString()));        
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
    			for(Integer j=1; lastMap.containsKey(j);j++){
    				Integer cont=positionMap;
    				SimulatorObjectTM so=(SimulatorObjectTM) lastMap.get(j);
    				if(!so.isValid() && !so.isWrong()){ //si no es valida ni incorrecta    				
    					State state= so.getState();
    					Iterator<Transition> i= state.getTransitionsOut().iterator();    					
    					Terminal ter=so.getTape().get(so.getHead());
    					while(i.hasNext()){//para cada transición
    						TransitionTM tran = (TransitionTM)i.next();
    						if(ter.equals(tran.getIn())){
    							ArrayList<Terminal> newTape=(ArrayList<Terminal>)so.getTape().clone();
    							int pos=so.getHead();
    							newTape.remove(pos); //eliminamos lo que hemos leido de la cinta
    							newTape.add(pos, tran.getWriteTape()); //lo sustituimos por el terminal de lectura de la transición
    							if(tran.getMoveTape()=='R')
    								pos++;
    							else{
    								if(tran.getMoveTape()=='L')
    									pos--;    									
    							}
    							if(pos>=newTape.size())
    								newTape.add(new TerminalTuring());
    							else{
    								if(pos==-1){
    									pos=0;
    									newTape.add(0,new TerminalTuring());
    								}
    							}
    							SimulatorObjectTM sot=new SimulatorObjectTM(tran.getNextState(),so,newTape,pos,this.getWordString());
    							if(tran.getNextState().isFinal()){
    								sot.validTrue();
    								sot.notHasNext();
    							}
    							positionMap++;
    							map.put(positionMap,sot);    														
    						}//if
    					}//while
    					if(cont.equals(positionMap)){
    						SimulatorObject simObj=new SimulatorObjectTM(so.getState(),so,so.getTape(),so.getHead(),this.getWordString());
    						simObj.notHasNext();
    						simObj.wrongTrue();
    						positionMap++;
    						map.put(positionMap,simObj);
    					}
    				}//if
    			}//for
    			if(!map.isEmpty()){
    				this.id++;
    				struct.put(id,map);    			
    			}else
    				this.noMoreStepsTrue();  
    			this.currentMap++;  		
    			return lastMap;
    		}else
    			if(this.struct.size()!=this.currentMap){
        			this.currentMap++;
        			return this.struct.get(this.currentMap);
    			}else{
    				return null;
    			}
    	}else{
    		if(this.struct.size()!=this.currentMap)
    			this.currentMap++;
    		return this.struct.get(this.currentMap);
    	}
    }//nextStep
    
    /**
     * Método que obtiene la traza completa de un objeto de simulación.
     * 
     * @param simObj Objeto de simulación del que se quiere obtener la traza.
     * @return devuelve el map que contiene los objetos de simulación que le preceden.
     */
    public HashMap<Integer,SimulatorObject> getTrace (SimulatorObject simObj) {
    	HashMap<Integer,SimulatorObject> map=new HashMap<Integer,SimulatorObject>();
    	Integer positionMap=0;
    	SimulatorObjectTM so=(SimulatorObjectTM)simObj.clone();
    	positionMap++;
		map.put(positionMap, so);

		if(so.isWrong() && so.getPrev()!=null) //para que en la traza no salga el mismo objeto repetido
			simObj=(SimulatorObjectTM)simObj.getPrev();
		
    	while(simObj.getPrev()!=null){    		
    		SimulatorObjectTM soPrev=(SimulatorObjectTM)simObj.getPrev().clone();    		
    		positionMap++;
			map.put(positionMap, soPrev);    		
    		simObj=simObj.getPrev();
    	}
    	return map;
    }//getTrace
    
}//SimulatorTM
        