package core.simulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;
import java.util.Vector;

import core.*;
import core.pushdownautomaton.*;
import core.simulatorobject.SimulatorObjectPDA;

/**
 * <b>Descripción</b><br>
 * Se encarga de controlar la forma de simular el proceso de validación de una palabra en un automata de pila.
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
public class SimulatorPDA extends Simulator implements Cloneable{
    
	//  Methods ---------------------------------------------------------------------
    
    /**
     * Constructor .
     * 
     * @param sWord Cadena de entrada.
     * @param sInitial Estado inicial del autómata de pila.
     */
    public SimulatorPDA (String sWord, State sInitial) {
    	super(sWord,sInitial);
        this.id=0;
        initialize();
    }//simulatorPDA
    
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
    			if(so.getState().isFinal()){
    				so.notHasNext();
    				so.validTrue();
    			}
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
        Integer positionMap=1; //posición que ocupara en el map.
        Stack<Character> stack=new Stack<Character>();
        stack.push(new TerminalPush().getToken());
        map.put(positionMap,new SimulatorObjectPDA(this.initial,null,null,stack,null,this.getWordString()));
        ArrayList<State> states=new ArrayList<State>();
        states.add(this.initial);
        for (Integer j=1;map.containsKey(j);j++){
        	Iterator<State> i= this.lockEpsilon(map.get(j).getState()).iterator();        	
        	while (i.hasNext()){
        		State st=i.next();
        		if(!states.contains(st)){
        			positionMap++;
        			map.put(positionMap,new SimulatorObjectPDA(st,null,map.get(j),stack,new TerminalEpsilon().toString(),this.getWordString()));
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
     * @return devuelve el map que contiene el eatdo siguiente.
     */
    public HashMap<Integer,SimulatorObject> nextStep () {
    	
    	if((struct.size()-this.currentMap)==2){
    		if(!this.hasNotMoreSteps()){
    			HashMap<Integer,SimulatorObject> map= new HashMap<Integer,SimulatorObject>();
    			Integer positionMap=0; //posición que ocupara en el map.
    			HashMap<Integer,SimulatorObject> lastMap=struct.get(id);
    			for(Integer j=1; lastMap.containsKey(j);j++){
    				Integer cont=positionMap;
    				SimulatorObjectPDA so=(SimulatorObjectPDA) lastMap.get(j);
    				if(!so.isValid() && !so.isWrong()){ //si no es valida ni incorrecta    				
    					if(!so.hasNext()){ //si no tiene siguiente
    						SimulatorObject simObj=new SimulatorObjectPDA(so.getState(),so.getWordRead(),so,so.getStack(),null,this.getWordString());
    						simObj.notHasNext();
    						simObj.wrongTrue();
    						positionMap++;
    						map.put(positionMap,simObj);
    					}else{ //si tiene siguiente				
    						State state= so.getState();
    						Iterator<Transition> i= state.getTransitionsOut().iterator();    					
    						while(i.hasNext()){//para cada transición
    							TransitionPDA tran = (TransitionPDA)i.next();
    							Stack<Character> s=(Stack<Character>)so.getStack().clone();
    							Character letter=characterToRead(this.word,so.getWordRead());    						
    							if(letter!=null && !(letter.equals(new TerminalEpsilon().getToken()))){//si queda alguna letra por leer y la letra no es la cadena vacia
    								ArrayList<Character> w=new ArrayList<Character>();						
    								if((so.getWordRead()!=null)){
    									w=(ArrayList<Character>)so.getWordRead().clone();    								
    								}    							
    								if(tran.getIn().getToken()==letter){
    									if(checkStack(s,tran.getInPush())){
    										fillStack(s,tran.getOutPush());											
    										w.add(letter);   								
    										positionMap++;
    										map.put(positionMap,new SimulatorObjectPDA(tran.getNextState(),w,so,s,tran.getInPush(),this.getWordString()));
    									}
    								}else{//si la letra que queda por leer no es el terminal de entrada de la transición
    									if(tran.getIn().equals(new TerminalEpsilon())){//si el terminal de entrada de la transición es epsilon
    										if(tran.getInPush().equals(new TerminalEpsilon().toString())){//si lo que lee de pila la transición es epsilon
    											if(!(tran.getOutPush().equals(new TerminalEpsilon().toString()))){//si lo que escribe en pila la transición no es epsilon
    												fillStack(s,tran.getOutPush());	
    											}
    											positionMap++;
    											map.put(positionMap,new SimulatorObjectPDA(tran.getNextState(),so.getWordRead(),so,s,tran.getInPush(),this.getWordString()));
    										}else{//si lo que lee de pila la transición no es epsilon
    											if(checkStack(s,tran.getInPush())){
    												fillStack(s,tran.getOutPush());
    												positionMap++;
    												map.put(positionMap,new SimulatorObjectPDA(tran.getNextState(),so.getWordRead(),so,s,tran.getInPush(),this.getWordString()));
    											}
    										}//else    									
    									}//if
    								}//else
    							}else{//no queda nada por leer    							
    								if(so.getState().isFinal()){
    									so.notHasNext();								
    									so.validTrue();
    								}else{// si tiene ninguna forma de pasar a otro estado sin consumir entrada  							
    									if(tran.getIn().equals(new TerminalEpsilon()) && this.checkStack(s,tran.getInPush())){
    										this.fillStack(s, tran.getOutPush());
    										SimulatorObjectPDA sOb=new SimulatorObjectPDA(tran.getNextState(),so.getWordRead(),so,s,tran.getInPush(),this.getWordString());
    										positionMap++;
    										map.put(positionMap,sOb);
    									}else{ // si no tiene ninguna forma de pasar a otro estado sin consumir entrada y no es final será no reconocida  
    										SimulatorObject simObj2=new SimulatorObjectPDA(so.getState(),so.getWordRead(),so,so.getStack(),null,this.getWordString());
    			    						simObj2.notHasNext();
    			    						simObj2.wrongTrue();
    			    						positionMap++;
    			    						map.put(positionMap,simObj2);
    									}
    								}//else
    							}//else
    						}//while
    					}//else
    				}//if
    				if(cont.equals(positionMap) && so.hasNext()){
    					Character let=characterToRead(this.word,so.getWordRead());
    					if(let==null || let.equals(new TerminalEpsilon().getToken())){
    						if(so.getState().isFinal()){
    							so.notHasNext();					
    							so.validTrue();
    						}else{
    							SimulatorObject simObj3=new SimulatorObjectPDA(so.getState(),so.getWordRead(),so,so.getStack(),null,this.getWordString());
	    						simObj3.notHasNext();
	    						simObj3.wrongTrue();
	    						positionMap++;
	    						map.put(positionMap,simObj3);
    						}
    					}else{
    						SimulatorObjectPDA simu=new SimulatorObjectPDA(so.getState(),so.getWordRead(),so,so.getStack(),null,this.getWordString());
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
    							SimulatorObjectPDA siOb=(SimulatorObjectPDA)map.get(j);
    							map.put(positionMap,new SimulatorObjectPDA(st,siOb.getWordRead(),siOb,siOb.getStack(),new TerminalEpsilon().toString(),this.getWordString()));
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
     * Se considera cerradura a que los tres campos de la transición sea epsilon.
     * 
     * @param state Estado del que se va a hacer la cerradura epsilon.
     * @return Vector de estados a los que se llega.
     */
    private Vector<State> lockEpsilon (State state) {

    	Iterator<Transition> i= state.getTransitionsOut().iterator();
    	Vector<State> vector= new Vector<State>();

		while(i.hasNext()){
			TransitionPDA tran=(TransitionPDA)i.next();
			if(tran.getIn().equals(new TerminalEpsilon())
					&& tran.getInPush().equals(new TerminalEpsilon().toString())
						&& tran.getOutPush().equals(new TerminalEpsilon().toString()))
				
				vector.add(tran.getNextState());
		}
		return vector;
    }//lockEpsilon
    
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
    		if(wordToRead.size()>0)
    			return wordToRead.get(0);
    		else
    			return new TerminalEpsilon().getToken();
    	}
    }//characterToRead
    
    /**
     * Método para validar que una cadena está en la pila.
     * Se sacará de la pila la cadena.
     * 
     * @param stack Pila.
     * @param inPush Cadena a comprobar.
     * @return true si ha sido sacada de la pila la cadena, false si no.
     */
    private boolean checkStack(Stack<Character> stack,String inPush){
    	
    	if(stack.size()!=0){
    		if(stack.size()<inPush.length())    		
    			return false;
    	}
    	if(inPush.equals(new TerminalEpsilon().toString()))
    		return true;
    	for(int a=(inPush.length()-1); a>=0;a--){
    		if(inPush.charAt(a)==stack.peek())
    			stack.pop();
    		else
    			return false;
    	}
    	return true;
    }//checkStack 
    
    /**
     * Método para llenar un pila con la cadena dada.
     * 
     * @param stack Pila a llenar.
     * @param outPush Cadena con la que llenar la pila.
     */
    private void fillStack(Stack<Character> stack,String outPush){
    	
    	if(outPush.equals(new TerminalEpsilon().toString()))
    		return;
    	for(int a=0; a<outPush.length();a++)
    		stack.push(outPush.charAt(a));
    }//fillStack
    
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
    	SimulatorObjectPDA so=(SimulatorObjectPDA)simObj.clone();
    	positionMap++;
		so.setWordRead(null);
		map.put(positionMap, so);

		if(so.isWrong() && so.getPrev()!=null) //para que en la traza no salga el mismo objeto repetido
			simObj=(SimulatorObjectPDA)simObj.getPrev();
		
    	while(simObj.getPrev()!=null){
    		so=(SimulatorObjectPDA)simObj.clone();
    		SimulatorObjectPDA soPrev=(SimulatorObjectPDA)simObj.getPrev().clone();
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
    
}//SimulatorPDA
        