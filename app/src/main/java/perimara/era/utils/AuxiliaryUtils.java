package perimara.era.utils;

import android.content.Context;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by periklismaravelias on 29/11/16.
 */
public class AuxiliaryUtils {

    public static String sha1(String input) {
        try {
            MessageDigest mDigest = MessageDigest.getInstance("SHA1");
            byte[] result = mDigest.digest(input.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < result.length; i++) {
                sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e){
            return input;
        }
    }

    public static int px(Context c, float dips) {
        float DP = c.getResources().getDisplayMetrics().density;
        return Math.round(dips * DP);
    }

}
