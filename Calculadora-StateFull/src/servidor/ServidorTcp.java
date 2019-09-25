package servidor;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import util.Calculadora;

public class ServidorTcp{
    
	public static void main (String args[]) {
		Socket clientSocket;
		try{
		int serverPort = 7896; 
		ServerSocket listenSocket = new ServerSocket(serverPort);
		while(true) {
			clientSocket = listenSocket.accept();
			Connection c = new Connection(clientSocket);
		}
	} catch(IOException e) {System.out.println("Listen :"+e.getMessage());}
    }
    
	
    
}

class Connection extends Thread {
	Despachante despachante = new Despachante();
	Socket s;
	public Connection (Socket aClientSocket) {
	    s = aClientSocket;
		this.start();
	}
	
	public String getRequest() throws IOException {
		DataInputStream in = new DataInputStream(s.getInputStream());
		String n = in.readUTF();
		return n;
	}
	
	public void sendResponse(String response) throws IOException {
		DataOutputStream out = new DataOutputStream(s.getOutputStream());
		out.writeUTF(response);
	}
	
	public void run(){
	    try {
	    	String data, response;
	    	System.out.println("Iniciando conexão com IP: " + s.getInetAddress().getHostAddress());
	    	while(true) {
	    	
	    	data = getRequest();
	    	if (data.equals("exit()")){
	    		break;
	    		}
	    	response = despachante.invoke(data);
	    	sendResponse(response);
	    	}
	    	
	    } catch(EOFException e) {System.out.println("EOF:"+e.getMessage());
	    } catch(IOException e) {System.out.println("IO:"+e.getMessage());
	    } finally{ try {
	    	System.out.println("Fechando Conexão com IP: " + s.getInetAddress().getHostAddress());
	    	s.close();
	    	}catch (IOException e){/*close failed*/}}
	}
}