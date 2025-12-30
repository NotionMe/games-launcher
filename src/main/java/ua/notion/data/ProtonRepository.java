package ua.notion.data;

import java.util.List;
import ua.notion.components.Proton;
import ua.notion.data.dto.ProtonDTO;

public interface ProtonRepository {

    public void save(List<ProtonDTO> avabliesProtons);
    public List<Proton> findAll();
    public Proton findByVersion(String version);
    public Proton findByName(String name);
}
