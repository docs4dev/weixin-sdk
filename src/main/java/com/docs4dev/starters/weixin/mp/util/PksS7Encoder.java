package com.docs4dev.starters.weixin.mp.util;

import static com.docs4dev.starters.weixin.mp.Constants.DEFAULT_CHARSET;

import java.util.Arrays;

public class PksS7Encoder {

    public static byte[] encode(int count) {
        int blockSize = 32;
        // 计算需要填充的位数
        int amountToPad = blockSize - (count % blockSize);
        if (amountToPad == 0) {
            amountToPad = blockSize;
        }
        // 获得补位所用的字符
        char padChr = chr(amountToPad);
        StringBuilder builder = new StringBuilder();
        for (int index = 0; index < amountToPad; index++) {
            builder.append(padChr);
        }
        return builder.toString().getBytes(DEFAULT_CHARSET);
    }

    public static byte[] decode(byte[] decrypted) {
        int pad = (int) decrypted[decrypted.length - 1];
        if (pad < 1 || pad > 32) {
            pad = 0;
        }
        return Arrays.copyOfRange(decrypted, 0, decrypted.length - pad);
    }

    /**
     * 将数字转化成ASCII码对应的字符，用于对明文进行补码
     *
     * @param a 需要转化的数字
     * @return 转化得到的字符
     */
    private static char chr(int a) {
        byte target = (byte) (a & 0xFF);
        return (char) target;
    }

}
