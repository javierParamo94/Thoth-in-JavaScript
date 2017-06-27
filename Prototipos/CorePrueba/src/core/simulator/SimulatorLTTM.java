package core.simulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import core.*;
import core.turingmachine.*;
import core.simulatorobject.SimulatorObjectTM;

/**
 * <b>Descripci�n</b><br>
 * Se encarga de controlar la forma de simular el proceso de validaci�n de una palabra en una m�quina de Turing con cinta limitada.
 * <p>
 * <b>Detalles</b><br>
 * Se va calculando el estado siguiente al que la m�quina de Turing pasar�.
 * Siempre va un paso por delante.
 * Contiene m�todos de acceso para facilitar a la parte gr�fica la obtenci�n de los datos.
 * </p>
 * 
 * @author Javier Jimeno Visitaci�n, I�igo Mediavilla S�iz
 * @version 2.0
 */
public class SimulatorLTTM extends SimulatorTM{
    
	//  Methods ---------------------------------------------------------------------
    
    /**
     * Constructor .
     * 
     * @param sWord Cadena de entrada.
     * @param sInitial Estado inicial de la m�quina de Turing con cinta limitada.
     */
    public SimulatorLTTM (String sWord, State sInitial) {
    	super(sWord,sInitial);
    }//simulatorLTTM
      
    /**
     * M�todo que devuelve los objetos de la simulaci�n siguientes al estado visualizado.
     * En caso de no tener lo calcula teniendo en cuenta los estados de los que se parte y las transiciones.
     * 
     * @return devuelve el map que contiene el estado siguiente.
     */
    public HashMap<Integer,SimulatorObject> nextStep () {
    	
    	if((struct.size()-this.currentMap)==2){
    		if(!this.hasNotMoreSteps()){  			
    			HashMap<Integer,SimulatorObject> map= new HashMap<Integer,SimulatorObject>();
    			Integer positionMap=0; //posici�n que ocupara en el map.
    			HashMap<Integer,SimulatorObject> lastMap=struct.get(id);
    			for(Integer j=1; lastMap.containsKey(j);j++){
    				Integer cont=positionMap;
    				SimulatorObjectTM so=(SimulatorObjectTM) lastMap.get(j);
    				if(!so.isValid() && !so.isWrong()){ //si no es valida ni incorrecta    				
    					State state= so.getState();
    					Iterator<Transition> i= state.getTransitionsOut().iterator();    					//ArrayList<Terminal> tape=so.getTape();
    					Terminal ter=so.getTape().get(so.getHead());
    					while(i.hasNext()){//para cada transici�n
    						TransitionTM tran = (TransitionTM)i.next();
    						if(ter.equals(tran.getIn())){
    							ArrayList<Terminal> newTape=(ArrayList<Terminal>)so.getTape().clone();
    							int pos=so.getHead();
    							newTape.remove(pos);
    							newTape.add(pos, tran.getWriteTape()); //lo sustituimos por el terminal de lectura de la transici�n
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
        