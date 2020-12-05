package com.zoom59rus.javacore.chapter13.model;

import lombok.*;
import lombok.EqualsAndHashCode.Exclude;


@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Region{

    @Exclude
    private Long id;

    private String name;
}
