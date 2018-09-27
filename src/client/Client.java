/**
 * Name: David Setyanugraha, Student Id: 867585
 */

package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.List;
import org.json.JSONObject;
import exception.ClientException;

public class Client {

  private Socket socket;
  private String encoding = "UTF-8";
  private BufferedReader in;
  private BufferedWriter out;

  // constant
  public static final int search = 1;
  public static final int add = 2;
  public static final int delete = 3;

  public Client(Socket socket) throws UnsupportedEncodingException, IOException {
    this.socket = socket;
    this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), encoding));
    this.out = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream(), encoding));
  }

  public String send(int command, String word) throws IOException, ClientException {
    if (word.isEmpty()) {
      throw new ClientException("Warning: please enter word!");
    } else {
      if ((command == search) || (command == delete)) {
        out.write(new JSONObject().put("command", command).put("word", word).toString());
      } else {
        throw new ClientException("Warning: command not found!");
      }
    }
    out.write("\n");
    out.flush();

    String received = this.in.readLine();
    return received;
  }

  public String send(int command, String word, List<String> meanings)
      throws IOException, ClientException {

    if (word.isEmpty()) {
      throw new ClientException("Warning: please enter word!");
    } else {
      if (command == add) {
        String[] meaning = meanings.toArray(new String[0]);
        if (meaning.length != 0 && meaning != null) {
          out.write(new JSONObject().put("command", command).put("word", word)
              .put("meaning", meaning).toString());
        } else {
          throw new ClientException("Warning: please enter meaning(s)!");
        }
      } else {
        throw new ClientException("Warning: command not found!");
      }
    }
    out.write("\n");
    out.flush();

    String received = this.in.readLine();
    return received;
  }
}
