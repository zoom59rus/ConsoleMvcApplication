package com.zoom59rus.javacore.chapter13.repository.post;

import com.zoom59rus.javacore.chapter13.model.Post;
import com.zoom59rus.javacore.chapter13.repository.GenericRepository;

import java.util.List;

public interface PostRepository extends GenericRepository<Post, Long> {

    List<Post> getPostListByIds(List<Long> postIds);

}
