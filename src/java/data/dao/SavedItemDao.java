package data.dao;

import model.Item;
import java.util.List;

public interface SavedItemDao {
    boolean isSaved(int userId, int itemId);

    boolean toggleSaveItem(int userId, int itemId);

    List<Item> getSavedItemsByUserId(int userId);

    boolean removeSavedItem(int userId, int itemId);
}
