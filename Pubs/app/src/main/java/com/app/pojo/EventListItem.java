package com.app.pojo;

/**
 * Created by ram on 29/05/16.
 */

/*
{
        "event_lists": [
        {
        "EventList": {
        "id": "1",
        "event_image": "1465805376-WP_20151008_13_19_13_Pro.jpg",
        "event_name": "asdf1",
        "event_address": "adfdsf1",
        "event_time": "sdfsdf1",
        "event_datetime": "sdfsdf1",
        "event_contact_no": "sdfsdf1",
        "event_description": "sdfsdf sdf sdf sdf sdf sdfsdf1",
        "entry_cost": "1251",
        "entry_type": "single1",
        "discount": "201",
        "date_added": "13-June-2016"
        }
        }
        ]
        }*/

public class EventListItem {

    String eventId;
    String eventName;
    String address;
    String dateTime;
    String time;
    String contactNo;
    String desc;
    String entryCost;
    String discount;
    String dateAdded;
    String price;
    String thumbnail;


    public EventListItem(String dateAdded, String discount, String entryCost,
                         String desc, String contactNo, String time, String eventId, String eventName, String address,
                         String dateTime, String price, String thumbnail) {
        this.dateAdded = dateAdded;
        this.discount = discount;
        this.entryCost = entryCost;
        this.desc = desc;
        this.contactNo = contactNo;
        this.time = time;
        this.eventId = eventId;
        this.eventName = eventName;
        this.address = address;
        this.dateTime = dateTime;
        this.price = price;
        this.thumbnail = thumbnail;
    }

    public String getTime() {
        return time;
    }

    public String getContactNo() {
        return contactNo;
    }

    public String getDesc() {
        return desc;
    }

    public String getEntryCost() {
        return entryCost;
    }

    public String getDiscount() {
        return discount;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public String getThumbnail() {
        return thumbnail;
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
