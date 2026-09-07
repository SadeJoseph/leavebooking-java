package com.example.leavebooking;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)

public abstract class Entity<T> {

    public static final String IDENTITY_CANNOT_BE_NULL = "Identity value cannot be null";

    @EqualsAndHashCode.Include
    protected final Identity<T> id;

    public Entity(Identity<T> id) {
        if(id == null) {
            throw new IllegalArgumentException(IDENTITY_CANNOT_BE_NULL);
        }
        this.id = id;
    }

    public Identity<T> id() {
        return id;
    }
}
