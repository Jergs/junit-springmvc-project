package com.luv2code.springmvc.repository;

import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.models.MathGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MathGradesRepository extends JpaRepository<MathGrade, Integer> {

    Iterable<MathGrade> findMathGradeByStudentId(Integer id);
}
