package core.turingmachine.multitape;

import java.util.Vector;

import core.Automaton;
import core.State;
import core.Terminal;
import core.Transition;
import core.turingmachine.TuringMachine;

/**
 * <b>Descripción</b><br>
 * Se encarga de proporcionar toda la funcionalidad que debe tener una Máquina de Turing multicinta.
 * <p>
 * <b>Detalles</b><br>
 * Nos proporciona todas las operaciones que se pueden obtener de la máquina de
 * estados de una máquina de turing multicinta.<br>
 * <p>
 * <b>Funcionalidad</b><br>
 * Implementa el tipo de datos Maquina de Turing multicinta.
 * </p>
 * 
 * @author Javier Jimeno Visitación, Iñigo Mediavilla Sáiz
 * @version 2.0
 */
public class MultiTapeTuringMachine extends TuringMachine implements Cloneable{
    
	//  Attributes ---------------------------------------------------------------------
    
    /**
     * Numero de cintas.
     */
    private int mTapes;
	
    //  Methods ---------------------------------------------------------------------
    
    /**
     * Constructor básico.
     * 
     * @param name Nombre de la máquina de turing multicinta a crear.
     */
    public MultiTapeTuringMachine (String name, int tapes) {
        super(name);
        mTapes=tapes;

    }//MultiTapeTuringMachine
    
    /**
     * Crea un estado dentro de esta máquina de turing multicinta asignándole sólo el nombre.<br>
     * Si el estado no existe lo crea y lo devuelve; si el estado ya existe devuelve la
     * referencia a dicho estado.
     * 
     * @param name Nombre del nuevo estado.
     * @return El nuevo estado.
     */
    public State createState (String name){
        StateMTTM state = new StateMTTM (name);
        
        return addState(state);
    }//createState
    
    /**
     * Metodo que devuelve el número de cintas de la máquina de turing multicinta.
     * 
     * @return El número de cintas.
     */
    public int getTapes (){
         
        return mTapes;
    }//getTapes

    /**
     * Devuelve si la máquina de turing es determinista o no.<br>
     * Actualiza el valor de determinismo de la máquina de turing.<br>
     * Una máquina de turing es determinista cuando;<br>
     * Al menos un estado tiene por lo menos dos transiciones con el mismo terminal de lectura de cinta.<br>
     * Guarda los estados no deterministas en un vector. 
     * 
     * @param states Vector donde se guardan los estados no deterministas.
     * @return True si es determinista, falso en caso contrario.
     */
    public boolean isDeterministic (Vector<State> states){
    	
    	if(this.mStates.size()==0){
    		mDeterministic = false;
            return false;
    	}
        Vector<State> temp = getStates();
        Vector<Vector<Terminal>> countTer = new Vector<Vector<Terminal>>();
        Vector<Transition> transOut;
        Vector<Terminal> ter;
        
            //Recorremos los estados
        for(int i=0; i<temp.size(); i++){
            transOut = temp.elementAt(i).getTransitionsOut();
                //Recorremos las transiciones
            for(int j=0; j<transOut.size(); j++){
                ter = ((TransitionMTTM)transOut.elementAt(j)).getReadings();
                if(countTer.contains(ter)){
                	states.add(temp.elementAt(i));
                }
                countTer.add(ter);
            }
            countTer.clear();
        }//for
        
        if(states.size()==0){
        	mDeterministic = true;
        	return true;
        }else{
        	mDeterministic = false;
        	return false;
        }
    }//isDeterministic
    
    /**
     * Método de clonación.<br>
     * Se encarga de clonarse a sí mismo; se trata de una clonación en profundidad.
     * 
     *  @return Clon de esta maquina de turing multicinta.
     */
    public Automaton clone (){
        TuringMachine automaton = new TuringMachine(mName);
        
            //Clonamos sus estados
        for(int i=0; i<mStates.size(); i++)
            automaton.addState(((StateMTTM)(mStates.elementAt(i))).clone());
            //Clonamos las transiciones de los estados
        for(int i=0; i<mStates.size(); i++)
            automaton.getStates().elementAt(i).cloneTransitions(mStates.elementAt(i), automaton.getStates());
            //Asignamos el estado inicial
        if(mInitialState != null)
            automaton.setInitialState(automaton.findState(getInitialState().getName()));
        
        return automaton;
    }//clone
    
    /**
     * Muestra una cadena con toda la información de la máquina de turing multicinta.<br>
     * En ella se incluyen todos los estados que la forman y las transiciones
     * de estos.
     * 
     * @return Cadena con la información.
     */
    public String toString () {
        Vector<Transition> trans;
        String solution = "";
        
            //Dibujamos el alfabeto de estados
        for(int i=0; i<getStates().size(); i++){
            if(getStates().elementAt(i).equals(getInitialState()))
                solution  += (">>");
            if(getStates().elementAt(i).isFinal())
                solution  += ("("+getStates().elementAt(i).getName().toString()+") ");
            else
                solution  += (getStates().elementAt(i).getName().toString()+"  ");
        }
        solution += "\n";
            //Dibujamos las transiciones
        for(int i=0; i<getStates().size(); i++){
            trans = getStates().elementAt(i).getTransitionsOut();
            for(int j=0; j<trans.size(); j++){
            	TransitionMTTM tr=(TransitionMTTM)trans.elementAt(j);
                solution  += (tr.getPrevState().getName()+" - "+
                        tr.toString()+" -> ");
                if(trans.elementAt(j).getNextState().isFinal())
                    solution  += ("("+trans.elementAt(j).getNextState().getName()+")");
                else
                    solution  += (" "+trans.elementAt(j).getNextState().getName());
                solution  += "\n";
            }//for
        }//for
            
        return solution;
    }//toString

    /**
     * Devuelve el tipo de autómata que se esta manejando.
	 * 
	 * @return Tipo de autómata
     */
	public String getAutomatonType() {
		return Automaton.MULTI_TAPE_TURING_MACHINE;
	}//getAutomatonType
    
}//MultiTapeTuringMachine
