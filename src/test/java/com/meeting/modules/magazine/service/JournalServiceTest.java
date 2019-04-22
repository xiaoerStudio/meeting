package com.meeting.modules.magazine.service;


import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class JournalServiceTest {

    @Test
    public void hashMapTest() {
        long star = System.currentTimeMillis();

        Set<Integer> hashSet = new HashSet<>();
        for (int i = 0; i < 100000000; i++) {
            hashSet.add(i);
        }
        Assert.assertTrue(hashSet.contains(1));
        Assert.assertTrue(hashSet.contains(2));
        Assert.assertTrue(hashSet.contains(3));
        long end = System.currentTimeMillis();
        System.out.println("执行时间：" + (end - star));

    }


}