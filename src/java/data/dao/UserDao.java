package data.dao;

import model.User;
import java.util.List;

public interface UserDao {
    User findUserByCode(String code);

    User findUserById(int id);

    User findUserByEmail(String email);

    User findUserByPhone(String phone);

    boolean checkCodeExists(String code);

    boolean checkEmailExists(String email);

    boolean checkPhoneExists(String phone);

    boolean insertUser(String code, String name, String email, String phone, String hashedPassword, String role);

    boolean updateProfile(int userId, String name, String phone, String email);

    boolean updatePassword(int userId, String newHashedPassword);

    List<User> getAllUsers();

    List<User> searchUsers(String keyword, String roleFilter);

    boolean updateUserByAdmin(int userId, String code, String name, String email, String phone, String role, String newHashedPassword);

    boolean deleteUser(int userId);

    boolean updateUserStatus(int userId, int status);

    int countUsersByRole(String role);
}
