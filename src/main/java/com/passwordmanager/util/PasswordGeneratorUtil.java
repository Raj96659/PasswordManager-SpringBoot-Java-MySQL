import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public static List<String> generatePasswords(
        int length,
        boolean useUpper,
        boolean useLower,
        boolean useNumbers,
        boolean useSpecial,
        boolean excludeSimilar,
        int count) {

    String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    String LOWER = "abcdefghijklmnopqrstuvwxyz";
    String NUMBERS = "0123456789";
    String SPECIAL = "!@#$%^&*()_+-=[]{}|;:,.<>?";

    StringBuilder pool = new StringBuilder();

    if (useUpper) pool.append(UPPER);
    if (useLower) pool.append(LOWER);
    if (useNumbers) pool.append(NUMBERS);
    if (useSpecial) pool.append(SPECIAL);

    if (excludeSimilar) {
        String filtered = pool.toString()
                .replaceAll("[0Ol1]", "");
        pool = new StringBuilder(filtered);
    }

    if (pool.length() == 0) {
        throw new RuntimeException("Select at least one character type");
    }

    SecureRandom random = new SecureRandom();
    List<String> passwords = new ArrayList<>();

    for (int j = 0; j < count; j++) {

        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(pool.length());
            password.append(pool.charAt(index));
        }

        passwords.add(password.toString());
    }

    return passwords;
}
