package ua.notion.data;

import ua.notion.components.User;

public interface UserRepository {

  public User save(User user); // save data in file

  public User findAll(); // get from file
}
