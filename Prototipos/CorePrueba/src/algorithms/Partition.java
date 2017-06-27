package algorithms;

import java.util.Vector;

import algorithms.minimize.MinimizeFA;

import core.*;

/**
 * <b>Descripción</b><br>
 * Se encarga de almacenar las particiones usadas en el método de minimización de
 * autómatas.
 * <p>
 * <b>Detalles</b><br>
 * Nos proporciona un acceso homogéneo a las particiones y operaciones de utilidad
 * como ordenación e inicialización de particiones.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Proporciona soporte al algoritmo de minimización.
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @version 1.0
 * @see MinimizeFA
 */
public class Partition {

    // Attributes ------------------------------------------------------------------
    
    /**
     * Matriz donde almacenamos los destinos de los estados.<br>
     * Cada fila representa un estado y cada columna un terminal del alfabeto.
     */
    public Integer[][] mDestiny;
    
    /**
     * Array donde se almacena la partición A.<br>
     * Almacenará la partición de estados del paso anterior.
     */
    public Integer[] mPartitionA;
    
    /**
     * Array donde se almacena la partición B.<br>
     * Almacenará la nueva partición creada.
     */
    public Integer[] mPartitionB;
    
    /**
     * Array de estados del autómata.
     */
    public Vector<State> mListStates;
    
    /**
     * Número de estados del autómata, y de las particiones.
     */
    public int mSize;
    
    //  Methods ---------------------------------------------------------------------
    
    /**
     * Constructor completo.<br>
     * Se encarga de inicializar todos los atributos de la clase. 
     * 
     * @param automaton Autómata del que obtiene los estados y las particiones.
     */
    public Partition (Automaton automaton){
        mSize = automaton.getStates().size();
        mDestiny = new Integer[mSize][automaton.getAlphabet().size()];
        mPartitionA = new Integer[mSize];
        mPartitionB = new Integer[mSize];        
        mListStates = new Vector<State>();
        for(int i=0; i<mSize; i++)
            mListStates.add(automaton.getStates().elementAt(i));
        
    }//Partition
    
    /**
     * Inicializa la lista donde almacenamos las funciones de transición de cada
     * transición.
     * 
     * @param automaton Autómata del que obtiene los estados y transiciones.
     */
    public void initializeDestinyList (Automaton automaton){
        Vector<Terminal> alphabet = automaton.getAlphabet();
        boolean flag;
        int index;
        
            //Recorremos los estados de mAutomaton
        for(int i=0; i<mSize; i++)
                //Para cada terminal del alfabeto
            for(int j=0; j<alphabet.size(); j++){
                flag = true;
                    //Recorremos las transiciones de salida
                for(int k=0; k<mListStates.elementAt(i).getTransitionsOut().size() && flag; k++)
                        //Si el caracter es el mismo que el de la transición de salida del estado -> Añadimos 
                    if(mListStates.elementAt(i).getTransitionsOut().elementAt(k).getIn().equals(alphabet.elementAt(j))){
                        index = mListStates.indexOf(mListStates.elementAt(i).getTransitionsOut().elementAt(k).getNextState());
                        mDestiny[i][j] = mPartitionA[index];
                        flag = false;
                    }//if
            }//for
        
    }//initializeDestinyList
    
    /**
     * Método recursivo de ordenación mediante el método QuickSort.
     * 
     * @param left Índice izquierdo del vector a ordenar.
     * @param right Índice derecho del vector a ordenar.
     */
    public void quickSort (int left, int right){
       int pivot;
       
       if(left < right){
           pivot = split(left, right);
           quickSort(left, pivot-1);
           quickSort(pivot+1, right);
       }//if
       
    }//quickSort
    
    /**
     * Divide el vector en dos partes.<br>
     * Se encarga de devolver el pivote que va a usar quickSort.
     * 
     * @param first Índice del primer elemento.
     * @param last Índice del último elemento.
     * @return Posición del pivote.
     * @see quickSort
     */
    private int split (int first, int last){
        Integer tempInt, pivot = mPartitionA[first];
        State tempState;
        Integer[] tempDest;
        int left = first+1, right = last;
        
        do{
            while((left<=right) && (mPartitionA[left]<=pivot))
                left++;
            while((left<=right) && (mPartitionA[right]>pivot))
                right--;
            if(left<right){
                tempInt = mPartitionA[left];
                tempState = mListStates.elementAt(left);
                tempDest = mDestiny[left];
                
                mPartitionA[left] = mPartitionA[right];
                mListStates.set(left, mListStates.elementAt(right));
                mDestiny[left] = mDestiny[right];
                
                mPartitionA[right] = tempInt;
                mListStates.set(right, tempState);
                mDestiny[right] = tempDest;
                
                right--;
                left++;
            }
        }while(left <= right);
        
        tempInt = mPartitionA[first];
        tempState = mListStates.elementAt(first);
        tempDest = mDestiny[first];
        
        mPartitionA[first] = mPartitionA[right];
        mListStates.set(first, mListStates.elementAt(right));
        mDestiny[first] = mDestiny[right];
        
        mPartitionA[right] = tempInt;
        mListStates.set(right, tempState);
        mDestiny[right] = tempDest;
        
        return right;
    }//split
    
    /**
     * Compara los elementos de dos arrays de Integer. 
     * 
     * @param a Array a comparar.
     * @param b Array a comparar.
     * @return True si son iguales y false en caso cotrario.
     */
    public boolean equalsVector (Integer[] a, Integer[] b){
        if(a.length!=b.length)
            return false;

        for(int i=0; i<a.length; i++)
            if(!a[i].equals(b[i]))
                return false;
        
        return true;
    }//equalsVector
    
}//Partition
