package main.java.com.zoom59rus.javacore.chapter13.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class User{
    private Long id;
    private String firstName;
    private String lastName;
    private List<Post> posts;
    private Region region;
}