package cn.cycad.jackson.support;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

/**
 * @author sucl
 * @date 2024/4/26 17:51
 * @since 1.0.0
 */
public abstract class CacheItemConvertAdapter<T> implements ItemConvertAdapter, InitializingBean {

    private String cacheName;

    private Class<T> target;

    @Resource
    private CacheManager cacheManager;

    private Cache cache;

    public CacheItemConvertAdapter(String cacheName, Class<T> target) {
        this.cacheName = cacheName;
        this.target = target;
    }

    public T fromCache(String id){
        if( cache != null ){
            Cache.ValueWrapper valueWrapper = cache.get(id);
            if(valueWrapper != null ){
                Object value = valueWrapper.get();
                if( value != null && target.isAssignableFrom(value.getClass()) ){
                    return target.cast(value);
                }
            }
        }
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if( cacheManager != null ){
            cache = cacheManager.getCache(cacheName);
        }
    }
}
