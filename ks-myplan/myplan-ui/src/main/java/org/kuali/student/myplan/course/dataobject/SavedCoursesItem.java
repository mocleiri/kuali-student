package org.kuali.student.myplan.course.dataobject;

import org.aspectj.weaver.SimpleAnnotationValue;

import java.util.Date;

public class SavedCoursesItem implements Comparable {

    private String id;
    private CourseDetails courseDetails;
    private Date dateAdded;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CourseDetails getCourseDetails() {
        return courseDetails;
    }

    public void setCourseDetails(CourseDetails courseDetails) {
        this.courseDetails = courseDetails;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    @Override
    public int compareTo( Object object ) {
        SavedCoursesItem that = (SavedCoursesItem) object;
        return this.getDateAdded().compareTo( that.getDateAdded() );
    }
}
