package core.turingmachine.limitedtape;

import java.util.Vector;

import core.Automaton;
import core.State;
import core.Terminal;
import core.turingmachine.TuringMachine;

/**
 * <b>Descripci�n</b><br>
 * Se encarga de proporcionar toda la funcionalidad que debe tener una M�quina de Turing con cinta limitada.
 * <p>
 * <b>Detalles</b><br>
 * Nos proporciona todas las operaciones que se pueden obtener de la m�quina de
 * estados de una m�quina de Turing con cinta limitada.<br>
 * <p>
 * <b>Funcionalidad</b><br>
 * Implementa el tipo de datos Maquina de Turing con cinta limitada.
 * </p>
 * 
 * @author Javier Jimeno Visitaci�n, I�igo Mediavilla S�iz
 * @version 2.0
 */
public class LimitedTapeTuringMachine extends TuringMachine implements Cloneable{
    
    //  Methods ---------------------------------------------------------------------
    
    /**
     * Constructor b�sico.
     * 
     * @param name Nombre de la m�quina de turing con cinta limitada a crear.
     */
    public LimitedTapeTuringMachine (String name) {
        super(name);

    }//LimitedTapeTuringMachine
    
    /**
     * Constructor con alfabeto de entrada.
     * 
     * @param name Nombre de la m�quina de turing binaria a crear.
     * @param alphabet Alfabeto de entrada de la m�quina de turing binaria.
     */
    public LimitedTapeTuringMachine (String name, Vector<Terminal> alphabet){
        super(name, alphabet);
    }//LimitedTapeTuringMachine
    
    /**
     * Constructor completo.
     * 
     * @param name Nombre de la m�quina de turing binaria a crear.
     * @param alphabet Alfabeto de entrada de la m�quina de turing binaria.
     * @param alphabetTape Alfabeto de cinta de la m�quina de turing binaria.
     */
    public LimitedTapeTuringMachine (String name, Vector<Terminal> alphabet, Vector<Terminal> alphabetTape){
        super(name, alphabet,alphabetTape);        
    }//LimitedTapeTuringMachine
    
    /**
     * Crea un estado dentro de esta m�quina de turing con cinta limitada asign�ndole s�lo el nombre.<br>
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
     * M�todo de clonaci�n.<br>
     * Se encarga de clonarse a s� mismo; se trata de una clonaci�n en profundidad.
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
     * Devuelve el tipo de aut�mata que se esta manejando.
	 * 
	 * @return Tipo de aut�mata
     */
	public String getAutomatonType() {
		return Automaton.LIMITED_TAPE_TURING_MACHINE;
	}//getAutomatonType
    
}//LimitedTapeTuringMachine
