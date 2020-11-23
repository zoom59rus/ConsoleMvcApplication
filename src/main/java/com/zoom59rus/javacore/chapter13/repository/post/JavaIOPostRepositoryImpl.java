package main.java.com.zoom59rus.javacore.chapter13.repository.post;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import main.java.com.zoom59rus.javacore.chapter13.model.Post;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class JavaIOPostRepositoryImpl implements PostRepository {
    private static final String path = "/Users/anton/JavaDev/ConsoleMvcApplication/src/main/resources/files/posts.txt";
    private static Gson gson = new GsonBuilder().setLenient().setPrettyPrinting().create();
    private AtomicLong currentId;

    public JavaIOPostRepositoryImpl() {
    }

    public AtomicLong getNextId() throws IOException {
        AtomicLong id = new AtomicLong(1);
        if (Files.size(Paths.get(path)) != 0) {
            try (FileReader fReader = new FileReader(path);
                 JsonReader jReader = gson.newJsonReader(fReader)
            ) {
                while (fReader.ready()) {
                    Post post = gson.fromJson(jReader, Post.class);
                    if (post.getId() < id.get()) {
                        continue;
                    }
                    id.set(post.getId());
                }

                currentId = new AtomicLong(id.incrementAndGet());
                return currentId;
            } catch (FileNotFoundException e) {
                System.err.print(e.getMessage());
            }
        }

        currentId = id;

        return currentId;
    }

    @Override
    public Post save(Post post) throws IOException {

        if (currentId == null) {
            post.setId(getNextId().getAndIncrement());
        } else {
            post.setId(currentId.getAndIncrement());
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
        try(FileReader fReader = new FileReader(path);
            JsonReader jReader = gson.newJsonReader(fReader)
        ) {
            while (fReader.ready()){
                Post p = gson.fromJson(jReader, Post.class);
                if(p.getId().equals(id)){
                    return p;
                }
            }
        }catch (FileNotFoundException e){
            System.err.print(e.getMessage());
        }

        return null;
    }

    @Override
    public Post get(String name) throws IOException {
        try(FileReader fReader = new FileReader(path);
            JsonReader jReader = gson.newJsonReader(fReader)
        ) {
            while (fReader.ready()){
                Post p = gson.fromJson(jReader, Post.class);
                if(p.getContent().equals(name)){
                    return p;
                }
            }
        }catch (FileNotFoundException e){
            System.err.print(e.getMessage());
        }

        return null;
    }

    @Override
    public void remove(Long id) {
        List<Post> postList = new ArrayList<>();
        try {
            postList = getAll();
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
        if (postList.size() == 0) {
            return;
        }

        List<Post> result = postList.stream()
                .filter(r -> !r.getId().equals(id))
                .collect(Collectors.toList());

        saveAll(result);
    }

    public void saveAll(List<Post> postList) {
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
        ) {
            while (fileReader.ready()) {
                postList.add(gson.fromJson(jsonReader, Post.class));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return postList;
    }
}
