package lk.ijse.dep.web.functions;

public class Validation {
    public static boolean validateCustomer(String id, String name, String address) {
        if (id.trim().isEmpty() || !id.matches("C\\d{3}")) {
            return false;
        } else if (name.trim().isEmpty()) {
            return false;
        } else if (address.trim().isEmpty()) {
            return false;
        } else {
            return true;
        }
    }


}
