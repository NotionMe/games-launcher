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
import ua.notion.data.dto.AppStorageDTO;
import ua.notion.data.mapper.DataMapper;
import ua.notion.utils.Constants.Data;

public class UserData implements UserRepository {

  private static final String VERSION = "1.0";
  private final Gson gson;

  public UserData() {
    this.gson = new GsonBuilder().setPrettyPrinting().create();
  }

  @Override
  public User save(User user) {
    AppStorageDTO storage = new AppStorageDTO(VERSION, DataMapper.toDTO(user));
    try (Writer writer = new FileWriter(Data.USER_DB_FILE)) {
      gson.toJson(storage, writer);
    } catch (IOException e) {
      throw new RuntimeException("Failed to save user data", e);
    }
    return user;
  }

  @Override
  public User findAll() {
    if (!fileIsExists()) {
      return new User();
    }

    try (Reader reader = new FileReader(Data.USER_DB_FILE)) {
      AppStorageDTO storage = gson.fromJson(reader, AppStorageDTO.class);

      if (storage == null || storage.user() == null) {
        return new User();
      }
      return DataMapper.toEntity(storage.user());
    } catch (JsonSyntaxException e) {
      return new User();
    } catch (IOException e) {
      throw new RuntimeException("Failed to load user data", e);
    }
  }


  public static boolean fileIsExists() {
    File file = new File(Data.USER_DB_FILE);
    if (file.exists()) {
      return true;
    }

    return false;
  }
}
