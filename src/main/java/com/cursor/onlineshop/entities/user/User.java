package com.cursor.onlineshop.entities.user;

import com.cursor.onlineshop.entities.orders.Order;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class User {
    @Id
    @Column(name = "account_id")
    private String accountId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "age")
    private Integer age;

    @Column(name = "phone_number")
    private String phoneNumber;
    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<Order> orders;

    public User(String accountId, String firstName, String lastName, Integer age, String phoneNumber) {
        this.accountId = accountId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.phoneNumber = phoneNumber;
    }

    public User(String accountId) {
        this.accountId = accountId;
    }

    public void addOrder(Order order) {
        orders.add(order);
        order.setUser(this);
    }

    public void removeOrder(Order order) {
        orders.remove(order);
        order.setUser(null);
    }

    @Override
    public String toString() {
        return "User{" +
                "accountId='" + accountId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", orders=" + orders +
                '}';
    }
}
