package step.learning.dal.dao;

import step.learning.dal.dto.ActionItem;

import java.util.UUID;

public class ActionDao {
    public ActionItem[] getAction() {
        return new ActionItem[] {
            new ActionItem(UUID.randomUUID(), UUID.randomUUID(), 20),
            new ActionItem(UUID.randomUUID(), UUID.randomUUID(), 15),
            new ActionItem(UUID.randomUUID(), UUID.randomUUID(), 30),
            new ActionItem(UUID.randomUUID(), UUID.randomUUID(), 5),
            new ActionItem(UUID.randomUUID(), UUID.randomUUID(), 10),
            new ActionItem(UUID.randomUUID(), UUID.randomUUID(), 50),
        };
    }
}
