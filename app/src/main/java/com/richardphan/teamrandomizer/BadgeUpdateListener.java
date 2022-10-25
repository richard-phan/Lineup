package com.richardphan.teamrandomizer;

public interface BadgeUpdateListener {
    void increment();
    void decrement();
    void set(int value);
}
