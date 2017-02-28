package algorithms.minimize;

import java.util.Vector;

import algorithms.Algorithm;
import algorithms.InitializeException;
import algorithms.Partition;

import core.*;
import core.finiteautomaton.*;

/**
 * <b>Descripción</b><br>
 * Se encarga de la minimización de autómatas finitos mediante el método de
 * construcción de subconjuntos.
 * <p>
 * <b>Detalles</b><br>
 * Comienza construyendo dos subconjuntos (uno con los estados finales y otro con
 * los no finales), a partir de ahí va calculando a donde va cada estado con los
 * terminales del alfabeto. El algoritmo finaliza cuando la partición actual es
 * igual que la partición anterior y es entonces cuando crea el nuevo autómata finito.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Desarrolla el algoritmo de construcción de subconjuntos.
 * Posibilita el paso a paso.
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @version 1.0
 */
public class MinimizeFA extends Algorithm {

    // Attributes ------------------------------------------------------------------
    
    /**
     * Autómata finito minimizado.
     */
    private FiniteAutomaton mMinimize;
    
    /**
     * Partición usada por el algoritmo.
     */
    public Partition mPartition;
    
    /**
     * Indica si el alutómata ha cambiado al completarse.<br>
     * Por defecto el valor será true.
     */
    public boolean mChanged;
 
    // Methods ---------------------------------------------------------------------
    
    /**
     * Constructor completo.
     * 
     * @param automaton Autómata que queremos minimizar.
     * @throws InitializeException Lanza esta excepción cuando el autómata no tiene
     * estado inicial.
     */
    public MinimizeFA (Automaton automaton) throws InitializeException {
        super(automaton.clone());
        
        mMinimize = new FiniteAutomaton("Minimal "+mAutomaton.getName());
        mMinimize.setAlphabet(mAutomaton.getAlphabet());
        mChanged = true;
        
    }//Minimize

    /**
     * Devuelve el nuevo automata mínimo.
     * 
     * @return El autómata mínimo.
     */
    public FiniteAutomaton getSolution(){

        return mMinimize;
    }//getSolution
    
    /**
     * Devuelve el autómata completado
     * 
     * @return Autómata completo.
     */
    public FiniteAutomaton getCompleted(){
        
        return (FiniteAutomaton)mAutomaton;
    }//getCompleted
    
    /**
     * Primer paso del algoritmo.<br>
     * Se encarga de transformarle en un autómata finito determinista completo para 
     * poder ejecutar el algoritmo de minimización. También asigna y ordena mListStates
     * y mPartitionA.
     * 
     * @return Devuelve true si debe continuar el algoritmo.
     */
    public boolean firstStep () {
            //Si el autómata no es determinista devuelve false.
        if(!mAutomaton.isDeterministic(new Vector<State>()))
            return false;
        
            //Creamos el estado sumidero
        mChanged = ((FiniteAutomaton)mAutomaton).completeAutomaton();
            //Inicializamos mPartition
        mPartition = new Partition(mAutomaton);
        setFinals();
            //Ordenamos mListStates.
        mPartition.quickSort(0, mPartition.mSize-1);
        
        return true;
    }//firstStep

    /**
     * Establece los estados finales a 1 y el resto a 0.
     */
    private void setFinals (){
            //Inicializamos mPartitionA: Estados finales a 1 y el resto a 0.
        for(int i=0; i<mPartition.mListStates.size(); i++)
            if(!mPartition.mListStates.elementAt(i).isFinal())
                mPartition.mPartitionA[i] = new Integer(0);
            else
                mPartition.mPartitionA[i] = new Integer(1);
        
    }//setFinals
    
    /**
     * Siguientes pasos del algoritmo.<br>
     * Actualiza la nueva partición y en caso de que sea igual que la partición
     * anterior devuelve false y el algoritmo ha acabado; en caso contrario
     * devuelve true y debe seguir ejecutandose el algoritmo. 
     * 
     * @return True si se puede seguir aplicando el algoritmo de minimización y
     * false si ya es mínimo el autómata.
     */
    public boolean nextStep () {
        int iNext = 0;
        boolean flag;
        
            //Inicializamos mDestinyList.
        mPartition.initializeDestinyList(mAutomaton);
        mPartition.mPartitionB[0] = 0;
            //Recorremos mPartitionA.
        for(int i=1; i<mPartition.mSize; i++){
            flag = true;
                //Si la particion antigua era la misma.
            if(mPartition.mPartitionA[i].equals(mPartition.mPartitionA[i-1])){
                    //Recorremos hasta el principio buscando alguna función de transición igual que ésta
                for(int j=i-1; j>=0 && flag; j--){
                        //Si la funcion de transicion es la misma.
                    if(mPartition.equalsVector(mPartition.mDestiny[i], mPartition.mDestiny[j]) &&
                            mPartition.mPartitionA[i].equals(mPartition.mPartitionA[j])){
                        mPartition.mPartitionB[i] = mPartition.mPartitionB[j];
                        flag = false;
                    }
                }
            }
                //Aumentamos iNext y le asignamos como nombre de partición.
            if(flag)
                mPartition.mPartitionB[i] = ++iNext;
        }//for
        
            //Si la partición antigua es igual a la nueva -> Autómata minimizado
        if(mPartition.equalsVector(mPartition.mPartitionA, mPartition.mPartitionB)){
            buildAutomaton();
            return false;
        }   //Sino
        else{
            mPartition.mPartitionA = mPartition.mPartitionB;
            mPartition.quickSort(0, mPartition.mSize-1);
            mPartition.mPartitionB = new Integer[mPartition.mSize];
            return true;
        }
    }//nextStep
        
    /**
     * Crea el autómata minimo asignándole sus estados y transiciones.<br>
     * Esto ocurre cuando se ha ejecutado el algoritmo satisfactoriamente.
     */
    private void buildAutomaton(){
        Vector<String> label;
        boolean flagEnd, flagStart;
        
            //Asignamos el mismo alfabeto.
        for(int i=0; i<mAutomaton.getAlphabet().size(); i++)
            mMinimize.addAlphabetToken(mAutomaton.getAlphabet().elementAt(i));
        
            //Recorremos mDestiny.
        for(Integer j=0, i=0; i<mPartition.mSize; i++){
            label = new Vector<String>();
            flagEnd = flagStart = false;
                //Recorremos mPartitionA mientras i==mPartitionA[j] y j<mSize.
            for(;j<mPartition.mSize && i.equals(mPartition.mPartitionA[j]); j++){
                    //Si mPartitionA[j] == i -> añadimos su nombre a la etiqueta.
                label.add(mPartition.mListStates.elementAt(j).toString());
                    //Si es final acutalizamos flagEnd.
                if(mPartition.mListStates.elementAt(j).isFinal())
                    flagEnd = true;
                    //Si es inicial actualizamos flagStart.
                if(mPartition.mListStates.elementAt(j).equals(mAutomaton.getInitialState()))
                    flagStart = true;
            }//for
            
                //Si la etiqueta no está vacía -> Creamos el estado.
            if(!label.isEmpty())
                createState(i.toString(), label, flagStart, flagEnd);
        }//for
        
            //Creamos las transiciones del autómata nuevo
        createTransitions();
            //Si existe el estado sumidero -> Le borramos
        removeDrain();
        
            //Asignamos el alfabeto.
        mMinimize.obtainAlphabet();
    }//buildAutomaton
    
    /**
     * Crea un estado en el autómata mínimo que estamos construyendo y le hace
     * inicial y/o final según corresponda.
     * 
     * @param name Nombre del autómata.
     * @param label Etiqueta que va a llevar asociada el estado.
     * @param start Bandera que indica si va a ser inicial.
     * @param end Bandera que indica si va a ser final.
     */
    private void createState(String name, Vector<String> label, boolean start, boolean end){
        State temp;
                //Creamos el estado y le hacemos final y/o inicial si corresponde.
        temp = mMinimize.createState(name, end);
        temp.setLabel(label.toString().substring(1, label.toString().length()-1));
        if(start==true)
            mMinimize.setInitialState(temp);
        
    }//createState
    
    /**
     * Crea las transiciones del nuevo autómata.
     */
    private void createTransitions(){
        Vector<Terminal> alphabet = mMinimize.getAlphabet();
        State temp;
        
            //Recorremos los estados.
        for(Integer i=0; i<mPartition.mSize; i++)
                //Para cada letra del alfabeto creamos transiciones.
            for(int j=0; j<alphabet.size(); j++){
                    //temp = estado destino de la transición.                
                temp = mMinimize.findState(mPartition.mDestiny[i][j].toString());
                ((StateFA)mMinimize.findState(mPartition.mPartitionA[i].toString())).createTransitionOut(alphabet.elementAt(j), temp);
            }//for
        
    }//createTransitions
    
    /**
     * Elimina el estado sumidero del autómata resultante para convertirlo
     * en mínimo. 
     */
    private void removeDrain(){
        Vector<Transition> transOut;
        boolean flag;

            //Recorremos los estados de el autómata minimizado
        for(int i=0; i<mMinimize.getStates().size(); i++)
                //Si no es final recorremos las transiciones de salida
            if(!mMinimize.getStates().elementAt(i).isFinal()){
                flag = true;
                transOut = mMinimize.getStates().elementAt(i).getTransitionsOut();
                    //Si todas las transiciones de salida van a él mismo -> es sumidero
                for(int j=0; j<transOut.size() && flag==true; j++)
                    if(!transOut.elementAt(j).getNextState().equals(transOut.elementAt(j).getPrevState()))
                        flag = false;
                
                    //Si hay un estado sumidero le borra y finaliza el método
                if(flag == true){
                    mMinimize.removeState(mMinimize.getStates().elementAt(i).getName());
                    return;
                }
            }//if
        
    }//removeDrain

    /**
     * Ejecuta el algoritmo completo de minimización de autómatas finitos.
     * 
     * @return True si la minimización ha sido posible.
     */
    public boolean allSteps() {
        firstStep();
        while(nextStep());
            
        if(mMinimize.getStates().size() <= mAutomaton.getStates().size())
            return true;
        
        return false;
    }//allSteps

}//MinimizeFA
