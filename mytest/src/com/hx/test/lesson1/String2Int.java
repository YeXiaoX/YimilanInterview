package com.hx.test.lesson1;

/**
 * Created by yimilan on 2015/11/21.
 */
public class String2Int {
    public static int myAtoi(String str) {
        char[] cArray = str.toCharArray();
        int res = 0;
        int clen = cArray.length;
        for(int i=0; i<clen; i++){
            int con = cArray[i] - 48;
            con = con > 0 ? con : 0;
            res += con * Math.pow(10, clen-i-1);
        }
        return res;
    }

    public static void main(String[] args){
        String s = "-1";
        System.out.println(myAtoi(s));
    }
}
