package com.luv2code.springmvc.controller;

import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.models.GradebookCollegeStudent;
import com.luv2code.springmvc.repository.StudentRepository;
import com.luv2code.springmvc.service.StudentAndGradeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource("/application.properties")
@AutoConfigureMockMvc
@SpringBootTest
public class GradeBookControllerTest {

    private static MockHttpServletRequest mockHttpServletRequest;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Mock
    private StudentAndGradeService studentAndGradeService;
    @Autowired
    private StudentRepository studentRepository;

    @BeforeAll
    public static void beforeAll() {
        mockHttpServletRequest = new MockHttpServletRequest();
        mockHttpServletRequest.setParameter("firstname", "Egor");
        mockHttpServletRequest.setParameter("lastname", "Gavriliuk");
        mockHttpServletRequest.setParameter("emailAddress", "someemail@gmail.com");
    }

    @BeforeEach
    public void beforeEach() {
        jdbcTemplate.execute("insert into student (id, firstname, lastname, email_address) " +
                "values(1, 'Egor', 'Gavriliuk', 'myemail2@gmail.com')");
    }

    @AfterEach
    public void afterEach() {
        jdbcTemplate.execute("delete from student");
    }

    @Test
    public void getStudentHttpRequest() throws Exception {
        CollegeStudent student1 = new GradebookCollegeStudent("Egor", "Gavriliuk",
                "someemail@gmail.com");
        CollegeStudent student2 = new GradebookCollegeStudent("Egor", "Hello",
                "someemail2@gmail.com");

        List<CollegeStudent> collegeStudents = new ArrayList<>(List.of(student1, student2));
        when(studentAndGradeService.getGradeBook()).thenReturn(collegeStudents);

        assertIterableEquals(collegeStudents, studentAndGradeService.getGradeBook());

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(status().isOk()).andReturn();

        ModelAndView mav = mvcResult.getModelAndView();
        ModelAndViewAssert.assertViewName(mav, "index");
    }

    @Test
    public void createStudentHttpRequest() throws Exception {

        CollegeStudent student1 = new CollegeStudent("Eric", "Roby", "email123@gmail.com");
        List<CollegeStudent> studentList = new ArrayList<>(List.of(student1));
        when(studentAndGradeService.getGradeBook()).thenReturn(studentList);

        assertEquals(studentList, studentAndGradeService.getGradeBook());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("firstname", mockHttpServletRequest.getParameterValues("firstname"))
                        .param("lastname", mockHttpServletRequest.getParameterValues("lastname"))
                        .param("emailAddress", mockHttpServletRequest.getParameterValues("emailAddress")))
                .andExpect(status().isOk()).andReturn();

        ModelAndView mav = result.getModelAndView();
        ModelAndViewAssert.assertViewName(mav, "index");

        Optional<CollegeStudent> studentOptional = studentRepository.findByEmailAddress("someemail@gmail.com");
        assertNotNull(studentOptional.get());
    }

    @Test
    public void deleteStudentHttpRequest() throws Exception {
        assertTrue(studentRepository.findById(1).isPresent());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/delete/student/{id}", 1))
                .andExpect(status().isOk()).andReturn();

        ModelAndView mav = result.getModelAndView();
        ModelAndViewAssert.assertViewName(mav, "index");

        // Deleted
        assertFalse(studentRepository.findById(1).isPresent());
    }

    @Test
    public void deleteStudentHttpRequestErrorPage() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/delete/student/{id}", 0))
                .andExpect(status().isOk()).andReturn();

        ModelAndView mav = result.getModelAndView();
        ModelAndViewAssert.assertViewName(mav, "error");
    }

}
