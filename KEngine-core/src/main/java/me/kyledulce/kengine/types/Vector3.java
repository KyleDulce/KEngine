package me.kyledulce.kengine.types;

import org.joml.Vector3f;

/**
 * Represents 3d vectors and positions
 * @param x the x component
 * @param y the y component
 * @param z the z component
 */
public record Vector3(
        float x,
        float y,
        float z
) {
    private static final float FLOAT_ERROR_EQUALS_SQUARED = 1e-5f * 1e-5f;

    /**
     * Shorthand for Vector(0, -1, 0)
     */
    public static final Vector3 DOWN = new Vector3(0, -1, 0);
    /**
     * Shorthand for Vector(0, 1, 0)
     */
    public static final Vector3 UP = new Vector3(0, 1, 0);
    /**
     * Shorthand for Vector(-1, 0, 0)
     */
    public static final Vector3 LEFT = new Vector3(-1, 0, 0);
    /**
     * Shorthand for Vector(1, 0, 0)
     */
    public static final Vector3 RIGHT = new Vector3(1, 0, 0);
    /**
     * Shorthand for Vector(0, 0, 1)
     */
    public static final Vector3 FORWARD = new Vector3(0, 0, 1);
    /**
     * Shorthand for Vector(0, 0, -1)
     */
    public static final Vector3 BACK = new Vector3(0, 0, -1);
    /**
     * Shorthand for Vector(1, 1, 1)
     */
    public static final Vector3 ONE = new Vector3(1, 1, 1);
    /**
     * Shorthand for Vector(0, 0, 0)
     */
    public static final Vector3 ZERO = new Vector3(0, 0, 0);

    /**
     * Returns the length of this vector calculated by the square root of (x*x+y*y+z*z)
     * If you are only comparing magnitudes, then you should use {@link #sqrMagnitude()} which
     * is faster
     * @return Returns the length of this vector.
     */
    public float magnitude() {
        return Mathf.sqrt(sqrMagnitude());
    }

    /**
     * Returns the squared length of this vector calculated by the square root of (x*x+y*y)
     * If you are only comparing magnitudes, then squared magnitude is faster than {@link #magnitude()}
     * @return Returns the squared length of this vector.
     */
    public float sqrMagnitude() {
        return y*y + x*x + z*z;
    }

    /**
     * When normalized the vector keeps same direction but with length 1.
     * @return Vector with magnitude of 1
     */
    public Vector3 normalize() {
        float mag = magnitude();
        return new Vector3(x / mag, y / mag, z/mag);
    }

    /**
     * Adds 2 vectors by adding their individual components
     * @param a the vector to add
     * @return vector that is the sum of this and a
     */
    public Vector3 add(Vector3 a) {
        return new Vector3(x + a.x, y + a.y, z + a.z);
    }

    /**
     * Subtract 2 vectors by subtracting their individual components
     * @param a the vector to subtract
     * @return vector that is the difference of this and a
     */
    public Vector3 subtract(Vector3 a) {
        return new Vector3(x - a.x, y - a.y, z - a.z);
    }

    /**
     * Multiplies a vector by a float by multiplying the individual components
     * @param d float to multiply
     * @return vector that is the product of this and d
     */
    public Vector3 multiply(float d) {
        return new Vector3(x * d, y * d, z * d);
    }

    /**
     * Divides a vector by a float by dividing the individual components
     * @param d the float to divide
     * @return vector that os the quotient of this and d
     */
    public Vector3 divide(float d) {
        return new Vector3(x / d, y / d, z / d);
    }

    /**
     * Returns the dot product of this vector and another vector
     * @param a a vector
     * @return the dot product of the 2 vector
     */
    public float dot(Vector3 a) {
        return x * a.x + y * a.y + z * a.z;
    }

    /**
     * Returns the cross product of this vector by another vector
     * @param a the other vector
     * @return the cross product vector
     */
    public Vector3 cross(Vector3 a) {
        return new Vector3(
                y * a.z - z * a.y,
                z * a.x - x * a.z,
                x * a.y - y * a.x
        );
    }

    /**
     * Multiplies 2 vectors component wise
     * @param a the vector to multiply
     * @return the scaled resultant vector
     */
    public Vector3 scale(Vector3 a) {
        return new Vector3(x * a.x, y * a.y, z * a.z);
    }

    @Override
    public boolean equals(Object other) {
        if(this == other) {
            return true;
        }
        if(other == null) {
            return false;
        }
        if(!(other instanceof Vector3 otherVector)) {
            return false;
        }

        if(x == otherVector.x && y == otherVector.y && z == otherVector.z) {
            return true;
        }

        // if not exactly equal, check if they are approximate
        Vector3 difference = this.subtract(otherVector);
        return difference.sqrMagnitude() < FLOAT_ERROR_EQUALS_SQUARED;
    }

    @Override
    public String toString() {
        return String.format("(%s, %s, %s)", x, y, z);
    }

    /**
     * Converts this vector to {@link org.joml.Vector3f}
     * @return a {@link org.joml.Vector3f} of this vector
     */
    Vector3f toJomlVector() {
        return new Vector3f(x, y, z);
    }

    /**
     * Gets a = {@link org.joml.Vector3f}
     * @return a {@link org.joml.Vector3f} of this vector
     */
    static Vector3 fromJomlVector(Vector3f a) {
        return new Vector3(a.x, a.y, a.z);
    }

    /**
     * Gets the unsigned angle between 2 vectors
     * @param a a vector
     * @param b another vector
     * @return the angle in radians
     */
    public static float angle(Vector3 a, Vector3 b) {
        Vector3f aj = a.toJomlVector();
        Vector3f bj = b.toJomlVector();

        return aj.angle(bj);
    }

    /**
     * Gets the distance between 2 vectors
     * @param a a vector
     * @param b another vector
     * @return the distance between the 2 vectors
     */
    public static float distance(Vector3 a, Vector3 b) {
        Vector3f aj = a.toJomlVector();
        Vector3f bj = b.toJomlVector();
        return aj.distance(bj);
    }

    /**
     * Clamps magnitude of vector to a specified length. If the vector is less than the max length, this vector
     * is returned. Otherwise a vector in the same direction of length maxLength is returned
     * @param a a vector
     * @param maxLength the max length
     * @return the clamped vector
     */
    public static Vector3 clampMagnitude(Vector3 a, float maxLength) {
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
    public static Vector3 lerp(Vector3 a, Vector3 b, float t) {
        float clampedValue = Mathf.clamp(t, 0, 1);
        return fromJomlVector(a.toJomlVector().lerp(b.toJomlVector(), clampedValue));
    }

    /**
     * Returns a vector made of the larger components of 2 provided vectors
     * @param a a vector
     * @param b another vector
     * @return The vector made of the larger components
     */
    public static Vector3 max(Vector3 a, Vector3 b) {
        return new Vector3(Math.max(a.x, b.x), Math.max(a.y, b.y), Math.max(a.z, b.z));
    }

    /**
     * Returns a vector made of the smaller components of 2 provided vectors
     * @param a a vector
     * @param b another vector
     * @return The vector made of the smaller components
     */
    public static Vector3 min(Vector3 a, Vector3 b) {
        return new Vector3(Math.min(a.x, b.x), Math.min(a.y, b.y), Math.min(a.z, b.z));
    }

    /**
     * Returns a vector that is reflected off of a normal
     * @param a the vector to reflect
     * @param normal a normal vector
     * @return the reflected vector
     */
    public static Vector3 reflect(Vector3 a, Vector3 normal) {
        return a.subtract(
                normal.multiply(2 * a.dot(normal))
        );
    }
}
