
package com.loop.lifestage.dto;

import java.util.List;

public class UserDTO {
    private String id;
    private String username;
    private List<Long> lifeEventIds;

    public UserDTO() {
    }

    public UserDTO(String id, String username) {
        this.id = id;
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Long> getLifeEventIds() {
        return lifeEventIds;
    }

    public void setLifeEventIds(List<Long> lifeEventIds) {
        this.lifeEventIds = lifeEventIds;
    }

    public void addLifeEvent(Long lifeEventId) {
        this.lifeEventIds.add(lifeEventId);
    }

    public void removeLifeEvent(Long lifeEventId) {
        this.lifeEventIds.remove(lifeEventId);
    }
}