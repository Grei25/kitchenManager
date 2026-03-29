package ru.top.kitchenmanager.dto;
public class OrderDto {
    private String clientName;
    private String clientPhone;
    private String address;
    private String comment;
    private boolean pickup;

    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }

    public String getClientPhone() { return clientPhone; }
    public void setClientPhone(String clientPhone) { this.clientPhone = clientPhone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public boolean isPickup() { return pickup; }
    public void setPickup(boolean pickup) { this.pickup = pickup; }
}