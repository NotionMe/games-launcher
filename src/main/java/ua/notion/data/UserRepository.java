package ua.notion.data;

import ua.notion.components.User;

public interface UserRepository {

  void write(User user); // write in file

  User read(); // read from file
}
