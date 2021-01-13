package com.zoom59rus.javacore.chapter13.model;

import com.opencsv.bean.CsvBindByName;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Region{

    @EqualsAndHashCode.Exclude
    @CsvBindByName
    private Long id;

    @CsvBindByName
    private String name;
}