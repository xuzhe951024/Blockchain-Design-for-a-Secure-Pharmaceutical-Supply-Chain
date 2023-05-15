package com.rbpsc.ctp;

import com.rbpsc.ctp.api.entities.base.BaseEntity;
import com.rbpsc.ctp.api.entities.supplychain.roles.Consumer;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;
import java.util.Random;

public class PlayGround {
    public static void main(String[] args) {
        Queue<String> queue = new ArrayDeque<>(Arrays.asList("a", "b", "c"));
        System.out.printf("queue poll = %s%n", queue.poll());
        queue.remove();
        System.out.printf("queue = %s%n", queue);

        System.out.println("queue class name: " + queue.getClass().getName());

        Consumer consumer = new Consumer();
        BaseEntity<String> entity = (BaseEntity<String>) consumer;
        System.out.println("entity class:" + entity.getClass().getName());
    }
}