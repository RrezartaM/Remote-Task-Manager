package Client;



import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;

public class Client extends UnicastRemoteObject implements ClientInt{
	
	private String name;
	private ClientUI ui;

	public Client (String n) throws RemoteException {
		name=n;
    }
	
	public void tell(String st) throws RemoteException{
	System.out.println(st);
		ui.writeMsg(st);
       
	}
	public String getName() throws RemoteException{
		return name;
	}
	
	public void setGUI( ClientUI t){ 
		ui=t ; 
	}


  
}
