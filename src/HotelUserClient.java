import java.io.*;
import java.net.*;
import java.util.*;

/**
 *         The HotelUserClient class which contains
 *         the client side for our server and hotel class.
 *         @author bbabai00
 */
public class HotelUserClient  {
	/**
	 *         our main method which connects to the server
	 *         and starts using it.
	 *         @author bbabai00
	 */
	public static void main(String[] args) {
		final int PORT = 1181;
		Scanner userIn = new Scanner(System.in);
		try (Socket s = new Socket("localhost", PORT)){
			DataInputStream in = new DataInputStream(s.getInputStream());
			DataOutputStream out = new DataOutputStream(s.getOutputStream());
			boolean done = false;
			// making the main menu
			String listOfCommands = "";
			listOfCommands += "\n\tMain Menu\n";
	        listOfCommands += "-To change your name enter 1\n";
	        listOfCommands += "-To make a reservation enter 2\n";
	        listOfCommands += "-To cancel your reservation enter 3\n";
	        listOfCommands += "-To see the availability information enter 4\n";
            listOfCommands += "-To quit enter 5\n";
	        listOfCommands += " Please enter command: ";	        
	        // welcome message
			String text = in.readUTF();
			System.out.println(text);
			// first command
			System.out.println("Please enter your username: ");
            String output = userIn.nextLine();
            out.writeUTF("USER");
            out.writeUTF(output);
            out.flush();
            System.out.println(in.readUTF());
            // starting the main menu
			while(!done) {
				System.out.println(listOfCommands);
				int command = userIn.nextInt();
				switch(command) {
				case 1: // changing the user name
					System.out.println("Please enter your new username: ");
					output = userIn.next();
					out.writeUTF("USER");
					out.writeUTF(output);
					out.flush();
					System.out.println(in.readUTF());
					break;
				case 2: // making a reservation
					System.out.println("Enter first day of reservation: ");
					int day1 = userIn.nextInt();
					System.out.println("Enter last day of reservation (inclusive): ");
					int day2 = userIn.nextInt();
					out.writeUTF("RESERVE");
					out.writeInt(day1);
					out.writeInt(day2);
					out.flush();
					System.out.println(in.readUTF());
					break;
				case 3: // canceling the user's reservations
					out.writeUTF("CANCEL");
					out.flush();
					System.out.println(in.readUTF());
					break;
				case 4: // checking the availability for reservation
					out.writeUTF("AVAIL");
					out.flush();
					System.out.println(in.readUTF());
					break;
				case 5: // quitting
					done = true;
					out.writeUTF("QUIT");
					out.flush();
					System.out.println(in.readUTF());
					break;
				default: // anything else
					System.out.println("Invalid command: Closing Connection.");
					done = true;
					out.writeUTF("QUIT");
					out.flush();
					break;
				}
			}
		} catch (IOException e) {e.printStackTrace();}
		  finally {userIn.close();}
	}
}