package src.client.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("coreservice")
public interface CoreService extends RemoteService{

	String diHola(String name);
}
