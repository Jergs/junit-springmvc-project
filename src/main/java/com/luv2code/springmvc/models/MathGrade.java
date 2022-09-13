package com.luv2code.springmvc.models;

import javax.persistence.*;

@Entity
@Table(name = "math_grade")
public class MathGrade implements Grade {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    @Column(name="grade")
    private double grade;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private CollegeStudent student;

    public MathGrade() {

    }

    public MathGrade(double grade) {
        this.grade = grade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public CollegeStudent getStudent() {
        return student;
    }

    public void setStudent(CollegeStudent student) {
        this.student = student;
    }

    @Override
    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }
}
