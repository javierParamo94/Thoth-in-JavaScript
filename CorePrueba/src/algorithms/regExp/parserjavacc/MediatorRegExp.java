package algorithms.regExp.parserjavacc;

import java.io.StringReader;

import algorithms.regExp.ahosethi.AhoTree;
import algorithms.regExp.ahosethi.VisualAho;
import algorithms.regExp.derive.DeriveTree;
import algorithms.regExp.derive.VisualDerive;
import algorithms.regExp.thompson.MediatorThompson;
import algorithms.regExp.thompson.ThompsonTree;
import algorithms.tree.Tree;

import view.utils.ShowDialog;

/**
 * <b>Descripción</b><br>
 * Clase que hace de mediador con el parser de JavaCC.
 * <p>
 * <b>Detalles</b><br>
 * Se encarga de mediar entre la parte gráfica y parser.<br>
 * Recoge de la parte visual el string y se lo pasa al parser que le devuelve una
 * expresión regular en forma de árbol.<br>
 * Muestra los errores por pantalla, en caso de que se hayan producido.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Evita el acomplamiento entre el parser y la parte visual.
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @version 1.0
 */
public class MediatorRegExp {
    
    // Attributes -----------------------------------------------------------------
    
    /**
     * Método de Thompson.
     */
    public static final int THOMPSON = 0;
    
    /**
     * Método de Aho-Sethi-Ullman.
     */
    public static final int AHO = 1;
    
    /**
     * Método de las derivadas.
     */
    public static final int DERIVE = 2;
    

    // Methods --------------------------------------------------------------------
    
    /**
     * Constructor de la clase.<br>
     * Se encarga de llamar al parser para pedirle el árbol a partir de la expresión
     * regular.
     * 
     * @param expression Expresión regular como un String.
     * @param type Puede valer 0, 1 y 2 según los atributos THOMPSON, AHO Y DERIVE.
     */
    public MediatorRegExp (String expression, int type) {
        Tree tree;
        
        try{
            switch (type) {
                case MediatorRegExp.THOMPSON: //Thompson
                	tree = ParserExpReg.getInstance(new StringReader(expression)).buildThompsonTree();
                	MediatorThompson jTh = new MediatorThompson((ThompsonTree)tree, expression);
                	jTh.accept();
                    break;
                case 1: //Aho
                    tree = ParserExpReg.getInstance(new StringReader(expression)).buildAhoTree();
                    new VisualAho((AhoTree)tree, expression);
                    break;
                case 2: //Derive
                    tree = ParserExpReg.getInstance(new StringReader(expression)).buildDeriveTree();
                    new VisualDerive((DeriveTree)tree, expression);
                    break;
                default:
                    return;
            }//switch        
        }catch (TokenMgrError error){
            ShowDialog.messageError(error.getMessage());
            return;
        }
        catch (ParseException parExcep){
            ShowDialog.messageError(parExcep.getMessage());
            return;
        }
        
    }//MediatorRegExp

}//MediatorRegExp
