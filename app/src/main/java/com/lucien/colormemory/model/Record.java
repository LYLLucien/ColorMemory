package com.lucien.colormemory.model;

/**
 * Created by Lucien on 16/7/2016.
 */
public class Record {

    private String username;
    private int score;
    private long createTime;

    public Record(String username, int score, long createTime) {
        this.username = username;
        this.score = score;
        this.createTime = createTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
