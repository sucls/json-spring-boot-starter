package cn.cycad.jackson.sample.convert;

import cn.cycad.jackson.serializer.ConvertItem;

/**
 * @author sucl
 * @date 2024/5/8 21:35
 * @since 1.0.0
 */
public interface UserConvert {

    String USER_CACHE = "USER_CACHE";

    String userId();

    default ConvertItem getUserConvert(){
        if( userId() == null ){
            return null;
        }
        return new ConvertItem(userId(), USER_CACHE);
    }

}
