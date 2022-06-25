package com.study.inflreanjpa1.service;

import com.study.inflreanjpa1.domain.Address;
import com.study.inflreanjpa1.domain.Member;
import com.study.inflreanjpa1.domain.Order;
import com.study.inflreanjpa1.domain.OrderStatus;
import com.study.inflreanjpa1.domain.item.Book;
import com.study.inflreanjpa1.exception.NotEnoughStockException;
import com.study.inflreanjpa1.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;


    @Test
    public void 상품주문() throws Exception {
        // given
        Member member = createMember();
        Book book = createBook();

        int count = 6;
        // when
        Long orderId = orderService.order(member.getId(), book.getId(), count);

        // then
        Order findOrder = orderRepository.findOne(orderId);

        assertEquals("상품 주문시 상품 상태는 ORDER", OrderStatus.ORDER, findOrder.getStatus());
        assertEquals("주문한 상품 종류 수가 정확해야 한다.", 1, findOrder.getOrderItems().size());
        assertEquals("주문 가격은 수량 * 가격이다.", count * book.getPrice(), findOrder.getTotalPrice());
        assertEquals("주문 수량만큼 재고가 줄어야 한다.", 99 - count, book.getStockQuantity());
    }

    @Test(expected = NotEnoughStockException.class)
    public void  상품주문_재고수량초과() throws Exception {
        // given
        Member member = createMember();
        Book book = createBook();

        int orderCount = 100;
        // when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // then
        fail("재고 수량 부족 예외가 발생해야 한다.");
    }

    @Test
    public void 주문취소() throws Exception {
        // given
        Member member = createMember();
        Book book = createBook();

        int orderCount = 10;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);
        // when
        orderService.cancel(orderId);

        // then
        Order findOrder = orderRepository.findOne(orderId);

        assertEquals("주문상태가 CANCEL 이어야 한다.", OrderStatus.CANCEL, findOrder.getStatus());
        assertEquals("주문 수량이 원복되어야 한다.", 99, book.getStockQuantity());

    }

    private Book createBook() {
        Book book = new Book();
        book.setName("역사의 오른편 옳은편");
        book.setAuthor("벤 샤피로");
        book.setPrice(29000);
        book.setStockQuantity(99);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("이규진");
        member.setAddress(new Address("뿌싼", "백두산 천지", "123-123"));
        em.persist(member);
        return member;
    }

}