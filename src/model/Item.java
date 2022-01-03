package model;

public class Item {
    private String itemId;
    private String itemName;
    private String itemType;
    private int qty;

    public Item() {
    }

    public Item(String itemId, String itemName, String itemType, int qty) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemType = itemType;
        this.qty = qty;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
