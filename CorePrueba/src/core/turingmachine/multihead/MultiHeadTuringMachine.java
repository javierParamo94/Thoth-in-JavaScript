package core.turingmachine.multihead;

import java.util.Vector;

import core.Automaton;
import core.State;
import core.Terminal;
import core.Transition;
import core.turingmachine.TuringMachine;

/**
 * <b>Descripci�n</b><br>
 * Se encarga de proporcionar toda la funcionalidad que debe tener una M�quina de Turing multicabeza.
 * <p>
 * <b>Detalles</b><br>
 * Nos proporciona todas las operaciones que se pueden obtener de la m�quina de
 * estados de una m�quina de turing multicabeza.<br>
 * <p>
 * <b>Funcionalidad</b><br>
 * Implementa el tipo de datos Maquina de Turing multicabeza.
 * </p>
 * 
 * @author Javier Jimeno Visitaci�n, I�igo Mediavilla S�iz
 * @version 2.0
 */
public class MultiHeadTuringMachine extends TuringMachine implements Cloneable{
    
	//  Attributes ---------------------------------------------------------------------
    
    /**
     * Numero de cabezas.
     */
    private int mHeads;
    
    /**
     * Tipo de modificaci�n.<br>
     * Si es 1 la modificaci�n ser� secuencial.<br>
     * Si es 2 la modificaci�n ser� paralela.
     */
    private int modification;
	
    //  Methods ---------------------------------------------------------------------
    
    /**
     * Constructor b�sico.
     * 
     * @param name Nombre de la m�quina de turing multicabeza a crear.
     */
    public MultiHeadTuringMachine (String name, int heads, int mod) {
        super(name);
        mHeads=heads;
        modification=mod;

    }//MultiHeadTuringMachine
    
    /**
     * Crea un estado dentro de esta m�quina de turing multicabeza asign�ndole s�lo el nombre.<br>
     * Si el estado no existe lo crea y lo devuelve; si el estado ya existe devuelve la
     * referencia a dicho estado.
     * 
     * @param name Nombre del nuevo estado.
     * @return El nuevo estado.
     */
    public State createState (String name){
        StateMHTM state = new StateMHTM (name);
        
        return addState(state);
    }//createState
    
    /**
     * Metodo que devuelve el n�mero de cabezas de la m�quina de turing multicabeza.
     * 
     * @return El n�mero de cabezas.
     */
    public int getHeads (){
         
        return mHeads;
    }//getHeads
    
    /**
     * Metodo que devuelve el tipo de modificaci�n.
     * 
     * @return El tipo de modificaci�n.
     */
    public int getMod (){
         
        return modification;
    }//getMod

    /**
     * Devuelve si la m�quina de turing es determinista o no.<br>
     * Actualiza el valor de determinismo de la m�quina de turing.<br>
     * Una m�quina de turing es determinista cuando;<br>
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
                ter = ((TransitionMHTM)transOut.elementAt(j)).getReadings();
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
	 * Cambia el estado inicial del aut�mata.<br>
     * Comprueba que el estado inicial ya pertenezca al automata, si no es as�
     * le agrega al aut�mata y se le indica que el estado es inicial.
     * Marca en el estado inicial el n�mero de cabeza y el tipo de modificaci�n, 
     * ya que ser� necesario para la smulaci�n.
	 * 
	 * @param initial Estado inicial.
	 */
	public void setInitialState (State initial){
        super.setInitialState(initial);
        ((StateMHTM)initial).setHeads(mHeads);
        ((StateMHTM)initial).setMod(modification);       
		
	}//setInitialState
    
    /**
     * M�todo de clonaci�n.<br>
     * Se encarga de clonarse a s� mismo; se trata de una clonaci�n en profundidad.
     * 
     *  @return Clon de esta maquina de turing multicabeza.
     */
    public Automaton clone (){
        TuringMachine automaton = new TuringMachine(mName);
        
            //Clonamos sus estados
        for(int i=0; i<mStates.size(); i++)
            automaton.addState(((StateMHTM)(mStates.elementAt(i))).clone());
            //Clonamos las transiciones de los estados
        for(int i=0; i<mStates.size(); i++)
            automaton.getStates().elementAt(i).cloneTransitions(mStates.elementAt(i), automaton.getStates());
            //Asignamos el estado inicial
        if(mInitialState != null)
            automaton.setInitialState(automaton.findState(getInitialState().getName()));
        
        return automaton;
    }//clone
    
    /**
     * Muestra una cadena con toda la informaci�n de la m�quina de turing multicabeza.<br>
     * En ella se incluyen todos los estados que la forman y las transiciones
     * de estos.
     * 
     * @return Cadena con la informaci�n.
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
            	TransitionMHTM tr=(TransitionMHTM)trans.elementAt(j);
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
     * Devuelve el tipo de aut�mata que se esta manejando.
	 * 
	 * @return Tipo de aut�mata
     */
	public String getAutomatonType() {
		return Automaton.MULTI_HEAD_TURING_MACHINE;
	}//getAutomatonType
    
}//MultiHeadTuringMachine
