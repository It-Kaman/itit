package com.itit.entry;

public class ItitFile {
    private Integer id;
    private String name;
    private String object_id;
    private byte status; //0 视频 1 专栏
    private User user;
    private String url;
    private String size;

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getObject_id() {
        return object_id;
    }

    public void setObject_id(String object_id) {
        this.object_id = object_id;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "ItitFile{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", object_id='" + object_id + '\'' +
                ", status=" + status +
                ", user=" + user +
                ", url='" + url + '\'' +
                ", size='" + size + '\'' +
                '}';
    }
}
