package com.example.exam.Models;

public class TestInfo {
    public String testId;
    public String testTime;

    public TestInfo(String testId, String testTime) {
        this.testId = testId;
        this.testTime = testTime;
    }


    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public String getTestTime() {
        return testTime;
    }

    public void setTestTime(String testTime) {
        this.testTime = testTime;
    }
}
