package beyond.ordersystem.ordering.service;

import beyond.ordersystem.member.domain.Member;
import beyond.ordersystem.member.repository.MemberRepository;
import beyond.ordersystem.ordering.domain.OrderDetail;
import beyond.ordersystem.ordering.domain.Ordering;
import beyond.ordersystem.ordering.dto.OrderCreateDto;
import beyond.ordersystem.ordering.repository.OrderingRepository;
import beyond.ordersystem.product.domain.Product;
import beyond.ordersystem.product.repository.ProductRepository;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderingService {

    private final OrderingRepository orderingRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    @Autowired
    public OrderingService(OrderingRepository orderingRepository, MemberRepository memberRepository, ProductRepository productRepository) {
        this.orderingRepository = orderingRepository;
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
    }

    public Long orderCreate(OrderCreateDto dto){
        Member member = memberRepository.findById(dto.getMemberId()).orElseThrow(()->new EntityNotFoundException("해당 멤버 ID가 존재하지 않습니다."));
        Ordering ordering = Ordering.builder()
                .member(member)
                .build();

        List<OrderDetail> orderDetailList = new ArrayList<>();
        for(OrderCreateDto.OrderInfoDto dtos : dto.getOrderInfoDto()){
            Product product = productRepository.findById(dtos.getProductId()).orElseThrow(()->new EntityNotFoundException("해당 상품 ID가 존재하지 않습니다."));
            OrderDetail orderDetail = OrderDetail.builder()
                    .product(product)
                    .quantity(dtos.getProductCount())
                    .ordering(ordering)
                    .build();
            ordering.getOrderDetails().add(orderDetail);
        }
        orderingRepository.save(ordering);
        return ordering.getId();
    }
}
