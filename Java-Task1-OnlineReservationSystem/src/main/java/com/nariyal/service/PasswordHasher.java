package com.nariyal.service;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHasher{

    // Generate hash from plain-text password

    public static String hashPassword(String plainPassword) {
        // Work factor of 12 is a solid standard
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
    }

    // Verify a plain-text password against the stored hash
    public static boolean verifyPassword(String plainPassword, String storedHash) {
        return BCrypt.checkpw(plainPassword, storedHash);
    }

}
