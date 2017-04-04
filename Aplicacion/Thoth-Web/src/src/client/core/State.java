package src.client.core;

import java.util.Vector;

/**
 * <b>Descripción</b><br>
 * Se encarga de establecer la interfaz de los estados de todos los autómatas.
 * <p>
 * <b>Detalles</b><br>
 * Define la estructura a seguir por todos los estados. También implementa los 
 * métodos comunes a todos ellos.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Implementa el tipo de datos Estado.
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @version 1.0 
 * 
 * @author Javier Jimeno Visitación, Iñigo Mediavilla Saiz
 * @version 2.0
 */
public abstract class State{

    //Attributes ------------------------------------------------------------------
    
    /**
     * Nombre del estado.
     */
    protected String mName;
    
    /**
     * Etiqueta del estado.
     */
    protected String mLabel;
    
    /**
     * Bandera que indica si el estado es final.
     */
    protected boolean mFinal;
    
    /**
     * Bandera que indica si el estado es inicial.
     */
    protected boolean mInitial;
    
    /**
     * Lista de transiciones de entrada.
     */
    protected Vector<Transition> mTransitionsIn;
    
    /**
     * Lista de transiciones de salida.
     */
    protected Vector<Transition> mTransitionsOut;
    
    /**
     * Etiqueta del estado en XML.
     */
    public static String mLabelXML = "state";
    
    //  Methods ---------------------------------------------------------------------

    /**
     * Constructor basico.<br>
     * Por defecto se creará el estado como no final.
     * 
     * @param name Nombre del estado.
     */
    public State(String name){
        this(name, false);
         
    }//State
    
    /**
     * Constructor completo.
     * 
     * @param name Nombre del estado.
     * @param fin Si es final o no.
     */
    public State(String name, boolean fin){
        mName = name;
        mLabel = new String("");
        mFinal = fin;
        mTransitionsIn = new Vector<Transition>(3,3);
        mTransitionsOut = new Vector<Transition>(3,3);
        
    }//State
    
    /**
     * Devuelve el nombre del estado.
     * 
     * @return Nombre del estado.
     */
    public String getName(){
       
        return mName;
    }//getName
    
    /**
     * Establece el nombre del estado.
     * 
     * @param name Nombre del estado.
     */
    public void setName(String name){
        mName = name;
        
    }//setName

    /**
     * Devuelve la etiqueta del estado.
     * 
     * @return Etiqueta del estado.
     */
    public String getLabel(){
       
        return mLabel;
    }//getLabel
    
    /**
     * Establece la etiqueta del estado.
     * 
     * @param label Etiqueta del estado.
     */
    public void setLabel(String label){
        mLabel = label;
        
    }//setLabel
    
    /**
     * Devuelve si el estado es inicial.
     * 
     * @return True si es inicial y false si no lo es.
     */
    public boolean isInitial(){
        
        return mInitial;
    }//isInitial
    
    /**
     * Devuelve si el estado es final.
     * 
     * @return True si es final y false si no lo es.
     */
    public boolean isFinal(){
        
        return mFinal;
    }//isFinal
    
    /**
     * Establece si es inicial o no un estado.
     * 
     * @param ini Si es o no inicial.
     */
    public void setInitial(boolean ini){
        mInitial = ini;
        
    }//setInitial
    
    /**
     * Establece si es final o no un estado.
     * 
     * @param fin Si es o no final.
     */
    public void setFinal(boolean fin){
        mFinal = fin;
        
    }//setFinal
    
    /**
     * Devuelve la lista de transiciones de entrada.
     * 
     * @return Lista de transiciones de entrada.
     */
    public Vector<Transition> getTransitionsIn(){
        
        return mTransitionsIn;
    }//getTransitionsIn

    /**
     * Devuelve la lista de transiciones de salida.
     * 
     * @return Lista de transiciones de salida.
     */
    public Vector<Transition> getTransitionsOut(){
        
        return mTransitionsOut;
    }//getTransitionsOut
    
    /**
     * Compara si un estado es igual a otro en base al nombre.
     * 
     * @param state Estado con el que va a ser comparado.
     * @return True si el nombre de los estados es igual, false en caso contrario.
     */
    public boolean equals (Object state){
    	if(state != null){
    		if(mName.equals(((State)state).getName()))
                return true;
    	}
    	return false;
    }//equals
    
    /**
     * Devuelve un String con el nombre del estado.
     * 
     * @return El String con el nombre del estado.
     */
    public String toString(){
        
        return mName.toString();
    }//toString   
   
   /**
    * Destruye las transiciones asociadas a este estado.
    */
   public void destroyTransitions(){
       while(0!=mTransitionsIn.size())
           mTransitionsIn.elementAt(0).freeStates();
       while(0!=mTransitionsOut.size())
           mTransitionsOut.elementAt(0).freeStates();
       
   }//destroyTransitions
   
   /**
    * Añade una nueva transicion de entrada si no existía ya en estado.
    * 
    * @param transition Nueva transicion de entrada.
    */
   public void addTransitionIn (Transition transition){
       if(!mTransitionsIn.contains(transition))
           mTransitionsIn.add(transition);
       
   }//addTransitionIn
   
   /**
    * Elimina una transicion de entrada.
    * 
    * @param transition Transicion que se desea eliminar.
    */
   public void removeTransitionIn (Transition transition){
       mTransitionsIn.remove(transition);
       
   }//removeTransitionIn
      
   /**
    * Añade una nueva transición de salida si no existía en estado.
    * 
    * @param transition Nueva transicion de salida.
    */
   public void addTransitionOut(Transition transition){
       if(!mTransitionsOut.contains(transition))
           mTransitionsOut.add(transition);
       
   }//addTransitionOut
   
   /**
    * Elimina una transicion de salida.
    * 
    * @param transition Transición que se desea eliminar.
    */
   public void removeTransitionOut (Transition transition){
       mTransitionsOut.remove(transition);
       
   }//removeTransitionOut
   
   /**
    * Clona el estado y lo devuelve.
    * 
    * @return Estado clonado.
    */
   public abstract State clone ();
   
   /**
    * Clona las transiciones del estado que se le pasa por parámetro.
    * 
    * @param state Estado del que queremos clonar sus transiciones.
    * @param vState Vector de estados del autómata clonado.
    */
   public abstract void cloneTransitions(State state, Vector<State> vState);
   
   /**
    * Devuelve un String con el tipo de la transición. Debe concordar con uno de los
    * tipos definidos en la clase Automaton.
    * 
    * @see core.Automaton
    * 
    * @return Tipo del estado
    */   
   public abstract String getStateType();
   
}//State
