package com.zoom59rus.javacore.chapter13.repository.converter;

import com.opencsv.bean.CsvBindByName;
import com.zoom59rus.javacore.chapter13.model.Post;
import com.zoom59rus.javacore.chapter13.model.Region;
import com.zoom59rus.javacore.chapter13.model.User;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class UserConverter {

    @EqualsAndHashCode.Exclude
    @CsvBindByName
    private Long id;

    @CsvBindByName
    private String firstName;
    @CsvBindByName
    private String lastName;

    @EqualsAndHashCode.Exclude
    @CsvBindByName
    private List<Long> postsId;

    @CsvBindByName
    private Long regionId;

    public User fromPersistUser(List<Post> postList, Region region){
        User user = new User(id, firstName, lastName, postList, region);

        return user;
    }

    public UserConverter fromUser(User user, List<Long> postsId, Long regionId){
        UserConverter userConverter = new UserConverter(user.getId(), user.getFirstName(), user.getLastName(), postsId, regionId);

        return userConverter;
    }
}