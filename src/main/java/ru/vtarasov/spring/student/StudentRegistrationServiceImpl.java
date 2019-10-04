package ru.vtarasov.spring.student;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author vtarasov
 * @since 21.09.2019
 */
@Service
public class StudentRegistrationServiceImpl implements StudentRegistrationService {
    @Autowired
    private StudentRepository repository;

    @Override
    public Student register(Student student) {
        return repository.save(student);
    }

    @Override
    public void unregister(Student student) {
        repository.deleteById(student.getId());
    }

    @Override
    public Optional<Student> find(String id) {
        return repository.findById(id);
    }
}
