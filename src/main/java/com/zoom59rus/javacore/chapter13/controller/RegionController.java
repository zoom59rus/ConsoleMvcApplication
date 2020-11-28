package com.zoom59rus.javacore.chapter13.controller;

import com.zoom59rus.javacore.chapter13.model.Region;
import com.zoom59rus.javacore.chapter13.repository.region.JavaIORegionRepositoryImpl;
import com.zoom59rus.javacore.chapter13.repository.region.RegionRepository;

import java.io.IOException;
import java.util.List;

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

    public List<Region> saveAll(List<Region> regionList) throws IOException {
        return regionRepository.saveAll(regionList);
    }

    public Region get(Long id) throws IOException {
        return regionRepository.get(id).orElse(null);
    }

    public Region get(String name) throws IOException {
        return regionRepository.get(name).orElse(null);
    }

    public List<Region> getAll() throws IOException {
        return regionRepository.getAll();
    }

    public void remove(Long id) throws IOException {
        if(regionRepository.remove(id)){
            System.out.println("Регион с id:" + id + " удален.");
        }else System.out.println("Регион с id:" + id + " не найден, или не получилось удалить.");
    }
}
