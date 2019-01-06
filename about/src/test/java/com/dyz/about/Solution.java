package com.dyz.about;

import java.util.LinkedList;
import java.util.List;

public class Solution {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        List<ListNode> longList = null;
        List<ListNode> shortList = null;
        List<ListNode> list1 = new LinkedList<ListNode>();
        List<ListNode> list2 = new LinkedList<ListNode>();
        list1.add(l1);
        list2.add(l2);
        while (l1.next != null) {
            list1.add(l1.next);
            l1 = l1.next;
        }
        while (l2.next != null) {
            list2.add(l2.next);
            l2 = l2.next;
        }
        if (list1.size() >= list2.size()) {
            longList = list1;
            shortList = list2;
        } else {
            longList = list2;
            shortList = list1;
        }
        int carryBit = 0;
        for (int i = 0; i < longList.size(); i++) {
            if (i >= shortList.size()) {
                break;
            }
            ListNode listNode = new ListNode(0);
            listNode.val = (longList.get(i).val + shortList.get(i).val + carryBit) % 10;
            carryBit = (longList.get(i).val + shortList.get(i).val + carryBit) / 10;
            longList.set(i, listNode);
        }

        for (int i = 0; i < longList.size() - 1; i++) {
            longList.get(i).next = longList.get(i + 1);
        }
        return longList.get(0);
    }
    public static void main(String[] args) {
        ListNode listNode1 = new ListNode(2);
        listNode1.next = new ListNode(4);
        listNode1.next.next = new ListNode(3);
        ListNode listNode2 = new ListNode(5);
        listNode2.next = new ListNode(6);
        listNode2.next.next = new ListNode(4);
        new Solution().addTwoNumbers(listNode1, listNode2);
    }
}

// Definition for singly-linked list.
 class ListNode {
    int val;
    ListNode next;

    ListNode(int x) {
        val = x;
    }
}

