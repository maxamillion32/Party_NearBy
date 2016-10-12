package com.app.pojo;

/**
 * Created by ram on 12/06/16.
 */
public class BookingItem {
    String event_id;
    String event_name;
    String entry_time;
    String booking_id;
    String entry_type;
    String event_address;
    String event_image;

    public BookingItem(String event_id, String event_name, String entry_time, String booking_id, String entry_type, String event_address, String event_image) {
        this.event_id = event_id;
        this.event_name = event_name;
        this.entry_time = entry_time;
        this.booking_id = booking_id;
        this.entry_type = entry_type;
        this.event_address = event_address;
        this.event_image = event_image;
    }

    public String getEvent_id() {
        return event_id;
    }

    public String getEvent_name() {
        return event_name;
    }

    public String getEntry_time() {
        return entry_time;
    }

    public String getBooking_id() {
        return booking_id;
    }

    public String getEvent_address() {
        return event_address;
    }

    public String getEntry_type() {
        return entry_type;
    }

    public String getEvent_image() {
        return event_image;
    }
}
