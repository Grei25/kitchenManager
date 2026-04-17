package ru.top.kitchenmanager.model;
public enum OrderStatus {
    NEW,           // Только создан клиентом
    ACCEPTED,      // Принят админом (для самовывоза)
    COOKING,       // Готовится на кухне
    READY,         // Приготовлен, готов к выдаче
    COMPLETED,     // Выдан клиенту (закрыт)
    CONFIRMED,     // Подтвержден администратором (для доставки)
    DELIVERING,    // Курьер везет
    DELIVERED,     // Доставлен
    CANCELLED      // Отменен
}
