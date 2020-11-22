package main.java.com.zoom59rus.javacore.chapter13.repository.region;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import main.java.com.zoom59rus.javacore.chapter13.model.Region;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JavaIORegionRepositoryImpl implements RegionRepository {
    private static final String path = "/Users/anton/JavaDev/ConsoleMvcApplication/src/main/resources/files/regions.txt";
    private static Gson gson = new GsonBuilder().setLenient().setPrettyPrinting().create();
    private Long currentId;

    public JavaIORegionRepositoryImpl() throws IOException {
        currentId = getNextId();
    }

    public static Long getNextId() throws IOException {
        Long id = 1L;
        try (FileReader fReader = new FileReader(path);
             JsonReader jReader = gson.newJsonReader(fReader)
        ) {

            while (fReader.ready()) {
                Region region = gson.fromJson(jReader, Region.class);
                if (region.getId() < id) {
                    continue;
                }
                id = region.getId() + 1;
            }
        } catch (FileNotFoundException e) {
            System.err.print(e.getMessage());
        }

        return id;
    }

    @Override
    public Region save(Region region) throws IOException {
        if (isExist(region.getName())) {
            return get(region.getName());
        }

        region.setId(currentId++);
        try (FileWriter fw = new FileWriter(path, true)) {
            fw.write(gson.toJson(region) + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return region;
    }

    @Override
    public Region get(Long id) throws IOException {
        Region region = searchById(id);
        if (region != null) {
            return region;
        }

        return null;
    }

    @Override
    public Region get(String name) throws IOException {
        Region region = searchByName(name);
        if (region != null) {
            return region;
        }

        return null;
    }

    @Override
    public Region searchById(Long id) throws IOException {
        Region region = null;
        try (FileReader fReader = new FileReader(path);
             JsonReader jReader = gson.newJsonReader(fReader)
        ) {
            while (fReader.ready()) {
                region = gson.fromJson(jReader, Region.class);
                if (region.getId().equals(id)) {
                    return region;
                }
            }
        } catch (FileNotFoundException e) {
            System.err.print(e.getMessage());
        }

        return region;
    }

    @Override
    public Region searchByName(String name) throws IOException {
        Region region = null;
        try (FileReader fReader = new FileReader(path);
             JsonReader jReader = gson.newJsonReader(fReader)
        ) {
            while (fReader.ready()) {
                region = gson.fromJson(jReader, Region.class);
                if (region.getName().equals(name)) {
                    return region;
                }
            }
        } catch (FileNotFoundException e) {
            System.err.print(e.getMessage());
        }

        return region;
    }

    @Override
    public boolean isExist(String name) throws IOException {
        Region r = searchByName(name);
        if (r != null) {
            return true;
        }

        return false;
    }

    @Override
    public void remove(Long id) {
        List<Region> regionList = new ArrayList<>();
        try {
            regionList = getAll();
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
        if(regionList.size() == 0){
            return;
        }

        List<Region> result = regionList.stream()
                .filter(r -> !r.getId().equals(id))
                .collect(Collectors.toList());

        saveAll(result);
    }

    public void saveAll(List<Region> regionList){
        try (FileWriter fileWriter = new FileWriter(path)) {
            for (Region r : regionList) {
                try {
                    fileWriter.write(gson.toJson(r) + "\n");
                } catch (IOException e) {
                    System.err.print(e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Region> getAll() throws IOException {
        List<Region> regionList = new ArrayList<>();
        try (FileReader fileReader = new FileReader(path);
             JsonReader jsonReader = gson.newJsonReader(fileReader)
        ){
            while (fileReader.ready()) {
                regionList.add(gson.fromJson(jsonReader, Region.class));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return regionList;
    }
}