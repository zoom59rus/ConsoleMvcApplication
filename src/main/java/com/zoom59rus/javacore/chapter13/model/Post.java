package com.zoom59rus.javacore.chapter13.model;

import lombok.*;
import lombok.EqualsAndHashCode.Exclude;

import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Post{
    @Exclude
    private Long id;

    private String content;
    @Exclude
    private Date created;
    @Exclude
    private Date updated;

    public Post(Long id, String content, Date created) {
        this.id = id;
        this.content = content;
        this.created = created;
        this.updated = created;
    }
}