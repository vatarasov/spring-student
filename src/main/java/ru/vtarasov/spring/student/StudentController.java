package ru.vtarasov.spring.student;

/**
 *
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private StudentRegistrationService service;

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") String id) {
        Student student = service.find(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student.getName());
    }

    @PostMapping
    public ResponseEntity post(@RequestParam("name") String name) {
        Student student = service.register(name);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().path("/" + student.getId()).build().toUri()).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") String id) {
        Student student = service.find(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        service.unregister(student);
        return ResponseEntity.ok().build();
    }
}
