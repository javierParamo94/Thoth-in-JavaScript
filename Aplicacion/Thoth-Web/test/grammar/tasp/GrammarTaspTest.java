package test.grammar.tasp;

import java.io.File;
import java.io.IOException;
import java.util.Vector;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import test.UseCasesTest;
import core.Terminal;
import core.TerminalEpsilon;
import core.grammar.Grammar;
import core.grammar.parserxml.ReadWriteGrammar;
import core.grammar.tasp.FirstFollow;
import core.grammar.tasp.TaspTable;
import core.grammar.tasp.WordTasp;

/**
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 */
public class GrammarTaspTest extends TestCase{

    private Grammar mGrammar;

    public static Test suite () {

        return new TestSuite(GrammarTaspTest.class);
    }//suite
    
    private Vector<Terminal> buildWord (String s) {
        Vector<Terminal> sol = new Vector<Terminal>();

        for(int i=0; i<s.length(); i++)
            sol.add(new Terminal(s.charAt(i)));
        if(s.length()==0)
            sol.add(new TerminalEpsilon());

        return sol;
    }

    public void testFirstFollow () {
        try{
            mGrammar = new ReadWriteGrammar().fileInput(new File(UseCasesTest.PATH_CHARGE + "all.gra"));
            assertTrue(mGrammar.equals(mGrammar));
        }
        catch(IOException e){fail("Excepción no deseada al guardar gramática");}
        catch(SAXException e){fail("Excepción no deseada al guardar gramática");}
        catch(ParserConfigurationException e){fail("Excepción no deseada al guardar gramática");}

        FirstFollow ff = new FirstFollow(mGrammar);

        assertTrue(ff.calculateFirst());
        assertTrue(ff.calculateFollow());
        assertNotNull(ff.getFirst());
        assertNotNull(ff.getFollow());

    }//test

    public void testChomsky () {
        try{
            mGrammar = new ReadWriteGrammar().fileInput(new File(UseCasesTest.PATH_CHARGE + "chomsky.gra"));
            assertTrue(mGrammar.equals(mGrammar));
        }
        catch(IOException e){fail("Excepción no deseada al guardar gramática");}
        catch(SAXException e){fail("Excepción no deseada al guardar gramática");}
        catch(ParserConfigurationException e){fail("Excepción no deseada al guardar gramática");}

        FirstFollow ff = new FirstFollow(mGrammar);

        assertFalse(ff.calculateFirst());
        assertFalse(ff.calculateFollow());
        assertEquals(ff.getFirst().size(), 4);
        assertEquals(ff.getFollow().size(), 4);
    }//test

    public void testTasp () {
        try{
            mGrammar = new ReadWriteGrammar().fileInput(new File(UseCasesTest.PATH_CHARGE + "all.gra"));
            assertTrue(mGrammar.equals(mGrammar));
        }
        catch(IOException e){fail("Excepción no deseada al guardar gramática");}
        catch(SAXException e){fail("Excepción no deseada al guardar gramática");}
        catch(ParserConfigurationException e){fail("Excepción no deseada al guardar gramática");}

        FirstFollow ff = new FirstFollow(mGrammar);

        TaspTable tasp = new TaspTable(ff);

        assertTrue(tasp.calculateTasp());
        assertEquals(tasp.getTerminals().size(), 6);
        assertEquals(tasp.getNonTerminals().size(), 5);
        assertEquals(tasp.getSolution().length, 6);
        assertNotNull(tasp.getGrammar().completeToString());

        try{
            mGrammar = new ReadWriteGrammar().fileInput(new File(UseCasesTest.PATH_CHARGE + "First.gra"));
            assertTrue(mGrammar.equals(mGrammar));
        }
        catch(IOException e){fail("Excepción no deseada al guardar gramática");}
        catch(SAXException e){fail("Excepción no deseada al guardar gramática");}
        catch(ParserConfigurationException e){fail("Excepción no deseada al guardar gramática");}

        ff = new FirstFollow(mGrammar);
        tasp = new TaspTable(ff);
        assertTrue(tasp.calculateTasp());

    }//test

    public void testAnalysis () {
        try{
            mGrammar = new ReadWriteGrammar().fileInput(new File(UseCasesTest.PATH_CHARGE + "all.gra"));
            assertTrue(mGrammar.equals(mGrammar));
        }
        catch(IOException e){fail("Excepción no deseada al guardar gramática");}
        catch(SAXException e){fail("Excepción no deseada al guardar gramática");}
        catch(ParserConfigurationException e){fail("Excepción no deseada al guardar gramática");}

        FirstFollow ff = new FirstFollow(mGrammar);
        assertTrue(ff.calculateFirst());
        assertTrue(ff.calculateFollow());
        assertNotNull(ff.getFirst());
        assertNotNull(ff.getFollow());
        TaspTable tasp = new TaspTable(ff);
        tasp.calculateTasp();
        WordTasp word = new WordTasp(mGrammar, tasp, buildWord("c"));
        assertTrue(word.allSteps());
        assertTrue(word.isAccept());
        word = new WordTasp(mGrammar, tasp, buildWord(""));
        assertFalse(word.allSteps());
        assertFalse(word.isAccept());

    }//test

}