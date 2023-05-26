package com.Bootcamp_Project.ECommerce.entities.order;

import com.Bootcamp_Project.ECommerce.entities.order.OrderProduct;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
//@PrimaryKeyJoinColumn(name = "order_Product_Id")
public class OrderStatus  {
    @Id
    @OneToOne
    @JoinColumn(name = "orderProductId")
    private OrderProduct orderProduct;
    private String fromStatus;
    private String toStatus;
    private String transitionNotesComment;
    private String transitionId;
}
