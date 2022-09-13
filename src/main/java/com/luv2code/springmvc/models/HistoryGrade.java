package com.luv2code.springmvc.models;

import javax.persistence.*;

@Entity
@Table(name = "history_grade")
public class HistoryGrade implements Grade {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    @Column(name="grade")
    private double grade;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private CollegeStudent student;

    public HistoryGrade() {
    }

    @Override
    public CollegeStudent getStudent() {
        return student;
    }

    public void setStudent(CollegeStudent student) {
        this.student = student;
    }

    public HistoryGrade(double grade) {
        this.grade = grade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }
}
