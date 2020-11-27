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
        return regionRepository.save(region);
    }

    public void saveAll(List<Region> regionList) throws IOException {
        regionRepository.saveAll(regionList);
    }

    public Region getOne(Long id) throws IOException {
        return regionRepository.get(id);
    }

    public Region getFirst(String name) throws IOException {
        return regionRepository.get(name);
    }

    public List<Region> getAll() throws IOException {
        return regionRepository.getAll();
    }

    public void remove(Long id) throws IOException {
        regionRepository.remove(id);
    }
}
