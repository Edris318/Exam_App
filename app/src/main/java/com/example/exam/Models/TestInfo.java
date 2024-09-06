package com.example.exam.Models;

public class TestInfo {
    private String TEST_ID;
    private long TEST_TIME;

    public TestInfo(String TEST_ID, long TEST_TIME) {
        this.TEST_ID = TEST_ID;
        this.TEST_TIME = TEST_TIME;
    }

    public long getTEST_TIME() {
        return TEST_TIME;
    }

    public void setTEST_TIME(long TEST_TIME) {
        this.TEST_TIME = TEST_TIME;
    }

    public String getTEST_ID() {
        return TEST_ID;
    }

    public void setTEST_ID(String TEST_ID) {
        this.TEST_ID = TEST_ID;
    }


}