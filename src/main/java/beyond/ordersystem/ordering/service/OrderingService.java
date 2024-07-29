package beyond.ordersystem.ordering.service;

import beyond.ordersystem.member.domain.Member;
import beyond.ordersystem.member.repository.MemberRepository;
import beyond.ordersystem.ordering.domain.OrderDetail;
import beyond.ordersystem.ordering.domain.OrderStatus;
import beyond.ordersystem.ordering.domain.Ordering;
import beyond.ordersystem.ordering.dto.OrderSaveReqDto;
import beyond.ordersystem.ordering.dto.OrderListResDto;
import beyond.ordersystem.ordering.repository.OrderDetailRepository;
import beyond.ordersystem.ordering.repository.OrderingRepository;
import beyond.ordersystem.product.domain.Product;
import beyond.ordersystem.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class OrderingService {

    private final OrderingRepository orderingRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final OrderDetailRepository orderDetailRepository;

    @Autowired
    public OrderingService(OrderingRepository orderingRepository, MemberRepository memberRepository, ProductRepository productRepository, OrderDetailRepository orderDetailRepository) {
        this.orderingRepository = orderingRepository;
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
        this.orderDetailRepository = orderDetailRepository;
    }

//    public Long orderCreate(OrderCreateDto dto){
//        Member member = memberRepository.findById(dto.getMemberId()).orElseThrow(()->new EntityNotFoundException("해당 멤버 ID가 존재하지 않습니다."));
//        Ordering ordering = Ordering.builder()
//                .member(member)
//                .build();
//
//        List<OrderDetail> orderDetailList = new ArrayList<>();
//        for(OrderCreateDto.OrderInfoDto dtos : dto.getOrderInfoDto()){
//            Product product = productRepository.findById(dtos.getProductId()).orElseThrow(()->new EntityNotFoundException("해당 상품 ID가 존재하지 않습니다."));
//            OrderDetail orderDetail = OrderDetail.builder()
//                    .product(product)
//                    .quantity(dtos.getProductCount())
//                    .ordering(ordering)
//                    .build();
//            ordering.getOrderDetails().add(orderDetail);
//        }
//        orderingRepository.save(ordering);
//        return ordering.getId();
//    }

    public Ordering orderCreate(List<OrderSaveReqDto> dto){
//        Member member = memberRepository.findById(dto.getMemberId()).orElseThrow(()->new EntityNotFoundException("해당 ID가 존재하지 않습니다."));
//        Ordering ordering = dto.toEntity(member);
//
//        for(OrderCreateDto.OrderInfoDto dtos : dto.getOrderInfoDto()){
//            Product product = productRepository.findById(dtos.getProductId()).orElseThrow(() -> new EntityNotFoundException("해당 상품 ID가 존재하지 않습니다."));
//            OrderDetail orderDetail = OrderDetail.builder()
//                    .product(product)
//                    .ordering(ordering)
//                    .quantity(dtos.getProductCount())
//                    .build();
//            ordering.getOrderDetails().add(orderDetail);
//        }
//        return orderingRepository.save(ordering);

//        방법2. JPA에 최적화된 방식
//        Member member = memberRepository.findById(dto.getMemberId()).orElseThrow(()->new EntityNotFoundException("해당 ID가 존재하지 않습니다."));

//        아래꺼 그냥 외우면 됨. -> 이해 하구^^
//        토큰 사용할때 간결하게 사용할 수 있는
        String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByEmail(memberEmail).orElseThrow(()->new EntityNotFoundException("member is not found"));
        Ordering ordering = Ordering.builder()
                .member(member)
                .build();

        for(OrderSaveReqDto dtos : dto){
            Product product = productRepository.findById(dtos.getProductId()).orElseThrow(() -> new EntityNotFoundException("해당 상품 ID가 존재하지 않습니다."));
            int quantity = dtos.getProductCount();
            if(product.getStockQuantity() < quantity)
                throw new IllegalArgumentException(product.getName() + "상품 재고 부족");
            OrderDetail orderDetail = OrderDetail.builder()
                    .product(product)
                    .ordering(ordering)
                    .quantity(quantity)
                    .build();
            ordering.getOrderDetails().add(orderDetail);
            product.updateStockQuantity(quantity);
        }
        return orderingRepository.save(ordering);
    }

    public List<OrderListResDto> orderList(){
        List<Ordering> orderings = orderingRepository.findAll();
        List<OrderListResDto> orderListResDtos = new ArrayList<>();
        for(Ordering ordering : orderings){
            orderListResDtos.add(ordering.toEntity());
        }
        return orderListResDtos;
    }

    public List<OrderListResDto> myOrder(){
        String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new EntityNotFoundException("해당 email의 멤버가 존재하지 않습니다."));

        List<Ordering> orderings = orderingRepository.findAllByMember(member);
        List<OrderListResDto> orderListResDtos = new ArrayList<>();
        for(Ordering ordering : orderings){
            orderListResDtos.add(ordering.toEntity());
        }
        return orderListResDtos;
    }

    public OrderListResDto orderCancel(Long id){
        Ordering ordering = orderingRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("해당 ID의 예약이 존재하지 않습니다."));

        ordering.cancelOrdering(OrderStatus.CANCELED);

        return ordering.toEntity();
    }
}
