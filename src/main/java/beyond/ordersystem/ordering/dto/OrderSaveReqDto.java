package beyond.ordersystem.ordering.dto;

import beyond.ordersystem.member.domain.Member;
import beyond.ordersystem.ordering.domain.OrderStatus;
import beyond.ordersystem.ordering.domain.Ordering;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderSaveReqDto {
    private Long memberId;
    private List<OrderSaveReqDto.OrderInfoDto> orderInfoDto;

    @Data
    public static class OrderInfoDto {
        private Long productId;
        private Integer productCount;
    }

    public Ordering toEntity(Member member){
        return Ordering.builder()
                .member(member)
                .orderStatus(OrderStatus.ORDERED)
                .build();
    }
}
