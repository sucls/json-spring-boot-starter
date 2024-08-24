package cn.cycad.jackson.sample.entity;

import cn.cycad.jackson.sample.convert.UserConvert;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author sucl
 * @date 2024/5/8 21:34
 * @since 1.0.0
 */
@Data
public class Order implements UserConvert {

    private String id;

    private String name;

    private LocalDateTime createTime = LocalDateTime.now();

    /**
     * 创建用户
     */
    private String creator;

    @Override
    public String userId() {
        return creator;
    }
}
