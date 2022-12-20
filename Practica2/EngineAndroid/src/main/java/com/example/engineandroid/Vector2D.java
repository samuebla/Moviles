package com.example.engineandroid;

//Estructura que permite guardar una posicion logica x, y
public class Vector2D {
    private float x, y;

    public Vector2D() {
        this(0,0);
    }
    public Vector2D(float value) {
        this(value, value);
    }

    public Vector2D(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D(double x, double y) {
        this.x = (float)x;
        this.y = (float)y;
    }


    public Vector2D(Vector2D vector2d) {
        this.x = vector2d.x;
        this.y = vector2d.y;
    }

    public void copy(Vector2D vector2d) {
        this.x = vector2d.x;
        this.y = vector2d.y;
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Vector2D add(Vector2D other) {
        float x = this.x + other.x;
        float y = this.y + other.y;
        return new Vector2D(x, y);
    }

    public Vector2D subtract(Vector2D other) {
        float x = this.x - other.x;
        float y = this.y - other.y;
        return new Vector2D(x, y);
    }

    public Vector2D multiply(float value) {
        return new Vector2D(value * x, value * y);
    }

    public double dotProduct(Vector2D other) {
        return other.x * x + other.y * y;
    }

    public float getLength() {
        return (float) Math.sqrt(dotProduct(this));
    }

    public Vector2D normalize() {
        float magnitude = getLength();
        if (magnitude == 0) {
            magnitude = 1;
        }
        x = x / magnitude;
        y = y / magnitude;
        return this;
    }
}