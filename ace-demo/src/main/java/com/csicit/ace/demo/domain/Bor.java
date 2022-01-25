package com.csicit.ace.demo.domain;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author shanwj
 * @date 2019/10/11 16:12
 */
@Entity
public class Bor {
    @Id
    private String id;
    @Basic
    private String name;
    @Basic
    private int age;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
