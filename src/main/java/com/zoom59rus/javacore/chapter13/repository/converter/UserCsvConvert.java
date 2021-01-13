package com.zoom59rus.javacore.chapter13.repository.converter;

import com.opencsv.bean.CsvBindByName;
import com.zoom59rus.javacore.chapter13.model.Post;
import com.zoom59rus.javacore.chapter13.model.Region;
import com.zoom59rus.javacore.chapter13.model.User;
import lombok.*;

import java.util.Arrays;
import java.util.List;

@Data
@NoArgsConstructor
public class UserCsvConvert{

    @EqualsAndHashCode.Exclude
    @CsvBindByName
    private Long id;

    @CsvBindByName
    private String firstName;
    @CsvBindByName
    private String lastName;

    @EqualsAndHashCode.Exclude
    @CsvBindByName
    private Long[] postsId;

    @EqualsAndHashCode.Exclude
    @CsvBindByName
    private Long regionId;

    public String getPostsId(){
        return Arrays.toString(postsId);
    }

    public List<Long> getPosts(){
        return Arrays.asList(postsId);
    }

    public static UserCsvConvert convertUser(User user, List<Long> postId, Long regionId){
        UserCsvConvert convertUser = new UserCsvConvert();
        convertUser.setFirstName(user.getFirstName());
        convertUser.setLastName(user.getLastName());
        Long[] arr = new Long[postId.size()];
        postId.toArray(arr);
        convertUser.setPostsId(arr);
        convertUser.setRegionId(regionId);

        return convertUser;
    }

    public static User convertCsvUser(UserCsvConvert userCsvConvert, List<Post> postList, Region region){
        User user = new User(
                null,
                userCsvConvert.getFirstName(),
                userCsvConvert.getLastName(),
                postList,
                region
        );

        return user;
    }
}