package com.study.inflreanjpa1.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderSearch {

    private String memberName;
    private OrderStatus orderStatus;
}
