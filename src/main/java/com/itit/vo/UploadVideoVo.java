package com.itit.vo;

import com.itit.entry.Video;

import java.util.List;

public class UploadVideoVo extends Video {
    private String guid;
    private String fileName;
    private List<String> column;

    public void setColumn(List<String> column) {
        this.column = column;
    }

    public List<String> getColumn() {
        return column;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "UploadVideoVo{" +
                "guid='" + guid + '\'' +
                ", fileName='" + fileName + '\'' +
                ", column=" + column +
                "} Video{" + super.toString() +
                "}";
    }
}
