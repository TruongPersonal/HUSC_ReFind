package data.dao;

import data.impl.*;

public class Database {
    public static UserDao getUserDao() {
        return new UserImpl();
    }

    public static CategoryDao getCategoryDao() {
        return new CategoryImpl();
    }

    public static LocationDao getLocationDao() {
        return new LocationImpl();
    }

    public static ItemDao getItemDao() {
        return new ItemImpl();
    }

    public static SavedItemDao getSavedItemDao() {
        return new SavedItemImpl();
    }
}
