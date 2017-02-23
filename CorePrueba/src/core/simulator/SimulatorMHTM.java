package core.simulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import core.*;
import core.simulatorobject.SimulatorObjectMHTM;
import core.turingmachine.multihead.*;

/**
 * <b>Descripción</b><br>
 * Se encarga de controlar la forma de simular el proceso de validación
 * de una palabra en una máquina de Turing multicabeza.
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
public class SimulatorMHTM extends SimulatorTM implements Cloneable{
	
	//  Attributes ---------------------------------------------------------------------
	
	/**
     * Tipo de modificación.<br>
     * Si es 1 la modificación será secuencial.<br>
     * Si es 2 la modificación será paralela.
     */
    private int modification;
    
    /**
     * Número de cabezas de la máquina.
     */
    private int numHeads;
    
	//  Methods ---------------------------------------------------------------------
    
    /**
     * Constructor completo.
     * 
     * @param sWords Cadenas que se quieren validar.
     * @param sInitial Estado inicial de la máquina de Turing multicinta.
     */
    public SimulatorMHTM (String sWord, State sInitial) {
    	super(sWord, sInitial);
    }//simulatorMHTM
    
    /**
     * Método que se encarga de calcular el primer paso de la simulación de la máquina de Turing multicinta,
     * este paso se calcula partiendo del estado inicial.
     * 
     * @return el primer map con el estado inicial.
     */
    protected HashMap<Integer,SimulatorObject> firstStep(){    	

    	modification=((StateMHTM)initial).getMod();;
    	numHeads=((StateMHTM)initial).getHeads();
        HashMap<Integer,SimulatorObject> map= new HashMap<Integer,SimulatorObject>();
        Integer positionMap=1; //posición que ocupara en el map.
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
    				SimulatorObjectMHTM so=(SimulatorObjectMHTM) lastMap.get(j);
    				if(!so.isValid() && !so.isWrong()){ //si no es valida ni incorrecta    				
    					State state= so.getState();
    					Iterator<Transition> i= state.getTransitionsOut().iterator();
    					    					
    					while(i.hasNext()){//para cada transición
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
        