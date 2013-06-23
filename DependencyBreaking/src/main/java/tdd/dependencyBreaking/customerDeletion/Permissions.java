package tdd.dependencyBreaking.customerDeletion;

import java.util.ArrayList;
import java.util.List;

public class Permissions{
    
    private static ThreadLocal<List<String>> permissions = new ThreadLocal<List<String>>(){
        protected List<String> initialValue() {
            return new ArrayList<String>();
        }
    };

    public static boolean hasPermission(String permission) {
        return permissions.get().contains(permission);
    }
    
}