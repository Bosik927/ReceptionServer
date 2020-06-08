package com.github.bosik927.database.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "accounts")
public class AccountEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    protected AccountEntity() {
    }

    private AccountEntity(Long accountId, String username, String email, String phoneNumber) {
        this.accountId = accountId;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public Long getAccountId() {
        return this.accountId;
    }

    public String getUsername() {
        return this.username;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public static AccountEntity.Builder builder(){
        return new AccountEntity.Builder();
    }

    static public class Builder {

        private Long accountId;
        private String username;
        private String email;
        private String phoneNumber;


        public Builder withAccountId(Long accountId) {
            this.accountId = accountId;
            return this;
        }

        public Builder withUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder withEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder withPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public AccountEntity build() {
            return new AccountEntity(accountId, username, email, phoneNumber);
        }
    }
}
