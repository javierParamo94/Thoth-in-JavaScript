package src.server;

import java.io.StringReader;

import src.client.GrammarService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import src.client.core.grammar.Grammar;
import src.server.parserjavacc.ParseException;
import src.server.parserjavacc.ParserGrammar;
import src.server.parserjavacc.TokenMgrError;

/**
 * 
 * @author User
 *
 */
public class GrammarServiceImp extends RemoteServiceServlet implements
		GrammarService {
 
	/**
	 * 
	 */
	public Grammar checkContent(String gram) {
		Grammar grammar;
		ParserGrammar parser = ParserGrammar
				.getInstance(new StringReader(gram));

		try {
			grammar = parser.buildGrammar();
			return grammar;
		} catch (ParseException parExcep) {
			System.err.println(parExcep);
			return new Grammar();
		} catch (TokenMgrError error) {
			System.err.println(error);
			return new Grammar();
		}

	}// checkContent

}
