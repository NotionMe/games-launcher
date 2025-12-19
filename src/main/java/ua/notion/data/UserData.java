package ua.notion.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import ua.notion.components.User;

public class UserData implements UserRepository {

  private static final String PATH = "user.json";
  private final Gson gson;

  public UserData() {
    this.gson = new GsonBuilder().setPrettyPrinting().create();
  }

  @Override
  public void write(User user) {
    try (Writer writer = new FileWriter(PATH)) {
      gson.toJson(user, writer);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public User read() {

    if (!fileIsExists()) {
      return new User();
    }

    try (Reader reader = new FileReader(PATH)) {
      User user = gson.fromJson(reader, User.class);

      if (user == null) {
        return new User();
      }
      return user;
    } catch (JsonSyntaxException e) {
      return new User();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }


  public static boolean fileIsExists() {
    File file = new File(PATH);
    if (file.exists())
      return true;

    return false;
  }
}
