package org.kuali.student.myplan.audit.service.model;

import java.util.ArrayList;

public class Subrequirement {

    // complete, inprogress
    public String status = "complete";
    public String caption = null;
    public Count count;
    public GPA gpa;
    public Credits credits;

    public String info = null;
    public String courseFilter = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCaption() {
        return caption;
    }

    public boolean hasCaption() {
        return caption != null;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public boolean hasCount() {
        return count != null;
    }

    public Count getCount() {
        return count;
    }

    public boolean hasGPA() {
        return gpa != null;
    }

    public GPA getGpa() {
        return gpa;
    }

    public boolean hasCredits() {
        return credits != null;
    }

    public Credits getCredits() {
        return credits;
    }

    public boolean hasInfo() {
        return info != null;
    }

    public String getInfo() {
        return info;
    }

    public void setCredits(Credits credits) {
        this.credits = credits;
    }

    public void setGPA(GPA gpa) {
        this.gpa = gpa;
    }

    public ArrayList<Course> courseList = new ArrayList<Course>();

    public boolean hasCourseFilter() {
        return courseFilter != null;
    }

    public String getCourseFilter() {
        return courseFilter;
    }

    public boolean hasCourseList() {
        return courseList.size() > 0;
    }

    public ArrayList<Course> getCourseList() {
        return courseList;
    }

    public void addCourse(Course course) {
        courseList.add(course);
    }


    public void setCount(Count count) {
        this.count = count;
    }
}
