package com.zoom59rus.javacore.chapter13.repository.region;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.zoom59rus.javacore.chapter13.model.Region;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class JavaIORegionRepositoryImpl implements RegionRepository{
    private static final String path = "/Users/anton/JavaDev/ConsoleMvcApplication/src/main/resources/files/regions.json";
    private static Gson gson = new GsonBuilder().setLenient().setPrettyPrinting().create();
    private volatile AtomicLong id;

    public JavaIORegionRepositoryImpl(){
    }

    private AtomicLong getNextId() throws IOException {
        AtomicLong id = new AtomicLong(1);

        List<Region> r = getAll();

        if(r.isEmpty()){
            return id;
        }

        for (Region region : Objects.requireNonNull(r)) {
            if(region.getId() > id.get()){
                id.set(region.getId());
            }
        }

        id.incrementAndGet();
        return id;
    }

    synchronized private Long getId() throws IOException {
        if(id == null){
            id = getNextId();
        }
        return id.getAndIncrement();
    }

    @Override
    public Region save(Region region)throws IOException {
        List<Region> regionList = getAll();
        if(regionList.contains(region)){
            return regionList.stream()
                    .filter(r -> r.equals(region))
                    .findFirst()
                    .orElse(region);
        }

        region.setId(getId());
        regionList.add(region);

        Type type = new TypeToken<List<Region>>(){}.getType();
        String json = gson.toJson(regionList, type);

        try(FileWriter fWriter = new FileWriter(path)){
            fWriter.write(json);
        }

        return region;
    }

    @Override
    public Region get(Long id) throws IOException {

        return getAll().stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Region get(String name) throws IOException {

        return getAll().stream()
                .filter(r -> r.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void remove(Long id) throws IOException {
        List<Region> regionList = getAll();
        if(regionList.isEmpty()){
            return;
        }

        regionList = regionList.stream().filter(r -> !r.getId().equals(id)).collect(Collectors.toList());
        Type type = new TypeToken<List<Region>>(){}.getType();
        String jsons = gson.toJson(regionList, type);

        try(FileWriter fWriter = new FileWriter(path)){
            fWriter.write(jsons);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Region> saveAll(List<Region> regionList) throws IOException{
        List<Region> getRegionList = getAll();

        regionList = regionList.stream()
                .peek(r -> {
                    try {
                        r.setId(getId());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                })
                .collect(Collectors.toList());

        getRegionList.addAll(regionList);

        try (FileWriter fileWriter = new FileWriter(path);
        ) {
            Type list = new TypeToken<List<Region>>(){}.getType();
            fileWriter.write(gson.toJson(regionList, list));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return regionList;
    }

    @Override
    public List<Region> getAll() throws IOException {
        Type listType = new TypeToken<List<Region>>(){}.getType();
        List<Region> regionList = gson.fromJson(loadFileData(), listType);
        if(regionList == null){
            return new ArrayList<>();
        }

        return regionList;
    }

    private String loadFileData() throws IOException{
        try(InputStream is = new FileInputStream(path)){
            return IOUtils.toString(is, StandardCharsets.UTF_8);
        }
    }
}