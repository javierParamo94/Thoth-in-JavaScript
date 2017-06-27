package algorithms.equivalence;

import java.util.Vector;


import core.State;
import core.finiteautomaton.StateFA;
import core.Terminal;

/**
* <b>Descripción</b><br>
* Proporciona la forma de almacenar la tabla para el algoritmo de equivalencia.<br>
* Además ofrece funciones de cálculo y acceso a la misma. 
* <p>
* <b>Detalles</b><br>
* Inicializa la tabla con los estados iniciales de los autómatas a comparar y
* posteriormente la rellena con los posibles siguientes estados dependiendo de la
* entrada consumida.<br>
* También compara si los pares de estados son ambos finales o no finales.
* </p>
* <p>
* <b>Funcionalidad</b><br>
* Disminuye el acoplamiento del algoritmo de equivalencia.
* </p>
* 
* @author Álvar Arnáiz González, Andrés Arnáiz Moreno
* @version 1.0
* @see EquivalenceFA
*/
public class EquivalenceTable {

    //  Attributes ------------------------------------------------------------------
    
    /**
     * Contador que indica por qué fila de la tabla avanza.
     */
    private int mCount = 0;
    
    /**
     * Pares de estados que forman la parte izquierda de la tabla. 
     */
    public Vector<Vector<State>> mPairStates;
    
    /**
     * Pares de estados a los que puede ir con cada terminal del alfabeto.<br>
     * Forma el cuerpo de la tabla.
     */
    public Vector<Vector<State>> mDestinyStates;
    
    //  Methods ------------------------------------------------------------------
    
    /**
     * Constructor completo de la tabla de equivalencia.
     * 
     * @param initialA Estado inicial de uno de los autómatas.
     * @param initialB Estado inicial del otro autómata.
     */
    public EquivalenceTable (State initialA, State initialB ){
        Vector<State> temp = new Vector<State>(2);
        mPairStates = new Vector<Vector<State>>();
        mDestinyStates = new Vector<Vector<State>>();
        temp.add(initialA);
        temp.add(initialB);
        mPairStates.add(0, temp);
        
    }//EquivalenceTable
    
    /**
     * Se encarga de completar el cuerpo de la tabla a partir de la fila
     * de la parte izquierda indicada por el contador.
     * 
     * @param alphabet Alfabeto de los autómatas.
     * @return True si puede seguir completando y false si ya ha recorrido todas
     * las parejas de la parte izquierda. 
     */
    public boolean completeTable (Vector<Terminal> alphabet){
        Vector<State> temp = new Vector<State>(alphabet.size()*2);
        
        if(mCount >= mPairStates.size())
            return false;
        
        for(int i=0; i<alphabet.size(); i++){
                //Inicializamos temp con los posibles estados para todo el alfabeto
            temp.add(i*2, ((StateFA)mPairStates.elementAt(mCount).elementAt(0)).
                    nextStates(alphabet.elementAt(i)).firstElement());
            temp.add(i*2+1,((StateFA)mPairStates.elementAt(mCount).elementAt(1)).
                    nextStates(alphabet.elementAt(i)).firstElement());
        }
            //Asignamos temp a mDestinyStates
        mDestinyStates.add(mCount, temp);
        completePair(alphabet);
        mCount++;
        
        return true;
    }//completeTable
    
    /**
     * Completa la parte izquierda de la tabla, insertando solo los pares de
     * estados que no estén ya contenidos.
     * 
     * @param alphabet Alfabeto del autómata.
     */
    private void completePair(Vector<Terminal> alphabet){
        Vector<State> temp = new Vector<State>(2);
        
            //Recorremos los pares de estados correspondientes a cada terminal
        for(int j=0; j<alphabet.size(); j++){
            temp.add(0, mDestinyStates.elementAt(mCount).elementAt(j*2));
            temp.add(1, mDestinyStates.elementAt(mCount).elementAt(j*2+1));
                //Solo los introduce en la parte izquierda si no están
            if(!mPairStates.contains(temp))
                mPairStates.add(temp);
            
            temp = new Vector<State>(2);
        }//for
        
    }//completePair
    
    /**
     * Compara si las parejas de estados son del mismo tipo (finales o no finales).
     * 
     * @param alphabet Alfabeto de entrada del autómata.
     * @return True si todas las parejas son del mismo tipo y false si hay alguna
     * de ellas que no lo son.
     */
    public boolean compare(Vector<Terminal> alphabet){
        
        for(int k=0; k<alphabet.size();k++)
                //Si las parejas de estados no son del mismo tipo(finales o no
                //finales) devolvemos falso.
            if(mDestinyStates.elementAt(mCount-1).elementAt(k*2).isFinal()!=
                mDestinyStates.elementAt(mCount-1).elementAt(k*2+1).isFinal())
                return false;
        
        return true;
    }//compare
    
}//EquivalenceTable
