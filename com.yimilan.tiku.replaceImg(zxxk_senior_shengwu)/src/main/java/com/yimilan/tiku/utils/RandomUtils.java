package com.yimilan.tiku.utils;


import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class RandomUtils {
    /**
     * 获取n位随机数字
     *
     * @param n
     * @return
     */
    public static Long getRandomNumber(int n) {
        double temp = Math.pow(10, n - 1);
        return Math.round(Math.random() * 9 * temp + temp);
    }

    /**
     * 获取一定长度的随机字符串
     *
     * @param length 指定字符串长度
     * @return 一定长度的字符串
     */
    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 在0，max之间随机生成n个不重复的整数
     *
     * @param max
     * @param n
     * @return
     */
    public static Integer[] getRandomNum(int max, int n) {
        Integer[] nums;
        if (max <= n) {
            nums = new Integer[max];
            for (int i = 0; i < max; i++) {
                nums[i] = i;
            }
        } else {
            nums = new Integer[n];
            Set<Integer> numSet = new HashSet<Integer>();
            for (int i = 0; i < n; i++) {
                int num = (int) (Math.random() * max);
                while (numSet.contains(num)) {
                    num = (int) (Math.random() * max);
                }
                numSet.add(num);
                nums[i] = num;
            }
        }
        return nums;
    }

    public static int getRandomNumber(int min, int max) {
        if(min >= max){
            return 0;
        }
        Random random = new Random();
        return random.nextInt(max) % (max - min + 1) + min;
    }

    public static int getRandomNum(int max) {
        if(max < 0){
            return 0;
        }
        return (int) (Math.random() * max);
    }

}
