package main.java.com.zoom59rus.javacore.chapter13.model;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Post{
    private Long id;
    private String content;
    private Date created;
    private Date updated;

    public Post(Long id, String content, Date created) {
        this.id = id;
        this.content = content;
        this.created = created;
        this.updated = created;
    }
}