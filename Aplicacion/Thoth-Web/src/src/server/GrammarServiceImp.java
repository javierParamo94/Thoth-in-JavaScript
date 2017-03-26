package src.server;

import java.io.StringReader;

import src.client.GrammarService;





import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import src.client.core.grammar.Grammar;
import src.server.parserjavacc.ParseException;
import src.server.parserjavacc.ParserGrammar;
import src.server.parserjavacc.TokenMgrError;

public class GrammarServiceImp extends RemoteServiceServlet implements GrammarService{
		
		@Override
		public String diHola(String name) {
			String saludo;
			saludo = "Hola " + name;
			return saludo;
		}
		
		public Grammar checkContent(String name) {
			System.out.println ("checkContent en el lado del servidor!!");
			Grammar grammar;
	        //String gram = mTextInput.getText();
	        ParserGrammar parser = ParserGrammar.getInstance(new StringReader(name));
	        
	        try{
	            grammar = parser.buildGrammar();
		        System.out.println ("Ha parseado la gramática: " + grammar.toString());
	            //update(grammar);
	            return grammar;
	        }catch (ParseException parExcep){
	        	System.err.println(parExcep);
	            return new Grammar();
	        }
	        catch (TokenMgrError error){
	        	System.err.println(error);
	            return new Grammar();
	        }
	        
		}//checkContent
}
