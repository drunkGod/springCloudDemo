package com.jvxb.manage;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class Test extends Thread{
static int i;
    public static void main(String[] args) {
        Test t = new Test();
        t.test();
        System.out.println(t.test());
    }

    private int test() {
        i++;
        return i;
    }

//    @org.junit.Test
//    public void testTwo(){
//        System.out.println("test hello 2");
//    }

}
