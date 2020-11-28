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
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class JavaIORegionRepositoryImpl implements RegionRepository{
    private final String sourcePath;
    private final Gson gson;
    private final Type regionsType;
    private volatile AtomicLong id;

    public JavaIORegionRepositoryImpl(){
        this.sourcePath = "src/main/resources/files/regions.json";
        this.gson = new GsonBuilder().setLenient().setPrettyPrinting().create();
        this.regionsType = new TypeToken<List<Region>>(){}.getType();
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
                    .findAny()
                    .orElse(null);
        }

        region.setId(getId());
        regionList.add(region);
        String json = gson.toJson(regionList, regionsType);

        try(FileWriter fWriter = new FileWriter(sourcePath)){
            fWriter.write(json);
        }

        return region;
    }

    @Override
    public Optional<Region> get(Long id) throws IOException {

        return getAll().stream()
                .filter(r -> r.getId().equals(id))
                .findAny();
    }

    @Override
    public Optional<Region> get(String name) throws IOException {

        return getAll().stream()
                .filter(r -> r.getName().equals(name))
                .findFirst();
    }

    @Override
    public boolean remove(Long id) throws IOException {
        List<Region> regionList = getAll();
        int currentSize = regionList.size();
        if(regionList.isEmpty()){
            return false;
        }

        regionList = regionList.stream()
                .filter(r -> !r.getId().equals(id))
                .collect(Collectors.toList());

        if(currentSize == regionList.size()){
            return false;
        }
        String jsons = gson.toJson(regionList, regionsType);

        try(FileWriter fWriter = new FileWriter(sourcePath)){
            fWriter.write(jsons);
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }

        return true;
    }

    @Override
    public List<Region> saveAll(List<Region> regionList) throws IOException{
        List<Region> regions = getAll();

        regionList = regionList.stream()
                .peek(r -> {
                    try {
                        r.setId(getId());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                })
                .collect(Collectors.toList());

        regions.addAll(regionList);

        try (FileWriter fileWriter = new FileWriter(sourcePath)
        ) {
            fileWriter.write(gson.toJson(regionList, regionsType));
        }

        return regionList;
    }

    @Override
    public List<Region> getAll() throws IOException {
        List<Region> regionList = gson.fromJson(loadFileData(), regionsType);
        if(regionList == null){
            return new ArrayList<>();
        }

        return regionList;
    }

    private String loadFileData() throws IOException{
        try(InputStream is = new FileInputStream(sourcePath)){
            return IOUtils.toString(is, StandardCharsets.UTF_8);
        }
    }
}