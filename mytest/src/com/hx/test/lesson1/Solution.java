package com.hx.test.lesson1;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by yimilan on 2015/11/21.
 */
public class Solution {
    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        int temp = 0; //½øÎ»
        ListNode lHead = null;
        ListNode lTemp = null;
        while(l1 != null || l2 != null){
            int val1 = 0, val2 = 0;
            if(l1 != null){
                val1 = l1.val;
            }
            if (l2 != null){
                val2 = l2.val;
            }

            if(lHead == null){
                lHead = new ListNode((val1 +val2 + temp) % 10);
                lTemp = lHead;
                temp = (val1 +val2 + temp) / 10;
            }else{
                ListNode ltemp = new ListNode((val1 +val2 + temp) % 10);
                temp = (val1 +val2 + temp) / 10;
                lTemp.next = ltemp;
                lTemp = lTemp.next;
            }
            if(l1 != null){
                l1 = l1.next;
            }
            if(l2 != null){
                l2 = l2.next;
            }
        }
        if(temp > 0){
            lTemp.next = new ListNode(temp);
        }
        return lHead;
    }

    public static void main(String[] args){
        ListNode l1 = new ListNode(5);
        ListNode l2 = new ListNode(5);
        ListNode l3 = addTwoNumbers(l1, l2);
        while(l3 != null){
            System.out.println(l3.val);
            l3 = l3.next;
        }
    }

    static class ListNode {
        int val;
        ListNode next;

        public ListNode () {
        }

        public ListNode (int n) {
            val = n;
        }
    }
}
