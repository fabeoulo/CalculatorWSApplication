/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.advantech.webapi.model;

/**
 *
 * @author Justin.Yeh
 */
public class WaTagNode {

    private String Name;
    private int Value;
    private int Quality;

    public WaTagNode() {
    }

    public WaTagNode(String Name, int Value) {
        this.Name = Name;
        this.Value = Value;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public int getValue() {
        return Value;
    }

    public void setValue(int Value) {
        this.Value = Value;
    }

    public int getQuality() {
        return Quality;
    }

    public void setQuality(int Quality) {
        this.Quality = Quality;
    }
}
