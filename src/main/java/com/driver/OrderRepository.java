package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Repository
public class OrderRepository {
    HashMap<String,Order> orderHashMap;
    HashMap<String,DeliveryPartner> deliveryPartnerHashMap;
    HashMap<String,String> orderDeliveryPartnerHashMap;
    HashMap<String,HashSet<String>> allOrdersOfPartner;

    OrderRepository(){
        orderHashMap=new HashMap<>();
        deliveryPartnerHashMap=new HashMap<>();
        orderDeliveryPartnerHashMap=new HashMap<>();
        allOrdersOfPartner=new HashMap<>();
    }
    public void addOrder(Order order) { //OK
        orderHashMap.put(order.getId(),order);
    }

    public void addPartner(String partnerId) { //OK
        deliveryPartnerHashMap.put(partnerId,new DeliveryPartner(partnerId));
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        if(orderHashMap.containsKey(orderId) && deliveryPartnerHashMap.containsKey(partnerId)){
            HashSet<String> currentOrders = new HashSet<String>();
            if(allOrdersOfPartner.containsKey(partnerId))
                currentOrders = allOrdersOfPartner.get(partnerId);
            currentOrders.add(orderId);
            allOrdersOfPartner.put(partnerId, currentOrders);
            DeliveryPartner partner = deliveryPartnerHashMap.get(partnerId);
            partner.setNumberOfOrders(currentOrders.size());
            orderDeliveryPartnerHashMap.put(orderId, partnerId);
        }
    }

    public DeliveryPartner getPartnerById(String partnerId) { //OK
        return deliveryPartnerHashMap.get(partnerId);
    }

    public Integer getOrderCountByPartnerId(String partnerId) {
        return null;
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        return null;
    }

    public List<String> getAllOrders() { //OK
        return new ArrayList<>(orderHashMap.keySet());
    }

    public Integer getCountOfUnassignedOrders() {
        return null;
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {
        return null;
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {
        return partnerId;
    }

    public void deletePartnerById(String partnerId) {
    }

    public void deleteOrderById(String orderId) {

    }

    public Order getOrderByID(String orderId) { //OK
        return orderHashMap.get(orderId);
    }
}
