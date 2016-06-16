package com.app.pojo;

/**
 * Created by ram on 29/05/16.
 */
public class EventListItem {

    String eventId;
    String eventName;
    String address;
    String dateTime;
    String price;
    String thumbnail;

    public String getThumbnail() {
        return thumbnail;
    }

    public EventListItem(String eventId, String eventName, String address, String dateTime, String price, String thumbnail) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.address = address;
        this.dateTime = dateTime;
        this.price = price;
        this.thumbnail = thumbnail;
    }

    public String getEventId() {
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
