package src.client;

import com.google.gwt.user.client.rpc.AsyncCallback;


public interface GrammarServiceAsync {
	void checkContent(String grammar, AsyncCallback callback);
}
