## json-spring-boot-starter

实现web查询序列化后，实体数据转换json后其中关联数据的处理，比如订单的创建人用户，在订单数据存储时，一般存储的都是创建人id，但是在页面中需要显示的是用户中文名称


### 使用方式

1. *定义实体转换接口*

由于需要对订单实体中的创建人id进行转换
```java
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
```

2. *定义接口转换适配器*

基于上面*UserConvert*的处理，基于缓存实现，同时支持一个实体中多个，比如商品名称、商品分类等
```java
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
```

3. 需要转换的数据缓存

该实现依赖缓存，需要优先对需要转换的数据进行缓存，因此示例中添加了缓存示例
```java
    public void init(){
    Cache cache = cacheManager.getCache(UserConvert.USER_CACHE);
    if( cache != null ){
        cache.put("u1", new User("u1","Tom"));
    }
}
```

4. *实体定义*

实体中需要通过实现接口UserConvert，这样对多个数据项转换时可以继续扩展
```java
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
```

5. *实现效果*

可以看到，在输出json中，多了一列*userConvert*，也就是接口中定义的get*方法
```json
{
    "id": "1",
    "name": "测试订单",
    "createTime": "2024-05-08T21:55:51.5747507",
    "creator": "u1",
    "userConvert": "Tom"
}
```

### 实现原理

上面说的，主要实现基于缓存，在web查询结果进行json序列化时，依赖于jackson的扩展，对输出结果匹配的类型进行转换。

```java
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
```

1. 在配置文件中基于*Jackson2ObjectMapperBuilderCustomizer*对jackson进行扩展
2. 定义*ItemConvertSerializer*对*ConvertItem*类型的属性进行处理，该类主要继承于*StdSerializer*
3. 在*ItemConvertSerializer*中基于*ConvertItem*的name属性来匹配对应的缓存并进行转换
4. 注意开启spring缓存*@EnableCaching*
5. 最后基于spring特性，定义*/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports*来实现自动注入配置

---
+ *ConvertItem*示例
```java
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
```

+ *ItemConvertAdapter*扩展适配器，主要于*ConvertItem*搭配扩展
```java
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
```

+ *ItemConvertSerializer*示例
```java
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
```

### 优缺点

+ 使用了jackson序列化的扩展，如果使用其他序列化工具，需要支持
+ 依赖于数据缓存，一般针对通用数据才有数据转换的需要，比如用户、部门数据等，一般这些数据更适合缓存

### 扩展

