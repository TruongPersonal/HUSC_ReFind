package data.dao;

import model.Item;
import java.util.List;

public interface ItemDao {
    List<Item> searchAndFilterItems(String type, int categoryId, int locationId, String keyword);

    Item getItemById(int id);

    List<Item> getSmartMatchingItems(int categoryId, int locationId, int currentItemId);

    boolean insertItem(String title, int categoryId, int locationId, String description, String image, int status, String adminNote, Integer userId);

    boolean updateItem(int id, String title, int categoryId, int locationId, String description, String image, int userId);
    boolean updateItemByAdmin(int id, String title, int categoryId, int locationId, String description, String image, String adminNote);

    boolean deleteItem(int id);

    boolean updateStatusAndNote(int id, int status, String adminNote);

    boolean closeItem(int id);

    List<Item> getItemsByUserId(int userId, Integer statusFilter);

    List<Item> getAllItems(String typeFilter, Integer statusFilter);
    List<Item> getAllItems(String typeFilter, Integer statusFilter, String roleFilter, String keyword);

    int countTotalItems();
    int countItemsByStatus(int status);
}
