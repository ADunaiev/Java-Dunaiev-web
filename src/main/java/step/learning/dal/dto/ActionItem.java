package step.learning.dal.dto;

import java.util.UUID;

public class ActionItem {
    private UUID id;
    private UUID productId;
    private int rebate;

    public ActionItem() {
    }

    public ActionItem(UUID id, UUID productId, int rebate) {
        this.id = id;
        this.productId = productId;
        this.rebate = rebate;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public int getRebate() {
        return rebate;
    }

    public void setRebate(int rebate) {
        this.rebate = rebate;
    }
}
