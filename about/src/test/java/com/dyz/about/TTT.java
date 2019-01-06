package com.dyz.about;

import java.util.*;

public class TTT {
    public static void main(String... arg) {
        System.out.println(arg.length);
        if (arg.length == 0 && new TTT(){{main("a");}} != null) {
            System.out.println("A");
        } else {
            System.out.println("B");
        }
        Integer[] nums = {1,1,2};
        Set<Integer> integerSet = new HashSet<>();
        List<Integer> list = (List<Integer>) Arrays.asList(nums);
//        Collections.addAll(list, Arrays.asList(nums));
        for (Integer num : nums) {
            integerSet.add(num);
        }
        integerSet.toArray(nums);
        for (int i = 0; i < integerSet.size(); i++) {
            System.out.println();
        }
        System.out.println(integerSet.size());
    }
    public int[] get() {
        return new int[]{1,2};
    }
}
