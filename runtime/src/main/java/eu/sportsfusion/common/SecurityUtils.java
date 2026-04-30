package eu.sportsfusion.common;

import java.security.SecureRandom;

/**
 * @author Igor Vashyst
 */
public class SecurityUtils {

    private static final String SYMBOLS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String generateToken(int tokenLen) {
        StringBuilder sb = new StringBuilder(tokenLen);
        SecureRandom random = new SecureRandom();

        for( int i = 0; i < tokenLen; i++ ) {
            sb.append(SYMBOLS.charAt(random.nextInt(SYMBOLS.length())));
        }
        return sb.toString();
    }
}
