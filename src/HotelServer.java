import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *         The HotelServer class which contains
 *         the server for our hotel class.
 *         @author bbabai00
 */
public class HotelServer {
	/**
	 *         our main method which starts running the server.
	 *         @author bbabai00
	 */
    public static void main(String[] args)   {
        final int PORT = 1181;
        Hotel myHotel = new Hotel();
    	try (ServerSocket server = new ServerSocket(PORT)){
    		System.out.println("Waiting for client to connect . . . ");
    		while (true) {
    			try {
    				Socket s = server.accept();
	    			System.out.println("Client connected.");
	                Thread t = new Thread(new ClientHandler(s, myHotel));
    		        t.start();
    			} catch (IOException e) {e.printStackTrace();}
    		}   
    	} catch (IOException e) {e.printStackTrace();}
	}
    
    /**
	 *         Class ClientHandler which deals with each client!
	 *         @author bbabai00
	 */
    public static class ClientHandler implements Runnable {
 	   private final Socket s;
 	   private String name;
 	   private Hotel hotel;
 	   
 	  /**
 	   *         constructor for our client handler which sets our
 	   *         hotel and the socket.
 	   *         @author bbabai00
 	   */
 	   public ClientHandler(Socket s, Hotel hotel) {
 		   this.s = s;
 		   this.hotel = hotel;
 	   }
 	   
 	  /**
  	   *         method which does everything that the server needs to do
  	   *         in response to each client.
  	   *         @author bbabai00
  	   */
 	   public void run() {
 	         try (Socket s2 = s) {
 	        	boolean done = false;
 	            DataInputStream in = new DataInputStream(s.getInputStream());
 	            DataOutputStream out = new DataOutputStream(s.getOutputStream());
 	            // welcome message
 	            out.writeUTF("Welcome, You have connected to the Hotel server.");
                out.flush();
                // getting the name at first
                String input = in.readUTF(); 
                if(!input.equals("USER")) {
                	out.writeUTF("Invalid command: Closing Connection.");
                    out.flush();
                    done = true;
                    System.out.println("Client disconnected.");
                } else {
                	name = in.readUTF(); 
                	out.writeUTF("Hello, " + name);
                	out.flush();
                }
                // responding to each command
 	            while(!done) {
 	            	input = in.readUTF();
 	            	switch(input) {
 	            	case "USER": // changing the user name
 	            		name = in.readUTF(); 
 	                	out.writeUTF("Hello, " + name);
 	                	out.flush();
 	                	break;
 	            	case "RESERVE": // making a reservation
 	            		int first = in.readInt();
 	            		int last = in.readInt();
 	            		if(hotel.requestReservation(this.name, first, last)) {
 	            			out.writeUTF("Reservation made: " + name + " from " + first + " through " + last);
 						} else {
 							out.writeUTF("Reservation unsuccessful: " + name + " from " + first + " through " + last);
 						}
 	            		out.flush();
 	            		break;
 	            	case "CANCEL": // canceling the client's current reservations
 	            		if(hotel.cancelReservation(name)) {
 	            			out.writeUTF("Reservations successfully canceled for " + name);
 						} else {
 							out.writeUTF("Reservations not canceled for " + name + ", no current reservation");
 						}
 	            		out.flush();
 	            		break;
 	            	case "AVAIL": // checking the availability for reservation
 	            		out.writeUTF(hotel.toString());
 	            		out.flush();
 						break;
 	            	case "QUIT": // quitting
 	            		out.writeUTF("Closing Connection.");
 	            		out.flush();
 						done = true;
 						System.out.println("Client disconnected.");
 						break;
 	            	default: // anything else
 	            		out.writeUTF("Invalid command: Closing Connection.");
 	            		out.flush();
 						done = true;
 						System.out.println("Client disconnected.");
 						break;
 	            	}
 	            }
 	         } catch (IOException e) {e.printStackTrace();}
 	   }
    }
    
}