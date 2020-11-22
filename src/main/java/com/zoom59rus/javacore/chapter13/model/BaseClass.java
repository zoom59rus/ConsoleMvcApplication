package main.java.com.zoom59rus.javacore.chapter13.model;

import java.util.Objects;

public class BaseClass {
    private transient Long id;
    private transient String name;

    public BaseClass(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseClass)) return false;
        BaseClass baseClass = (BaseClass) o;
        return Objects.equals(getId(), baseClass.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
