package com.luv2code.springmvc.service;

import com.luv2code.springmvc.models.*;
import com.luv2code.springmvc.repository.HistoryGradesRepository;
import com.luv2code.springmvc.repository.MathGradesRepository;
import com.luv2code.springmvc.repository.ScienceGradesRepository;
import com.luv2code.springmvc.repository.StudentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource("/application-test.properties")
@SpringBootTest
@Transactional
public class StudentAndGradeServiceTest {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private MathGradesRepository mathGradesRepository;
    @Autowired
    private ScienceGradesRepository scienceGradesRepository;
    @Autowired
    private HistoryGradesRepository historyGradesRepository;
    @Autowired
    private StudentAndGradeService studentAndGradeService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${sql.scripts.create.student}")
    private String createStudentScript;
    @Value("${sql.scripts.create.math.grade}")
    private String createMathGradeScript;
    @Value("${sql.scripts.create.science.grade}")
    private String createScienceGradeScript;
    @Value("${sql.scripts.create.history.grade}")
    private String createHistoryGradeScript;

    @BeforeEach
    public void setUp() {
        jdbcTemplate.execute(createStudentScript);

        jdbcTemplate.execute(createMathGradeScript);
        jdbcTemplate.execute(createScienceGradeScript);
        jdbcTemplate.execute(createHistoryGradeScript);

    }

    @AfterEach
    public void afterEach() {
        studentRepository.deleteAll();
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

        CollegeStudent student = deletedStudent.get();

        assertThat(student.getMathGrade()).hasSize(1);
        assertThat(student.getScienceGrade()).hasSize(1);
        assertThat(student.getHistoryGrade()).hasSize(1);

        studentAndGradeService.deleteStudent(1);

        deletedStudent = studentRepository.findById(1);
        assertFalse(deletedStudent.isPresent());

        assertFalse(mathGradesRepository.findById(1).isPresent());
        assertFalse(scienceGradesRepository.findById(1).isPresent());
        assertFalse(historyGradesRepository.findById(1).isPresent());
    }

    @Test
    @Sql("/insert-data.sql")
    public void getGradeBookService() {
        Iterable<CollegeStudent> gradeBook = studentAndGradeService.getGradeBook();

        List<CollegeStudent> collegeStudents = new ArrayList<>();
        gradeBook.forEach(collegeStudents::add);

        assertEquals(5, collegeStudents.size());
    }

    @Test
    public void createMathGradeService() {
        assertThat(studentAndGradeService.createGrade(80.50, 1, "math")).isTrue();

        Iterable<MathGrade> mathGrades = mathGradesRepository.findMathGradeByStudentId(1);

        assertThat(mathGrades).hasSize(2);
    }

    @Test
    public void createScienceGradeService() {
        assertThat(studentAndGradeService.createGrade(80.50, 1, "science")).isTrue();

        Iterable<ScienceGrade> scienceGrades = scienceGradesRepository.findScienceGradeByStudentId(1);

        assertThat(scienceGrades).hasSize(2);
    }

    @Test
    public void createHistoryGradeService() {
        assertThat(studentAndGradeService.createGrade(80.50, 1, "history")).isTrue();

        Iterable<HistoryGrade> historyGrades = historyGradesRepository.findHistoryGradeByStudentId(1);

        assertThat(historyGrades).hasSize(2);
    }

    @Test
    public void createGradeServiceReturnFalse() {
        assertThat(studentAndGradeService.createGrade(105, 1, "history")).isFalse();
        assertThat(studentAndGradeService.createGrade(-5, 1, "history")).isFalse();
        assertThat(studentAndGradeService.createGrade(105, 2, "history")).isFalse();
        assertThat(studentAndGradeService.createGrade(105, 1, "literature")).isFalse();
    }

    @Test
    public void deleteGradeService() {
        assertThat(studentAndGradeService.deleteGrade(1, "math"))
                .as("Must delete math test").isEqualTo(1);

        assertThat(studentAndGradeService.deleteGrade(1, "science"))
                .as("Must delete science test").isEqualTo(1);

        assertThat(studentAndGradeService.deleteGrade(1, "history"))
                .as("Must delete history test").isEqualTo(1);
    }

    @Test
    public void deleteGradeServiceReturnZero() {
        assertThat(studentAndGradeService.deleteGrade(100, "math"))
                .as("Must return 0").isEqualTo(0);

        assertThat(studentAndGradeService.deleteGrade(1, "literature"))
                .as("Must return 0").isEqualTo(0);
    }

    @Test
    public void studentInfomation() {
        GradebookCollegeStudent gradebookCollegeStudent = studentAndGradeService.getStudentInformation(1);

        assertNotNull(gradebookCollegeStudent);
        assertEquals(1, gradebookCollegeStudent.getId());
        assertEquals("Egor", gradebookCollegeStudent.getFirstname());
        assertEquals("Gavriliuk", gradebookCollegeStudent.getLastname());
        assertEquals("myemail2@gmail.com", gradebookCollegeStudent.getEmailAddress());
        assertEquals(1, gradebookCollegeStudent.getStudentGrades().getMathGradeResults().size());
        assertEquals(1, gradebookCollegeStudent.getStudentGrades().getScienceGradeResults().size());
        assertEquals(1, gradebookCollegeStudent.getStudentGrades().getHistoryGradeResults().size());
    }

    @Test
    public void studentIdDoesntExist() {
        GradebookCollegeStudent gradebookCollegeStudent = studentAndGradeService.getStudentInformation(100);
        assertNull(gradebookCollegeStudent);
    }
}
