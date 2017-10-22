package com.blockchain.robot.dao;

import com.blockchain.robot.entity.db.OrderRecordGrid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderRecordGridDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void save(OrderRecordGrid record) {
        mongoTemplate.save(record);
    }

    public void deleteById(Long id) {
        Query query = new Query(Criteria.where("id").is(id));
        mongoTemplate.remove(query, OrderRecordGrid.class);
    }

    public List<OrderRecordGrid> findAll() {
        return mongoTemplate.findAll(OrderRecordGrid.class);
    }

    public List<OrderRecordGrid> findOrderUnFinish(String symbol, String strategy) {
        Query query = new Query(
                Criteria
                        .where("symbol").is(symbol)
                        .and("strategy").is(strategy)
                        .and("status").is(0));
        return mongoTemplate.find(query, OrderRecordGrid.class);
    }
}