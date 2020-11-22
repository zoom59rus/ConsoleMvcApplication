package main.java.com.zoom59rus.javacore.chapter13.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import main.java.com.zoom59rus.javacore.chapter13.model.BaseClass;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JavaIORepository<T extends BaseClass, ID extends Number> implements GenericRepository<T, ID> {
    private final String path;
    private final Gson gson;
    private ID currentId;

    public JavaIORepository(String path) {
        this.path = path;
        this.gson = new GsonBuilder().setLenient().setPrettyPrinting().create();
    }

    public ID nextId(){
        Long id = (Long) currentId;
        Long nextId = ++id;

        return (ID) nextId;
    }

    public ID getLastId() throws IOException {
        Long id = 1L;
        try (FileReader fReader = new FileReader(path);
             JsonReader jReader = gson.newJsonReader(fReader)
        ) {

            while (fReader.ready()) {
                T t = gson.fromJson(jReader, BaseClass.class);
                if (t.getId() < id) {
                    continue;
                }
                id = t.getId() + 1;
            }
        } catch (FileNotFoundException e) {
            System.err.print(e.getMessage());
        }

        return (ID) id;
    }

    @Override
    public T save(T t) throws IOException {
        if (isExist(t.getName())) {
            return get(t.getName());
        }

        if(currentId == null){
            currentId = getLastId();
        }

        t.setId((Long) nextId());
        try (FileWriter fw = new FileWriter(path, true)) {
            fw.write(gson.toJson(t) + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return t;
    }

    @Override
    public T get(ID id) throws IOException {
        T t = searchById(id);
        if(t != null){
            return t;
        }

        return null;
    }

    @Override
    public T get(String name) throws IOException {
        T t = searchByName(name);
        if (t != null) {
            return t;
        }

        return null;
    }

    @Override
    public List<T> getAll() throws IOException {
        List<T> lists = new ArrayList<>();
        try(FileReader fReader = new FileReader(path);
            JsonReader jReader = gson.newJsonReader(fReader)
        ){
            while (fReader.ready()){
                lists.add(gson.fromJson(jReader, BaseClass.class));
            }
        }catch (FileNotFoundException e){
            System.err.print(e.getMessage());
        }
        return lists;
    }

    @Override
    public T searchById(ID id) throws IOException {
        T t = null;
        try(FileReader fReader = new FileReader(path);
            JsonReader jReader = gson.newJsonReader(fReader)
        ){
            while (fReader.ready()){
                t = gson.fromJson(jReader, BaseClass.class);
                if(t.getId().equals(id)){
                    return t;
                }
            }
        }catch (FileNotFoundException e){
            System.err.print(e.getMessage());
        }

        return t;
    }

    @Override
    public T searchByName(String name) throws IOException {
        T t = null;
        try(FileReader fReader = new FileReader(path);
            JsonReader jReader = gson.newJsonReader(fReader)
        ){
            while (fReader.ready()){
                t = gson.fromJson(jReader, BaseClass.class);
                if(t.getName().equals(name)){
                    return t;
                }
            }
        }catch (FileNotFoundException e){
            System.err.print(e.getMessage());
        }

        return t;
    }

    @Override
    public boolean isExist(String name) throws IOException {
        T t = searchByName(name);

        return t != null;
    }

    @Override
    public void remove(ID id) {

    }
}
