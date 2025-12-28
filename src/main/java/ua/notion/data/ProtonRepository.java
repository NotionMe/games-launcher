package ua.notion.data;

import java.util.List;
import ua.notion.components.Proton;

public interface ProtonRepository {
    public List<Proton> findAll();
    public Proton findByVersion(String version);
    public Proton findByName(String name);
}
