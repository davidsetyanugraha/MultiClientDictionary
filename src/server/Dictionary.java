/**
 * Name: David Setyanugraha, Student Id: 867585
 */

package server;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import exception.DictionaryException;

public class Dictionary {

  private String filename;
  private JSONObject jsonObject;

  public Dictionary(String filename) {
    this.filename = filename;
  }

  public JSONObject parseJSONFile(String filename) throws JSONException, IOException {
    String content = new String(Files.readAllBytes(Paths.get(filename)));
    return new JSONObject(content);
  }

  public String search(String word) throws IOException, DictionaryException {
    jsonObject = this.parseJSONFile(filename);

    if (!jsonObject.has(word)) {
      throw new DictionaryException("There is no \"" + word + "\" in Dictionary");
    }

    String meaning = jsonObject.get(word).toString();

    return meaning;
  }

  public String insert(String word, String[] meaning) throws IOException, DictionaryException {
    jsonObject = this.parseJSONFile(filename);

    if (jsonObject.has(word)) {
      throw new DictionaryException("\"" + word + "\" has already existed in dictionary");
    }

    JSONArray ja = new JSONArray();
    for (String string : meaning) {
      ja.put(string);
    }
    jsonObject.put(word, ja);

    try (BufferedWriter bw = new BufferedWriter(new FileWriter(this.filename))) {
      bw.write(jsonObject.toString());
    }

    return "Add word successful!";
  }

  public String remove(String word) throws IOException, DictionaryException {
    jsonObject = this.parseJSONFile(filename);

    if (jsonObject.remove(word) != null) {
      try (BufferedWriter bw = new BufferedWriter(new FileWriter(this.filename))) {
        bw.write(jsonObject.toString());
      }
    } else {
      throw new DictionaryException("There is no \"" + word + "\" in Dictionary");
    }

    return "Delete word successful!";
  }
}
