package com.example.presence.model;

public class AppStatus {
    private boolean active;
    private String user;
    private String since;

    public AppStatus() {}

    public AppStatus(boolean active, String user, String since) {
        this.active = active;
        this.user = user;
        this.since = since;
    }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public String getUser() { return user; }
    public void setUser(String user) { this.user = user; }

    public String getSince() { return since; }
    public void setSince(String since) { this.since = since; }
}
