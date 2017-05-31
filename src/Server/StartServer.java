package Server;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

 
public class StartServer {

   

	public static void main(String[] args) {

        Server server;

		try {
				
            server = new Server();
			Registry registry = LocateRegistry.createRegistry(1099);
			registry.rebind("rrezartamusmurati", server);

            System.out.println("Server started");
		} catch (RemoteException e) {
			e.printStackTrace();
		} 
    }


}