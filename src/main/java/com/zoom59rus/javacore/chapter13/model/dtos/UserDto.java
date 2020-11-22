package main.java.com.zoom59rus.javacore.chapter13.model.dtos;

import main.java.com.zoom59rus.javacore.chapter13.model.Post;
import main.java.com.zoom59rus.javacore.chapter13.model.Region;
import main.java.com.zoom59rus.javacore.chapter13.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private List<Long> postsId;
    private Region region;

    public UserDto(String firstName, String lastName, List<Long> postsId, Region region) {
        this.id = null;
        this.firstName = firstName;
        this.lastName = lastName;
        this.postsId = postsId;
        this.region = region;
    }

    public static UserDto fromUser(User user){
        List<Long> postsId = new ArrayList<>();
        user.getPosts().forEach(post -> postsId.add(post.getId()));
        UserDto userDto = new UserDto(user.getFirstName(), user.getLastName(), postsId, user.getRegion());
        userDto.setId(user.getId());
        return userDto;
    }

    public static User fromUserDto(UserDto userDto, List<Post> postList){
        return new User(null, userDto.getFirstName(), userDto.getLastName(), postList, userDto.getRegion());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Long> getPostsId() {
        return postsId;
    }

    public void setPostsId(List<Long> postsId) {
        this.postsId = postsId;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDto)) return false;
        UserDto userDto = (UserDto) o;
        return getFirstName().equals(userDto.getFirstName()) &&
                getLastName().equals(userDto.getLastName()) &&
                getPostsId().equals(userDto.getPostsId()) &&
                getRegion().equals(userDto.getRegion());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName(), getPostsId(), getRegion());
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", postsId=" + postsId +
                ", region=" + region +
                '}';
    }
}