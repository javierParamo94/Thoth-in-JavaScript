package algorithms;

import java.util.Vector;

import algorithms.minimize.MinimizeFA;

import core.*;

/**
 * <b>Descripci�n</b><br>
 * Se encarga de almacenar las particiones usadas en el m�todo de minimizaci�n de
 * aut�matas.
 * <p>
 * <b>Detalles</b><br>
 * Nos proporciona un acceso homog�neo a las particiones y operaciones de utilidad
 * como ordenaci�n e inicializaci�n de particiones.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Proporciona soporte al algoritmo de minimizaci�n.
 * </p>
 * 
 * @author �lvar Arn�iz Gonz�lez, Andr�s Arn�iz Moreno
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
     * Array donde se almacena la partici�n A.<br>
     * Almacenar� la partici�n de estados del paso anterior.
     */
    public Integer[] mPartitionA;
    
    /**
     * Array donde se almacena la partici�n B.<br>
     * Almacenar� la nueva partici�n creada.
     */
    public Integer[] mPartitionB;
    
    /**
     * Array de estados del aut�mata.
     */
    public Vector<State> mListStates;
    
    /**
     * N�mero de estados del aut�mata, y de las particiones.
     */
    public int mSize;
    
    //  Methods ---------------------------------------------------------------------
    
    /**
     * Constructor completo.<br>
     * Se encarga de inicializar todos los atributos de la clase. 
     * 
     * @param automaton Aut�mata del que obtiene los estados y las particiones.
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
     * Inicializa la lista donde almacenamos las funciones de transici�n de cada
     * transici�n.
     * 
     * @param automaton Aut�mata del que obtiene los estados y transiciones.
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
                        //Si el caracter es el mismo que el de la transici�n de salida del estado -> A�adimos 
                    if(mListStates.elementAt(i).getTransitionsOut().elementAt(k).getIn().equals(alphabet.elementAt(j))){
                        index = mListStates.indexOf(mListStates.elementAt(i).getTransitionsOut().elementAt(k).getNextState());
                        mDestiny[i][j] = mPartitionA[index];
                        flag = false;
                    }//if
            }//for
        
    }//initializeDestinyList
    
    /**
     * M�todo recursivo de ordenaci�n mediante el m�todo QuickSort.
     * 
     * @param left �ndice izquierdo del vector a ordenar.
     * @param right �ndice derecho del vector a ordenar.
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
     * @param first �ndice del primer elemento.
     * @param last �ndice del �ltimo elemento.
     * @return Posici�n del pivote.
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
