package ru.alexandr.BookingCinemaTickets.controller.web.enums;

public enum ToastType {
    ERROR("red"),
    SUCCESS("green"),
    WARNING("yellow");

    private final String color;

    ToastType(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public static ToastType valueOfByColor(String color) {
        for (ToastType type : ToastType.values()) {
            if (type.getColor().equals(color)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Неизвестный цвет: " + color);
    }
}
