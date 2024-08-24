package cn.cycad.jackson.serializer;

import lombok.Getter;
import lombok.Setter;

/**
 * @author sucl
 * @date 2024/5/8 21:31
 * @since 1.0.0
 */
@Getter
@Setter
public class ConvertItem {

    private String id;

    private String text;

    private String name;

    public ConvertItem() {

    }

    public ConvertItem(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
