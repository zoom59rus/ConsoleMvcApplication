package main.java.com.zoom59rus.javacore.chapter13.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Region extends BaseClass{
    private Long id;
    private String name;
    private List<Long> usersId;

    public Region(Long id, String name) {
        super(id);
        this.id = id;
        this.name = name;
        usersId = new ArrayList<>();
    }

    public Region(Long id) {
        super(id);
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getUsersId() {
        return usersId;
    }

    public void setUsersId(List<Long> usersId) {
        this.usersId = usersId;
    }

    public void addUsersId(Long usersId){
        this.usersId.add(usersId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Region)) return false;
        Region region = (Region) o;
        return getName().equals(region.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

    @Override
    public String toString() {
        return "Region{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
