package com.example.demo.service;

import com.example.demo.model.Item;
import com.example.demo.repo.ItemRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ItemService {

    private final ItemRepository repository;

    public ItemService(ItemRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Item> list() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Item get(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(id));
    }

    public Item create(String name, int quantity) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("quantity must not be negative");
        }
        return repository.save(new Item(name.trim(), quantity));
    }

    public Item update(long id, String name, int quantity) {
        Item item = get(id);
        item.setName(name);
        item.setQuantity(quantity);
        return repository.save(item);
    }

    @Transactional(readOnly = true)
    public List<Item> lowStock(int threshold) {
        return repository.findByQuantityLessThanOrderByQuantityAsc(threshold);
    }

    public void delete(long id) {
        if (!repository.existsById(id)) {
            throw new ItemNotFoundException(id);
        }
        repository.deleteById(id);
    }
}
