package com.example.eleven;

public class Course {

    private String courseId;
    private String courseName;
    private String courseDuration;

    public Course() {
        // Default constructor required for calls to DataSnapshot.getValue(Course.class)
    }

    public Course(String courseId, String courseName, String courseDuration) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseDuration = courseDuration;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseDuration() {
        return courseDuration;
    }
}

