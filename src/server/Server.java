/**
 * Name: David Setyanugraha, Student Id: 867585
 */

package server;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

  private static Dictionary dictionary;
  private static ServerSocket socket;

  public static void main(String[] args) {

    int port = Integer.parseInt(args[0]);
    File dictionaryPath = new File(args[1]);

    if (dictionaryPath.exists() && !dictionaryPath.isDirectory()) {
      dictionary = new Dictionary(args[1]);

      try {
        socket = new ServerSocket(port);
        int clientId = 0;

        while (true) {
          clientId++;
          System.out.println("Server listening on port " + port
              + " for a connection with dictionary path " + dictionaryPath);
          Socket clientSocket = socket.accept();
          ServerThread thread = new ServerThread(clientSocket, clientId, dictionary);
          thread.start();
        }
      } catch (IOException ex) {
        System.out.println("Unable to Run! Please check again port or your connection!");
      }

    } else {
      System.err.println("file " + args[1] + " not found");
      System.err.println("Please check again your dictionary file path!");
    }
  }
}
