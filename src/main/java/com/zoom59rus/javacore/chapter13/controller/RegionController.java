package com.zoom59rus.javacore.chapter13.controller;

import com.zoom59rus.javacore.chapter13.model.Region;
import com.zoom59rus.javacore.chapter13.repository.io.JavaIORegionRepositoryImpl;
import com.zoom59rus.javacore.chapter13.repository.RegionRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public final class RegionController {
    private final RegionRepository regionRepository;

    public RegionController() {
        this.regionRepository = new JavaIORegionRepositoryImpl();
    }

    public Region save(Region region) throws IOException {
        Region persist = regionRepository.save(region);

        if(persist == null){
            System.err.println("Регион " + region + " не сохранен.");
            return null;
        }

        return persist;
    }

    public String save(String name){
        Region persist = get(name);
        if(persist != null){
            return persist.getName();
        }else persist = new Region(null, name);

        try {
            persist = save(persist);
            return persist.getName();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Region> saveAll(List<Region> regionList) {
        try {
            return regionRepository.saveAll(regionList);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public Region get(Long id){
        Region region = null;
        try {
            region = regionRepository.get(id).orElse(null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return region;
    }

    public Region get(String name){
        Region region = null;
        try {
            region = regionRepository.get(name).orElse(null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return region;
    }

    public List<Region> getAll(){
        try {
            return regionRepository.getAll();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public void remove(Long id) throws IOException {
        if(regionRepository.remove(id)){
            System.out.println("Регион с id:" + id + " удален.");
        }else System.out.println("Регион с id:" + id + " не найден, или не получилось удалить.");
    }

    public Map<Integer, String> search(String name){
        AtomicInteger count = new AtomicInteger(1);
        Map<Integer, String> result = new HashMap<>();
        try {
            regionRepository.search(name).forEach(r -> result.put(count.getAndIncrement(), r.getName()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public Region update(String oldName, String newName){
        Region region = get(oldName);
        if(oldName.equals(newName)){
            return region;
        }

        region.setName(newName);
        System.out.println(region);

        try {
            region = regionRepository.update(region);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(region != null){
            return region;
        }

        return null;
    }
}
