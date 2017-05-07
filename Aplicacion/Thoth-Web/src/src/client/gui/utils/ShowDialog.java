package src.client.gui.utils;

import src.client.gui.MessageMessages;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Window;



/**
 * <b>Descripción</b><br>
 * Almacena todos los diálogos de error, advertencia e información de la aplicación.
 * <p>
 * <b>Detalles</b><br>
 * Cada uno de sus métodos estáticos muestra un mensaje específico.<br>
 * También ofrece la disponibilidad de mostrar mensajes que se le pasen al método.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Encapsula todos los mensajes de error en una única clase.
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @version 1.0
 * 
 * @author Javier Jimeno Visitación, Iñigo Mediavilla Saiz
 * @version 2.0
 */
public class ShowDialog {
    
	private static MessageMessages sms = GWT.create(MessageMessages.class);
	
    // Methods -------------------------------------------------------------------
    
    /**
     * Muestra un mensaje de información para que el usuario sepa que hasta que no
     * reinicie el programa no se cambiará el idioma.
     */
   /* public static void restartChanges () {
        JOptionPane.showMessageDialog(null,
                Messages.RESTART_CHANGES, "Information", JOptionPane.INFORMATION_MESSAGE);
    }//restartChanges
    
    /**
     * Muestra un mensaje de información si son o no equivalentes dos expresiones
     * regulares.
     * 
     * @param flag Si es true muestra que han sido equivalentes y que no son 
     * equivalentes si es false.
     */
   /* public static void checkRegExp (boolean flag) {
        if(flag)
            JOptionPane.showMessageDialog(null,
                    Messages.EQUIVALENCE_ER_TRUE, "Information", JOptionPane.INFORMATION_MESSAGE);
        else
            JOptionPane.showMessageDialog(null,
                    Messages.EQUIVALENCE_ER_FALSE, "Information", JOptionPane.INFORMATION_MESSAGE);
    }//checkRegExp
    
    /**
     * Muestra un mensaje de error cuando no se han podido comparar dos expresiones
     * regulares.
     */
 /*   public static void errorCompareER () {
        JOptionPane.showMessageDialog(null,
                Messages.ERROR_COMPARE_ER, "ERROR", JOptionPane.ERROR_MESSAGE);
    }//errorCompareER
    
    /**
     * Pregunta al usuario si desea completar el autómata.
     * 
     * @return Si quiere completarle o no.
     */
  /*  public static int questionEquation () {
        return JOptionPane.showConfirmDialog(null, Messages.QUESTION_MINIMIZE2
                + "\n" + Messages.CONTINUE, "Information", JOptionPane.YES_NO_OPTION);
    }//questionEquation
    
    /**
     * Muestra un mensaje de advertencia cuando el autómata no tiene estado inicial
     * o final.
     */
 /*   public static void noInitialFinalState () {
        JOptionPane.showMessageDialog(null,
                Messages.NO_INITIAL_FINAL_STATE, "Warning", JOptionPane.WARNING_MESSAGE);
    }//noInitialFinalState    
    
    /**
     * Muestra un mensaje de advertencia cuando el autómata solo reconoce el lenguaje
     * vacío.
     */
 /*   public static void recognizedEpsilonLanguage () {
    	JOptionPane.showMessageDialog(null,
                Messages.EMPTY_LANGUAGE, "Warning", JOptionPane.WARNING_MESSAGE);
    }//recognizedEpsilonLanguage
    
    /**
     * Muestra un mensaje y pregunta si desea salir sin guardar los cambios.
     * 
     * @return Si quiere guardar o no los cambios o, si por el contario desea salir. 
     */
 /*   public static int saveChanges () {
        Object[] options = {Messages.YES, Messages.NO, Messages.CANCEL};
        
        return JOptionPane.showOptionDialog(null, Messages.SAVE_CHANGES, 
                "Save", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, options, options[2]);
    }//saveChanges
    
    /**
     * Muestra un mensaje de advertencia cuando el autómata es determinista.
     */
 /*   public static void isDeterministic () {
        JOptionPane.showMessageDialog(null,
                Messages.IS_DETERMINISTIC, "Warning", JOptionPane.WARNING_MESSAGE);
    }//isDeterministic
    
    /**
     * Muestra un mensaje de error cuando el formato de fichero es incorrecto.
     */
 /*   public static void formatFileIncorrect () {
        JOptionPane.showMessageDialog(null,
                Messages.FORMAT_FILE_INCORRECT, "ERROR", JOptionPane.ERROR_MESSAGE);
    }//formatFileIncorrect
    
    /**
     * Muestra un mensaje de error cuando no hay nada para salvar.
     */
 /*   public static void nothingToSave () {
        JOptionPane.showMessageDialog(null, Messages.NOTHING_TO_SAVE,
                "ERROR", JOptionPane.ERROR_MESSAGE);
    }//nothingToSave
    
    /**
     * Muestra un mensaje de error cuando no se ha podido guardar.
     */
 /*   public static void dontSave () {
        JOptionPane.showMessageDialog(null, Messages.DONT_SAVE,
                "ERROR", JOptionPane.ERROR_MESSAGE);
    }//dontSave
    
    /**
     * Muestra un mensaje preguntando si se desea sobreescribir el archivo seleccionado.
     * 
     * @return Si se quiere sobreescribir o no el archivo seleccionado.
     */
 /*   public static int wantRewrite () {
        
        return JOptionPane.showConfirmDialog(null, Messages.WANT_REWRITE, "Question",
                JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
    }//wantRewrite
    
    /**
     * Muestra un diálogo advirtiendo que necesitas un nombre para crear un autómata.
     */
 /*   public static void emptyName () {
        JOptionPane.showMessageDialog(null,
                Messages.MESSAGE_NAME, "Warning", JOptionPane.WARNING_MESSAGE);
    }//emptyName
    
    /**
     * Lanza mensajes de error que indica que el tipo de la gramática es incorrecto
     * para el algoritmo seleccionado.
     */
    public static void incorrectTypeGrammar () {
        Window.alert("La gramática es incorrecta");
    }//incorrectTypeGrammar
    
    /**
     * Lanza un mensaje de advertencia si la gramática no necesita ejecutar ese algoritmo
     */
    public static void innecesaryAlgorithm () {
    	Window.alert(sms.innecesaryalgorithm());
    	
    }//innecesaryAlgorithm
    
    /**
     * Lanza un mensaje de advertencia si la gramática no ha podido ser limpiada.
     */
    public static void cleanError () {
    	Window.alert(sms.cleanerrora()+"\n"+sms.cleanerrorb());

    }//cleanError
    
    /**
     * Muestra un mensaje de error.
     * 
     * @param message Mensaje que se va a mostrar.
     */
    public static void messageError (String message) {
    	Window.alert(message);
    }//messageError
    
    /**
     * Muestra un mensaje inidicando si queremos limpiar la gramática antes
     * de ejecutar el algoritmo.
     * 
     * @return La elección del usuario.
     */
  /*  public static int clearQuestion () {
        Object[] options = {Messages.YES, Messages.NO};
        
        return JOptionPane.showOptionDialog(null,
                    Messages.CLEAR_QUESTION, "Question", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
    }//clearQuestion
    
    /**
     * Muestra un mensaje de error cuando no se ha podido ejecutar el algoritmo
     * de chomsky.
     */
    public static void chomskyError () {
    	Window.alert(sms.chomskyerror());
    }//chomskyError
    
    /**
     * Muestra un mensaje de error por pantalla indicando que no hay recursividad
     * directa.
     */
    public static void nonRecursiveDir () {
    	Window.alert(sms.nodirectrecursive());
    }//nonRecursiveDir
    
    /**
     * Muestra un mensaje de error por pantalla indicando que no hay recursividad
     * indirecta
     */
    public static void nonRecursiveIndir () {
    	Window.alert(sms.noindirectrecursive());

    }//nonRecursiveIndir
    /**
     * Muestra un mensaje de error por pantalla cuando la gramática no tiene
     * recursión a izquierdas.
     */
    public static void nonRecursive () {
    	Window.alert(sms.norecursive());
    }//nonRecursive
    
    /**
     * Muestra un mensaje de error por pantalla indicando que la gramática no se puede
     * factorizar.
     */
    public static void nonFactorSymbols () {
    	Window.alert(sms.nofactor());
    }//nonFactorSymbols
    
    /**
     * Muestra un mensaje de error por pantalla indicando que no hay
     * producciones generativas.
     */
    public static void nonUniSymbols () {
    	Window.alert(sms.allpng());
    }//nonUniSymbols
    
    /**
     * Muestra un mensaje de error por pantalla indicando que no hay
     * producciones épsilon.
     */
    public static void nonCancelSymbols () {
    	Window.alert(sms.allsa());
    }//nonCancelSymbols
    
    /**
     * Muestra un mensaje de error por pantalla indicando que no hay  símbolos
     * alcanzables.
     */
    public static void nonReachableSymbols () {
    	Window.alert(sms.allsna());
    }//nonReachableSymbols
    
    /**
     * Muestra un mensaje de error por pantalla indicando que no hay  símbolos
     * terminables.
     */
    public static void nonTerminalSymbols () {
    	Window.alert(sms.allsnt());
    }//nonTerminalSymbols
    
    /**
     * Pide la confirmación del usuario para seguir con la ejecución del algoritmo de First
     * y Follow ejecutando los algoritmo necesarios (Factorización y eliminación de recursividad).
     * 
     * @return Un entero que representa si o no.
     */
 /*   public static int questionFirstFollow () {
        return JOptionPane.showConfirmDialog(null, Messages.QUESTION_FIRST_FOLLOW
                + "\n" + Messages.CONTINUE, "Information", JOptionPane.YES_NO_OPTION);
    }//questionFirstFollow
    
    /**
     * Muestra un error producido en la ejecución del first o el follow.
     */
    /*public static void nonFirstFollow () {
    	Window.alert("La gramática no tiene símbolos no terminales");
    }//nonFirstFollow
    
    /**
     * Muestra un error cuando la gramática es ambigua.
     */
    /*public static void grammarAmbiguous () {
        JOptionPane.showMessageDialog(null,
                Messages.AMBIGUOUS_GRAMMAR, "ERROR", JOptionPane.ERROR_MESSAGE);
    }//nonFirstFollow*/
    
    /**
     * Pide la confirmación del usuario para seguir con la ejecución del algoritmo
     * de CYK ejecutando los algoritmos necesarios (SA y FNC).
     * @return Un entero que representa si o no. 
     */
   /* public static int questionCYK () {
        return JOptionPane.showConfirmDialog(null, Messages.QUESTION_CYK 
                + "\n" + Messages.CONTINUE, "Information", JOptionPane.YES_NO_OPTION);
    }//questionCYK*/
    
    /**
     * Muestra la información de que ha sido reconocida la palabra.
     */
   /* public static void recognizedWord () {
        JOptionPane.showMessageDialog(null,
                Messages.RECOGNIZED_WORD, "Information", JOptionPane.INFORMATION_MESSAGE);
    }//recognizedWord*/
    
    /**
     * Muestra la información de que no ha sido reconocida la palabra.
     */
   /* public static void nonRecognizedWord () {
        JOptionPane.showMessageDialog(null,
                Messages.NON_RECOGNIZED_WORD, "Information", JOptionPane.INFORMATION_MESSAGE);
    }//nonRecognizedWord*/
    
    /**
     * Muestra un diálogo de error causado por no haber introducido un símbolo
     * válido para buscar y reemplazar en una gramática.
     */
   /* public static void nonSymbolRename () {
        JOptionPane.showMessageDialog(null, Messages.RENAME_ERROR,
                "ERROR", JOptionPane.ERROR_MESSAGE);
    }//nonSymbolRename*/
    
    /**
     * Muestra un diálogo informando que el buscar y reemplazar no ha provocado
     * cambios en la gramática.
     */
   /* public static void nonChangesRename () {
        JOptionPane.showMessageDialog(null, Messages.CHANGE_ERROR,
                "Information", JOptionPane.INFORMATION_MESSAGE);
    }//nonChangesRename*/
    
    /**
     * Muestra un diálogo informando que no se ha podido construir un estado
     * porque ya existía en el autómata.
     */
    /*public static void repeatNameState () {
        JOptionPane.showMessageDialog(null, Messages.NAME_STATE,
                "Information", JOptionPane.INFORMATION_MESSAGE);
    }//repeatNameState*/
    
    /**
     * Muestra un diálogo advirtiendo que el terminal introducido no es válido.<br>
     * Este error es debido a que el terminal escogido esta reservado para otras
     * funciones.
     */
    /*public static void invalidTerminal () {
        JOptionPane.showMessageDialog(null, Messages.INVALID_TERMINAL,
                "Warning", JOptionPane.WARNING_MESSAGE);
    }//invalidTerminal*/
    
    /**
     * Muestra un mensaje solicitando el nuevo nombre del estado.
     * 
     * @param s Nombre del estado antiguo que aparecerá por defecto.
     * @return Cadena introducida por el usuario.
     */
    /*public static String changeName (String s) {
        return JOptionPane.showInputDialog(Messages.NEW_NAME, s);
    }//changeName*/
    
    /**
     * Muestra un mensaje solicitando la etiqueta del estado.<br>
     * @param s Nombre del estado antiguo que aparecerá por defecto.
     * @return Cadena introducida por el usuario.
     */
    /*public static String changeLabel (String s) {
        return JOptionPane.showInputDialog(Messages.NEW_LABEL, s);
    }//changeName*/
    
    /**
     * Muestra un mensaje indicando que es necesario que el autómata sea
     * determinista y completo para poder ejecutar el algoritmo de minimización.<br>
     * El usuario podrá elegir si desea continuar o no.
     * 
     * @return La respuesta del usuario.
     */
   /* public static int questionMinimize () {
        return JOptionPane.showConfirmDialog(null, Messages.QUESTION_MINIMIZE 
                + "\n" + Messages.CONTINUE, "Information", JOptionPane.YES_NO_OPTION);
    }//questionMinimize*/
    
    /**
     * Muestra un mensaje indicando que es necesario que el autómata sea
     * completo para poder ejecutar el algoritmo de minimización.<br>
     * El usuario podrá elegir si desea continuar o no.
     * 
     * @return La respuesta del usuario.
     */
   /* public static int questionMinimize2 () {
        return JOptionPane.showConfirmDialog(null, Messages.QUESTION_MINIMIZE2 
                + "\n" + Messages.CONTINUE, "Information", JOptionPane.YES_NO_OPTION);
    }//questionMinimize*/
    
    /**
     * Indica al usuario que el autómata ya es mínimo y por lo tanto no necesita
     * ejecutar el algoritmo de minimización.
     */
   /* public static void isMinimal () {
        JOptionPane.showMessageDialog(null, Messages.IS_MINIMAL,
                "Information", JOptionPane.INFORMATION_MESSAGE);
    }//isMinimal*/
    
    /**
     * Indica que no hay autómatas para poder compararlos.
     */
   /* public static void equivalenceInsufficient () {
        JOptionPane.showMessageDialog(null, Messages.INSUFFICIENT,
                "ERROR", JOptionPane.ERROR_MESSAGE);
    }//equivalentInsufficient*/
    
    /**
     * Indica si los autómatas son o no equivalentes.
     * 
     * @param accept True si son equivalentes y false si no.
     */
    /*public static void equivalence (boolean accept) {
        if(accept)
            JOptionPane.showMessageDialog(null, Messages.EQUIVALENCE_TRUE,
                    "Information", JOptionPane.INFORMATION_MESSAGE);
        else
            JOptionPane.showMessageDialog(null, Messages.EQUIVALENCE_FALSE,
                    "Information", JOptionPane.INFORMATION_MESSAGE);
    }//equivalece
    
    /**
     * Muestra un mensaje de advertencia indicando que la gramática no es
     * lineal a derechas.
     */
   /* public static void noRightLinear () {
        JOptionPane.showMessageDialog(null, Messages.NO_RIGHT_LINEAR,
                "Warning", JOptionPane.WARNING_MESSAGE);
    }//noRightLinear
    
    /**
     * Se ha producido un error al intentar imprimir.
     */
   /* public static void errorPrint () {
        JOptionPane.showMessageDialog(null, Messages.ERROR_PRINT,
                "ERROR", JOptionPane.ERROR_MESSAGE);
    }//errorPrint
    
    /**
     * No existen elementos para imprimir.
     */
    /*public static void nothingPrint () {
        JOptionPane.showMessageDialog(null, Messages.NOTHING_PRINT,
                "Information", JOptionPane.INFORMATION_MESSAGE);
    }//nothingPrint
    
    /**
     * No existen elementos para imprimir.
     */
    /*public static void closeValidation () {
        JOptionPane.showMessageDialog(null, Messages.CLOSE_VALIDATION,
                "Information", JOptionPane.INFORMATION_MESSAGE);
    }//nothingPrint*/
    
}//ShowDialog
