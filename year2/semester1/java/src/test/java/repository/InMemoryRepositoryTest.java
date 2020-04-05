package repository;

import domain.AcademicYearStructure;
import domain.Entity;
import org.junit.Before;
import org.junit.Test;
import validators.ValidationException;
import validators.Validator;

import java.io.IOException;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;

public abstract class InMemoryRepositoryTest<ID, T extends Entity<ID>> {
    private InMemoryRepository<ID, T> repository;

    @Before
    public void before() {
        repository = new InMemoryRepository<ID, T>(validator());
        try {
            AcademicYearStructure structure = AcademicYearStructure.readFromFile("testdata/academicyeartest.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected abstract Validator<T> validator();

    @Test
    public void save() {
        assertEquals(false, repository.findAll().iterator().hasNext());
        T t1 = createValidEntity();
        assertEquals(null, repository.save(t1));
        assertEquals(t1, repository.findAll().iterator().next());
        T t2 = createExistingEntity();
        assertEquals(t2, repository.save(t2));
        try {
            repository.save(null);
            fail();
        } catch (IllegalArgumentException e) {

        }
        try {
            repository.save(createInvalidEntity());
            fail();
        } catch (ValidationException ve) {

        }
    }

    @Test
    public void findOne() {
        try {
            repository.findOne(null);
        } catch (IllegalArgumentException e) {

        }
        T t = createValidEntity();
        assertEquals(null, repository.findOne(t.getId()));
        repository.save(t);
        T t2 = createExistingEntity();
        assertEquals(t, repository.findOne(t2.getId()));
    }

    @Test
    public void findAll() {
        assertEquals(false, repository.findAll().iterator().hasNext());
        T t = createValidEntity();
        repository.save(t);
        assertEquals(t, repository.findAll().iterator().next());
    }

    @Test
    public void delete(){
        try{
            repository.delete(null);
            fail();
        }catch (IllegalArgumentException e){

        }
        T t = createValidEntity();
        ID id = t.getId();
        assertEquals(null, repository.delete(id));
        repository.save(t);
        assertEquals(t, repository.findAll().iterator().next());
        assertEquals(t, repository.delete(t.getId()));
        assertEquals(false, repository.findAll().iterator().hasNext());
    }

    @Test
    public void update(){
        try{
            repository.update(null);
            fail();
        }catch (IllegalArgumentException e){

        }
        T t = createValidEntity();
        assertEquals(t, repository.update(t));
        repository.save(t);
        T t2 = createExistingEntity();
        assertEquals(t, repository.findOne(t.getId()));
        repository.update(t2);
        assertEquals(t2, repository.findOne(t.getId()));
        try{
            repository.update(createInvalidEntity());
            fail();
        }catch (ValidationException ve){

        }
    }
    protected abstract T createInvalidEntity();

    protected abstract T createExistingEntity();

    protected abstract T createValidEntity();
}
