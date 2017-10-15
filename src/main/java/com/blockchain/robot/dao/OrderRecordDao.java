package com.blockchain.robot.dao;

import com.blockchain.robot.entity.db.OrderRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderRecordDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void save(OrderRecord record) {
        mongoTemplate.save(record);
    }

//    public void update(OrderRecord record) {
//        Query query = new Query(Criteria.where("id").is(record.getId()));
//        Update update = new Update()
//                .set("sellOrderId", record.getSellOrderId())
//                .set("sellPrice", record.getSellPrice());
//
//        //更新查询返回结果集的第一条
//        mongoTemplate.updateFirst(query, update, OrderRecord.class);
//    }

    public void deleteById(Long id) {
        Query query = new Query(Criteria.where("id").is(id));
        mongoTemplate.remove(query, OrderRecord.class);
    }

    public List<OrderRecord> findAll() {
        return mongoTemplate.findAll(OrderRecord.class);
    }

    public List<OrderRecord> findAllBySymbol(String symbol, String strategy) {
        Query query = new Query(
                Criteria.where("symbol").is(symbol)
                        .and("strategy").is(strategy)
                        .andOperator(
                                Criteria.where("buyStatus").is(0).orOperator(
                                        Criteria.where("sellStatus").is(0))));
        return mongoTemplate.find(query, OrderRecord.class);
    }

    public List<OrderRecord> findAllByStrategy(String strategy) {
        Query query = new Query(
                Criteria.where("strategy").is(strategy));
        return mongoTemplate.find(query, OrderRecord.class);
    }

    public List<OrderRecord> findOrderUnFinish(String symbol, String strategy) {
        Query query = new Query(
                Criteria
                        .where("symbol").is(symbol)
                        .and("strategy").is(strategy)
                        .and("status").is(0));
        return mongoTemplate.find(query, OrderRecord.class);
    }
}