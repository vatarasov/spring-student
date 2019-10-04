package ru.vtarasov.spring.student;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

/**
 * @author vtarasov
 * @since 21.09.2019
 */
@Service
public class StudentRepositoryImpl implements StudentRepository {
    private Map<String, Student> students = new ConcurrentHashMap<>();

    @Override
    public Optional<Student> findById(String id) {
        return Optional.ofNullable(students.get(id));
    }

    @Override
    public void deleteById(String id) {
        students.remove(id);
    }

    @Override
    public Student save(Student student) {
        Student registered = student.toBuilder().id(UUID.randomUUID().toString()).build();
        students.put(registered.getId(), registered);
        return registered;
    }

    @Override
    public <S extends Student> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public Iterable<Student> findAll() {
        return null;
    }

    @Override
    public Iterable<Student> findAllById(Iterable<String> strings) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void delete(Student entity) {

    }

    @Override
    public void deleteAll(Iterable<? extends Student> entities) {

    }

    @Override
    public void deleteAll() {

    }
}