package com.zoom59rus.javacore.chapter13.repository.io.csv;

import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.zoom59rus.javacore.chapter13.model.Region;
import com.zoom59rus.javacore.chapter13.repository.RegionRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class CsvRegionRepositoryImpl implements RegionRepository {
    private static CsvRegionRepositoryImpl INSTANCE = null;
    private static final String SOURCE_PATH = "src/main/resources/files/csv/region.csv";
    private volatile AtomicLong id;
    private Lock lock;

    private CsvRegionRepositoryImpl() {
        this.lock = new ReentrantLock();
    }

    public static CsvRegionRepositoryImpl getInstance() {
        if (INSTANCE == null) {
            synchronized (CsvRegionRepositoryImpl.class) {
                INSTANCE = new CsvRegionRepositoryImpl();
            }
        }

        return INSTANCE;
    }

    @Override
    public Optional<Region> get(String name) throws IOException {
        return getAll().stream()
                .filter(r -> r.getName().equals(name))
                .findFirst();
    }

    @Override
    public List<Region> search(String name) throws IOException {
        return getAll().stream()
                .filter(r -> r.getName().equals(name))
                .collect(Collectors.toList());
    }

    @Override
    public Region save(Region region) throws IOException {
        List<Region> regions = getAll();
        if (regions.contains(region)) {
            return regions.stream()
                    .filter(r -> r.getName().equals(region.getName()))
                    .findFirst()
                    .get();
        }

        region.setId(getId());

        try (CSVWriter csw = new CSVWriter(new FileWriter(SOURCE_PATH, true))) {
            String[] str = {String.valueOf(region.getId()), region.getName()};
            csw.writeNext(str);
        }
        return region;
    }

    @Override
    public List<Region> saveAll(List<Region> lists) throws IOException {
        List<Region> regions = getAll();

        if (regions.size() == 0) {
            return saveToData(
                    lists.stream()
                            .peek(r -> r.setId(getId()))
                            .collect(Collectors.toList())
            );
        }

        lists.removeAll(regions);
        regions.addAll(
                lists.stream()
                        .peek(r -> r.setId(getId()))
                        .collect(Collectors.toList())
        );

        return saveToData(regions);
    }

    @Override
    public Optional<Region> get(Long id) {
        return getAll().stream()
                .filter(r -> r.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Region> getAll() {
        try {
            return new CsvToBeanBuilder<Region>(new FileReader(SOURCE_PATH))
                    .withType(Region.class)
                    .build()
                    .parse();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        return new ArrayList<>();
    }

    @Override
    public boolean remove(Long id) throws IOException {
        List<Region> regions = getAll();

        if (regions.removeIf(r -> r.getId().equals(id))) {
            saveToData(regions);
            return true;
        }

        return false;
    }

    @Override
    public Region update(Region region) throws IOException {
        List<Region> regions = getAll();

        if(regions.removeIf(r -> r.getId().equals(region.getId()))){
            regions.add(region);
            saveToData(regions);
            return region;
        }

        return null;
    }

    private Long getId() {
        lock.lock();
        try {
            if (id == null) {
                id = new AtomicLong(lastId() + 1L);
            }

            return id.getAndIncrement();
        } finally {
            lock.unlock();
        }
    }

    private Long lastId() {
        List<Region> allRegionsRecorded = getAll();

        if (allRegionsRecorded.size() == 0) {
            return 0L;
        }

        return allRegionsRecorded.stream()
                .max(Comparator.comparingLong(Region::getId))
                .get()
                .getId();
    }

    private List<Region> saveToData(List<Region> regions) throws IOException {
        try (CSVWriter csw = new CSVWriter(new FileWriter(SOURCE_PATH))) {
            StatefulBeanToCsv<Region> writer = new StatefulBeanToCsvBuilder<Region>(csw).build();
            writer.write(regions);

            return regions;
        } catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }
}