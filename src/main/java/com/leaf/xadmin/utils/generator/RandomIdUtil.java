package com.leaf.xadmin.utils.generator;

import lombok.Synchronized;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * 数据库主键id生成算法(很简单的实现，不适用复杂的生产环境)
 *
 * @author leaf
 * <p>date: 2017-12-29 14:46</p>
 */
public class RandomIdUtil {

    /**
     * 指定前缀 + 年月日时分秒毫秒 + 指定位数随机码
     *
     * @param prefix
     * @param size
     * @return
     */
    @Synchronized
    public static String getRandomId(String prefix, int size) {
        // 当前年月日时分秒毫秒
        SimpleDateFormat tmpFmtDate = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String retStr = prefix + String.valueOf(tmpFmtDate.format(new Date()));

        // 三位随机码
        int tmpInt = new Random().nextInt((int) Math.pow(10, size));
        retStr += String.valueOf(tmpInt);

        return retStr;
    }

    /**
     * 生成流水号，例如000001、000002、000003
     *
     * @param val
     * @param size
     * @return
     */
    @Synchronized
    public static String fmtLong(Long val, int size) {
        StringBuilder sb = new StringBuilder("");
        sb.append(val);
        if (sb.length() < size) {
            int cnt = size - sb.length();
            for (int i = 0; i < cnt; i++) {
                sb.insert(0, "0");
            }
            return sb.toString();
        } else if (sb.length() > size) {
            return sb.substring(sb.length() - size, size);
        } else {
            return sb.toString();
        }
    }

    /**
     * 生成无"-"UUID标识
     *
     * @return
     */
    @Synchronized
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }


}
