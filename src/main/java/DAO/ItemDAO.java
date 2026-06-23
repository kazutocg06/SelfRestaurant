package DAO;

import Model.Item;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import ConnectDatabase.*;

public class ItemDAO {

    public List<Item> getAllItems() {
        List<Item> list = new ArrayList<>();
        String sql = "SELECT * FROM Item";
        
        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
             
            while (rs.next()) {
                Item item = new Item();
                item.setItemId(rs.getString("item_id"));
                item.setMenuId(rs.getString("menu_id"));
                item.setName(rs.getString("name"));
                item.setPrice(rs.getDouble("price"));
                item.setImgUrl(rs.getString("img_url"));
                item.setDescription(rs.getString("description"));
                item.setIsAvailable(rs.getInt("is_available"));
                
                list.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Model.Item> getItemsByCategory(String menuId) {
        List<Model.Item> list = new ArrayList<>();
        String sql = "SELECT * FROM Item WHERE menu_id = ?"; 
        
        try (Connection con = ConnectDatabase.DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, menuId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Model.Item item = new Model.Item();
                item.setItemId(rs.getString("item_id"));
                item.setMenuId(rs.getString("menu_id"));
                item.setName(rs.getString("name"));
                item.setPrice(rs.getDouble("price"));
                item.setImgUrl(rs.getString("img_url"));
                item.setDescription(rs.getString("description"));
                item.setIsAvailable(rs.getInt("is_available"));
                list.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public List<Model.Item> searchItems(String keyword, String menuId) {
        List<Model.Item> list = new ArrayList<>();
        
        String sql = "SELECT * FROM Item WHERE name LIKE ?";
        
        if (!"ALL".equals(menuId)) {
            sql += " AND menu_id = ?";
        }
        
        try (Connection con = ConnectDatabase.DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setNString(1, "%" + keyword + "%"); 
            
            if (!"ALL".equals(menuId)) {
                ps.setString(2, menuId); 
            }
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Model.Item item = new Model.Item();
                item.setItemId(rs.getString("item_id"));
                item.setMenuId(rs.getString("menu_id"));
                item.setName(rs.getString("name"));
                item.setPrice(rs.getDouble("price"));
                item.setImgUrl(rs.getString("img_url"));
                item.setDescription(rs.getString("description"));
                list.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}