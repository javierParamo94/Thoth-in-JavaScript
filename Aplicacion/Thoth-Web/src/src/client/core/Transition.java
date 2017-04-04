package src.client.core;

import java.util.Vector;

/**
 * <b>Descripción</b><br>
 * Se encarga de establecer la interfaz de todas las transiciones.
 * <p>
 * <b>Detalles</b><br>
 * Define la estructura a seguir por todas las transiciones. También implementa los 
 * métodos comunes a todas ellas.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Implementa el tipo de datos Transición.
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @version 1.0
 * 
 * @author Javier Jimeno Visitación, Iñigo Mediavilla Saiz
 * @version 2.0
 */
public abstract class Transition{

    //Attributes ------------------------------------------------------------------
    
    /**
     * Token de entrada de la transición.
     */
    protected Terminal mIn;
    
    /**
     * Estado origen.
     */
    protected State mPrevState;
    
    /**
     * Estado siguiente.
     */
    protected State mNextState;
    
    /**
     * Identificador de la transición usado para guardarla en disco.
     */
    protected int mID;
    
    /**
     * Etiqueta de la transición en XML.
     */
    public static String mLabelXML = "transition";
    
    //  Methods ------------------------------------------------------------------
    
    public Transition (){
        mIn = new TerminalEpsilon(); 
    }//Transition
    
    /**
     * Constructor sin terminal de entrada.<br>
     * Por defecto el terminal de entrada será épsilon.
     * 
     * @param prevState Estado origen.
     * @param nextState Siguiente estado.
     */
    public Transition (State prevState, State nextState){
        mIn = new TerminalEpsilon();
        mPrevState = prevState;
        mNextState = nextState;
        
    }//Transition
    
    /**
     * Constructor completo.
     * 
     * @param in Terminal de entrada de la transición.
     * @param prevState Estado origen.
     * @param nextState Siguiente estado.
     */
    public Transition (Terminal in, State prevState, State nextState){
        mIn = in;
        mPrevState = prevState;
        mNextState = nextState;
        
    }//Transition
    
    /**
     * Constructor completo con asignación del identificador de la transición.
     * 
     * @param in Terminal de entrada de la transición.
     * @param prevState Estado origen.
     * @param nextState Siguiente estado.
     * @param id Identificador de la transición.
     */
    public Transition (Terminal in, State prevState, State nextState, int id) {
        this(in, prevState, nextState);
        mID = id;
        
    }//Transition
    
    /**
     * Devuelve el terminal de entrada de la transición.
     * 
     * @return Terminal de entrada.
     */
    public Terminal getIn (){
        
        return mIn;
    }//getIn
    
    /**
     * Establece el terminal de entrada de la transición.
     * Si la entrada ya existía en otra transición con el mismo estado
     * origen y destino, esta transición se borrará para evitar repeticiones.
     * 
     * @param in Terminal de entrada.
     */
    public void setIn (Terminal in){
        Transition temp;
        
        for(int i=0; i<mPrevState.getTransitionsOut().size(); i++){
            temp = mPrevState.getTransitionsOut().elementAt(i);
            
            if(temp.mIn.equals(in) && temp.mNextState.equals(mNextState)){
                freeStates();
                return;
            }
        }
        mIn = in;
        
    }//setIn
    
    /**
     * Devuelve el estado origen de la transición.
     * 
     * @return Estado origen.
     */
    public State getPrevState (){
        
        return mPrevState;
    }//getPrevState

    /**
     * Establece el estado origen de la transición.
     * 
     * @param prevState Estado origen.
     */
    public void setPrevState (State prevState){
        mPrevState = prevState;
        
    }//setPrevState
    
    /**
     * Devuelve el siguiente estado de la transición.
     * 
     * @return Siguiente estado.
     */
    public State getNextState (){
        
        return mNextState;
    }//getNextState

    /**
     * Establece el destino de la transición.
     * 
     * @param nextState Estado destino.
     */
    public void setNextState (State nextState){
        mNextState = nextState;
        
    }//setNextState
    
    /**
     * Devuelve el identificador de la transición.
     * 
     * @return Identificador de la transición.
     */
    public int getID () {
        
        return mID;
    }//getID
    
    /**
     * Establece el identificador de la transición.
     * 
     * @param id Nuevo identificador de la transición.
     */
    public void setID (int id) {
        mID = id;
        
    }//setID
    
    /**
     * Libera las referencias a ambos estados.
     */
    public void freeStates (){
        mPrevState.removeTransitionOut(this);
        mNextState.removeTransitionIn(this);
        
    }//freeStates
    
    /**
     * Devuelve true si la transicion que se le pasa es igual.
     * 
     * @return True si es igual.
     */
    public abstract boolean equals (Object transition);
    
    /**
     * Clona la transición.
     * 
     * @return Transición clonada.
     */
    public abstract Transition clone ();
    
    /**
     * Devuelve un String con el tipo de la transición. Debe concordar con uno de los
     * tipos definidos en la clase Automaton.
     * 
     * @see core.Automaton
     * 
     * @return Tipo de la transicion
     */    
    public abstract String getTransitionType();
    
    /**
     * Devuelve un vector de Strings con los datos de la transición
     * 
     * @return Datos de la transicion
     */    
    public abstract Vector<String> getTransitionData();
    
    /**
     * Asigna el valor de los datos de la transición
     * 
     * @param Datos de la transicion
     */    
    public abstract void setTransitionData(Vector<String> data);    
    
    
}//Transition
