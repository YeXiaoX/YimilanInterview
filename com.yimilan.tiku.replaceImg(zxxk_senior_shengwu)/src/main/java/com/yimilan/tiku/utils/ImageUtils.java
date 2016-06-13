package com.yimilan.tiku.utils;

import java.util.Arrays;

/**
 * Created by xufeng on 2014/10/11.
 */
public class ImageUtils {

    public static boolean isImage(String suffix) {

        String[] suffixs = new String[]{"bmp", "dib", "gif", "jfif", "jpe", "jpeg", "jpg", "png", "tif", "tiff", "ico"};

        return Arrays.asList(suffixs).contains(suffix);
    }

}
