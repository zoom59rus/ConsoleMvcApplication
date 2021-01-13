package com.zoom59rus.javacore.chapter13.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Post{
    @EqualsAndHashCode.Exclude
    @CsvBindByName
    private Long id;

    @CsvBindByName
    private String content;

    @EqualsAndHashCode.Exclude
    @CsvDate("dd/MM/yyyy hh:mm:ss")
    @CsvBindByName
    private Date created;

    @EqualsAndHashCode.Exclude
    @CsvDate("dd/MM/yyyy hh:mm:ss")
    @CsvBindByName
    private Date updated;

    public Post(Long id, String content, Date created) {
        this.id = id;
        this.content = content;
        this.created = created;
        this.updated = created;
    }
}