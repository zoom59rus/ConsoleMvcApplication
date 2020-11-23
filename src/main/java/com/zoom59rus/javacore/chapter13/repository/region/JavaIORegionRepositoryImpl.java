package main.java.com.zoom59rus.javacore.chapter13.repository.region;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import main.java.com.zoom59rus.javacore.chapter13.model.Post;
import main.java.com.zoom59rus.javacore.chapter13.model.Region;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class JavaIORegionRepositoryImpl implements RegionRepository {
    private static final String path = "/Users/anton/JavaDev/ConsoleMvcApplication/src/main/resources/files/regions.txt";
    private static Gson gson = new GsonBuilder().setLenient().setPrettyPrinting().create();
    private AtomicLong currentId;

    public JavaIORegionRepositoryImpl(){
    }

    @Override
    public AtomicLong getNextId() throws IOException {
        AtomicLong id = new AtomicLong(1);
        if (Files.size(Paths.get(path)) != 0) {
            try (FileReader fReader = new FileReader(path);
                 JsonReader jReader = gson.newJsonReader(fReader)
            ) {
                while (fReader.ready()) {
                    Post post = gson.fromJson(jReader, Post.class);
                    if (post.getId() < id.get()) {
                        continue;
                    }
                    id.set(post.getId());
                }

                currentId = new AtomicLong(id.incrementAndGet());
                return currentId;
            } catch (FileNotFoundException e) {
                System.err.print(e.getMessage());
            }
        }

        currentId = id;

        return currentId;
    }

    @Override
    public Region save(Region region) throws IOException {

        if (currentId == null) {
            region.setId(getNextId().getAndIncrement());
        } else {
            region.setId(currentId.getAndIncrement());
        }

        try (FileWriter fw = new FileWriter(path, true)) {
            fw.write(gson.toJson(region) + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return region;
    }

    @Override
    public Region get(Long id) throws IOException {
        try(FileReader fReader = new FileReader(path);
            JsonReader jReader = gson.newJsonReader(fReader);
        ){
            while (fReader.ready()){
                Region r = gson.fromJson(jReader, Region.class);
                if(r.getId().equals(id)){
                    return r;
                }
            }
        }catch (FileNotFoundException e){
            System.err.print(e.getMessage());
        }

        return null;
    }

    @Override
    public Region get(String name) throws IOException {
        try(FileReader fReader = new FileReader(path);
            JsonReader jReader = gson.newJsonReader(fReader)
        ){
            while (fReader.ready()){
                Region r = gson.fromJson(jReader, Region.class);
                if(r.getName().equals(name)){
                    return r;
                }
            }
        }catch (FileNotFoundException e){
            System.err.print(e.getMessage());
        }

        return null;
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

    @Override
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

    @Override
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