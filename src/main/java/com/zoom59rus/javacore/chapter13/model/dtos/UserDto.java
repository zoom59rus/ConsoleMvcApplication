package main.java.com.zoom59rus.javacore.chapter13.model.dtos;

import lombok.*;
import main.java.com.zoom59rus.javacore.chapter13.model.Post;
import main.java.com.zoom59rus.javacore.chapter13.model.Region;
import main.java.com.zoom59rus.javacore.chapter13.model.User;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private List<Long> postsId;
    private Region region;

    public static UserDto fromUser(User user){
        List<Long> postsId = new ArrayList<>();
        user.getPosts().forEach(post -> postsId.add(post.getId()));
        UserDto userDto = new UserDto(user.getId(), user.getFirstName(), user.getLastName(), postsId, user.getRegion());
        userDto.setId(user.getId());
        return userDto;
    }

    public static User fromUserDto(UserDto userDto, List<Post> postList){
        return new User(null, userDto.getFirstName(), userDto.getLastName(), postList, userDto.getRegion());
    }
}