package com.luv2code.springmvc.repository;

import com.luv2code.springmvc.models.ScienceGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScienceGradesRepository extends JpaRepository<ScienceGrade, Integer> {

    Iterable<ScienceGrade> findScienceGradeByStudentId(Integer id);
}
