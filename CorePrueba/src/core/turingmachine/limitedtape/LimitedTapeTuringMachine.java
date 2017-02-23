package core.turingmachine.limitedtape;

import java.util.Vector;

import core.Automaton;
import core.State;
import core.Terminal;
import core.turingmachine.TuringMachine;

/**
 * <b>Descripción</b><br>
 * Se encarga de proporcionar toda la funcionalidad que debe tener una Máquina de Turing con cinta limitada.
 * <p>
 * <b>Detalles</b><br>
 * Nos proporciona todas las operaciones que se pueden obtener de la máquina de
 * estados de una máquina de Turing con cinta limitada.<br>
 * <p>
 * <b>Funcionalidad</b><br>
 * Implementa el tipo de datos Maquina de Turing con cinta limitada.
 * </p>
 * 
 * @author Javier Jimeno Visitación, Iñigo Mediavilla Sáiz
 * @version 2.0
 */
public class LimitedTapeTuringMachine extends TuringMachine implements Cloneable{
    
    //  Methods ---------------------------------------------------------------------
    
    /**
     * Constructor básico.
     * 
     * @param name Nombre de la máquina de turing con cinta limitada a crear.
     */
    public LimitedTapeTuringMachine (String name) {
        super(name);

    }//LimitedTapeTuringMachine
    
    /**
     * Constructor con alfabeto de entrada.
     * 
     * @param name Nombre de la máquina de turing binaria a crear.
     * @param alphabet Alfabeto de entrada de la máquina de turing binaria.
     */
    public LimitedTapeTuringMachine (String name, Vector<Terminal> alphabet){
        super(name, alphabet);
    }//LimitedTapeTuringMachine
    
    /**
     * Constructor completo.
     * 
     * @param name Nombre de la máquina de turing binaria a crear.
     * @param alphabet Alfabeto de entrada de la máquina de turing binaria.
     * @param alphabetTape Alfabeto de cinta de la máquina de turing binaria.
     */
    public LimitedTapeTuringMachine (String name, Vector<Terminal> alphabet, Vector<Terminal> alphabetTape){
        super(name, alphabet,alphabetTape);        
    }//LimitedTapeTuringMachine
    
    /**
     * Crea un estado dentro de esta máquina de turing con cinta limitada asignándole sólo el nombre.<br>
     * Si el estado no existe lo crea y lo devuelve; si el estado ya existe devuelve la
     * referencia a dicho estado.
     * 
     * @param name Nombre del nuevo estado.
     * @return El nuevo estado.
     */
    public State createState (String name){
        StateLTTM state = new StateLTTM (name);
        
        return addState(state);
    }//createState
    
    /**
     * Método de clonación.<br>
     * Se encarga de clonarse a sí mismo; se trata de una clonación en profundidad.
     * 
     *  @return Clon de esta maquina de turing con cinta limitada.
     */
    public Automaton clone (){
        TuringMachine automaton = new LimitedTapeTuringMachine(mName);
        
            //Clonamos sus estados
        for(int i=0; i<mStates.size(); i++)
            automaton.addState(((StateLTTM)(mStates.elementAt(i))).clone());
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
     * Devuelve el tipo de autómata que se esta manejando.
	 * 
	 * @return Tipo de autómata
     */
	public String getAutomatonType() {
		return Automaton.LIMITED_TAPE_TURING_MACHINE;
	}//getAutomatonType
    
}//LimitedTapeTuringMachine
