package me.kyledulce.kengine.types;

import org.joml.Vector2f;

/**
 * Represents 2d positions and points
 * @param x The x component
 * @param y The y Component
 */
public record Vector2(
        float x,
        float y
) {
    private static final float FLOAT_ERROR_EQUALS_SQUARED = 1e-5f * 1e-5f;

    /**
     * Shorthand for Vector(0, -1)
     */
    public static final Vector2 DOWN = new Vector2(0, -1);
    /**
     * Shorthand for Vector(0, 1)
     */
    public static final Vector2 UP = new Vector2(0, 1);
    /**
     * Shorthand for Vector(-1, 0)
     */
    public static final Vector2 LEFT = new Vector2(-1, 0);
    /**
     * Shorthand for Vector(1, 0)
     */
    public static final Vector2 RIGHT = new Vector2(1, 0);
    /**
     * Shorthand for Vector(1, 1)
     */
    public static final Vector2 ONE = new Vector2(1, 1);
    /**
     * Shorthand for Vector(0, 0)
     */
    public static final Vector2 ZERO = new Vector2(0, 0);

    /**
     * Returns the length of this vector calculated by the square root of (x*x+y*y)
     * If you are only comparing magnitudes, then you should use {@link #sqrMagnitude()} which
     * is faster
     * @return Returns the length of this vector.
     */
    public float magnitude() {
        return Mathf.hypot(y, x);
    }

    /**
     * Returns the squared length of this vector calculated by the square root of (x*x+y*y)
     * If you are only comparing magnitudes, then squared magnitude is faster than {@link #magnitude()}
     * @return Returns the squared length of this vector.
     */
    public float sqrMagnitude() {
        return y*y+x*x;
    }

    /**
     * When normalized the vector keeps same direction but with length 1.
     * @return Vector with magnitude of 1
     */
    public Vector2 normalize() {
        float mag = magnitude();
        return new Vector2(x / mag, y / mag);
    }

    /**
     * Adds 2 vectors by adding their individual components
     * @param a the vector to add
     * @return vector that is the sum of this and a
     */
    public Vector2 add(Vector2 a) {
        return new Vector2(x + a.x, y + a.y);
    }

    /**
     * Subtract 2 vectors by subtracting their individual components
     * @param a the vector to subtract
     * @return vector that is the difference of this and a
     */
    public Vector2 subtract(Vector2 a) {
        return new Vector2(x - a.x, y - a.y);
    }

    /**
     * Multiplies a vector by a float by multiplying the individual components
     * @param d float to multiply
     * @return vector that is the product of this and d
     */
    public Vector2 multiply(float d) {
        return new Vector2(x * d, y * d);
    }

    /**
     * Divides a vector by a float by dividing the individual components
     * @param d the float to divide
     * @return vector that os the quotient of this and d
     */
    public Vector2 divide(float d) {
        return new Vector2(x / d, y / d);
    }

    /**
     * Returns the dot product of this vector and another vector
     * @param a a vector
     * @return the dot product of the 2 vector
     */
    public float dot(Vector2 a) {
        return x * a.x + y * a.y;
    }

    /**
     * Multiplies 2 vectors component wise
     * @param a the vector to multiply
     * @return the scaled resultant vector
     */
    public Vector2 scale(Vector2 a) {
        return new Vector2(x * a.x, y * a.y);
    }

    @Override
    public boolean equals(Object other) {
        if(this == other) {
            return true;
        }
        if(other == null) {
            return false;
        }
        if(!(other instanceof Vector2 otherVector)) {
            return false;
        }
        if(x == otherVector.x && y == otherVector.y) {
            return true;
        }

        // if not exactly equal, check if they are approximate
        Vector2 difference = this.subtract(otherVector);
        return difference.sqrMagnitude() < FLOAT_ERROR_EQUALS_SQUARED;
    }

    @Override
    public String toString() {
        return String.format("(%s, %s)", x, y);
    }

    /**
     * Converts this vector to {@link org.joml.Vector2f}
     * @return a {@link org.joml.Vector2f} of this vector
     */
    Vector2f toJomlVector() {
        return new Vector2f(x, y);
    }

    /**
     * Gets a = {@link org.joml.Vector2f}
     * @return a {@link org.joml.Vector2f} of this vector
     */
    static Vector2 fromJomlVector(Vector2f a) {
        return new Vector2(a.x, a.y);
    }

    /**
     * Gets the unsigned angle between 2 vectors
     * @param a a vector
     * @param b another vector
     * @return the angle in radians
     */
    public static float angle(Vector2 a, Vector2 b) {
        Vector2f aj = a.toJomlVector();
        Vector2f bj = b.toJomlVector();

        return aj.angle(bj);
    }

    /**
     * Gets the distance between 2 vectors
     * @param a a vector
     * @param b another vector
     * @return the distance between the 2 vectors
     */
    public static float distance(Vector2 a, Vector2 b) {
        Vector2f aj = a.toJomlVector();
        Vector2f bj = b.toJomlVector();
        return aj.distance(bj);
    }

    /**
     * Clamps magnitude of vector to a specified length. If the vector is less than the max length, this vector
     * is returned. Otherwise a vector in the same direction of length maxLength is returned
     * @param a a vector
     * @param maxLength the max length
     * @return the clamped vector
     */
    public static Vector2 clampMagnitude(Vector2 a, float maxLength) {
        if(a.sqrMagnitude() <= (maxLength * maxLength)) {
            return a;
        }
        float coefficient = maxLength / a.magnitude();
        return a.multiply(coefficient);
    }

    /**
     * Linearly interpolates a vector to another vector. The interpolation factor will be clamped
     * to [0,1]
     * @param a a vector
     * @param b another vector
     * @param t the interpolation factor
     * @return the interpolated vector
     */
    public static Vector2 lerp(Vector2 a, Vector2 b, float t) {
        float clampedValue = Mathf.clamp(t, 0, 1);
        return fromJomlVector(a.toJomlVector().lerp(b.toJomlVector(), clampedValue));
    }

    /**
     * Returns a vector made of the larger components of 2 provided vectors
     * @param a a vector
     * @param b another vector
     * @return The vector made of the larger components
     */
    public static Vector2 max(Vector2 a, Vector2 b) {
        return new Vector2(Math.max(a.x, b.x), Math.max(a.y, b.y));
    }

    /**
     * Returns a vector made of the smaller components of 2 provided vectors
     * @param a a vector
     * @param b another vector
     * @return The vector made of the smaller components
     */
    public static Vector2 min(Vector2 a, Vector2 b) {
        return new Vector2(Math.min(a.x, b.x), Math.min(a.y, b.y));
    }

    /**
     * Returns a perpendicular vector
     * @param a a vector
     * @return a perpendicular vector to a
     */
    public static Vector2 perpendicular(Vector2 a) {
        return Vector2.fromJomlVector(a.toJomlVector().perpendicular());
    }

    /**
     * Returns a vector that is reflected off of a normal
     * @param a the vector to reflect
     * @param normal a normal vector
     * @return the reflected vector
     */
    public static Vector2 reflect(Vector2 a, Vector2 normal) {
        return a.subtract(
                normal.multiply(2 * a.dot(normal))
        );
    }
}
