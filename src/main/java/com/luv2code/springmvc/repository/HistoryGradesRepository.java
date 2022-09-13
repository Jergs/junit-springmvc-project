package com.luv2code.springmvc.repository;

import com.luv2code.springmvc.models.HistoryGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryGradesRepository extends JpaRepository<HistoryGrade, Integer> {

    Iterable<HistoryGrade> findHistoryGradeByStudentId(Integer id);
}
