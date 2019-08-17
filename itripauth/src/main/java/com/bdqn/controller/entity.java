package com.bdqn.controller;

public class entity {
    private  int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public entity(int id, String name) {
        this.id = id;
        this.name = name;
    }

    private  String name;
}
