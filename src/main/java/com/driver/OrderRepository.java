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
        Integer orderCount = 0;
        if(deliveryPartnerHashMap.containsKey(partnerId)){
            orderCount = deliveryPartnerHashMap.get(partnerId).getNumberOfOrders();
        }
        return orderCount;
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        HashSet<String> orderList = new HashSet<>();
        if(allOrdersOfPartner.containsKey(partnerId)) orderList = allOrdersOfPartner.get(partnerId);
        return new ArrayList<>(orderList);
    }

    public List<String> getAllOrders() { //OK
        return new ArrayList<>(orderHashMap.keySet());
    }

    public Integer getCountOfUnassignedOrders() {
        int countOfOrders = 0;
        List<String> orders =  new ArrayList<>(orderHashMap.keySet());
        for(String orderId: orders){
            if(!orderDeliveryPartnerHashMap.containsKey(orderId)){
                countOfOrders += 1;
            }
        }
        return countOfOrders;
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {
        int hour = Integer.parseInt(time.substring(0, 2));
        int minutes = Integer.parseInt(time.substring(3));
        int t = hour*60 + minutes;

        int countOfOrders = 0;
        if(allOrdersOfPartner.containsKey(partnerId)){
            HashSet<String> orders = allOrdersOfPartner.get(partnerId);
            for(String order: orders){
                if(orderHashMap.containsKey(order)){
                    Order currOrder = orderHashMap.get(order);
                    if(t < currOrder.getDeliveryTime()){
                        countOfOrders += 1;
                    }
                }
            }
        }
        return countOfOrders;
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {
        int time = 0;
        if(allOrdersOfPartner.containsKey(partnerId)){
            HashSet<String> orders = allOrdersOfPartner.get(partnerId);
            for(String order: orders){
                if(orderHashMap.containsKey(order)){
                    Order currOrder = orderHashMap.get(order);
                    time = Math.max(time, currOrder.getDeliveryTime());
                }
            }
        }
        Integer hour = time/60;
        Integer minutes = time%60;
        String hourInString = String.valueOf(hour);
        String minInString = String.valueOf(minutes);
        if(hourInString.length() == 1){
            hourInString = "0" + hourInString;
        }
        if(minInString.length() == 1){
            minInString = "0" + minInString;
        }
        return  hourInString + ":" + minInString;
    }

    public void deletePartnerById(String partnerId) {
        HashSet<String> orders = new HashSet<>();
        if(allOrdersOfPartner.containsKey(partnerId)){
            orders = allOrdersOfPartner.get(partnerId);
            for(String order: orders){
                orderDeliveryPartnerHashMap.remove(order);
            }
            allOrdersOfPartner.remove(partnerId);
        }
        deliveryPartnerHashMap.remove(partnerId);
    }
    public void deleteOrderById(String orderId) {
        if(orderDeliveryPartnerHashMap.containsKey(orderId)){
            String partnerId = orderDeliveryPartnerHashMap.get(orderId);
            HashSet<String> orders = allOrdersOfPartner.get(partnerId);
            orders.remove(orderId);
            allOrdersOfPartner.put(partnerId, orders);
            DeliveryPartner partner = deliveryPartnerHashMap.get(partnerId);
            partner.setNumberOfOrders(orders.size());
        }
        orderHashMap.remove(orderId);
    }

    public Order getOrderByID(String orderId) { //OK
        return orderHashMap.get(orderId);
    }
}
