package lk.ijse.dep.web.model;

public class Customer {
    private String id;
    private String name;
    private String address;

    @Override
    public String toString() {
        return "Customer{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", address='" + getAddress() + '\'' +
                '}';
    }

    public Customer() {
    }

    public Customer(String id, String name, String address) {
        this.setId(id);
        this.setName(name);
        this.setAddress(address);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
