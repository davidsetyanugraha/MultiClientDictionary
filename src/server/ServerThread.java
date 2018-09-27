/**
 * Name: David Setyanugraha, Student Id: 867585
 */

package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import org.json.JSONArray;
import org.json.JSONObject;
import client.Client;
import exception.DictionaryException;

public class ServerThread extends Thread {

  private final String encoding = "UTF-8";
  private Socket clientSocket;
  private int clientId;
  private String clientMessage;
  private Dictionary dictionary;

  public ServerThread(final Socket clientSocket, final int clientId, final Dictionary dictionary) {
    this.clientSocket = clientSocket;
    this.clientId = clientId;
    this.dictionary = dictionary;
  }

  public void run() {
    System.out.println("Client conection number " + clientId + " accepted. Details: "
        + clientSocket.toString() + "\n");

    try (
        BufferedReader in =
            new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), this.encoding));
        BufferedWriter out = new BufferedWriter(
            new OutputStreamWriter(clientSocket.getOutputStream(), this.encoding));) {

      while ((clientMessage = in.readLine()) != null) {
        System.out.println("Message from client " + clientId + ": " + clientMessage);

        JSONObject jsonObj = new JSONObject(clientMessage);
        String response = this.process(jsonObj);

        System.out.println("Sending Response:" + response);
        out.write(response + "\n");

        out.flush();
        System.out.println("Response sent! \n");
      }

      clientSocket.close();

    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  private String process(JSONObject jsonObj) {

    String response = "";
    String word = jsonObj.get("word").toString();
    int command = Integer.parseInt(jsonObj.get("command").toString());

    try {
      if (command == Client.search) {
        String meaning = dictionary.search(word);
        response = word + " : " + meaning;
      } else if (command == Client.delete) {
        response = dictionary.remove(word);
      } else if (command == Client.add) {

        JSONArray arrJson = jsonObj.getJSONArray("meaning");
        String[] meaning = new String[arrJson.length()];

        for (int i = 0; i < arrJson.length(); i++)
          meaning[i] = arrJson.getString(i);

        response = dictionary.insert(word, meaning);
      }
    } catch (DictionaryException | IOException e) {
      response = e.getMessage();
    }

    return response;
  }
}
