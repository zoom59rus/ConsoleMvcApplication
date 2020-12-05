package com.zoom59rus.javacore.chapter13.repository.converter;

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
public class PersistUser {

    @EqualsAndHashCode.Exclude
    private Long id;

    private String firstName;
    private String lastName;

    @EqualsAndHashCode.Exclude
    private List<Long> postsId;

    private Long regionId;

    public User fromPersistUser(List<Post> postList, Region region){
        User user = new User(id, firstName, lastName, postList, region);

        return user;
    }

    public PersistUser fromUser(User user, List<Long> postsId, Long regionId){
        PersistUser persistUser = new PersistUser(user.getId(), user.getFirstName(), user.getLastName(), postsId, regionId);

        return persistUser;
    }
}
