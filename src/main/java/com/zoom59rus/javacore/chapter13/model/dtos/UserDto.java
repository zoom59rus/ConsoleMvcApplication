package com.zoom59rus.javacore.chapter13.model.dtos;

import com.zoom59rus.javacore.chapter13.model.Post;
import com.zoom59rus.javacore.chapter13.model.Region;
import com.zoom59rus.javacore.chapter13.model.User;
import lombok.*;
import lombok.EqualsAndHashCode.Exclude;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class UserDto {
    @Exclude
    private Long id;

    private String firstName;
    private String lastName;
    @Exclude
    private List<Post> posts;

    private Region region;

    public static UserDto fromUser(User user, List<Post> postList){
        return new UserDto(user.getId(), user.getFirstName(), user.getLastName(), postList, user.getRegion());
    }

    public static User fromUserDto(UserDto userDto, List<Long> postsId){
        return new User(null, userDto.getFirstName(), userDto.getLastName(), postsId, userDto.getRegion());
    }
}