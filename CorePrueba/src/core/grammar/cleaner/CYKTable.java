package core.grammar.cleaner;

import java.util.Vector;

import core.NonTerminal;

/**
 * <b>Descripción</b><br>
 * Tabla de apoyo al algoritmo de Cocke-Younger-Kasami.
 * <p>
 * <b>Detalles</b><br>
 * Tabla de dos dimensiones.<br>
 * En cada casilla de la tabla puede haber mas de un no terminal. 
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Representación de la tabla del algoritmo CYK.
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @version 1.0
 */
public class CYKTable {
    
    // Attributes ------------------------------------------------------------------
    
    /**
     * Celdas de la tabla.
     */
    private Vector<Vector<Vector<NonTerminal>>> mTable;
    
    /**
     * Número de columnas y líneas.
     */
    private int mColNumber;
    
    // Methods ---------------------------------------------------------------------
    
    /**
     * Constructor básico de la tabla.<br>
     * Construye la tabla para el algoritmo de CYK.
     * 
     * @param nLines Líneas y columnas de la tabla.
     */
    public CYKTable (int nLines) {
        
        if(nLines >= 1){
                //Inicializamos las líneas
            mTable = new Vector<Vector<Vector<NonTerminal>>>(nLines, 0);
                //Inicializamos las columnas
            for(int i=0; i<nLines; i++){
                mTable.add(new Vector<Vector<NonTerminal>>(nLines-i, 0));
                for(int j=0; j<nLines-i; j++)
                    mTable.elementAt(i).add(new Vector<NonTerminal>(3, 3));
            }
            mColNumber = nLines;
        }
        else
            mTable = null;

    }//CYKTable
    
    /**
     * Devuelve todos los no terminales de la celda que queramos.
     *  
     * @param i Línea en la queremos insertar el no terminal.
     * @param j Columna en la que queremos insertar el no terminal.
     * @return Contenido de la celda.
     */
    public Vector<NonTerminal> get (int i, int j) {
        
        return mTable.elementAt(i).elementAt(j);
    }//get
    
    /**
     * Se encarga de introducir un único terminal en la celda que queramos.
     * 
     * @param i Línea en la queremos insertar el no terminal.
     * @param j Columna en la que queremos insertar el no terminal.
     * @param noTerm No terminal a insertar.
     */
    public void add (int i, int j, NonTerminal noTerm) {
        
        if(!mTable.elementAt(i).elementAt(j).contains(noTerm))
            mTable.elementAt(i).elementAt(j).add(noTerm);
        
    }//add
    
    /**
     * Se encarga de introducir nuevos no terminales en la celda que queramos.
     * 
     * @param i Línea en la queremos insertar el no terminal.
     * @param j Columna en la que queremos insertar el no terminal.
     * @param vNoTerm No terminales a insertar.
     */
    public void add (int i, int j, Vector<NonTerminal> vNoTerm) {
        
        for(int k=0; k<vNoTerm.size(); k++)
            if(!mTable.elementAt(i).elementAt(j).contains(vNoTerm.elementAt(k)))
                mTable.elementAt(i).elementAt(j).add(vNoTerm.elementAt(k));
        
    }//add
    
    /**
     * Devuelve el contenido de la tabla CYK.<br>
     * Devuelve la tabla en el formato que va a ser mostrado.
     * 
     * @return Datos de la tabla CYK.
     */
    public Object[][] getData () {
        Object[][] data = new Object[mColNumber+1][mColNumber+1];
        String temp;
        
            //Cabecera
        data[0][0] = "N";
        for(int i=1; i<mTable.firstElement().size()+1; i++)
            data[0][i] = i-1;
            //Datos
        for(int i=1; i<mTable.firstElement().size()+1; i++)
            for(int j=0; j<mTable.elementAt(i-1).size()+1; j++)
                    //Primera columna
                if(j == 0)
                    data[i][j] = i-1;
                else{
                    temp = mTable.elementAt(i-1).elementAt(j-1).toString();
                    if(!temp.equals(""))
                        temp = temp.substring(1, temp.length());
                    data[i][j] = temp;
                }
        
        return data;
    }//getData

}//CYKTable
