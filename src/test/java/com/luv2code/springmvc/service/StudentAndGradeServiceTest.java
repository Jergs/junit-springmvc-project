package com.luv2code.springmvc.service;

import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.repository.StudentRepository;
import com.luv2code.springmvc.service.StudentAndGradeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource("/application.properties")
@SpringBootTest
public class StudentAndGradeServiceTest {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private StudentAndGradeService studentAndGradeService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() {
        jdbcTemplate.execute("insert into student (id, firstname, lastname, email_address) " +
                "values(1, 'Egor', 'Gavriliuk', 'myemail2@gmail.com')");
    }

    @AfterEach
    public void afterEach() {
        jdbcTemplate.execute("delete from student");
    }

    @Test
    public void createStudentService() {
        studentAndGradeService.createStudent("Egor", "Gavriliuk", "myemail@gmail.com");
        Optional<CollegeStudent> studentOpt = studentRepository.findByEmailAddress("myemail@gmail.com");

        assertEquals("myemail@gmail.com", studentOpt.get().getEmailAddress());
    }

    @Test
    public void checkIfStudentIsNull() {
        assertTrue(studentAndGradeService.checkIfStudentIsNull(1));
        assertFalse(studentAndGradeService.checkIfStudentIsNull(0));
    }

    @Test
    public void deleteStudent() {
        Optional<CollegeStudent> deletedStudent = studentRepository.findById(1);
        assertTrue(deletedStudent.isPresent(), "Return true");

        studentAndGradeService.deleteStudent(1);

        deletedStudent = studentRepository.findById(1);
        assertFalse(deletedStudent.isPresent());
    }

    @Test
    @Sql("/insert-data.sql")
    public void getGradeBookService() {
        Iterable<CollegeStudent> gradeBook = studentAndGradeService.getGradeBook();

        List<CollegeStudent> collegeStudents = new ArrayList<>();
        gradeBook.forEach(collegeStudents::add);

        assertEquals(5, collegeStudents.size());
    }
}
