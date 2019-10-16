package com.docs4dev.starters.weixin.mp.util;

import static com.docs4dev.starters.weixin.mp.Constants.DEFAULT_CHARSET;

import com.docs4dev.starters.weixin.mp.excepton.WeixinException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WeixinSecurityUtils {

    public static String sha1(String source) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.update(source.getBytes());
            byte[] digest = messageDigest.digest();

            StringBuilder builder = new StringBuilder();
            for (byte data : digest) {
                String shaHex = Integer.toHexString(data & 0xFF);
                if (shaHex.length() < 2) {
                    builder.append(0);
                }
                builder.append(shaHex);
            }
            return builder.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String sign(Object... params) {
        String sortedStr = Arrays.stream(params).map(String::valueOf).sorted().collect(Collectors.joining());
        return WeixinSecurityUtils.sha1(sortedStr);
    }

    public static String encrypt(String appId, String encodingAesKey, String source) {
        byte[] randomStrBytes = nonceString().getBytes(DEFAULT_CHARSET);
        byte[] textBytes = source.getBytes(DEFAULT_CHARSET);
        byte[] networkBytesOrder = getNetworkBytesOrder(textBytes.length);
        byte[] appidBytes = appId.getBytes(DEFAULT_CHARSET);

        // randomStr + networkBytesOrder + text + from
        ByteGroup byteCollector = new ByteGroup();
        byteCollector.addBytes(randomStrBytes);
        byteCollector.addBytes(networkBytesOrder);
        byteCollector.addBytes(textBytes);
        byteCollector.addBytes(appidBytes);
        // ... + pad: 使用自定义的填充方式对明文进行补位填充
        byte[] padBytes = PksS7Encoder.encode(byteCollector.size());
        byteCollector.addBytes(padBytes);

        // 获得最终的字节流, 未加密
        byte[] unencrypted = byteCollector.toBytes();
        try {
            byte[] aesKey = getEncodingAesKey(encodingAesKey);
            // 设置加密模式为AES的CBC模式
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(aesKey, "AES");
            IvParameterSpec iv = new IvParameterSpec(aesKey, 0, 16);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
            // 加密
            byte[] encrypted = cipher.doFinal(unencrypted);
            // 使用BASE64对加密后的字符串进行编码
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new WeixinException("encode message failed", e);
        }
    }

    /**
     * 部分代码拷贝自微信示例代码
     */
    public static String decrypt(String appId, String encodingAesKey, String source) {
        byte[] aesKey = getEncodingAesKey(encodingAesKey);
        byte[] original;
        try {
            // 设置解密模式为AES的CBC模式
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(aesKey, "AES");
            IvParameterSpec iv = new IvParameterSpec(Arrays.copyOfRange(aesKey, 0, 16));
            cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
            // 使用BASE64对密文进行解码
            byte[] encrypted = Base64.getDecoder().decode(source);
            // 解密
            original = cipher.doFinal(encrypted);
        } catch (Exception e) {
            throw new WeixinException("decrypt message failed", e);
        }
        String decryptStr;
        String appIdFromWx;
        try {
            // 去除补位字符
            byte[] bytes = PksS7Encoder.decode(original);

            // 分离16位随机字符串,网络字节序和AppId
            byte[] networkOrder = Arrays.copyOfRange(bytes, 16, 20);
            int xmlLength = recoverNetworkBytesOrder(networkOrder);
            decryptStr = new String(Arrays.copyOfRange(bytes, 20, 20 + xmlLength), DEFAULT_CHARSET);
            appIdFromWx = new String(Arrays.copyOfRange(bytes, 20 + xmlLength, bytes.length), DEFAULT_CHARSET);
        } catch (Exception e) {
            throw new WeixinException("decrypt message error", e);
        }
        if (!Objects.equals(appIdFromWx, appId)) {
            throw new WeixinException("invalid appId[" + appIdFromWx + "] from weixin");
        }
        return decryptStr;
    }

    private static String nonceString() {
        return RandomStringUtils.randomAlphabetic(16);
    }

    private static byte[] getEncodingAesKey(String key) {
        return Base64.getDecoder().decode(key + "=");
    }

    // 生成4个字节的网络字节序
    private static byte[] getNetworkBytesOrder(int sourceNumber) {
        byte[] orderBytes = new byte[4];
        orderBytes[3] = (byte) (sourceNumber & 0xFF);
        orderBytes[2] = (byte) (sourceNumber >> 8 & 0xFF);
        orderBytes[1] = (byte) (sourceNumber >> 16 & 0xFF);
        orderBytes[0] = (byte) (sourceNumber >> 24 & 0xFF);
        return orderBytes;
    }

    private static int recoverNetworkBytesOrder(byte[] orderBytes) {
        int sourceNumber = 0;
        for (int i = 0; i < 4; i++) {
            sourceNumber <<= 8;
            sourceNumber |= orderBytes[i] & 0xff;
        }
        return sourceNumber;
    }

    static class ByteGroup {

        private ArrayList<Byte> byteContainer = new ArrayList<>();

        byte[] toBytes() {
            byte[] bytes = new byte[byteContainer.size()];
            for (int i = 0; i < byteContainer.size(); i++) {
                bytes[i] = byteContainer.get(i);
            }
            return bytes;
        }

        void addBytes(byte[] bytes) {
            for (byte b : bytes) {
                byteContainer.add(b);
            }
        }

        int size() {
            return byteContainer.size();
        }
    }

}
