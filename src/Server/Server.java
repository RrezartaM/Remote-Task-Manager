package Server;

import Client.ClientInt;
import Client.ClientUI;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;


public class Server  extends UnicastRemoteObject implements ServerInterface {
	
	private Vector v=new Vector();	
	public Server() throws RemoteException{}
  //  private ChatUIServer server;
    private  ClientUI client;
		
	public boolean login(ClientInt a) throws RemoteException{
		System.out.println(a.getName() + "  got connected....");
		a.tell("You have Connected successfully.");
		publish(a.getName()+ " has just connected.");
		v.add(a);
		return true;		
	}
	
	public void publish(String s) throws RemoteException{
	    System.out.println(s);
		for(int i=0;i<v.size();i++){
		    try{
		    	ClientInt tmp=(ClientInt)v.get(i);
				tmp.tell(s);
		    }catch(Exception e){
		    	
		    }
		}
	}

	public Vector getConnected() throws RemoteException{
		return v;
	}
	

}
