package core.simulator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import core.*;
import core.simulatorobject.SimulatorObjectMTTM;
import core.turingmachine.multitape.*;

/**
 * <b>Descripci�n</b><br>
 * Se encarga de controlar la forma de simular el proceso de validaci�n de una palabra en una m�quina de Turing multicinta.
 * <p>
 * <b>Detalles</b><br>
 * Se va calculando el estado siguiente al que el aut�mata pasar�.
 * Siempre va un paso por delante.
 * Contiene m�todos de acceso para facilitar a la parte gr�fica la obtenci�n de los datos.
 * </p>
 * 
 * @author Javier Jimeno Visitaci�n, I�igo Mediavilla S�iz
 * @version 2.0
 */
public class SimulatorMTTM extends SimulatorTM implements Cloneable{
	
	//  Attributes ---------------------------------------------------------------------
	
	/**
     * Cadenas que se quieren validar.
     */
    private Vector<String> words;
    
	//  Methods ---------------------------------------------------------------------
    
    /**
     * Constructor completo.
     * 
     * @param sWords Cadenas que se quieren validar.
     * @param sInitial Estado inicial de la m�quina de Turing multicinta.
     */
    public SimulatorMTTM (Vector<String> sWords, State sInitial) {
    	super(sInitial);
    	words=sWords;
    	initialize();
    }//simulatorMTTM
    
    /**
     * M�todo que se encarga de calcular el primer paso de la simulaci�n de la m�quina de Turing multicinta,
     * este paso se calcula partiendo del estado inicial..
     * 
     * @return el primer map con el estado inicial.
     */
    protected HashMap<Integer,SimulatorObject> firstStep(){
    	
        HashMap<Integer,SimulatorObject> map= new HashMap<Integer,SimulatorObject>();
        Integer positionMap=1; //posici�n que ocupara en el map.
        Vector<Vector<Terminal>> mTape=new Vector<Vector<Terminal>>();
        Vector<Terminal> v;
        int tam=words.size();
        for(int i=0;i<tam;i++){
        	v=new Vector<Terminal>();
        	char[] array=words.get(i).toCharArray();
        	if(array.length!=0){
        		for(int j=0;j<array.length;j++)
        			v.add(new Terminal(array[j]));
        	}else
        		v.add(new TerminalTuring());
        	mTape.add(v);
        }
        int[] heads=new int[tam];
        for(int j=0;j<tam;j++)
        	heads[j]=0;
        map.put(positionMap,new SimulatorObjectMTTM(this.initial,null,mTape,heads,this.getWordString()));        
        return map;
    }//firstStep
      
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
    				SimulatorObjectMTTM so=(SimulatorObjectMTTM) lastMap.get(j);
    				if(!so.isValid() && !so.isWrong()){ //si no es valida ni incorrecta    				
    					State state= so.getState();
    					Iterator<Transition> i= state.getTransitionsOut().iterator();
    					    					
    					while(i.hasNext()){//para cada transici�n
    						TransitionMTTM tran = (TransitionMTTM)i.next();
    						int tam=words.size();
        					int[] pos=so.getHeads().clone();
        					boolean flag=true;
        					for(int h=0;h<tam && flag;h++){
        						if(!(so.getMTTape().get(h).get(pos[h]).equals(tran.getReadings().get(h))))
        								flag=false;
        					}
    						
    						if(flag){
    							Vector<Vector<Terminal>> newTape = new Vector<Vector<Terminal>>();
    							for(int f=0;f<tam;f++)
    									newTape.add((Vector<Terminal>)so.getMTTape().get(f).clone());    							
    							for(int g=0;g<tam;g++){
            						newTape.get(g).set(pos[g], tran.getWritings().get(g));
            						if(tran.getMovements().get(g)=='R'){
            							pos[g]++;
            							if(pos[g]>=newTape.get(g).size())
            								newTape.get(g).add(new TerminalTuring());
            						}else{
            							if(tran.getMovements().get(g)=='L'){
            								pos[g]--;
            								if(pos[g]==-1){
            									pos[g]=0;
            									newTape.get(g).add(0,new TerminalTuring());
    										}
            							}//if
            						}//else
    							}//for
    							SimulatorObjectMTTM sot=new SimulatorObjectMTTM(tran.getNextState(),so,newTape,pos,this.getWordString());
    							if(tran.getNextState().isFinal()){
    								sot.validTrue();
    								sot.notHasNext();
    							}
    							positionMap++;
    							map.put(positionMap,sot);    														
    						}
    					}//while
    					if(cont.equals(positionMap)){
    						SimulatorObject simObj=new SimulatorObjectMTTM(so.getState(),so,so.getMTTape(),so.getHeads(),this.getWordString());
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
    
}//SimulatorMTTM
        