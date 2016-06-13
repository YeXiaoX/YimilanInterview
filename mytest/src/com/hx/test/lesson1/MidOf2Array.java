package com.hx.test.lesson1;

/**
 * Created by yimilan on 2015/11/21.
 */
public class MidOf2Array {
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int len1 = nums1.length;
        int len2 = nums2.length;
        int total = len1 + len2;
        if(total % 2==0){
            return (findMid(nums1, nums2, total / 2)+findMid(nums1,nums2,total/2+1))/2.0;
        } else {
            return findMid(nums1, nums2, total / 2 + 1);
        }
    }

    private int findMid(int[] nums1, int[] nums2, int k){
        int p = 0, q = 0;
        for(int i = 0; i < k - 1; i++){
            if(p>=nums1.length && q<nums2.length){
                q++;
            } else if(q>=nums2.length && p<nums1.length){
                p++;
            } else if(nums1[p]>nums2[q]){
                q++;
            } else {
                p++;
            }
        }
        if(p>=nums1.length) {
            return nums2[q];
        } else if(q>=nums2.length) {
            return nums1[p];
        } else {
            return Math.min(nums1[p],nums2[q]);
        }
    }

    public static void main(String[] args){
        int[] num1 = new int[]{1};
        int[] num2 = new int[]{1,2,3};
        MidOf2Array midOf2Array = new MidOf2Array();
        System.out.println(midOf2Array.findMedianSortedArrays(num1, num2));
    }
}
