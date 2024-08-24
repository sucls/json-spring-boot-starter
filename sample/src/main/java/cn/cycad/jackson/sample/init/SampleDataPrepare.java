package cn.cycad.jackson.sample.init;

import cn.cycad.jackson.sample.convert.UserConvert;
import cn.cycad.jackson.sample.entity.User;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

/**
 * @author sucl
 * @date 2024/5/8 21:48
 * @since 1.0.0
 */
@Component
public class SampleDataPrepare implements InitializingBean {

    @Resource
    private CacheManager cacheManager;

    public void init(){
        Cache cache = cacheManager.getCache(UserConvert.USER_CACHE);
        if( cache != null ){
            cache.put("u1", new User("u1","Tom"));
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }
}
