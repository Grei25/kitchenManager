package ru.top.kitchenmanager.model;
public enum OrderStatus {
    NEW,           // Только создан клиентом
    CONFIRMED,     // Подтвержден администратором
    COOKING,       // Готовится на кухне
    READY,         // Приготовлен, ждет курьера
    DELIVERING,    // Курьер везет
    DELIVERED,     // Доставлен
    CANCELLED      // Отменен
}
