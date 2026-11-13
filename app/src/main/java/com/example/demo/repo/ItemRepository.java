package com.example.demo.repo;

import com.example.demo.model.Item;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByQuantityLessThanOrderByQuantityAsc(int threshold);
}
