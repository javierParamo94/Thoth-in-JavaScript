package core.grammar.tasp;

import java.util.Vector;

import core.Symbol;
import core.Terminal;
import core.TerminalEnd;
import core.TerminalEpsilon;
import core.grammar.Grammar;
import core.grammar.Production;

/**
 * <b>Descripci�n</b><br>
 * Se encarga de realizar el an�lisis descendente de una gram�tica.
 * <p>
 * <b>Detalles</b><br>
 * A partir de la tabla TASP podr� diferenciar si una palabra es reconocida
 * por la gram�tica que representa la tabla TASP o no.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Reconocimiento de palabras a partir de una gram�tica.
 * </p>
 * 
 * @author �lvar Arn�iz Gonz�lez, Andr�s Arn�iz Moreno
 * @version 1.0
 * @see TaspTable
 */
public class WordTasp {
    
    // Attributes -----------------------------------------------------------------
    
    /**
     * Gram�tica de la que queremos saber si una palabra puede ser producida por
     * ella o no.
     */
    public Grammar mGrammar;
    
    /**
     * Tabla TASP necesaria para el algoritmo.
     */
    public TaspTable mTASP;
    
    /**
     * Palabra a reconocer.
     */
    public Vector<Terminal> mWord;
    
    /**
     * Historial de la pila del aut�mata.
     */
    public Vector<Vector<Symbol>> mStack;
    
    /**
     * Historial del tope de pila.
     */
    public Vector<Symbol> mTop;
    
    /**
     * Historial de la entrada que nos queda por analizar.
     */
    public Vector<Vector<Terminal>> mIn;
    
    /**
     * Historial del car�cter de entrada.
     */
    public Vector<Terminal> mChar;
    
    /**
     * Historial de la producci�n de salida correspondiente.
     */
    public Vector<Production> mOut;
    
    // Methods --------------------------------------------------------------------
    
    /**
     * Constructor del algoritmo.<br>
     * Se encarga de la inicializaci�n completa del algoritmo, entre otros de la
     * tabla TASP y de la palabra a reconocer.
     * 
     * @param gra Gram�tica sobre la que se va a aplicar el algoritmo.
     * @param tasp Tabla TASP que va a ser usada.
     * @param word Palabra a reconocer.
     */
    public WordTasp (Grammar gra, TaspTable tasp, Vector<Terminal> word) {
        mGrammar = gra;
        mTASP = tasp;
        mWord = word;
        mStack = new Vector<Vector<Symbol>>();
        mTop = new Vector<Symbol>();
        mIn = new Vector<Vector<Terminal>>();
        mChar = new Vector<Terminal>();
        mOut = new Vector<Production>();
        
    }//WordTasp
    
    /**
     * Inicializa los historiales y deja la palabra apta para el reconocimiento.<br>
     * Si la palabra no termina con el terminal de fin de entrada se le ponemos ($).
     */
    public void firstStep () {
        Vector<Symbol> stack = new Vector<Symbol>(2, 5);
        TerminalEnd end = new TerminalEnd();
        
            //Si no termina en $ se le a�adimos 
        if(!mWord.lastElement().equals(end))
            mWord.add(end);
        
        stack.add(end);
        stack.add(mGrammar.getAxiom());
        
        mStack.add(stack);
        mTop.add(mStack.lastElement().lastElement());
        mIn.add(mWord);
        mChar.add(mIn.lastElement().firstElement());
        
    }//firstStep

    /**
     * Realiza cada paso del algoritmo.<br>
     * Calcula la transici�n que debe utilizar para la entrada y la pila actual.
     * 
     * @return True si debe continuar el algoritmo, false si ya ha finalizado y
     * no hay mas pasos que hacer.
     */
    public boolean nextStep () {
        Vector<Symbol> tempStack = new Vector<Symbol>(5, 5);
        Vector<Terminal> tempIn = new Vector<Terminal>(5, 5);
        Production tempProd;
        int i, j;
        
            //Si hay que hacer POP
        if(mTop.lastElement().equals(mChar.lastElement())){
                //Vista instant�nea de la pila
            for(Symbol s : mStack.lastElement())
                tempStack.add(s);
            tempStack.removeElementAt(tempStack.size()-1);
            mStack.add(tempStack);
                //Vista instant�nea de la entrada
            for(Terminal t : mIn.lastElement())
                tempIn.add(t);
            tempIn.removeElementAt(0);
            mIn.add(tempIn);
            mOut.add(null);
        }
            //Si hay que buscar una producci�n adecuada
        else{
            i = mTASP.getTerminals().indexOf(mChar.lastElement());
            j = mTASP.getNonTerminals().indexOf(mTop.lastElement());
                //No podemos seguir con el reconocimiento
            if(i == -1 || j == -1)
                return false;
            
                //Producci�n que necesitamos para obtener el car�cter con lo
                // que tenemos en el tope de pila
            tempProd = mTASP.getSolution()[i][j];
            
                //No podemos seguir con el reconocimiento
            if(tempProd == null)
                return false;
            
                //A�adimos la producci�n a la salida
            mOut.add(tempProd);
            
                //Creamos la nueva vista instant�nea de la pila
            for(Symbol s : mStack.lastElement())
                tempStack.add(s);
            tempStack.removeElementAt(tempStack.size()-1);
                //Si es ALGO -> epsilon
            if(!tempProd.getRight().firstElement().equals(new TerminalEpsilon()))
                for(int k=tempProd.getRight().size()-1; k>=0; k--)
                    tempStack.add(tempProd.getRight().elementAt(k));
            mStack.add(tempStack);
                //Vista instant�nea de la entrada
            for(Terminal t : mIn.lastElement())
                tempIn.add(t);
            mIn.add(tempIn);
        }
        
        mTop.add(mStack.lastElement().lastElement());
        mChar.add(mIn.lastElement().firstElement());
        
        if(mStack.lastElement().size() == 1 &&
                mStack.lastElement().firstElement().equals(new TerminalEnd()))
            return false;
        
        return true;
    }//nextStep
    
    /**
     * Ejecuta el algoritmo completo.<br>
     * Devuelve si la palabra puede haber sido creada con la gram�tica que estamos
     * analizando.
     * 
     * @return True si la palabra ha sido reconocida y false en caso contrario.
     */
    public boolean allSteps () {
        firstStep();
        while(nextStep());
        
        return isAccept();
    }//allSteps
    
    /**
     * Devuelve si la palabra ha podido ser reconocida por la gram�tica.<br>
     * Debe ejecutarse tras haber realizado todos los pasos del algoritmo.
     * 
     * @return True si la palabra ha sido reconocida y false en caso contrario.
     */
    public boolean isAccept () {
        Terminal end = new TerminalEnd();
        
        return mStack.lastElement().size() == 1 &&
                mStack.lastElement().firstElement().equals(end) &&
                mIn.lastElement().size() == 1 &&
                mIn.lastElement().firstElement().equals(end);
    }//isAccept
    
}//WordTasp
