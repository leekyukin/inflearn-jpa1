package com.study.inflreanjpa1.domain;

import lombok.Getter;
import lombok.Setter;
import net.bytebuddy.matcher.FilterableList;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();


}
