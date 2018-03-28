package com.chat.vo;

/**
 *
 * Created by lw on 16-12-21.
 */
public class MessageVo {
    private String content;

    private boolean self;

    private String date;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isSelf() {
        return self;
    }

    public void setSelf(boolean self) {
        this.self = self;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
