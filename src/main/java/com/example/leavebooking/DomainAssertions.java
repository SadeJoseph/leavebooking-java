package com.example.leavebooking;

public final class DomainAssertions {
    
    private DomainAssertions () {}

    public static void argumentLength(String value, int min, int max, String errorMessage) {
       int length = value.trim().length();

       if( length < min || length > max ) {
           throw new IllegalArgumentException(errorMessage);
        }
    }

    public static void argumentNotEmpty(String value, String errorMessage) {
        if(value == null) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
}

