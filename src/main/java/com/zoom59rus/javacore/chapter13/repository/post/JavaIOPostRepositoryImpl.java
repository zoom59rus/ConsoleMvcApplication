package main.java.com.zoom59rus.javacore.chapter13.repository.post;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import main.java.com.zoom59rus.javacore.chapter13.model.Post;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JavaIOPostRepositoryImpl implements PostRepository {
    private static final String path = "/Users/anton/JavaDev/ConsoleMvcApplication/src/main/resources/files/posts.txt";
    private static Gson gson = new GsonBuilder().setLenient().setPrettyPrinting().create();
    private Long currentId;

    public JavaIOPostRepositoryImpl() throws IOException {
    }

    public Long getNextId() throws IOException {
        Long id = 1L;
        try (FileReader fReader = new FileReader(path);
             JsonReader jReader = gson.newJsonReader(fReader)
        ) {
            while (fReader.ready()) {
                Post post = gson.fromJson(jReader, Post.class);
                if (post.getId() < id) {
                    continue;
                }
                id = post.getId() + 1;
            }
        } catch (FileNotFoundException e) {
            System.err.print(e.getMessage());
        }

        currentId = id;

        return id;
    }

    @Override
    public Post save(Post post) throws IOException {
        if (isExist(post.getContent())) {
            return get(post.getContent());
        }

        if (currentId == null) {
            post.setId(getNextId());
        } else {
            post.setId(currentId);
        }
        try (FileWriter fw = new FileWriter(path, true)) {
            fw.write(gson.toJson(post) + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return post;
    }

    @Override
    public Post get(Long id) throws IOException {
        Post post = searchById(id);
        if (post != null) {
            return post;
        }

        return null;
    }

    @Override
    public Post get(String name) throws IOException {
        Post post = searchByName(name);
        if (post != null) {
            return post;
        }

        return null;
    }

    @Override
    public Post searchById(Long id) throws IOException {
        Post post = null;
        try (FileReader fReader = new FileReader(path);
             JsonReader jReader = gson.newJsonReader(fReader)
        ) {
            while (fReader.ready()) {
                post = gson.fromJson(jReader, Post.class);
                if (post.getId().equals(id)) {
                    return post;
                }
            }
        } catch (FileNotFoundException e) {
            System.err.print(e.getMessage());
        }

        return post;
    }

    @Override
    public Post searchByName(String name) throws IOException {
        Post post = null;
        try (FileReader fReader = new FileReader(path);
             JsonReader jReader = gson.newJsonReader(fReader)
        ) {
            while (fReader.ready()) {
                post = gson.fromJson(jReader, Post.class);
                if (post.getContent().equals(name)) {
                    return post;
                }
            }
        } catch (FileNotFoundException e) {
            System.err.print(e.getMessage());
        }

        return post;
    }

    @Override
    public boolean isExist(String name) throws IOException {
        Post r = searchByName(name);

        return r != null;
    }

    @Override
    public void remove(Long id) {
        List<Post> postList = new ArrayList<>();
        try {
            postList = getAll();
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
        if(postList.size() == 0){
            return;
        }

        List<Post> result = postList.stream()
                .filter(r -> !r.getId().equals(id))
                .collect(Collectors.toList());

        saveAll(result);
    }

    public void saveAll(List<Post> postList){
        try (FileWriter fileWriter = new FileWriter(path)) {
            for (Post r : postList) {
                try {
                    fileWriter.write(gson.toJson(r) + "\n");
                } catch (IOException e) {
                    System.err.print(e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Post> getAll() throws IOException {
        List<Post> postList = new ArrayList<>();
        try (FileReader fileReader = new FileReader(path);
             JsonReader jsonReader = gson.newJsonReader(fileReader)
        ){
            while (fileReader.ready()) {
                postList.add(gson.fromJson(jsonReader, Post.class));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return postList;
    }
}
