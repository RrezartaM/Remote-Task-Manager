package Server;

import java.rmi.*;
import java.util.*;

import Client.ClientInt;

public interface ServerInterface extends Remote{	
	public boolean login(ClientInt a)throws RemoteException ;
	public void publish(String s)throws RemoteException ;
   public Vector getConnected() throws RemoteException ;
}