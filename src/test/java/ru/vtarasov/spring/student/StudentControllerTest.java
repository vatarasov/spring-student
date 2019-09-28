package ru.vtarasov.spring.student;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import ru.vtarasov.spring.student.StudentControllerTest.StudentControllerTestConfiguration;

/**
 * @author vtarasov
 * @since 27.09.2019
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { StudentController.class, StudentControllerTestConfiguration.class })
@WebAppConfiguration
public class StudentControllerTest {
    @Configuration
    @EnableWebMvc
    public static class StudentControllerTestConfiguration {
        @Bean
        public StudentRegistrationService studentRegistrationService() {
            return Mockito.mock(StudentRegistrationService.class);
        }
    }

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private StudentRegistrationService studentRegistrationService;

    private MockMvc mvc;

    private ObjectMapper objectMapper;

    private Student notRegisteredStudent;
    private Student registeredStudent;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(this.wac).build();

        objectMapper = Jackson2ObjectMapperBuilder.json().build();

        notRegisteredStudent = Student.of("Student", 16);
        registeredStudent = notRegisteredStudent.toBuilder().id("id-registered").build();

        Mockito.when(studentRegistrationService.find("id-not-registered")).thenReturn(Optional.ofNullable(null));
        Mockito.when(studentRegistrationService.find("id-registered")).thenReturn(Optional.of(registeredStudent));
    }

    @Test
    public void shouldNotFoundStudentIfNotRegistered() throws Exception {
        mvc
            .perform(MockMvcRequestBuilders.get("/student/{id}", "id-not-registered"))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void shouldFoundStudentIfRegistered() throws Exception {
        mvc
            .perform(MockMvcRequestBuilders.get("/student/{id}", "id-registered"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(registeredStudent)));
    }

    @Test
    public void shouldReturnRegisteredStudentLocation() throws Exception {
        Mockito.when(studentRegistrationService.register(notRegisteredStudent)).thenReturn(registeredStudent);
        mvc
            .perform(MockMvcRequestBuilders
                .post("/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(notRegisteredStudent)))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.header().string("Location", Matchers.endsWith("/student/id-registered")));
    }

    @Test
    public void shouldReturnBadRequestWhenTryingToRegisterStudentWithNonNullId() throws Exception {
        Mockito.when(studentRegistrationService.register(registeredStudent)).thenReturn(registeredStudent);
        mvc
            .perform(MockMvcRequestBuilders
                .post("/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registeredStudent)))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequestWhenTryingToRegisterStudentWithEmptyNullOrNotPresentedName() throws Exception {
        Student emptyNameStudent = Student.of("", 16);
        Student nullNameStudent = Student.of(null, 16);

        Mockito.when(studentRegistrationService.register(emptyNameStudent)).thenReturn(emptyNameStudent.toBuilder().id("id").build());
        Mockito.when(studentRegistrationService.register(nullNameStudent)).thenReturn(nullNameStudent.toBuilder().id("id").build());

        mvc
            .perform(MockMvcRequestBuilders
                .post("/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emptyNameStudent)))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());

        mvc
            .perform(MockMvcRequestBuilders
                .post("/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nullNameStudent)))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());

        mvc
            .perform(MockMvcRequestBuilders
                .post("/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"age\": 16}"))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequestWhenTryingToRegisterStudentWithNullLessThanSixteenOrNotPresentedAge() throws Exception {
        Student nullAgeStudent = Student.of("Student", null);
        Student lessThanSixteenAgeStudent = Student.of("Student", 15);

        Mockito.when(studentRegistrationService.register(nullAgeStudent)).thenReturn(nullAgeStudent.toBuilder().id("id").build());
        Mockito.when(studentRegistrationService.register(lessThanSixteenAgeStudent)).thenReturn(lessThanSixteenAgeStudent.toBuilder().id("id").build());

        mvc
            .perform(MockMvcRequestBuilders
                .post("/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nullAgeStudent)))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());

        mvc
            .perform(MockMvcRequestBuilders
                .post("/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(lessThanSixteenAgeStudent)))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());

        mvc
            .perform(MockMvcRequestBuilders
                .post("/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Student\"}"))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void shouldUnregisterStudentIfRegistered() throws Exception {
        mvc
            .perform(MockMvcRequestBuilders.delete("/student/{id}", "id-registered"))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void shouldNotFoundStudentWhenUnregisteringOfNotRegistered() throws Exception {
        mvc
            .perform(MockMvcRequestBuilders.delete("/student/{id}", "id-not-registered"))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
