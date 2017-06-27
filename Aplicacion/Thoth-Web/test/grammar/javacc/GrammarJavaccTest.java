package test.grammar.javacc;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

import core.grammar.Grammar;
import core.grammar.parserjavacc.ParserGrammar;
import core.grammar.parserxml.ReadWriteGrammar;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import test.UseCasesTest;

public class GrammarJavaccTest extends TestCase{

    private Grammar mGrammar;

    public static Test suite () {

        return new TestSuite(GrammarJavaccTest.class);
    }//suite

    public void testError () {
        try{
            mGrammar = ParserGrammar.getInstance(new StringReader("%start S %% A:a; %%")).buildGrammar();
        }
        catch (Exception e){
        assertNull(mGrammar);
        }
        catch (Error e){
            fail("Error no deseada al parsear una gramática");
        }
        assertNull(mGrammar);

        try{
            mGrammar = ParserGrammar.getInstance(new StringReader("%ññstart  %% A:a; %%")).buildGrammar();
        }
        catch (Exception e){
            fail("Error no deseada al parsear una gramática");
        }
        catch (Error e){

        }
        assertNull(mGrammar);
    }//test

    public void testTokenManager () {
        Grammar g2 = new Grammar();

        try{
            g2 = new ReadWriteGrammar().fileInput(new File(UseCasesTest.PATH_CHARGE + "grammarIn.gra"));
        }
        catch(IOException e){fail("Excepción no deseada al abrir gramática");}
        catch(SAXException e){fail("Excepción no deseada al abrir gramática");}
        catch(ParserConfigurationException e){fail("Excepción no deseada al abrir gramática");}

        assertNotNull(g2);

    }//test

    public void testAxiom () {
        try{
            mGrammar = ParserGrammar.getInstance(new StringReader("%start  %% A:a; %%")).buildGrammar();
        }
        catch (Exception e){}
        catch (Error e){fail("Error no deseado al parsear una gramática");}
        assertNull(mGrammar);
    }//test

    public void testSyntaxis () {
        try{
            mGrammar = ParserGrammar.getInstance(new StringReader("%start  %% A||:a; %%")).buildGrammar();
        }
        catch (Exception e){

        }
        catch (Error e){
            fail("Error no deseada al parsear una gramática");
        }
        assertNull(mGrammar);
    }//test

}