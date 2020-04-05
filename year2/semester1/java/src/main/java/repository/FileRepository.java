package repository;

import domain.Entity;
import validators.Validator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public abstract class FileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID, E> {
    private String fileName;

    public FileRepository(Validator<E> validator, String fileName) {
        super(validator);
        this.fileName = fileName;
        loadData();
    }

    private void loadData() {
        Path path = Paths.get(fileName);
        try {
            List<String> lines = Files.readAllLines(path);
            lines.forEach(line -> {
                E entity = createEntity(line);
                super.save(entity);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public E save(E entity) {
        E e = super.save(entity);
        writeData();
        return e;
    }

    @Override
    public E delete(ID id) {
        E e = super.delete(id);
        writeData();
        return e;
    }

    @Override
    public E update(E entity) {
        E e = super.update(entity);
        writeData();
        return  e;
    }

    private void writeData(){
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName)))){
            findAll().forEach(e -> {
                String result = writeEntity(e);
                try {
                    writer.write(result);
                    writer.newLine();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected abstract String writeEntity(E e);

    public abstract E createEntity(String line);
}
