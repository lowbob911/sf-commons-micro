package eu.sportsfusion.common;

/**
 * @author Anatoli Pranovich
 */
public class StringUtils {

    public static boolean isBlank(String string) {
        //return string == null || string.trim().isEmpty();

        if (string == null || string.isEmpty()) {
            return true;
        }
        for (int i = 0; i < string.length(); i++) {
            if ((!Character.isWhitespace(string.charAt(i)))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(String string) {
        return !isBlank(string);
    }
}
