package Model;

public class Item {
    private String itemId;
    private String menuId;
    private String name;
    private double price;
    private String imgUrl;
    private String description;
    private int isAvailable;

    public Item() {}

    public Item(String itemId, String menuId, String name, double price, String imgUrl, String description, int isAvailable) {
        this.itemId = itemId;
        this.menuId = menuId;
        this.name = name;
        this.price = price;
        this.imgUrl = imgUrl;
        this.description = description;
        this.isAvailable = isAvailable;
    }

    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }

    public String getMenuId() { return menuId; }
    public void setMenuId(String menuId) { this.menuId = menuId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getImgUrl() { return imgUrl; }
    public void setImgUrl(String imgUrl) { this.imgUrl = imgUrl; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getIsAvailable() { return isAvailable; }
    public void setIsAvailable(int isAvailable) { this.isAvailable = isAvailable; }
}