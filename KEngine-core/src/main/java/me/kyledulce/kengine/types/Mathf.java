package me.kyledulce.kengine.types;

import org.jetbrains.annotations.Contract;

/**
 * Float wrapper for {@link java.lang.Math} class
 */
public final class Mathf {
    public static final float E_F = (float) Math.E;
    public static final float PI_F = (float) Math.PI;

    /**
     * Returns the absolute value of a float value. If the argument is not negative, the argument is returned. If the argument is negative, the negation of the argument is returned. Special cases:
     *
     *     If the argument is positive zero or negative zero, the result is positive zero.
     *     If the argument is infinite, the result is positive infinity.
     *     If the argument is NaN, the result is NaN.
     * @param a the argument whose absolute value is to be determined
     * @return the absolute value of the argument.
     */
    @Contract(pure = true)
    public static float abs(float a) {
        return Math.abs(a);
    }

    /**
     * Returns the arc cosine of a value; the returned angle is in the range 0.0 through pi. Special case:
     *
     *     If the argument is NaN or its absolute value is greater than 1, then the result is NaN.
     *
     * The computed result must be within 1 ulp of the exact result. Results must be semi-monotonic.
     * @param a the value whose arc cosine is to be returned.
     * @return the arc cosine of the argument.
     */
    @Contract(pure = true)
    public static float acos(float a) {
        return (float) Math.acos(a);
    }

    /**
     * Returns the arc sine of a value; the returned angle is in the range -pi/2 through pi/2. Special cases:
     *
     *     If the argument is NaN or its absolute value is greater than 1, then the result is NaN.
     *     If the argument is zero, then the result is a zero with the same sign as the argument.
     *
     * The computed result must be within 1 ulp of the exact result. Results must be semi-monotonic.
     * @param a the value whose arc sine is to be returned.
     * @return the arc sine of the argument.
     */
    @Contract(pure = true)
    public static float asin(float a) {
        return (float) Math.asin(a);
    }

    /**
     * Returns the arc tangent of a value; the returned angle is in the range -pi/2 through pi/2. Special cases:
     *
     *     If the argument is NaN, then the result is NaN.
     *     If the argument is zero, then the result is a zero with the same sign as the argument.
     *
     * The computed result must be within 1 ulp of the exact result. Results must be semi-monotonic.
     * @param a the value whose arc tangent is to be returned.
     * @return the arc tangent of the argument.
     */
    @Contract(pure = true)
    public static float atan(float a) {
        return (float) Math.atan(a);
    }

    /**
     * Returns the angle theta from the conversion of rectangular coordinates (x, y) to polar coordinates (r, theta). This method computes the phase theta by computing an arc tangent of y/x in the range of -pi to pi. Special cases:
     *
     *     If either argument is NaN, then the result is NaN.
     *     If the first argument is positive zero and the second argument is positive, or the first argument is positive and finite and the second argument is positive infinity, then the result is positive zero.
     *     If the first argument is negative zero and the second argument is positive, or the first argument is negative and finite and the second argument is positive infinity, then the result is negative zero.
     *     If the first argument is positive zero and the second argument is negative, or the first argument is positive and finite and the second argument is negative infinity, then the result is the float value closest to pi.
     *     If the first argument is negative zero and the second argument is negative, or the first argument is negative and finite and the second argument is negative infinity, then the result is the float value closest to -pi.
     *     If the first argument is positive and the second argument is positive zero or negative zero, or the first argument is positive infinity and the second argument is finite, then the result is the float value closest to pi/2.
     *     If the first argument is negative and the second argument is positive zero or negative zero, or the first argument is negative infinity and the second argument is finite, then the result is the float value closest to -pi/2.
     *     If both arguments are positive infinity, then the result is the float value closest to pi/4.
     *     If the first argument is positive infinity and the second argument is negative infinity, then the result is the float value closest to 3*pi/4.
     *     If the first argument is negative infinity and the second argument is positive infinity, then the result is the float value closest to -pi/4.
     *     If both arguments are negative infinity, then the result is the float value closest to -3*pi/4.
     *
     * The computed result must be within 2 ulps of the exact result. Results must be semi-monotonic.
     * @param y the ordinate coordinate
     * @param x the abscissa coordinate
     * @return the theta component of the point (r, theta) in polar coordinates that corresponds to the point (x, y) in Cartesian coordinates.
     */
    @Contract(pure = true)
    public static float atan2(float y, float x) {
        return (float) Math.atan2(y, x);
    }

    /**
     * Returns the cube root of a float value. For positive finite x, cbrt(-x) == -cbrt(x); that is, the cube root of a negative value is the negative of the cube root of that value's magnitude. Special cases:
     *
     *     If the argument is NaN, then the result is NaN.
     *     If the argument is infinite, then the result is an infinity with the same sign as the argument.
     *     If the argument is zero, then the result is a zero with the same sign as the argument.
     *
     * The computed result must be within 1 ulp of the exact result.
     * @param a a value.
     * @return the cube root of a.
     */
    @Contract(pure = true)
    public static float cbrt(float a) {
        return (float) Math.cbrt(a);
    }

    /**
     * Returns the smallest (closest to negative infinity) float value that is greater than or equal to the argument and is equal to a mathematical integer. Special cases:
     *
     *     If the argument value is already equal to a mathematical integer, then the result is the same as the argument.
     *     If the argument is NaN or an infinity or positive zero or negative zero, then the result is the same as the argument.
     *     If the argument value is less than zero but greater than -1.0, then the result is negative zero.
     *
     * Note that the value of Math.ceil(x) is exactly the value of -Math.floor(-x).
     * @param a a value.
     * @return the smallest (closest to negative infinity) floating-point value that is greater than or equal to the argument and is equal to a mathematical integer.
     */
    @Contract(pure = true)
    public static float ceil(float a) {
        return (float) Math.ceil(a);
    }

    /**
     * Returns the trigonometric cosine of an angle. Special cases:
     *
     *     If the argument is NaN or an infinity, then the result is NaN.
     *
     * The computed result must be within 1 ulp of the exact result. Results must be semi-monotonic.
     * @param a an angle, in radians.
     * @return the cosine of the argument.
     */
    @Contract(pure = true)
    public static float cos(float a) {
        return (float) Math.cos(a);
    }

    /**
     * Returns the hyperbolic cosine of a float value. The hyperbolic cosine of x is defined to be (ex + e-x)/2 where e is Euler's number.
     *
     * Special cases:
     *
     *     If the argument is NaN, then the result is NaN.
     *     If the argument is infinite, then the result is positive infinity.
     *     If the argument is zero, then the result is 1.0.
     *
     * The computed result must be within 2.5 ulps of the exact result.
     * @param x The number whose hyperbolic cosine is to be returned.
     * @return The hyperbolic cosine of x.
     */
    @Contract(pure = true)
    public static float cosh(float x) {
        return (float) Math.cosh(x);
    }

    /**
     * Returns Euler's number e raised to the power of a float value. Special cases:
     *
     *     If the argument is NaN, the result is NaN.
     *     If the argument is positive infinity, then the result is positive infinity.
     *     If the argument is negative infinity, then the result is positive zero.
     *
     * The computed result must be within 1 ulp of the exact result. Results must be semi-monotonic.
     * @param a the exponent to raise e to.
     * @return the value ea, where e is the base of the natural logarithms.
     */
    @Contract(pure = true)
    public static float exp(float a) {
        return (float) Math.exp(a);
    }

    /**
     * Returns ex -1. Note that for values of x near 0, the exact sum of expm1(x) + 1 is much closer to the true result of ex than exp(x).
     *
     * Special cases:
     *
     *     If the argument is NaN, the result is NaN.
     *     If the argument is positive infinity, then the result is positive infinity.
     *     If the argument is negative infinity, then the result is -1.0.
     *     If the argument is zero, then the result is a zero with the same sign as the argument.
     *
     * The computed result must be within 1 ulp of the exact result. Results must be semi-monotonic. The result of expm1 for any finite input must be greater than or equal to -1.0. Note that once the exact result of ex - 1 is within 1/2 ulp of the limit value -1, -1.0 should be returned.
     * @param x the exponent to raise e to in the computation of ex -1.
     * @return the value ex - 1.
     */
    @Contract(pure = true)
    public static float expm1(float x) {
        return (float) Math.expm1(x);
    }

    /**
     * Returns the largest (closest to positive infinity) float value that is less than or equal to the argument and is equal to a mathematical integer. Special cases:
     *
     *     If the argument value is already equal to a mathematical integer, then the result is the same as the argument.
     *     If the argument is NaN or an infinity or positive zero or negative zero, then the result is the same as the argument.
     * @param a a value.
     * @return the largest (closest to positive infinity) floating-point value that less than or equal to the argument and is equal to a mathematical integer.
     */
    @Contract(pure = true)
    public static float floor(float a) {
        return (float) Math.floor(a);
    }

    /**
     * Returns sqrt(x2 +y2) without intermediate overflow or underflow.
     *
     * Special cases:
     *
     *     If either argument is infinite, then the result is positive infinity.
     *     If either argument is NaN and neither argument is infinite, then the result is NaN.
     *
     * The computed result must be within 1 ulp of the exact result. If one parameter is held constant, the results must be semi-monotonic in the other parameter.
     * @param y a value
     * @param x a value
     * @return sqrt(x2 +y2) without intermediate overflow or underflow
     */
    @Contract(pure = true)
    public static float hypot(float y, float x) {
        return (float) Math.hypot(y,x);
    }

    /**
     * Computes the remainder operation on two arguments as prescribed by the IEEE 754 standard. The remainder value is mathematically equal to f1 - f2 × n, where n is the mathematical integer closest to the exact mathematical value of the quotient f1/f2, and if two mathematical integers are equally close to f1/f2, then n is the integer that is even. If the remainder is zero, its sign is the same as the sign of the first argument. Special cases:
     *
     *     If either argument is NaN, or the first argument is infinite, or the second argument is positive zero or negative zero, then the result is NaN.
     *     If the first argument is finite and the second argument is infinite, then the result is the same as the first argument.
     * @param f1 the dividend.
     * @param f2 the divisor.
     * @return the remainder when f1 is divided by f2.
     */
    @Contract(pure = true)
    public static float IEEEremainder(float f1, float f2) {
        return (float) Math.IEEEremainder(f1, f2);
    }

    /**
     * Returns the natural logarithm (base e) of a float value. Special cases:
     *
     *     If the argument is NaN or less than zero, then the result is NaN.
     *     If the argument is positive infinity, then the result is positive infinity.
     *     If the argument is positive zero or negative zero, then the result is negative infinity.
     *
     * The computed result must be within 1 ulp of the exact result. Results must be semi-monotonic.
     * @param a a value
     * @return the value ln a, the natural logarithm of a.
     */
    @Contract(pure = true)
    public static float log(float a) {
        return (float) Math.log(a);
    }

    /**
     * Returns the base 10 logarithm of a float value. Special cases:
     *
     *     If the argument is NaN or less than zero, then the result is NaN.
     *     If the argument is positive infinity, then the result is positive infinity.
     *     If the argument is positive zero or negative zero, then the result is negative infinity.
     *     If the argument is equal to 10n for integer n, then the result is n.
     *
     * The computed result must be within 1 ulp of the exact result. Results must be semi-monotonic.
     * @param a a value
     * @return the base 10 logarithm of a.
     */
    @Contract(pure = true)
    public static float log10(float a) {
        return (float) Math.log10(a);
    }

    /**
     * Returns the natural logarithm of the sum of the argument and 1. Note that for small values x, the result of log1p(x) is much closer to the true result of ln(1 + x) than the floating-point evaluation of log(1.0+x).
     *
     * Special cases:
     *
     *     If the argument is NaN or less than -1, then the result is NaN.
     *     If the argument is positive infinity, then the result is positive infinity.
     *     If the argument is negative one, then the result is negative infinity.
     *     If the argument is zero, then the result is a zero with the same sign as the argument.
     *
     * The computed result must be within 1 ulp of the exact result. Results must be semi-monotonic.
     * @param a a value
     * @return the value ln(x + 1), the natural log of x + 1
     */
    @Contract(pure = true)
    public static float log1p(float a) {
        return (float) Math.log1p(a);
    }

    /**
     * Returns the value of the first argument raised to the power of the second argument. Special cases:
     *
     *     If the second argument is positive or negative zero, then the result is 1.0.
     *     If the second argument is 1.0, then the result is the same as the first argument.
     *     If the second argument is NaN, then the result is NaN.
     *     If the first argument is NaN and the second argument is nonzero, then the result is NaN.
     *     If
     *         the absolute value of the first argument is greater than 1 and the second argument is positive infinity, or
     *         the absolute value of the first argument is less than 1 and the second argument is negative infinity,
     *     then the result is positive infinity.
     *     If
     *         the absolute value of the first argument is greater than 1 and the second argument is negative infinity, or
     *         the absolute value of the first argument is less than 1 and the second argument is positive infinity,
     *     then the result is positive zero.
     *     If the absolute value of the first argument equals 1 and the second argument is infinite, then the result is NaN.
     *     If
     *         the first argument is positive zero and the second argument is greater than zero, or
     *         the first argument is positive infinity and the second argument is less than zero,
     *     then the result is positive zero.
     *     If
     *         the first argument is positive zero and the second argument is less than zero, or
     *         the first argument is positive infinity and the second argument is greater than zero,
     *     then the result is positive infinity.
     *     If
     *         the first argument is negative zero and the second argument is greater than zero but not a finite odd integer, or
     *         the first argument is negative infinity and the second argument is less than zero but not a finite odd integer,
     *     then the result is positive zero.
     *     If
     *         the first argument is negative zero and the second argument is a positive finite odd integer, or
     *         the first argument is negative infinity and the second argument is a negative finite odd integer,
     *     then the result is negative zero.
     *     If
     *         the first argument is negative zero and the second argument is less than zero but not a finite odd integer, or
     *         the first argument is negative infinity and the second argument is greater than zero but not a finite odd integer,
     *     then the result is positive infinity.
     *     If
     *         the first argument is negative zero and the second argument is a negative finite odd integer, or
     *         the first argument is negative infinity and the second argument is a positive finite odd integer,
     *     then the result is negative infinity.
     *     If the first argument is finite and less than zero
     *         if the second argument is a finite even integer, the result is equal to the result of raising the absolute value of the first argument to the power of the second argument
     *         if the second argument is a finite odd integer, the result is equal to the negative of the result of raising the absolute value of the first argument to the power of the second argument
     *         if the second argument is finite and not an integer, then the result is NaN.
     *     If both arguments are integers, then the result is exactly equal to the mathematical result of raising the first argument to the power of the second argument if that result can in fact be represented exactly as a float value.
     *
     * (In the foregoing descriptions, a floating-point value is considered to be an integer if and only if it is finite and a fixed point of the method ceil or, equivalently, a fixed point of the method floor. A value is a fixed point of a one-argument method if and only if the result of applying the method to the value is equal to the value.)
     *
     * The computed result must be within 1 ulp of the exact result. Results must be semi-monotonic.
     * @param a the base.
     * @param b the exponent.
     * @return the value ab.
     */
    @Contract(pure = true)
    public static float pow(float a, float b) {
        return (float) Math.pow(a, b);
    }

    /**
     * Returns the float value that is closest in value to the argument and is equal to a mathematical integer. If two float values that are mathematical integers are equally close, the result is the integer value that is even. Special cases:
     *
     *     If the argument value is already equal to a mathematical integer, then the result is the same as the argument.
     *     If the argument is NaN or an infinity or positive zero or negative zero, then the result is the same as the argument.
     * @param a a float value.
     * @return the closest floating-point value to a that is equal to a mathematical integer.
     */
    @Contract(pure = true)
    public static float rint(float a) {
        return (float) Math.rint(a);
    }

    /**
     * Returns the trigonometric sine of an angle. Special cases:
     *
     *     If the argument is NaN or an infinity, then the result is NaN.
     *     If the argument is zero, then the result is a zero with the same sign as the argument.
     *
     * The computed result must be within 1 ulp of the exact result. Results must be semi-monotonic.
     * @param a an angle, in radians.
     * @return the sine of the argument.
     */
    @Contract(pure = true)
    public static float sin(float a) {
        return (float) Math.sin(a);
    }

    /**
     * Returns the hyperbolic sine of a float value. The hyperbolic sine of x is defined to be (ex - e-x)/2 where e is Euler's number.
     *
     * Special cases:
     *
     *     If the argument is NaN, then the result is NaN.
     *     If the argument is infinite, then the result is an infinity with the same sign as the argument.
     *     If the argument is zero, then the result is a zero with the same sign as the argument.
     *
     * The computed result must be within 2.5 ulps of the exact result.
     * @param x The number whose hyperbolic sine is to be returned.
     * @return The hyperbolic sine of x.
     */
    @Contract(pure = true)
    public static float sinh(float x) {
        return (float) Math.sinh(x);
    }

    /**
     * Returns the correctly rounded positive square root of a float value. Special cases:
     *
     *     If the argument is NaN or less than zero, then the result is NaN.
     *     If the argument is positive infinity, then the result is positive infinity.
     *     If the argument is positive zero or negative zero, then the result is the same as the argument.
     *
     * Otherwise, the result is the float value closest to the true mathematical square root of the argument value.
     * @param a a value.
     * @return the positive square root of a. If the argument is NaN or less than zero, the result is NaN.
     */
    @Contract(pure = true)
    public static float sqrt(float a) {
        return (float) Math.sqrt(a);
    }

    /**
     * Returns the trigonometric tangent of an angle. Special cases:
     *
     *     If the argument is NaN or an infinity, then the result is NaN.
     *     If the argument is zero, then the result is a zero with the same sign as the argument.
     *
     * The computed result must be within 1 ulp of the exact result. Results must be semi-monotonic.
     * @param a an angle, in radians.
     * @return the tangent of the argument.
     */
    @Contract(pure = true)
    public static float tan(float a) {
        return (float) Math.tan(a);
    }

    /**
     * Returns the hyperbolic tangent of a float value. The hyperbolic tangent of x is defined to be (ex - e-x)/(ex + e-x), in other words, sinh(x)/cosh(x). Note that the absolute value of the exact tanh is always less than 1.
     *
     * Special cases:
     *
     *     If the argument is NaN, then the result is NaN.
     *     If the argument is zero, then the result is a zero with the same sign as the argument.
     *     If the argument is positive infinity, then the result is +1.0.
     *     If the argument is negative infinity, then the result is -1.0.
     *
     * The computed result must be within 2.5 ulps of the exact result. The result of tanh for any finite input must have an absolute value less than or equal to 1. Note that once the exact result of tanh is within 1/2 of an ulp of the limit value of ±1, correctly signed ±1.0 should be returned.
     * @param x The number whose hyperbolic tangent is to be returned.
     * @return The hyperbolic tangent of x.
     */
    @Contract(pure = true)
    public static float tanh(float x) {
        return (float) Math.tanh(x);
    }

    /**
     * Converts an angle measured in radians to an approximately equivalent angle measured in degrees. The conversion from radians to degrees is generally inexact; users should not expect cos(toRadians(90.0)) to exactly equal 0.0.
     * @param angrad an angle, in radians
     * @return the measurement of the angle angrad in degrees.
     */
    @Contract(pure = true)
    public static float toDegrees(float angrad) {
        return (float) Math.toDegrees(angrad);
    }

    /**
     * Converts an angle measured in degrees to an approximately equivalent angle measured in radians. The conversion from degrees to radians is generally inexact.
     * @param angDeg an angle, in degrees
     * @return the measurement of the angle angdeg in radians.
     */
    @Contract(pure = true)
    public static float toRadians(float angDeg) {
        return (float) Math.toRadians(angDeg);
    }

    /**
     * Clamps a given float value to between 2 values. if the value is less than the minimum value, the minimum value is
     * returned. if the given float is larger than the maximum value, then the maximum value is returned. otherwise
     * value is returned
     * @param value The value to clamp
     * @param minimum The minimum value
     * @param maximum The maximum value
     * @return the result between the minimum and maximum values
     */
    @Contract(pure = true)
    public static float clamp(float value, float minimum, float maximum) {
        if(value < minimum){
            return minimum;
        }
        return Math.min(value, maximum);
    }
}
