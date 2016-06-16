package com.app.pojo;

/**
 * Created by ram on 29/05/16.
 */
public class EventListItem {

    int eventId;
    String eventName;
    String address;
    String dateTime;
    String price;

    public EventListItem(int eventId, String eventName, String address, String dateTime, String price) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.address = address;
        this.dateTime = dateTime;
        this.price = price;
    }

    public int getEventId() {
        return eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public String getAddress() {
        return address;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getPrice() {
        return price;
    }
}
