package com.example.rantissie_commerce;

import java.io.Serializable;

public class TrashItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private String trashId;
    private String originalNode;
    private Object itemData;
    private long deletedAt;

    public TrashItem() {
    }

    public TrashItem(String trashId, String originalNode, Object itemData, long deletedAt) {
        this.trashId = trashId;
        this.originalNode = originalNode;
        this.itemData = itemData;
        this.deletedAt = deletedAt;
    }

    public String getTrashId() { return trashId; }
    public void setTrashId(String trashId) { this.trashId = trashId; }

    public String getOriginalNode() { return originalNode; }
    public void setOriginalNode(String originalNode) { this.originalNode = originalNode; }

    public Object getItemData() { return itemData; }
    public void setItemData(Object itemData) { this.itemData = itemData; }

    public long getDeletedAt() { return deletedAt; }
    public void setDeletedAt(long deletedAt) { this.deletedAt = deletedAt; }
}