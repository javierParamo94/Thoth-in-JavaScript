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
import core.grammar.parserjavacc.ParserGrammar;
import core.grammar.parserxml.ReadWriteGrammar;
import test.UseCasesTest;

/**
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 */
public class GrammarTest extends TestCase {
    
    public static Test suite () {
        
        return new TestSuite(GrammarTest.class);
    }//suite
    
    public void testAxiom () {
        Grammar g2 = new Grammar();
        
        try{
            g2 = new ReadWriteGrammar().fileInput(new File(UseCasesTest.PATH_CHARGE + "grammarIn.gra"));
        }
        catch(IOException e){fail("Excepción no deseada al abrir gramática");}
        catch(SAXException e){fail("Excepción no deseada al abrir gramática");}
        catch(ParserConfigurationException e){fail("Excepción no deseada al abrir gramática");}
        
        assertNotNull(g2);
        g2.setAxiom(g2.getNonTerminals().lastElement());
        assertEquals(g2.getAxiom(), g2.getNonTerminals().firstElement());
        g2.setAxiom(g2.getAxiom());
        
        
    }

    public void testType () {
        Grammar g2 = new Grammar();
        
        try{
            g2 = new ReadWriteGrammar().fileInput(new File(UseCasesTest.PATH_CHARGE + "regular.gra"));
        }
        catch(IOException e){fail("Excepción no deseada al abrir gramática");}
        catch(SAXException e){fail("Excepción no deseada al abrir gramática");}
        catch(ParserConfigurationException e){fail("Excepción no deseada al abrir gramática");}
        
        assertNotNull(g2);
        assertEquals(3, g2.calculateType());
        
        try{
            g2 = new ReadWriteGrammar().fileInput(new File(UseCasesTest.PATH_CHARGE + "independiente.gra"));
        }
        catch(IOException e){fail("Excepción no deseada al abrir gramática");}
        catch(SAXException e){fail("Excepción no deseada al abrir gramática");}
        catch(ParserConfigurationException e){fail("Excepción no deseada al abrir gramática");}
        
        assertNotNull(g2);
        assertEquals(2, g2.calculateType());
        
        try{
            g2 = new ReadWriteGrammar().fileInput(new File(UseCasesTest.PATH_CHARGE + "dependiente.gra"));
        }
        catch(IOException e){fail("Excepción no deseada al abrir gramática");}
        catch(SAXException e){fail("Excepción no deseada al abrir gramática");}
        catch(ParserConfigurationException e){fail("Excepción no deseada al abrir gramática");}
        
        assertNotNull(g2);
        assertEquals(1, g2.calculateType());
        
        try{
            g2 = new ReadWriteGrammar().fileInput(new File(UseCasesTest.PATH_CHARGE + "chomsky.gra"));
        }
        catch(IOException e){fail("Excepción no deseada al abrir gramática");}
        catch(SAXException e){fail("Excepción no deseada al abrir gramática");}
        catch(ParserConfigurationException e){fail("Excepción no deseada al abrir gramática");}
        
        g2.toString();
        g2.isDirectRecursive();
        g2.isIndirectRecursive();
        assertNotNull(g2);
        assertEquals(0, g2.calculateType());
        g2.removeProduction(g2.getProductions().firstElement());
        assertEquals(0, g2.calculateType());
                
    }
    
    public void testEliminateProduction() {
        Grammar g2 = new Grammar();
        
        try{
            g2 = new ReadWriteGrammar().fileInput(new File(UseCasesTest.PATH_CHARGE + "regular.gra"));
        }
        catch(IOException e){fail("Excepción no deseada al abrir gramática");}
        catch(SAXException e){fail("Excepción no deseada al abrir gramática");}
        catch(ParserConfigurationException e){fail("Excepción no deseada al abrir gramática");}
        
        Grammar g3 = g2.clone();
        g3.removeProduction(g3.getProductions().lastElement());
        assertNotNull(g2);
        assertEquals(g2.getProductions().elementAt(1), g3.getProductions().lastElement());
        g3.removeProduction(g2.getProductions().lastElement());
        
    }//test
    
    public void testRemoveSymbol () {
        Grammar g2 = new Grammar();
        
        try{
            g2 = ParserGrammar.getInstance(new StringReader("%start S %% S:a; T:A a;%%")).buildGrammar();
        }
        catch (Exception e){
            fail("Excepción no deseada al parsear una gramática");
        }
        catch (Error e){
            fail("Error no deseada al parsear una gramática");
        }
        assertEquals(g2.getProductions().size(), 2);
        g2.removeSymbol(new Terminal('a'));
        g2.removeSymbol(new NonTerminal("T"));
        assertEquals(g2.getProductions().size(), 1);
            }//test
    
    public void testIsRecursive () {
        Grammar g2 = new Grammar();
        Vector<Symbol> left = new Vector<Symbol>(1,0);
        try{
            g2 = ParserGrammar.getInstance(new StringReader("%start S %% S:S T; S:Ê; T:A a; A:R s; R:E; R:T; E:Ê; E:T;%%")).buildGrammar();
            g2.getXML();
        }
        catch (Exception e){
            fail("Excepción no deseada al parsear una gramática");
        }
        catch (Error e){
            fail("Error no deseada al parsear una gramática");
        }
        assertNotNull(g2);
        assertEquals(true, g2.isDirectRecursive());
        assertEquals(true, g2.isIndirectRecursive());
        g2.removeProduction(g2.getProductions().firstElement());
        assertEquals(true, g2.isIndirectRecursive());
        g2.removeProduction(g2.getProductions().firstElement());
        assertEquals(true, g2.isIndirectRecursive());
        g2.removeProduction(g2.getProductions().firstElement());
        assertEquals(false, g2.isIndirectRecursive());
        g2.removeProduction(g2.getProductions().firstElement());
        assertEquals(false, g2.isIndirectRecursive());
        left.add(new NonTerminal("S"));
        g2.createProduction(left);
        
    }//test
    
    public void testEqualGrammar () {
        Grammar g1 = new Grammar();
        Grammar g2 = new Grammar();
        
        try{
            g1 = ParserGrammar.getInstance(new StringReader("%start A %% S:S T; A:Ê; A:T Y t;%%")).buildGrammar();
            g2 = ParserGrammar.getInstance(new StringReader("%start S %% S:S T; A:Ê;%%")).buildGrammar();
        }
        catch (Exception e){
            fail("Excepción no deseada al parsear una gramática");
        }
        catch (Error e){
            fail("Error no deseada al parsear una gramática");
        }
        assertFalse(g1.equals(g2));
        g1.setAxiom(new NonTerminal("S"));
        assertFalse(g1.equals(g2));
        g1.removeProduction(g1.getProductions().lastElement());
        assertTrue(g1.equals(g2));
        
    }//test
    
    public void testStringGrammar () {
        Grammar g1 = new Grammar();
        Grammar g2 = new Grammar();
        Vector<Symbol> left = new Vector<Symbol>();
        left.add(new NonTerminal("Y"));
        
        try{
            g1 = ParserGrammar.getInstance(new StringReader("%start S %% S:S T; A:Ê;%%")).buildGrammar();
            g2 = ParserGrammar.getInstance(new StringReader("%start S %% S:S T; A:Ê;%%")).buildGrammar();
        }
        catch (Exception e){
            fail("Excepción no deseada al parsear una gramática");
        }
        catch (Error e){
            fail("Error no deseada al parsear una gramática");
        }
        assertEquals(g2.completeToString(), g1.completeToString());
        assertEquals(g2.toString(), g1.toString());
        g2.removeSymbol(new NonTerminal("S"));
        assertFalse(g2.toString().equals(g1.toString()));
        g2.setAxiom(null);
        assertFalse(g2.completeToString().equals(g1.completeToString()));
        g1.createProduction(left, new Vector<Symbol>());
        
    }//test
    
}//CoreTest
