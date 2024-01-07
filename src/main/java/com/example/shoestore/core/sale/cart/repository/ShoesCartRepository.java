package com.example.shoestore.core.sale.cart.repository;

import com.example.shoestore.core.sale.cart.model.entity.ShoesCart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ShoesCartRepository {

    @Autowired
    private RedisTemplate<Long, ShoesCart> redisTemplate;

    public void save(Long key, ShoesCart values) {
        redisTemplate.opsForList().rightPush(key, values);
    }

    public List<ShoesCart> findAll(Long key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    public ShoesCart findById(Long key, Long id) {
        List<ShoesCart> shoesCartList = this.findAll(key);

        return shoesCartList.stream()
                .filter(shoesCart -> shoesCart.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<ShoesCart> findByList(Long key, List<Long> listId) {
        List<ShoesCart> listShoesCart = new ArrayList<>();

        for (Long id : listId) {
            ShoesCart shoesCart = this.findById(key, id);
            if (shoesCart != null) {
                listShoesCart.add(shoesCart);
            }
        }

        return listShoesCart;
    }

    public void updateQuantity(Long key, Long id, Integer newQuantity, BigDecimal newPrice) {
        List<ShoesCart> list = findAll(key);

        list.stream()
                .filter(item -> item.getId().equals(id))
                .forEach(item -> {
                    item.setQuantity(newQuantity);
                    item.setTotalPrice(newPrice);
                    int index = list.indexOf(item);
                    redisTemplate.opsForList().set(key, index, item);
                });
    }

    public void deleteItemsInValue(Long key, Long id) {
        List<ShoesCart> list = findAll(key);
        if (list.size() == 1) {
            this.delete(key);
        } else {

            list.forEach(items -> {
                if (items.getId().equals(id)) {
                    redisTemplate.opsForList().remove(key, 1, items);
                }
            });
        }
    }

    public void deleteListItems(Long key, List<Long> ids) {
        List<ShoesCart> list = this.findByList(key, ids);

        list.forEach(items -> {
            redisTemplate.opsForList().remove(key, 1, items);
        });
    }


    public void delete(Long key) {
        redisTemplate.delete(key);
    }

    public boolean existsById(Long key, Long id) {
        List<ShoesCart> list = findAll(key);
        return list.stream().anyMatch(item -> item.getId().equals(id));
    }
}

