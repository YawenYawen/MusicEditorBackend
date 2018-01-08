package com.example.demo.domain;

import javax.validation.constraints.NotNull;


import javax.persistence.*;

@Entity
@Table(name = "USER", indexes = {@Index(name = "idx_user_username", columnList = "username", unique = true),@Index(name = "idx_user_nickname", columnList = "nickname", unique = true)})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String username;

    @NotNull
    @Column(unique = true)
    private String nickname;

    @NotNull
    private String password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!id.equals(user.id)) return false;
        if (!username.equals(user.username)) return false;
        if (!nickname.equals(user.nickname)) return false;
        return password.equals(user.password);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + username.hashCode();
        result = 31 * result + nickname.hashCode();
        result = 31 * result + password.hashCode();
        return result;
    }
}
