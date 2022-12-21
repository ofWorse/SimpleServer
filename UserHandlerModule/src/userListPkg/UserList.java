package userListPkg;

import java.util.ArrayList;
import java.util.List;

public class UserList {
    public static List<String> users = new ArrayList<>();
    public static int index;
    public static boolean addUser(String name) {
        if (isUnique(name)){
            users.add(name);
            return true;
        } else return false;
    }

    public static List<String> getUsers() {
        return users;
    }

    public static boolean isUnique(String name) {
        return !users.contains(name);
    }
}
