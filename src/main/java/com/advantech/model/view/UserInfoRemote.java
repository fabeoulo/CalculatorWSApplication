/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model.view;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.Immutable;

/**
 *
 * @author Wei.Cheng
 * vw_UserInfoRemote
 */
@Entity
@Immutable
@Table(name = "vw_UserInfoRemote")
public class UserInfoRemote implements Serializable {

    private String jobnumber;
    private String name;
    private String sitefloor;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "jobnumber", unique = true, updatable = false)
    public String getJobnumber() {
        return jobnumber;
    }

    public void setJobnumber(String jobnumber) {
        this.jobnumber = jobnumber;
    }

    @Column(name = "name", updatable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "sitefloor", updatable = false)
    public String getSitefloor() {
        return sitefloor;
    }

    public void setSitefloor(String sitefloor) {
        this.sitefloor = sitefloor;
    }

}