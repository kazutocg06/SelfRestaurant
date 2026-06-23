package Model;

public class Menu {
    private String menuId;
    private String name;

    public Menu() {}

    public Menu(String menuId, String name) {
        this.menuId = menuId;
        this.name = name;
    }

    public String getMenuId() { return menuId; }
    public void setMenuId(String menuId) { this.menuId = menuId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}