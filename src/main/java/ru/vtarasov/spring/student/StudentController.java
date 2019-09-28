package ru.vtarasov.spring.student;

/**
 *
 */

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/student")
public class StudentController {

    @RequiredArgsConstructor
    private final class StudentNotFoundException extends Exception {}

    @Autowired
    private StudentRegistrationService service;

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Student get(@PathVariable("id") String id) throws StudentNotFoundException {
        return service.find(id).orElseThrow(StudentNotFoundException::new);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity post(@Valid @RequestBody Student student) {
        student = service.register(student);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().path("/" + student.getId()).build().toUri()).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") String id) throws StudentNotFoundException {
        Student student = service.find(id).orElseThrow(StudentNotFoundException::new);
        service.unregister(student);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(StudentNotFoundException.class)
    private ResponseEntity handleNotFound(StudentNotFoundException e) {
        return ResponseEntity.notFound().build();
    }
}
