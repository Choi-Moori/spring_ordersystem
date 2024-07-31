package beyond.ordersystem.ordering.service;

import beyond.ordersystem.common.configs.RabbitMqConfig;
import beyond.ordersystem.ordering.dto.StockDecreaseEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

// Component 에서 트렌젝셔널 처리를 할 수 있따.
// (Service, Controller 드읃ㅇ)
@Component
public class StockDecreaseEventHandler {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void publish(StockDecreaseEvent event){
        rabbitTemplate.convertAndSend(RabbitMqConfig.STOCK_DECREASE_QUEUE, event);
    }
    //ORderingService 와 별도의 트렌잭션으로 돌아간다.
//    @Transactional
//    @RabbitListener(queues = RabbitMqConfig.STOCK_DECREASE_QUEUE)
//    public void listen(){
//
//    }
}
