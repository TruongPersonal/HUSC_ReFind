package data.dao;

import model.Category;
import java.util.List;

public interface CategoryDao {
    List<Category> allCategories();
    Category findById(int id);
    boolean insertCategory(String name);
    boolean updateCategory(int id, String name);
    boolean deleteCategory(int id);
}
