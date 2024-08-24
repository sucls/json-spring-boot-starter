package cn.cycad.jackson.sample.convert;

import cn.cycad.jackson.sample.entity.User;
import cn.cycad.jackson.serializer.ConvertItem;
import cn.cycad.jackson.support.CacheItemConvertAdapter;
import org.springframework.stereotype.Component;

/**
 * @author sucl
 * @date 2024/5/8 21:43
 * @since 1.0.0
 */
@Component
public class UserConvertProvider extends CacheItemConvertAdapter {

    private static String name = UserConvert.USER_CACHE;

    public UserConvertProvider() {
        super(name, User.class);
    }

    @Override
    public boolean support(ConvertItem convertItem) {
        return convertItem != null && convertItem.getName().equals(name);
    }

    @Override
    public String convert(ConvertItem convertItem) {
        if( convertItem == null ){
            return null;
        }
        User user = (User) fromCache(convertItem.getId());
        return user != null ? user.getCaption() : null;
    }
}
