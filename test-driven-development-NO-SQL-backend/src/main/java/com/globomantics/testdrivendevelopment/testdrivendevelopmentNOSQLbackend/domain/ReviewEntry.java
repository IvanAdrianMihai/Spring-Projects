package com.globomantics.testdrivendevelopment.testdrivendevelopmentNOSQLbackend.domain;

import java.util.Date;
import java.util.Objects;

public class ReviewEntry {
    private String username;
    private Date date;
    private String review;

    public ReviewEntry() {
    }

    public ReviewEntry(String username, Date date, String review) {
        this.username = username;
        this.date = date;
        this.review = review;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReviewEntry that = (ReviewEntry) o;
        return Objects.equals(username, that.username) && Objects.equals(date, that.date) && Objects.equals(review, that.review);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, date, review);
    }

    @Override
    public String toString() {
        return "ReviewEntry{" +
                "username='" + username + '\'' +
                ", date=" + date +
                ", review='" + review + '\'' +
                '}';
    }
}