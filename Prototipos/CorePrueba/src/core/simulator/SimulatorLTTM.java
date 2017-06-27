package core.simulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import core.*;
import core.turingmachine.*;
import core.simulatorobject.SimulatorObjectTM;

/**
 * <b>Descripción</b><br>
 * Se encarga de controlar la forma de simular el proceso de validación de una palabra en una máquina de Turing con cinta limitada.
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
public class SimulatorLTTM extends SimulatorTM{
    
	//  Methods ---------------------------------------------------------------------
    
    /**
     * Constructor .
     * 
     * @param sWord Cadena de entrada.
     * @param sInitial Estado inicial de la máquina de Turing con cinta limitada.
     */
    public SimulatorLTTM (String sWord, State sInitial) {
    	super(sWord,sInitial);
    }//simulatorLTTM
      
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
    					Iterator<Transition> i= state.getTransitionsOut().iterator();    					//ArrayList<Terminal> tape=so.getTape();
    					Terminal ter=so.getTape().get(so.getHead());
    					while(i.hasNext()){//para cada transición
    						TransitionTM tran = (TransitionTM)i.next();
    						if(ter.equals(tran.getIn())){
    							ArrayList<Terminal> newTape=(ArrayList<Terminal>)so.getTape().clone();
    							int pos=so.getHead();
    							newTape.remove(pos);
    							newTape.add(pos, tran.getWriteTape()); //lo sustituimos por el terminal de lectura de la transición
    							if(tran.getMoveTape()=='R')
    								pos++;
    							else{
    								if(tran.getMoveTape()=='L')
    									pos--;							
    							}
    							if(pos>=newTape.size())
    								newTape.add(new TerminalTuring());
    							SimulatorObjectTM sot=new SimulatorObjectTM(tran.getNextState(),so,newTape,pos,this.getWordString());
    							if(pos==-1){
    		    					sot.notHasNext();
    		    					sot.wrongTrue();
    							}else{
    								if(tran.getNextState().isFinal()){
    									sot.validTrue();
    									sot.notHasNext();
    								}
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
    
}//SimulatorLTTM
        