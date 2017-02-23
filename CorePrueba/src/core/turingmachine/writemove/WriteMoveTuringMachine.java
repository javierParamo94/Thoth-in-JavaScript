package core.turingmachine.writemove;

import java.util.Vector;

import core.Automaton;
import core.State;
import core.Terminal;
import core.Transition;
import core.turingmachine.TuringMachine;

/**
 * <b>Descripción</b><br>
 * Se encarga de proporcionar toda la funcionalidad que debe tener una Máquina de Turing "escribir o mover".
 * <p>
 * <b>Detalles</b><br>
 * Nos proporciona todas las operaciones que se pueden obtener de la máquina de
 * estados de una máquina de turing "escribir o mover".<br>
 * <p>
 * <b>Funcionalidad</b><br>
 * Implementa el tipo de datos Maquina de Turing "escribir o mover".
 * </p>
 * 
 * @author Javier Jimeno Visitación, Iñigo Mediavilla Sáiz
 * @version 2.0
 */
public class WriteMoveTuringMachine extends TuringMachine implements Cloneable{
    
    //  Methods ---------------------------------------------------------------------
    
    /**
     * Constructor básico.
     * 
     * @param name Nombre de la máquina de turing "escribir o mover" a crear.
     */
    public WriteMoveTuringMachine (String name) {
        super(name);

    }//WriteMoveTuringMachine
    
    /**
     * Constructor con alfabeto de entrada.
     * 
     * @param name Nombre de la máquina de turing "escribir o mover" a crear.
     * @param alphabet Alfabeto de entrada de la máquina de turing binaria.
     */
    public WriteMoveTuringMachine (String name, Vector<Terminal> alphabet){
        super(name, alphabet);
    }//WriteMoveTuringMachine
    
    /**
     * Constructor completo.
     * 
     * @param name Nombre de la máquina de turing  "escribir o mover" a crear.
     * @param alphabet Alfabeto de entrada de la máquina de turing binaria.
     * @param alphabetTape Alfabeto de cinta de la máquina de turing binaria.
     */
    public WriteMoveTuringMachine (String name, Vector<Terminal> alphabet, Vector<Terminal> alphabetTape){
        super(name, alphabet,alphabetTape);        
    }//WriteMoveTuringMachine
    
    /**
     * Crea un estado dentro de esta máquina de turing "escribir o mover" asignándole sólo el nombre.<br>
     * Si el estado no existe lo crea y lo devuelve; si el estado ya existe devuelve la
     * referencia a dicho estado.
     * 
     * @param name Nombre del nuevo estado.
     * @return El nuevo estado.
     */
    public State createState (String name){
        StateWMTM state = new StateWMTM (name);
        
        return addState(state);
    }//createState
    
    /**
     * Método de clonación.<br>
     * Se encarga de clonarse a sí mismo; se trata de una clonación en profundidad.
     * 
     *  @return Clon de esta maquina de turing "escribir o mover".
     */
    public Automaton clone (){
        TuringMachine automaton = new WriteMoveTuringMachine(mName);
        
            //Clonamos sus estados
        for(int i=0; i<mStates.size(); i++)
            automaton.addState(((StateWMTM)(mStates.elementAt(i))).clone());
            //Clonamos las transiciones de los estados
        for(int i=0; i<mStates.size(); i++)
            automaton.getStates().elementAt(i).cloneTransitions(mStates.elementAt(i), automaton.getStates());
            //Asignamos el estado inicial
        if(mInitialState != null)
            automaton.setInitialState(automaton.findState(getInitialState().getName()));
            //Actualizamos el alfabeto del clon
        for(int i=0; i<mAlphabet.size(); i++){
            automaton.addAlphabetToken(mAlphabet.elementAt(i));
            automaton.addAlphabetTape(mAlphabetTape.elementAt(i));
        }
        
        return automaton;
    }//clone
    
    /**
     * Muestra una cadena con toda la información de la máquina de turing "escribir o mover".<br>
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
            	TransitionWMTM tr=(TransitionWMTM)trans.elementAt(j);
                solution  += tr.getPrevState().getName()+" - " + tr.getIn().getToken();
                Terminal t=tr.getWriteTape();
                if(t!=null)
                	solution+=" , "+ t +" -> ";
                else
                	solution+= tr.getMoveTape()+" -> ";
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
		return Automaton.WRITE_MOVE_TURING_MACHINE;
	}//getAutomatonType
    
}//WriteMoveTuringMachine
