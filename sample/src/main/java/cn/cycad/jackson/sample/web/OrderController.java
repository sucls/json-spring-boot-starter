package cn.cycad.jackson.sample.web;

import cn.cycad.jackson.sample.entity.Order;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sucl
 * @date 2024/5/8 21:38
 * @since 1.0.0
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @GetMapping("/{id}")
    public Order getOrder(@PathVariable("id") String id){
        Order order = new Order();
        order.setId(id);
        order.setName("测试订单");
        order.setCreator("u1");
        return order;
    }

}
