package com.g_art.munchkinlevelcounter.billing.util;

/**
 * MunchinLevelCounter
 * Created by G_Art on 7/12/2014.
 */

/**
 * Exception thrown when encountering an invalid Base64 input character.
 *
 * @author nelson
 */
public class Base64DecoderException extends Exception {
    public Base64DecoderException() {
        super();
    }

    public Base64DecoderException(String s) {
        super(s);
    }

    private static final long serialVersionUID = 1L;
}