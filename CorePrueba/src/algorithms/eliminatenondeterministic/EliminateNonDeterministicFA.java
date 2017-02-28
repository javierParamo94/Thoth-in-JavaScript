package algorithms.eliminatenondeterministic;

import java.util.Vector;

import algorithms.Algorithm;
import algorithms.InitializeException;

import core.*;
import core.finiteautomaton.FiniteAutomaton;
import core.finiteautomaton.StateFA;

/**
 * <b>Descripci�n</b><br>
 * Elimina el no determinismo de un aut�mata, si el aut�mata ya es determinista
 * no hace nada.
 * <p>
 * <b>Detalles</b><br>
 * Primero comprueba si el aut�mata es determinista, si no lo es sigue con el algoritmo.<br>
 * Se encarga de ir creando cada estado del nuevo aut�mata que engloba a varios estados
 * del anterior. Implementar� el algoritmo de construcci�n de subconjuntos.<br>
 * Las transiciones desde ahora ser�n �nicas para cada entrada consumida y estado
 * concretos eliminando el no determinismo del aut�mata.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Desarrolla el algoritmo de eliminaci�n de no determinismo en un aut�mata finito.<br>
 * Posibilita el paso a paso.
 * </p>
 * 
 * @author �lvar Arn�iz Gonz�lez, Andr�s Arn�iz Moreno
 * @version 1.0
 */
public class EliminateNonDeterministicFA extends Algorithm {
    
    //  Attributes ------------------------------------------------------------------
    
    /**
     * Aut�mata determinista a devolver tras el proceso.
     */
    private FiniteAutomaton mAutomaton2;
    
    /**
     * Lista de antiguos estados que han sido englobados en cada nuevo estado. 
     */
    private Vector<Vector<State>> mStatesGroup;
    
    /**
     * Sucesi�n que utilizaremos para darle nombre a los nuevos estados.
     */
    private Integer mStatesName;
    
    /**
     * Contador utilizado para ir recorriendo todos los estados.
     */
    private Integer mStatesNumber;
    
    // Methods ---------------------------------------------------------------------
    
    /**
     * Constructor completo del algoritmo de eliminaci�n de no determinismo.
     * 
     * @param automaton Aut�mata finito que queremos transformar a determinista.
     * @throws Lanza la excepci�n InitializeException cuando no haya estado inicial.
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
     * @return El nuevo aut�mata no determinista o uno vac�o si no se ha
     * aplicado el algoritmo.
     */
    public FiniteAutomaton getSolution () {

        return mAutomaton2;
    }//getSolution
    
    /**
     * Devuelve la lista de estados antiguos englobados en el nuevo estado del
     * aut�mata.<br>
     * Es decir, en la posici�n 1 del vector est�n todos los estados que ha
     * englobado ese estado.
     * 
     * @return Vector de vectores de estados.
     */
    public Vector<Vector<State>> getStatesGroup () {
        
        return mStatesGroup;
    }//getStatesGroup
    
    /**
     * Ejecuta el primer paso del algoritmo.<br>
     * Comprueba que el aut�mata sea no determinista para poder convertirlo.<br>
     * Hace la cerradura �psilon del estado inicial, es decir, los estados a los que se
     * puede llegar desde el estado inicial sin consumir entrada.
     * 
     * @return Devuelve falso si el aut�mata finito es determinista y no puede 
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
     * Crea un nuevo estado en el aut�mata que va a ser determinista.<br>
     * Se encarga de crear todas sus transiciones y los estados que necesite, es
     * decir los estados que sean destino de dichas transiciones.
     * 
     * @return Devuelve falso al finalizar el algoritmo, true en caso contrario.
     */
    public boolean nextStep(){
        Vector<State> temp, lockTemp, states, next;
        Vector<Terminal> alphabet = ((FiniteAutomaton)mAutomaton).obtainAlphabet();
        TerminalEpsilon eps = new TerminalEpsilon();
        
            //Si el aut�mata ya ha sido completado.
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
                        //Recorremos los estados de temp obteniendo su cerradura �psilon
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
     * Si el nuevo estado ya existe creamos la transici�n a �l, salimos de la funci�n
     * y devolvemos true, en caso contrario devolvemos false.
     * 
     * @param ter Terminal del alfabeto necesario para crear la transici�n.
     * @param next Vector de estados donde queremos saber si existe el estado.
     * @return True si el estado existe y false si no existe.
     */
    private boolean existState (Terminal ter, Vector<State> next){
            // Compara si el nuevo estado ya existe
        for(Integer k=0; k<mStatesGroup.size(); k++)
                //Si ya tenemos el estado creado -> Creamos la transici�n y salimos del bucle
            if(mStatesGroup.elementAt(k).containsAll(next) && next.containsAll(mStatesGroup.elementAt(k))){
            	((StateFA)mAutomaton2.findState(mStatesNumber.toString())).createTransitionOut(ter, mAutomaton2.findState(k.toString()));
                return true;
            }//if
    
        return false;
    }//existState
    
    /**
     * Se encarga de construir un nuevo estado ya que no ha sido creado todav�a.<br>
     * Se crean las transiciones de los estados que han sido creados hacia �l.
     * Asigna la etiqueta con los nombres de los estados que agrupa.
     * 
     * @param ter Terminal de entrada de la transici�n.
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
     * @return True si alg�n estado es final.
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
     * @return Devuelve true si se ha podido convertir un aut�mata finito no 
     * determinista en uno determinista.
     */
    public boolean allSteps(){
        if(!firstStep())
            return false;
        
        while(nextStep());
        
        return mAutomaton2.isDeterministic(new Vector<State>());
    }//allSteps
    
}//EliminateNonDeterministicFA
