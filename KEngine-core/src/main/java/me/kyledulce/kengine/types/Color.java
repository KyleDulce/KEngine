package me.kyledulce.kengine.types;

public record Color(
    int red,
    int green,
    int blue,
    int alpha
) {

    public Color(int red, int green, int blue) {
        this(red, green, blue, 255);
    }

    public Color(int rgba) {
        this(
                (rgba >> 16) & 0xFF,
                (rgba >> 8) & 0xFF,
                rgba & 0xFF,
                (rgba >> 24) & 0xFF
        );
    }

    public Color normalize() {
        return new Color(
            Math.clamp(red, 0, 255),
            Math.clamp(green, 0, 255),
            Math.clamp(blue, 0, 255),
            Math.clamp(alpha, 0, 255)
        );
    }

    public Color darken(float factor) {
        return new Color(
                Math.round(red * (1 - factor)),
                Math.round(green * (1 - factor)),
                Math.round(blue * (1 - factor)),
                alpha
        ).normalize();
    }

    public Color lighten(float factor) {
        return new Color(
                Math.round(red + ((255 - red) * factor)),
                Math.round(green + ((255 - green) * factor)),
                Math.round(blue + ((255 - blue) * factor)),
                alpha
        ).normalize();
    }

    public String toString() {
        return String.format("r:%s g:%s b:%s a:%s", red, green, blue, alpha);
    }
}
