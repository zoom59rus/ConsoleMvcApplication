package com.zoom59rus.javacore.chapter13.model.dtos;

import com.zoom59rus.javacore.chapter13.model.Post;
import com.zoom59rus.javacore.chapter13.model.Region;
import com.zoom59rus.javacore.chapter13.model.User;
import lombok.*;
import lombok.EqualsAndHashCode.Exclude;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
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

    public String postsToString(){
        StringBuilder sb = new StringBuilder();
        AtomicInteger count = new AtomicInteger(1);
        posts.forEach(p -> sb.append("\t\t")
                                .append(count.getAndIncrement())
                                .append(". ")
                                .append(p.getContent())
                                .append("\n"));

        return sb.toString();
    }


    public String toString(){
        return "Имя: " + firstName + "\n" +
                "Фамилия: " + lastName + "\n" +
                "Регион: " + region.getName() + "\n" +
                "Контент: \n" + postsToString();
    }
}