package com.advantech.model.db1;
// Generated 2017/4/7 下午 02:26:06 by Hibernate Tools 4.3.1

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * UserType generated by hbm2java
 */
@Entity
@Table(name = "Unit")
public class Unit implements java.io.Serializable {

    private int id;
    private String name;

    @JsonIgnore
    private Set<User> users = new HashSet<User>(0);

    @JsonIgnore
    private Set<ActionCodeResponsor> actionCodeMappings = new HashSet<ActionCodeResponsor>(0);

    public Unit() {
    }

    public Unit(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Unit(int id, String name, Set<User> users) {
        this.id = id;
        this.name = name;
        this.users = users;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "[name]", nullable = false, length = 50)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "unit")
    public Set<User> getUsers() {
        return this.users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "unit")
    public Set<ActionCodeResponsor> getActionCodeMappings() {
        return actionCodeMappings;
    }

    public void setActionCodeMappings(Set<ActionCodeResponsor> actionCodeMappings) {
        this.actionCodeMappings = actionCodeMappings;
    }

}
