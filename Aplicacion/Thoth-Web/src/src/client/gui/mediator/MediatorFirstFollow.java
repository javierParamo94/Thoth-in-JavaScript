package view.grammar.mediator;

import java.util.Vector;
import javax.swing.JOptionPane;

import core.NonTerminal;
import core.grammar.Grammar;
import core.grammar.cleaner.EliminateDirectRecursion;
import core.grammar.cleaner.EliminateIndirectRecursion;
import core.grammar.cleaner.LeftFactoring;
import core.grammar.tasp.FirstFollow;
import view.application.Application;
import view.grammar.visual.VisualFirstFollow;
import view.grammar.visual.VisualTasp;
import view.utils.ShowDialog;

/**
 * <b>Descripción</b><br>
 * Clase que hace de mediador con la clase física que calcula el First y el Follow.
 * <p>
 * <b>Detalles</b><br>
 * Se encarga de mediar entre la parte gráfica y la parte física.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Evita el acomplamiento entre la parte física y la parte visual.
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @version 1.0
 */
public class MediatorFirstFollow {
    
    // Attributes --------------------------------------------------------------------
    
    /**
     * Algoritmo de limpieza asociado al mediador.
     */
    private FirstFollow mFirstFollow;
    
    /**
     * Gramática a limpiar.
     */
    private Grammar mGrammar;
    
    /**
     * Algoritmo visual al que está asociado.
     */
    private VisualFirstFollow mVisual;
    
    // Methods -----------------------------------------------------------------------
    
    /**
     * Constructor completo del mediador del algoritmo del cálculo del First y el
     * Follow.
     * 
     * @param ff Pantalla del algoritmo.
     * @param grammar Gramática a limpiar.
     */
    public MediatorFirstFollow (VisualFirstFollow ff, Grammar grammar) {
            //Mostramos el diálogo
        if(ShowDialog.questionFirstFollow() == JOptionPane.YES_OPTION){
                //Quitamos recursividad
            EliminateDirectRecursion edr;
            EliminateIndirectRecursion eir;
            
            if(grammar.isDirectRecursive()){
                edr = new EliminateDirectRecursion(grammar);
                edr.allSteps();
                grammar = edr.getSolution();
            }
            if(grammar.isIndirectRecursive()){
                eir = new EliminateIndirectRecursion(grammar);
                eir.allSteps();
                grammar = eir.getSolution();
            }
            LeftFactoring leftFac = new LeftFactoring(grammar);
            if(leftFac.allSteps())
                grammar = leftFac.getSolution();
            
            mVisual = ff;
            mVisual.mVisible = true;
        }
        else{
            ff.mVisible = false;
            mVisual = null;
            return;
        }
        
        mFirstFollow = new FirstFollow(grammar);
        mGrammar = grammar;
        mVisual.mGrammar.setText(mGrammar.completeToString());
        
    }//MediatorFirstFollow
    
    /**
     * Calcula el first y la cabecera con todos los no terminales.<br>
     * 
     * @return True si ha podido calcular el first.
     */
    public boolean first () {
        Vector<NonTerminal> temp = mGrammar.getNonTerminals();
        
        if(mFirstFollow.calculateFirst()){
            mVisual.mFirst = mFirstFollow.getFirstTable();
            mVisual.mHeader = new String[temp.size()];
            
            for(int i=0; i<temp.size(); i++)
                mVisual.mHeader[i] = temp.elementAt(i).toString();
            
            return true;
        }
        
        return false;
    }//first
    
    /**
     * Calcula el follow.
     * 
     * @return True si ha podido calcular el follow.
     */
    public boolean follow () {
        if(mFirstFollow.calculateFollow()){
            mVisual.mFollow = mFirstFollow.getFollowTable();
            return true;
        }
        
        return false;
    }//follow
    
    /**
     * Crea un nuevo algoritmo para reconocimiento con la TASP.
     */
    public void tasp () {
        new VisualTasp(Application.getInstance(), mFirstFollow);
        exit();
        
    }//tasp
    
    /**
     * Cierra la ventana visual y elimina las referencias.
     */
    public void exit () {
        mVisual.setVisible(false);
        mVisual.mMediator = null;
        mVisual = null;
        
    }//exit
    
}//MediatorFirstFollow
