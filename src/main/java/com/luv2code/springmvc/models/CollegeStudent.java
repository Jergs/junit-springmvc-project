package com.luv2code.springmvc.models;

import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "student")
@SuperBuilder
public class CollegeStudent implements Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String firstname;
    @Column
    private String lastname;
    @Column(name = "email_address")
    private String emailAddress;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "student")
    private List<MathGrade> mathGrade;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "student")
    private List<HistoryGrade> historyGrade;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "student")
    private List<ScienceGrade> scienceGrade;

    public CollegeStudent() {

    }

    public List<MathGrade> getMathGrade() {
        return mathGrade;
    }

    public void setMathGrade(List<MathGrade> mathGrade) {
        this.mathGrade = mathGrade;
    }

    public List<HistoryGrade> getHistoryGrade() {
        return historyGrade;
    }

    public void setHistoryGrade(List<HistoryGrade> historyGrade) {
        this.historyGrade = historyGrade;
    }

    public List<ScienceGrade> getScienceGrade() {
        return scienceGrade;
    }

    public void setScienceGrade(List<ScienceGrade> scienceGrade) {
        this.scienceGrade = scienceGrade;
    }

    public CollegeStudent(String firstname, String lastname, String emailAddress) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.emailAddress = emailAddress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }


    public String getFullName() {
        return getFirstname() + " " + getLastname();
    }

    @Override
    public String toString() {
        return "CollegeStudent{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                '}';
    }

    public String studentInformation() {
        return getFullName() + " " + getEmailAddress();
    }

    public void addMathGrade(MathGrade mathGrade) {
        mathGrade.setStudent(this);
        this.mathGrade.add(mathGrade);
    }

    public void addScienceGrade(ScienceGrade scienceGrade) {
        scienceGrade.setStudent(this);
        this.scienceGrade.add(scienceGrade);
    }

    public void addHistoryGrade(HistoryGrade historyGrade) {
        historyGrade.setStudent(this);
        this.historyGrade.add(historyGrade);
    }
}
