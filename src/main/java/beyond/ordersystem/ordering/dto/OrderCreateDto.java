package beyond.ordersystem.ordering.dto;

import beyond.ordersystem.member.domain.Member;
import beyond.ordersystem.ordering.domain.OrderDetail;
import beyond.ordersystem.ordering.domain.OrderStatus;
import beyond.ordersystem.ordering.domain.Ordering;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embedded;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderCreateDto {
    private Long memberId;
    private List<OrderCreateDto.OrderInfoDto> orderInfoDto;

    @Data
    public static class OrderInfoDto {
        private Long productId;
        private Integer productCount;
    }

}
