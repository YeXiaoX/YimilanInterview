package com.hx.test.lesson1;

import java.util.*;

/**
 * Created by yimilan on 2015/11/21.
 */
public class Test4 {
    public static int lengthOfLongestSubstring(String s) {
        int slen = s.length();
        boolean[] flag = new  boolean[256];
        int maxlen = 0;
        int start=0;
        char[] cArray = s.toCharArray();
        for(int i=0; i<slen; i++){
            char ctemp = cArray[i];
            if(flag[ctemp]){
                maxlen = Math.max(maxlen, i-start);
                for(int j=start; j<i; j++){
                    if(cArray[j] == ctemp){
                        start = j + 1;
                        break;
                    }
                    flag[cArray[j]] = false;
                }
            }else {
                flag[ctemp] = true;
            }
        }
        return Math.max(maxlen, cArray.length - start);
    }

    public static void main(String[] args){
        String a = "dvdfdcv";
        System.out.println(lengthOfLongestSubstring(a));
    }
}
