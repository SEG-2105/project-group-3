package ca.uottawa.team3.rentron.Users;

import android.app.Application;
import android.widget.Toast;
import android.content.Context;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class Hashing extends Application {
    Context context;
    public Hashing(Context context) { this.context = context; }

    // implementation of PBKDF2 -- does what it says on the tin. API 26+ required for SHA256 hashing
    public SecretKey hashPassword(char[] password, byte[] salt) throws NullPointerException {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSha256");
            PBEKeySpec spec = new PBEKeySpec(password, salt, 4096, 256);
            return factory.generateSecret(spec);
        }
        catch (NoSuchAlgorithmException e) {
            Toast.makeText(getApplicationContext(), "Unable to hash password, algorithm not found...??", Toast.LENGTH_SHORT).show();
            throw new NullPointerException();
        }
        catch (InvalidKeySpecException e) {
            Toast.makeText(getApplicationContext(), "Unable to hash password, key spec invalid.", Toast.LENGTH_SHORT).show();
            throw new NullPointerException();
        }
    }

    // generate random salt
    public byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[20];
        random.nextBytes(salt);
        return salt;
    }

    // verify password input with hash/salt in database
    public boolean verifyPassword(byte[] salt, String passwordPlainText, SecretKey password) {
        byte[] hash = password.getEncoded();
        SecretKey passwordToVerify = this.hashPassword(passwordPlainText.toCharArray(), salt);
        return Arrays.equals(hash, passwordToVerify.getEncoded());
    }
}
