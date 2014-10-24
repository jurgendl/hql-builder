package org.tools.hqlbuilder.common;

import java.security.NoSuchAlgorithmException;

/**
 * work-around: does not give an asm exception when using Spring 3 with Java 8
 */
public class SecureRandom {
    public static java.security.SecureRandom getInstance(String algorithm) throws NoSuchAlgorithmException {
        return java.security.SecureRandom.getInstance(algorithm);
    }
}
