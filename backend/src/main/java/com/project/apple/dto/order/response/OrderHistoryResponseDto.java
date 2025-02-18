package com.project.apple.dto.order.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.project.apple.domain.Member;
import com.project.apple.domain.Order;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedModel;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderHistoryResponseDto {

    private String memberName;

    private String address;

    // 오더 생성 날짜
    @JsonSerialize(using = LocalDateTimeSerializer.class) // 직렬화 시 필요
    @JsonDeserialize(using = LocalDateTimeDeserializer.class) // 역직렬화 시 필요
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime orderDate;

    private List<OrderProductResponseDto> orderProducts;

    private int totalquantity;

    private int totalprice;


    public static OrderHistoryResponseDto fromOrder(Order order/*, Member member*/) {

        List<OrderProductResponseDto> orderProductDtos = order.getOrderProducts()
                .stream()
                .map(OrderProductResponseDto::fromEntity)
                .toList();

        return OrderHistoryResponseDto.builder()
                //.memberName(member.getNickname())
                //.address(member.getAddress())
                .orderDate(order.getCreateAt())
                .orderProducts(orderProductDtos)
                .totalquantity(order.getTotalqnt())
                .totalprice(order.getTotalPrice())
                .build();
    }
}
