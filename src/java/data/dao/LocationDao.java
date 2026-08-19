package data.dao;

import model.Location;
import java.util.List;

public interface LocationDao {
    List<Location> allLocations();
    Location findById(int id);
    boolean insertLocation(String name);
    boolean updateLocation(int id, String name);
    boolean deleteLocation(int id);
}
