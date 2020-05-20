package com.itit.entry.admin;

public class Swiper {
    private Integer id;
    private String title;
    private String header;
    private String url;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Swiper{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", header='" + header + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
