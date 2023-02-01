package org.prgrms.kdt.order;

import java.util.UUID;

//record형태로 만들어주면 알아서 불변객체로 만들어짐.
/*
public record OrderItem(UUID productId,
                        long productPrice,
                        long quantity){

}
*/

public class OrderItem{
    public final UUID productId;
    public final long productPrice;
    public final long quantity;

    public OrderItem(UUID productId, long productPrice, long quantity) {
        this.productId = productId;
        this.productPrice = productPrice;
        this.quantity = quantity;
    }

    public UUID getProductId() {
        return productId;
    }

    public long getProductPrice() {
        return productPrice;
    }

    public long getQuantity() {
        return quantity;
    }
}