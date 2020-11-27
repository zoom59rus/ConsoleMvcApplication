package com.zoom59rus.javacore.chapter13.model;

import lombok.*;
import lombok.EqualsAndHashCode.Exclude;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class User{
    @Exclude
    private Long id;

    private String firstName;
    private String lastName;
    @Exclude
    private List<Long> postsId;

    private Region region;
}