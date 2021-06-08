package utility;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

/**
 * Hashes password
 * */
public class PasswordHasher {
    public static String hashPassword(String password)
    {
        /**
         * Hashes password;.
         *
         * @param password Password itself.
         * @return Hashed password.
         */
        try{
            // getInstance() method is called with algorithm SHA-384
            MessageDigest md = MessageDigest.getInstance("SHA-384");
            // digest() method is called
            // to calculate message digest of the input string
            // returned as array of byte
            byte[] bytes = md.digest(password.getBytes());
            // Convert byte array into signum representation
            BigInteger integer = new BigInteger(1,bytes);
            // Convert message digest into hex value
            String newPassword = integer.toString(16);
            while (newPassword.length()<32){
                newPassword = "0" +newPassword;
            }
            return newPassword;

        } catch (NoSuchAlgorithmException e) {
            Logger.getLogger("No password hashing algorithm found!");
            throw new IllegalStateException();
        }
    }

}
