package ru.top.kitchenmanager.dto;


import ru.top.kitchenmanager.model.UserRole;

public class UserDto {
    private Long id;
    private String username;
    private String password;
    private String fullName;
    private String phone;
    private UserRole role;

    // Конструкторы
    public UserDto() {}

    public UserDto(Long id, String username, String fullName, String phone, UserRole role) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.phone = phone;
        this.role = role;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }
}