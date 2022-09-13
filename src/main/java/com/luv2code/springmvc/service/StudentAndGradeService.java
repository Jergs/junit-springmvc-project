package com.luv2code.springmvc.service;

import com.luv2code.springmvc.models.*;
import com.luv2code.springmvc.repository.HistoryGradesRepository;
import com.luv2code.springmvc.repository.MathGradesRepository;
import com.luv2code.springmvc.repository.ScienceGradesRepository;
import com.luv2code.springmvc.repository.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class StudentAndGradeService {

    private final StudentRepository studentRepository;
    private final MathGradesRepository mathGradesRepository;
    private final ScienceGradesRepository scienceGradesRepository;
    private final HistoryGradesRepository historyGradesRepository;
    private final MathGrade mathGrade;
    private final ScienceGrade scienceGrade;
    private final HistoryGrade historyGrade;
    private StudentGrades studentGrades;

    public void createStudent(String firstName, String lastName, String email) {
        CollegeStudent student = new CollegeStudent(firstName, lastName, email);
        studentRepository.save(student);
    }

    public Boolean checkIfStudentIsNull(Integer id) {
        Optional<CollegeStudent> student = studentRepository.findById(id);
        return student.isPresent();
    }

    public void deleteStudent(Integer id) {
        studentRepository.deleteById(id);
    }

    public Iterable<CollegeStudent> getGradeBook() {
        Iterable<CollegeStudent> collegeStudents = studentRepository.findAll();
        return collegeStudents;
    }

    public boolean createGrade(double grade, int studentId, String gradeType) {
        if (!checkIfStudentIsNull(studentId))
            return false;

        if (grade >= 0 && grade <= 100) {
            if ("math".equals(gradeType)) {
                mathGrade.setGrade(grade);
                Optional<CollegeStudent> studentOptional = studentRepository.findById(studentId);
                if(studentOptional.isPresent()){
                    CollegeStudent student = studentOptional.get();
                    student.addMathGrade(mathGrade);
                    studentRepository.save(student);
                }
                return true;
            }
            if ("science".equals(gradeType)) {
                //scienceGrade.setId(0);
                scienceGrade.setGrade(grade);
                Optional<CollegeStudent> studentOptional = studentRepository.findById(studentId);
                if(studentOptional.isPresent()){
                    CollegeStudent student = studentOptional.get();
                    student.addScienceGrade(scienceGrade);
                    studentRepository.save(student);
                }
                return true;
            }
            if ("history".equals(gradeType)) {
                historyGrade.setGrade(grade);
                Optional<CollegeStudent> studentOptional = studentRepository.findById(studentId);
                if(studentOptional.isPresent()){
                    CollegeStudent student = studentOptional.get();
                    student.addHistoryGrade(historyGrade);
                    studentRepository.save(student);
                }
                return true;
            }
        }

        return false;
    }

    public int deleteGrade(int id, String gradeType) {
        int studentId = 0;

        if ("math".equals(gradeType)) {
            Optional<MathGrade> mathGrade = mathGradesRepository.findById(id);
            if (mathGrade.isEmpty()) {
                return studentId;
            }
            studentId = mathGrade.get().getStudent().getId();
            mathGradesRepository.deleteById(id);
        }
        if ("science".equals(gradeType)) {
            Optional<ScienceGrade> scienceGrade = scienceGradesRepository.findById(id);
            if (scienceGrade.isEmpty()) {
                return studentId;
            }
            studentId = scienceGrade.get().getStudent().getId();
            scienceGradesRepository.deleteById(id);
        }
        if ("history".equals(gradeType)) {
            Optional<HistoryGrade> historyGrade = historyGradesRepository.findById(id);
            if (historyGrade.isEmpty()) {
                return studentId;
            }
            studentId = historyGrade.get().getStudent().getId();
            historyGradesRepository.deleteById(id);
        }

        return studentId;
    }

    public GradebookCollegeStudent getStudentInformation(int id) {
        Optional<CollegeStudent> studentOptional = studentRepository.findById(id);
        if(studentOptional.isEmpty())
            return null;
        CollegeStudent student = studentOptional.get();

        Iterable<MathGrade> mathGrades = mathGradesRepository.findMathGradeByStudentId(id);
        Iterable<ScienceGrade> scienceGrades = scienceGradesRepository.findScienceGradeByStudentId(id);
        Iterable<HistoryGrade> historyGrades = historyGradesRepository.findHistoryGradeByStudentId(id);

        List<Grade> mathGradeList = new ArrayList<>();
        mathGrades.forEach(mathGradeList::add);

        List<Grade> scienceGradeList = new ArrayList<>();
        scienceGrades.forEach(scienceGradeList::add);

        List<Grade> historyGradeList = new ArrayList<>();
        historyGrades.forEach(historyGradeList::add);

        studentGrades.setMathGradeResults(mathGradeList);
        studentGrades.setScienceGradeResults(scienceGradeList);
        studentGrades.setHistoryGradeResults(historyGradeList);

        return GradebookCollegeStudent.builder()
                .id(id)
                .studentGrades(studentGrades)
                .firstname(student.getFirstname())
                .lastname(student.getLastname())
                .emailAddress(student.getEmailAddress())
                .build();
    }
}
