package com.study.inflreanjpa1.service;

import com.study.inflreanjpa1.domain.Delivery;
import com.study.inflreanjpa1.domain.Member;
import com.study.inflreanjpa1.domain.Order;
import com.study.inflreanjpa1.domain.OrderItem;
import com.study.inflreanjpa1.domain.item.Item;
import com.study.inflreanjpa1.repository.ItemRepository;
import com.study.inflreanjpa1.repository.MemberRepository;
import com.study.inflreanjpa1.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public Long order(Long memberId, Long itemId, int count) {

        // 앤티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        //주분정보 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        //주분 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        //주문 저장
        orderRepository.save(order);
        // order 만 저장하는 이유 👇🏾
        // cascade = Cascade.ALL : 연관관계가 패핑된 객체까지 영속시킴

        return order.getId();
    }

    @Transactional
    public void cancel(Long orderId) {
        //주문 조회
        Order order = orderRepository.findOne(orderId);

        //주문 취소
        order.cancel();
    }
}
