package core.simulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import core.*;
import core.simulatorobject.SimulatorObjectMHTM;
import core.turingmachine.multihead.*;

/**
 * <b>Descripci�n</b><br>
 * Se encarga de controlar la forma de simular el proceso de validaci�n
 * de una palabra en una m�quina de Turing multicabeza.
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
public class SimulatorMHTM extends SimulatorTM implements Cloneable{
	
	//  Attributes ---------------------------------------------------------------------
	
	/**
     * Tipo de modificaci�n.<br>
     * Si es 1 la modificaci�n ser� secuencial.<br>
     * Si es 2 la modificaci�n ser� paralela.
     */
    private int modification;
    
    /**
     * N�mero de cabezas de la m�quina.
     */
    private int numHeads;
    
	//  Methods ---------------------------------------------------------------------
    
    /**
     * Constructor completo.
     * 
     * @param sWords Cadenas que se quieren validar.
     * @param sInitial Estado inicial de la m�quina de Turing multicinta.
     */
    public SimulatorMHTM (String sWord, State sInitial) {
    	super(sWord, sInitial);
    }//simulatorMHTM
    
    /**
     * M�todo que se encarga de calcular el primer paso de la simulaci�n de la m�quina de Turing multicinta,
     * este paso se calcula partiendo del estado inicial.
     * 
     * @return el primer map con el estado inicial.
     */
    protected HashMap<Integer,SimulatorObject> firstStep(){    	

    	modification=((StateMHTM)initial).getMod();;
    	numHeads=((StateMHTM)initial).getHeads();
        HashMap<Integer,SimulatorObject> map= new HashMap<Integer,SimulatorObject>();
        Integer positionMap=1; //posici�n que ocupara en el map.
        ArrayList<Terminal> tape=new ArrayList<Terminal>();
        if(word.size()!=0){
        	Iterator<Character> ite=this.word.iterator();
        	while(ite.hasNext())
        		tape.add(new Terminal(ite.next()));
        }else
        	tape.add(new TerminalTuring());
        int[] heads=new int[numHeads];
        for(int j=0;j<numHeads;j++)
        	heads[j]=0;
        map.put(positionMap,new SimulatorObjectMHTM(this.initial,null,tape,heads,this.getWordString()));        
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
    				SimulatorObjectMHTM so=(SimulatorObjectMHTM) lastMap.get(j);
    				if(!so.isValid() && !so.isWrong()){ //si no es valida ni incorrecta    				
    					State state= so.getState();
    					Iterator<Transition> i= state.getTransitionsOut().iterator();
    					    					
    					while(i.hasNext()){//para cada transici�n
    						TransitionMHTM tran = (TransitionMHTM)i.next();
    						int tam=so.getHeads().length;
        					int[] pos=so.getHeads().clone();
        					ArrayList<Terminal> newTape = (ArrayList<Terminal>) so.getTape().clone();
        					boolean flag=true;
        					
        					if(modification==1){
        						for(int h=0;h<tam && flag;h++){
        							if(newTape.get(pos[h]).equals(tran.getReadings().get(h))){
        								newTape.set(pos[h], tran.getWritings().get(h));
        							}else{
        								newTape=(ArrayList<Terminal>) so.getTape().clone();
        								flag=false;
        							}
        						}
        					}else{
            					for(int h=0;h<tam && flag;h++){
            						if(!(newTape.get(pos[h]).equals(tran.getReadings().get(h))))
            								flag=false;
            					}
            					if(flag){
            						Vector<Integer> v=new Vector<Integer>();
            						for(int h=0;h<tam && flag;h++){
            							if(!v.contains(pos[h])){
            								v.add(pos[h]);
            								if(newTape.get(pos[h]).equals(tran.getReadings().get(h))){
            									newTape.set(pos[h], tran.getWritings().get(h));
            								}else{
            									newTape=(ArrayList<Terminal>) so.getTape().clone();
            									flag=false;
            								}
            							}
            						}
            					}
        					}
    						
    						if(flag){
    							boolean negative=false;
    							boolean positive=false;
    							for(int g=0;g<tam;g++){
            						if(tran.getMovements().get(g)=='R'){
            							pos[g]++;
            							if(pos[g]>=newTape.size())
            								positive=true;
            						}else{
            							if(tran.getMovements().get(g)=='L'){
            								pos[g]--;
            								if(pos[g]==-1){
            									negative=true;
    										}
            							}//if
            						}//else
    							}//for
    							if(positive)
    								newTape.add(new TerminalTuring());
    							if(negative){
									newTape.add(0,new TerminalTuring());  
    								for(int m=0;m<pos.length;m++){
    									pos[m]++;  									
    								}
    							}
    							
    							SimulatorObjectMHTM sot=new SimulatorObjectMHTM(tran.getNextState(),so,newTape,pos,this.getWordString());
    							if(tran.getNextState().isFinal()){
    								sot.validTrue();
    								sot.notHasNext();
    							}
    							positionMap++;
    							map.put(positionMap,sot);    														
    						}
    					}//while
    					if(cont.equals(positionMap)){
    						SimulatorObject simObj=new SimulatorObjectMHTM(so.getState(),so,so.getTape(),so.getHeads(),this.getWordString());
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
    
}//SimulatorMHTM
        