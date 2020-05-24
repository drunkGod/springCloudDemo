package com.jvxb.beauty;

public class Test {

    public static void main(String[] args) {
        String clientValue = "2020-04-03";
        System.out.println(clientValue.length());
        String lastDate = clientValue.substring(clientValue.length()-10);
        String lastDate2 = clientValue.toString().replaceFirst("(\\d{4}\\-\\d{2}\\-\\d{2},?)+(.+)", "$2");
        System.out.println(lastDate);
        System.out.println(lastDate2);

        for (int i=1;i<300;i++){
            System.out.println("update beauty set name = 'm"+i+"' where id =" +i+";");
        }

    }
}
