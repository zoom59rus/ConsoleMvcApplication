package com.zoom59rus.javacore.chapter13.repository;

import com.zoom59rus.javacore.chapter13.model.Region;
import com.zoom59rus.javacore.chapter13.repository.GenericRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface RegionRepository extends GenericRepository<Region, Long> {

    Optional<Region> get(String name) throws IOException;
    List<Region> search (String name)throws IOException;
}