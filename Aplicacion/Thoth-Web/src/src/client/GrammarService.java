package src.client;

import src.client.core.grammar.Grammar;


import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("grammarservice")
public interface GrammarService extends RemoteService{


	public Grammar checkContent(String name);
}
