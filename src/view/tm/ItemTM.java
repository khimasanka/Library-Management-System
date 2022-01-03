package view.tm;

public class ItemTM {
    private String id;
    private String itemName;
    private String itemType;
    private int qty;

    public ItemTM() {
    }

    public ItemTM(String id, String itemName, String itemType, int qty) {
        this.id = id;
        this.itemName = itemName;
        this.itemType = itemType;
        this.qty = qty;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
