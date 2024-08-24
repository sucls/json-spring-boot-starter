package cn.cycad.jackson.sample.entity;

import lombok.Data;

/**
 * @author sucl
 * @date 2024/5/8 21:47
 * @since 1.0.0
 */
@Data
public class User {

    private String id;

    private String caption;

    public User() {
    }

    public User(String id, String caption) {
        this.id = id;
        this.caption = caption;
    }
}
