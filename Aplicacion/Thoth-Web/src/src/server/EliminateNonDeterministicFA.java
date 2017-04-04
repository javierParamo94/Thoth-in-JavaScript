package src.server;

import java.util.Vector;

import src.client.algorithms.Algorithm;
import src.client.algorithms.InitializeException;

import src.client.core.*;
import src.client.core.finiteautomaton.FiniteAutomaton;
import src.client.core.finiteautomaton.StateFA;

/**
 * <b>Descripción</b><br>
 * Elimina el no determinismo de un autómata, si el autómata ya es determinista
 * no hace nada.
 * <p>
 * <b>Detalles</b><br>
 * Primero comprueba si el autómata es determinista, si no lo es sigue con el algoritmo.<br>
 * Se encarga de ir creando cada estado del nuevo autómata que engloba a varios estados
 * del anterior. Implementará el algoritmo de construcción de subconjuntos.<br>
 * Las transiciones desde ahora serán únicas para cada entrada consumida y estado
 * concretos eliminando el no determinismo del autómata.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Desarrolla el algoritmo de eliminación de no determinismo en un autómata finito.<br>
 * Posibilita el paso a paso.
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @version 1.0
 */
public class EliminateNonDeterministicFA extends Algorithm {
    
    //  Attributes ------------------------------------------------------------------
    
    /**
     * Autómata determinista a devolver tras el proceso.
     */
    private FiniteAutomaton mAutomaton2;
    
    /**
     * Lista de antiguos estados que han sido englobados en cada nuevo estado. 
     */
    private Vector<Vector<State>> mStatesGroup;
    
    /**
     * Sucesión que utilizaremos para darle nombre a los nuevos estados.
     */
    private Integer mStatesName;
    
    /**
     * Contador utilizado para ir recorriendo todos los estados.
     */
    private Integer mStatesNumber;
    
    // Methods ---------------------------------------------------------------------
    
    /**
     * Constructor completo del algoritmo de eliminación de no determinismo.
     * 
     * @param automaton Autómata finito que queremos transformar a determinista.
     * @throws Lanza la excepción InitializeException cuando no haya estado inicial.
     */
    public EliminateNonDeterministicFA (FiniteAutomaton automaton) throws InitializeException{
        super(automaton);
        
        mAutomaton2 = new FiniteAutomaton(mAutomaton.getName()+" AFD");
        mStatesGroup = new Vector<Vector<State>>(5, 5);
        mStatesName = 0;
        mStatesNumber = 0;
        
    }//EliminateNonDetrministicFA
    
    /**
     * Devuelve el nuevo automata no determinista creado por el algoritmo.
     * 
     * @return El nuevo autómata no determinista o uno vacío si no se ha
     * aplicado el algoritmo.
     */
    public FiniteAutomaton getSolution () {

        return mAutomaton2;
    }//getSolution
    
    /**
     * Devuelve la lista de estados antiguos englobados en el nuevo estado del
     * autómata.<br>
     * Es decir, en la posición 1 del vector están todos los estados que ha
     * englobado ese estado.
     * 
     * @return Vector de vectores de estados.
     */
    public Vector<Vector<State>> getStatesGroup () {
        
        return mStatesGroup;
    }//getStatesGroup
    
    /**
     * Ejecuta el primer paso del algoritmo.<br>
     * Comprueba que el autómata sea no determinista para poder convertirlo.<br>
     * Hace la cerradura épsilon del estado inicial, es decir, los estados a los que se
     * puede llegar desde el estado inicial sin consumir entrada.
     * 
     * @return Devuelve falso si el autómata finito es determinista y no puede 
     * continuar el algoritmo.
     */
    public boolean firstStep(){
        if(mAutomaton.isDeterministic(new Vector<State>())){
                //Si es determinista 
            mAutomaton2 = (FiniteAutomaton)mAutomaton.clone();
            return false;
        }
        StateFA st=(StateFA)mAutomaton.getInitialState();
        Vector<State> states =st.nextStates(null);
            //Asignamos como inicial el nuevo estado. Con nombre '0'.
        mAutomaton2.setInitialState(mAutomaton2.createState(mStatesName.toString(), isSomeStateFinal(states)));
        mStatesGroup.add(mStatesName, states);
            //Asignamos la etiqueta con el nombre de los estados que ha agrupado.
        mAutomaton2.getInitialState().setLabel(states.toString().substring(1, states.toString().length()-1));
        
        return true;
    }//firstStep

    /**
     * Siguiente paso del algoritmo.<br>
     * Crea un nuevo estado en el autómata que va a ser determinista.<br>
     * Se encarga de crear todas sus transiciones y los estados que necesite, es
     * decir los estados que sean destino de dichas transiciones.
     * 
     * @return Devuelve falso al finalizar el algoritmo, true en caso contrario.
     */
    public boolean nextStep(){
        Vector<State> temp, lockTemp, states, next;
        Vector<Terminal> alphabet = ((FiniteAutomaton)mAutomaton).obtainAlphabet();
        TerminalEpsilon eps = new TerminalEpsilon();
        
            //Si el autómata ya ha sido completado.
        if(mStatesNumber >= mAutomaton2.getStates().size()){
                //Asignamos el alfabeto.
            mAutomaton2.obtainAlphabet();
            return false;
        }
        
        states = mStatesGroup.elementAt(mStatesNumber);
            //Obtenemos los siguientes estados para todo el alfabeto
        for(Terminal ter : alphabet){
            if(!ter.equals(eps)){
                    //Inicializamos next
                next = new Vector<State>();
                    //Recorremos los antiguos estados del AFND que han sido englobados
                for(State st : states){
                    temp = ((StateFA)st).nextStates(ter);
                        //Recorremos los estados de temp obteniendo su cerradura épsilon
                    for(State stTemp : temp){
                        lockTemp =((StateFA)stTemp).nextStates(null);
                        next.removeAll(lockTemp);
                        next.addAll(lockTemp);
                    }
                }//for
                    
                    //Si no existe siguientes estado ni comprobamos ni creamos.
                if(next.size() != 0)
                        //Si el estado no ha sido creado -> Le creamos
                    if(!existState(ter, next))
                        buildState(ter, next);
            }
        }//for
            //Aumentamos en 1 los nuevos estados que hemos analizado
        mStatesNumber++;
        
        return true;
    }//nextStep
    
    /**
     * Compara si existe el nuevo estado.<br>
     * Si el nuevo estado ya existe creamos la transición a él, salimos de la función
     * y devolvemos true, en caso contrario devolvemos false.
     * 
     * @param ter Terminal del alfabeto necesario para crear la transición.
     * @param next Vector de estados donde queremos saber si existe el estado.
     * @return True si el estado existe y false si no existe.
     */
    private boolean existState (Terminal ter, Vector<State> next){
            // Compara si el nuevo estado ya existe
        for(Integer k=0; k<mStatesGroup.size(); k++)
                //Si ya tenemos el estado creado -> Creamos la transición y salimos del bucle
            if(mStatesGroup.elementAt(k).containsAll(next) && next.containsAll(mStatesGroup.elementAt(k))){
            	((StateFA)mAutomaton2.findState(mStatesNumber.toString())).createTransitionOut(ter, mAutomaton2.findState(k.toString()));
                return true;
            }//if
    
        return false;
    }//existState
    
    /**
     * Se encarga de construir un nuevo estado ya que no ha sido creado todavía.<br>
     * Se crean las transiciones de los estados que han sido creados hacia él.
     * Asigna la etiqueta con los nombres de los estados que agrupa.
     * 
     * @param ter Terminal de entrada de la transición.
     * @param next Estados a los que agrupa.
     */
    private void buildState(Terminal ter, Vector<State> next){
        mStatesName++;
            //Creamos el estado
        mAutomaton2.createState(mStatesName.toString(), isSomeStateFinal(next));
        mStatesGroup.add(mStatesName, next);
            //Asignamos la etiqueta con el nombre de los estados que ha agrupado.
        mAutomaton2.findState(mStatesName.toString()).setLabel(next.toString().substring(1, next.toString().length()-1));
            //Crear transicion
        ((StateFA)mAutomaton2.findState(mStatesNumber.toString())).createTransitionOut(ter, mAutomaton2.findState(mStatesName.toString()));
        
    }//buildState
    
    /**
     * Recorre el vector y devuelve true si alguno de los estados es final.
     * 
     * @return True si algún estado es final.
     */
    private boolean isSomeStateFinal (Vector<State> list){
        for(int i=0; i<list.size(); i++)
            if(list.elementAt(i).isFinal())
                return true;
        
        return false;
    }//isSomeStateFinal
    
    /**
     * Realiza todos los pasos del algoritmo.
     * 
     * @return Devuelve true si se ha podido convertir un autómata finito no 
     * determinista en uno determinista.
     */
    public boolean allSteps(){
        if(!firstStep())
            return false;
        
        while(nextStep());
        
        return mAutomaton2.isDeterministic(new Vector<State>());
    }//allSteps
    
}//EliminateNonDeterministicFA
