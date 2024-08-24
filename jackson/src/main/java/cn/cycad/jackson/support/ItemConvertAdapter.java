package cn.cycad.jackson.support;

import cn.cycad.jackson.serializer.ConvertItem;

/**
 * @author sucl
 * @date 2024/4/26 17:08
 * @since 1.0.0
 */
public interface ItemConvertAdapter {

    /**
     * @param convertItem
     * @return
     */
    boolean support(ConvertItem convertItem);

    /**
     *
     * @param convertItem
     * @return
     */
    String convert(ConvertItem convertItem);

}
