package cn.cycad.jackson.support;

import cn.cycad.jackson.serializer.ConvertItem;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.OrderComparator;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author sucl
 * @date 2024/4/26 17:11
 * @since 1.0.0
 */
public class ItemConvertSerializer extends StdSerializer<ConvertItem> implements ApplicationContextAware {

    private List<ItemConvertAdapter> itemConvertAdapters;

    public ItemConvertSerializer(Class<ConvertItem> t) {
        super(t);
    }

    @Override
    public void serialize(ConvertItem value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        String text = "";
        if(!CollectionUtils.isEmpty(itemConvertAdapters)){
            for (ItemConvertAdapter itemConvertAdapter : itemConvertAdapters) {
                if( itemConvertAdapter.support(value) ){
                    text = itemConvertAdapter.convert(value);
                    break;
                }
            }
        }
        gen.writeString(text);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, ItemConvertAdapter> itemConvertAdapterMap
                = BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, ItemConvertAdapter.class, true, false);
        if( !itemConvertAdapterMap.isEmpty() ){
            itemConvertAdapters = new ArrayList<>(itemConvertAdapterMap.values());
            itemConvertAdapters.sort(OrderComparator.INSTANCE);
        }
    }
}
