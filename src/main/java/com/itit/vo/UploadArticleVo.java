package com.itit.vo;

import com.itit.entry.Article;
import com.itit.entry.Video;

import java.util.List;

public class UploadArticleVo extends Article {
    private List<String> column;

    private String reason;

    public List<String> getColumn() {
        return column;
    }

    public void setColumn(List<String> column) {
        this.column = column;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "UploadArticleVo{" + super.toString() +
                "column=" + column +
                "reason=" + reason +
                '}';
    }
}
