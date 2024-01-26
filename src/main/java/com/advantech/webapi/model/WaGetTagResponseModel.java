/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.advantech.webapi.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Justin.Yeh
 */
public class WaGetTagResponseModel {

    private ResultInfo Result;
    
    private List<WaTagNode> Values;

    public WaGetTagResponseModel() {
        this.Values = new ArrayList<>();
    }

    public ResultInfo getResult() {
        return Result;
    }

    public void setResult(ResultInfo Result) {
        this.Result = Result;
    }

    public List<WaTagNode> getValues() {
        return Values;
    }

    public void setValues(List<WaTagNode> Values) {
        this.Values = Values;
    }

    public class ResultInfo {

        private int Ret;

        private int Total;

        public int getRet() {
            return Ret;
        }

        public void setRet(int Ret) {
            this.Ret = Ret;
        }

        public int getTotal() {
            return Total;
        }

        public void setTotal(int Total) {
            this.Total = Total;
        }

    }

}
