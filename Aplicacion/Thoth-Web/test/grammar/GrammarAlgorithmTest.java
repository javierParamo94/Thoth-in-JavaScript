package test.grammar;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Vector;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import core.*;
import core.grammar.Grammar;
import core.grammar.cleaner.*;
import core.grammar.parserjavacc.ParserGrammar;
import core.grammar.parserxml.ReadWriteGrammar;
import test.UseCasesTest;

/**
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 */
public class GrammarAlgorithmTest extends TestCase {

    private Grammar mGrammar;

    private Grammar mTemp;

    public static Test suite () {
        return new TestSuite(GrammarAlgorithmTest.class);

    }//suite

    private void init () {
        try{
            mGrammar = new ReadWriteGrammar().fileInput(new File(UseCasesTest.PATH_CHARGE + "Complete.gra"));
        }
        catch(IOException e){fail("Excepción no deseada al abrir gramática");}
        catch(SAXException e){fail("Excepción no deseada al abrir gramática");}
        catch(ParserConfigurationException e){fail("Excepción no deseada al abrir gramática");}
    }//init

    private Vector<Terminal> buildWord (String s) {
        Vector<Terminal> sol = new Vector<Terminal>();

        for(int i=0; i<s.length(); i++)
            sol.add(new Terminal(s.charAt(i)));

        return sol;
    }

    public void testChomsky () {
        init();
        mTemp = mGrammar.clone();
        Chomsky chomsky = new Chomsky(mTemp);

        assertNotNull(mTemp);
        assertEquals(mTemp.getAxiom(), new NonTerminal("S"));
        assertTrue(chomsky.allSteps());
        assertNotNull(chomsky.getSolution());
        assertFalse(chomsky.getChanges().isEmpty());

        try{
            mTemp = ParserGrammar.getInstance(new StringReader("%start S %% a S b:a; S:A a;%%")).buildGrammar();
        }
        catch (Exception e){
            fail("Excepción no deseada al parsear una gramática");
        }
        catch (Error e){
            fail("Error no deseada al parsear una gramática");
        }
        chomsky = new Chomsky(mTemp);
        assertFalse(chomsky.allSteps());

    }//test

    public void testCleaner () {
        init();
        Vector<Symbol> left = new Vector<Symbol>();
        Vector<Symbol> right = new Vector<Symbol>();

        left.add(new Terminal('a'));
        left.add(new NonTerminal("A"));
        left.add(new Terminal('a'));
        right.add(new NonTerminal("L"));

        mTemp = mGrammar.clone();
        Cleaner clean = new Cleaner(mTemp);
        assertNotNull(mTemp);
        assertTrue(clean.toClean());
        assertNotNull(clean.getSolution());

        mTemp = mGrammar.clone();
        mTemp.createProduction(left, right);
        clean = new Cleaner(mTemp);
        assertTrue(clean.toClean());

        mTemp = mGrammar.clone();
        mTemp.createProduction(left);
        left = new Vector<Symbol>();
        right = new Vector<Symbol>();
        left.add(new NonTerminal("X"));
        right.add(new NonTerminal("L"));
        clean = new Cleaner(mTemp);
        assertTrue(clean.toClean());
        assertNotNull(mTemp);
        assertTrue(clean.toClean());
        assertNotNull(clean.getSolution());

        mTemp = mGrammar.clone();
        mTemp.createProduction(left, right);
        mTemp.setAxiom((NonTerminal)left.firstElement());
        mTemp.calculateType();
        clean = new Cleaner(mTemp);
        assertFalse(clean.toClean());
        assertNotNull(mTemp);
        assertTrue(clean.toClean());

        mTemp = mGrammar.clone();
        mTemp.createProduction(left, right);
        left = new Vector<Symbol>();
        right = new Vector<Symbol>();
        left.add(new NonTerminal("L"));
        right.add(new Terminal('a'));
        mTemp.createProduction(left, right);
        mTemp.setAxiom(new NonTerminal("X"));
        mTemp.calculateType();
        clean = new Cleaner(mTemp);
        assertTrue(clean.toClean());



    }//test

    public void testCYK () {
        init();
        Vector<Terminal> word = new Vector<Terminal>();
        try{
            mTemp = ParserGrammar.getInstance(new StringReader("% start S%%S : Ê | f | S | T_· T_a' | T_r T_* | S S' | T_r S'' | C C' ; "+
        "T_· : · ;"+
        "T_a' : a ;"+
        "T_r : r ;"+
        "T_* : * ;"+
        "S' : T_· T_a' | T_r T_* | T_r S'' ;"+
        "S'' : T_r T_* ;"+
        "C : f | C C' ;"+
        "C' : T_a' C'' ;"+
        "C'' : T_d C''' ;"+
        "T_d : d ;"+
        "C''' : T_r T_t ;"+
        "T_t : t ;"+

        "%%")).buildGrammar();
        }
        catch (Exception e){
            fail("Excepción no deseada al parsear una gramática");
        }
        catch (Error e){
            fail("Error no deseada al parsear una gramática");
        }
        word = buildWord("fadrt");
        CYKAlgorithm cyk = new CYKAlgorithm(mTemp, word);
        cyk.allSteps();
        assertTrue(cyk.getAccept());
        assertNotNull(cyk.getTable());
        word = buildWord("fdfadrt");
        cyk = new CYKAlgorithm(mTemp, word);
        cyk.allSteps();
        assertFalse(cyk.getAccept());
        assertNotNull(cyk.getTable());
        assertNotNull(cyk.getTable().getData());

    }//test

    public void testRecursion () {
        init();
        mTemp = mGrammar.clone();

        EliminateDirectRecursion direct = new EliminateDirectRecursion(mTemp);
        EliminateIndirectRecursion indirect = new EliminateIndirectRecursion(mTemp.clone());
        assertTrue(direct.allSteps());
        assertFalse(indirect.allSteps());
        
        try{
            mTemp = ParserGrammar.getInstance(new StringReader("% start S%%S:A; A:S; S:r; a A:P;%%")).buildGrammar();
        }
        catch (Exception e){
            fail("Excepción no deseada al parsear una gramática");
        }
        catch (Error e){
            fail("Error no deseada al parsear una gramática");
        }
        direct = new EliminateDirectRecursion(mTemp);
        indirect = new EliminateIndirectRecursion(mTemp.clone());
        assertFalse(direct.allSteps());
        assertFalse(indirect.allSteps());
        mTemp.removeProduction(mTemp.getProductions().lastElement());
        mTemp.calculateType();
        direct = new EliminateDirectRecursion(mTemp);
        indirect = new EliminateIndirectRecursion(mTemp.clone());
        assertFalse(direct.allSteps());
        assertTrue(indirect.allSteps());
        assertNull(direct.getNewProductions());
        assertNotNull(direct.getRecProductions());
        assertNotNull(indirect.getAllProds());
        assertNotNull(indirect.getOldProd());
        assertNotNull(indirect.getRecDirProds());

    }//test

    public void testPNG () {
        init();
        mTemp = mGrammar.clone();
        
        EliminatePNG png = new EliminatePNG(mTemp);
        
        assertTrue(png.allSteps());
        assertNotNull(png.getUnited());
        assertNotNull(png.getUnitedProductions());
        assertFalse(png.getSolution().equals(mTemp));

    }//test

    public void testSA () {
        init();
        mTemp = mGrammar.clone();
        
        EliminateSA sa = new EliminateSA(mTemp);
        
        assertTrue(sa.allSteps());
        assertNotNull(sa.getCancel());
        assertFalse(sa.getSolution().equals(mTemp));
        sa = new EliminateSA(sa.getSolution());
        assertTrue(sa.firstStep());
        assertNotNull(sa.currentCancel());
        assertTrue(sa.nextStep());

    }//test

    public void testLeftFactoring () {
        init();
        mTemp = mGrammar.clone();
        
        LeftFactoring lf = new LeftFactoring(mTemp);
        
        assertTrue(lf.allSteps());
        assertNotNull(lf.getNewProd());
        assertNotNull(lf.getPrefix());
        assertTrue(lf.getSolution().equals(mTemp));
        
        try{
            mTemp = new ReadWriteGrammar().fileInput(new File(UseCasesTest.PATH_CHARGE + "factoring.gra"));
        }
        catch(IOException e){fail("Excepción no deseada al abrir gramática");}
        catch(SAXException e){fail("Excepción no deseada al abrir gramática");}
        catch(ParserConfigurationException e){fail("Excepción no deseada al abrir gramática");}
        
        lf = new LeftFactoring(mTemp);
        
        assertTrue(lf.allSteps());
        
    }//test
}