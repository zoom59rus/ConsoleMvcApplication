package com.zoom59rus.javacore.chapter13.repository;

import com.zoom59rus.javacore.chapter13.model.Post;
import com.zoom59rus.javacore.chapter13.repository.GenericRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends GenericRepository<Post, Long> {

    List<Post> getPostListByIds(List<Long> postIds);
    Optional<Post> get(String content) throws IOException;

}
