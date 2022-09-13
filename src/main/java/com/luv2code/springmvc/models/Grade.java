package com.luv2code.springmvc.models;

public interface Grade {
    double getGrade();

    int getId();

    void setId(int id);

    CollegeStudent getStudent();

    void setStudent(CollegeStudent student);

    void setGrade(double grade);
}
