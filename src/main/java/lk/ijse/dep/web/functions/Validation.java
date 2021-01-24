package lk.ijse.dep.web.functions;

import java.math.BigDecimal;

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

    public static boolean validateItem(String code, String description, int qtyOnHand, double unitPrice) {
        if (code.trim().isEmpty() || !code.matches("I\\d{3}")) {
            return false;
        } else if (description.trim().isEmpty()) {
            return false;
        } else if (qtyOnHand<0) {
            return false;
        } else if (unitPrice<0) {
            return false;
        } else {
            return true;
        }
    }

}
