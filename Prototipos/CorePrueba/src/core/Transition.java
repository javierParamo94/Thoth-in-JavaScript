package core;

import java.util.Vector;

/**
 * <b>Descripci�n</b><br>
 * Se encarga de establecer la interfaz de todas las transiciones.
 * <p>
 * <b>Detalles</b><br>
 * Define la estructura a seguir por todas las transiciones. Tambi�n implementa los 
 * m�todos comunes a todas ellas.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Implementa el tipo de datos Transici�n.
 * </p>
 * 
 * @author �lvar Arn�iz Gonz�lez, Andr�s Arn�iz Moreno
 * @version 1.0
 * 
 * @author Javier Jimeno Visitaci�n, I�igo Mediavilla Saiz
 * @version 2.0
 */
public abstract class Transition{

    //Attributes ------------------------------------------------------------------
    
    /**
     * Token de entrada de la transici�n.
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
     * Identificador de la transici�n usado para guardarla en disco.
     */
    protected int mID;
    
    /**
     * Etiqueta de la transici�n en XML.
     */
    public static String mLabelXML = "transition";
    
    //  Methods ------------------------------------------------------------------
    
    public Transition (){
        mIn = new TerminalEpsilon(); 
    }//Transition
    
    /**
     * Constructor sin terminal de entrada.<br>
     * Por defecto el terminal de entrada ser� �psilon.
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
     * @param in Terminal de entrada de la transici�n.
     * @param prevState Estado origen.
     * @param nextState Siguiente estado.
     */
    public Transition (Terminal in, State prevState, State nextState){
        mIn = in;
        mPrevState = prevState;
        mNextState = nextState;
        
    }//Transition
    
    /**
     * Constructor completo con asignaci�n del identificador de la transici�n.
     * 
     * @param in Terminal de entrada de la transici�n.
     * @param prevState Estado origen.
     * @param nextState Siguiente estado.
     * @param id Identificador de la transici�n.
     */
    public Transition (Terminal in, State prevState, State nextState, int id) {
        this(in, prevState, nextState);
        mID = id;
        
    }//Transition
    
    /**
     * Devuelve el terminal de entrada de la transici�n.
     * 
     * @return Terminal de entrada.
     */
    public Terminal getIn (){
        
        return mIn;
    }//getIn
    
    /**
     * Establece el terminal de entrada de la transici�n.
     * Si la entrada ya exist�a en otra transici�n con el mismo estado
     * origen y destino, esta transici�n se borrar� para evitar repeticiones.
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
     * Devuelve el estado origen de la transici�n.
     * 
     * @return Estado origen.
     */
    public State getPrevState (){
        
        return mPrevState;
    }//getPrevState

    /**
     * Establece el estado origen de la transici�n.
     * 
     * @param prevState Estado origen.
     */
    public void setPrevState (State prevState){
        mPrevState = prevState;
        
    }//setPrevState
    
    /**
     * Devuelve el siguiente estado de la transici�n.
     * 
     * @return Siguiente estado.
     */
    public State getNextState (){
        
        return mNextState;
    }//getNextState

    /**
     * Establece el destino de la transici�n.
     * 
     * @param nextState Estado destino.
     */
    public void setNextState (State nextState){
        mNextState = nextState;
        
    }//setNextState
    
    /**
     * Devuelve el identificador de la transici�n.
     * 
     * @return Identificador de la transici�n.
     */
    public int getID () {
        
        return mID;
    }//getID
    
    /**
     * Establece el identificador de la transici�n.
     * 
     * @param id Nuevo identificador de la transici�n.
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
     * Clona la transici�n.
     * 
     * @return Transici�n clonada.
     */
    public abstract Transition clone ();
    
    /**
     * Devuelve un String con el tipo de la transici�n. Debe concordar con uno de los
     * tipos definidos en la clase Automaton.
     * 
     * @see core.Automaton
     * 
     * @return Tipo de la transicion
     */    
    public abstract String getTransitionType();
    
    /**
     * Devuelve un vector de Strings con los datos de la transici�n
     * 
     * @return Datos de la transicion
     */    
    public abstract Vector<String> getTransitionData();
    
    /**
     * Asigna el valor de los datos de la transici�n
     * 
     * @param Datos de la transicion
     */    
    public abstract void setTransitionData(Vector<String> data);    
    
    
}//Transition
