package co.lujun.ghouse.util;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;

import co.lujun.ghouse.bean.SignCarrier;
import co.lujun.ghouse.bean.Config;

/**
 * Created by lujun on 2015/9/2.
 */
public class SignatureUtil {

    public static SignCarrier getSignature(Map<String, String> map){
        SignCarrier signCarrier = new SignCarrier();
        signCarrier.setNonce(RandomUtil.getRandomNumbersAndLetters(Config.NONCE_LENGTH));
        signCarrier.setTimestamp(System.currentTimeMillis() / 1000 + "");
        map.put("appid", SignCarrier.getAppId());
        map.put("timestamp", signCarrier.getTimestamp());
        map.put("nonce", signCarrier.getNonce());
        map.put("token", SignCarrier.getAppToken());
        signCarrier.setSignature(makeSignature(map));
        return signCarrier;
    }

    /**
     * make signature
     * @param map
     * @return
     */
    private static String makeSignature(Map<String, String> map){
        try {
            String[] values = map.values().toArray(new String[0]);
            Arrays.sort(values);
            StringBuilder builder = new StringBuilder();
            for (String string : values) {
                builder.append(string);
            }
//            builder.append(SignCarrier.getAppToken());
            return MD5.getMD5(builder.toString());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}