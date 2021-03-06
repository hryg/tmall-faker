package com.tmall.bean;

public class User {
    private int id;
    private String name;
    private String password;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取用户匿名名称
     * @return
     */
    public String getAnonymousName(){
        if (null == name) {
            return null;
        }

        if (name.length() <= 1) {
            return "*";
        }

        if (name.length() == 2) {
            return name.substring(0, 1) + "*";
        }

        char[] nameChars = name.toCharArray();
        for (int i = 1; i < name.length() - 1; i++) {
            nameChars[i] = '*';
        }
        return new String(nameChars);
    }
}
