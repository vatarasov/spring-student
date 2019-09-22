package ru.vtarasov.spring.student;

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
    public Student register(String name) {
        Student student = repository.save(Student.builder().name(name).build());
        return student;
    }

    @Override
    public void unregister(Student student) {
        repository.delete(student.getId());
    }

    @Override
    public Student find(String id) {
        return repository.get(id);
    }
}
