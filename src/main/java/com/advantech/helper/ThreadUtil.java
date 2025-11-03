/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.advantech.helper;

/**
 *
 * @author Justin.Yeh
 */
public class ThreadUtil {

    public static String currentMethod() {
        return Thread.currentThread().getStackTrace()[2].getMethodName(); // [2] means caller.
    }
}
