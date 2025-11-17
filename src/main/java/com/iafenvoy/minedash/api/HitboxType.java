package com.iafenvoy.minedash.api;

public enum HitboxType {
    PLAYER(0xFFFFFF00),
    BLOCK(0xFF0000FF),
    CRITICAL(0xFFFF0000),
    INTERACTABLE(0xFF00FF00);

    private final int color;

    HitboxType(int color) {
        this.color = color;
    }

    public int getColor() {
        return this.color;
    }
}
