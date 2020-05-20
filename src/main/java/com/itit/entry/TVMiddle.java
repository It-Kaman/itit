package com.itit.entry;

import java.util.List;

public class TVMiddle {
    private Integer id;
    private Video video;
    private List<Tag> tag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public List<Tag> getTag() {
        return tag;
    }

    public void setTag(List<Tag> tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "TVMiddle{" +
                "id=" + id +
                ", video=" + video +
                ", tag=" + tag +
                '}';
    }
}
