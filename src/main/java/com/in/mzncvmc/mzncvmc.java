package com.in.mzncvmc;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class mzncvmc {
    public static void main(String[] args){

        System.out.println(LocalDateTime.now());
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));

    }
}
