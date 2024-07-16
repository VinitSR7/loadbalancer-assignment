package com.example.loadbalancer.model;
public class Server {
    private String url;

    public Server() {
    }

    public Server(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Server that = (Server) o;
        return url.equals(that.url);
    }

    @Override
    public int hashCode() {
        return url.hashCode();
    }

    @Override
    public String toString() {
        return "BackendServer{" +
                "url='" + url + '\'' +
                '}';
    }
}
