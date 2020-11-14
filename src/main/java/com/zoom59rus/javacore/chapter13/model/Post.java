package main.java.com.zoom59rus.javacore.chapter13.model;

import java.util.Date;
import java.util.Objects;

public class Post {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Post)) return false;
        Post post = (Post) o;
        return getId().equals(post.getId()) &&
                getContent().equals(post.getContent()) &&
                getCreated().equals(post.getCreated()) &&
                getUpdated().equals(post.getUpdated());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getContent(), getCreated(), getUpdated());
    }


}
