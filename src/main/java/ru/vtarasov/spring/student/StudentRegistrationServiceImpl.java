package ru.vtarasov.spring.student;

import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author vtarasov
 * @since 21.09.2019
 */
@Log4j2
@Service
public class StudentRegistrationServiceImpl implements StudentRegistrationService {
    @Autowired
    private StudentRepository repository;

    @Override
    public Student register(Student student) {
        Student saved = repository.save(student);
        LOG.info("Student was saved. Id: {}, Name: {}, Age: {}", student.getId(), student.getName(), student.getAge());
        return saved;
    }

    @Override
    public void unregister(Student student) {
        repository.deleteById(student.getId());
        LOG.info("Student was deleted. Id: {}, Name: {}, Age: {}", student.getId(), student.getName(), student.getAge());
    }

    @Override
    public Optional<Student> find(String id) {
        Optional<Student> student = repository.findById(id);
        student.ifPresent(student1 ->
            LOG.info("Student was found. Id: {}, Name: {}, Age: {}", student1.getId(), student1.getName(), student1.getAge()));
        return student;
    }
}
