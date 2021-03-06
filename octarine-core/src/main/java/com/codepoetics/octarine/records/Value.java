package com.codepoetics.octarine.records;

import java.util.Objects;

public final class Value {

    private final Key<?> key;
    private final Object value;

    Value(Key<?> key, Object value) {
        this.key = key;
        this.value = value;
    }

    public <T> Key<T> key() {
        return (Key<T>) key;
    }

    public <T> T value() {
        return (T) value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Value other = (Value) obj;
        return Objects.equals(this.key, other.key) && Objects.equals(this.value, other.value);
    }

    @Override
    public String toString() {
        return String.format("%s: %s", key, value);
    }
}
