package cn.cycad.jackson.config;

import cn.cycad.jackson.serializer.ConvertItem;
import cn.cycad.jackson.support.ItemConvertSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@EnableCaching
@Configuration
public class JacksonCustomConfiguration{

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer(){
        return jacksonObjectMapperBuilder -> configureMapperBuilder(jacksonObjectMapperBuilder);
    }

    private void configureMapperBuilder(Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder) {
        jackson2ObjectMapperBuilder.serializers(convertSerializer());
    }

    @Bean
    public ItemConvertSerializer convertSerializer(){
        return new ItemConvertSerializer(ConvertItem.class);
    }
}
