package ua.notion.data;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ua.notion.components.Proton;
import ua.notion.data.dto.ProtonDTO;
import ua.notion.data.mapper.DataMapper;
import ua.notion.utils.Constants.Data;

public class ProtonImpl implements ProtonRepository {

    private final Gson gson;

    public ProtonImpl() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Override
    public List<Proton> findAll() {
        if (!fileIsExists())
            return new ArrayList<>();

        try (Reader reader = new FileReader(new File(Data.PROTON_PATH_JSON.toString()))) {
            ProtonDTO[] protonDTOs = gson.fromJson(reader, ProtonDTO[].class);

            if (protonDTOs == null)
                return new ArrayList<>();

            return Arrays.stream(protonDTOs).map(DataMapper::toEntity).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private final boolean fileIsExists() {
        return Data.PROTON_PATH_JSON.exists();
    }

    @Override
    public Proton findByName(String name) {
        String normalizedVersion = (name == null) ? "" : name.trim().toLowerCase();

        return findAll().stream()
                .filter(proton -> proton.name().trim().toLowerCase().contains(name)).findFirst()
                .orElse(new Proton("", "", ""));
    }

    @Override
    public Proton findByVersion(String version) {
        String normalizedVersion = (version == null) ? "" : version.trim().toLowerCase();

        return findAll().stream()
                .filter(proton -> proton.name().toLowerCase().contains(normalizedVersion))
                .findFirst().orElse(new Proton("", "", ""));
    }
}
