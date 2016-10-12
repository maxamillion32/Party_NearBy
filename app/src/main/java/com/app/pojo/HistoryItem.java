package com.app.pojo;

/**
 * Created by ram on 27/07/16.
 */
public class HistoryItem {
    String title;
    String address;
    String date;
    String time;
    String ticketNo;
    String price;
    String entryType;
    String img;

    public String getDiscount() {
        return discount;
    }

    String discount;

    public HistoryItem(String title, String address, String date, String time, String ticketNo, String price, String entryType, String img, String discount) {
        this.title = title;
        this.address = address;
        this.date = date;
        this.time = time;
        this.ticketNo = ticketNo;
        this.price = price;
        this.entryType = entryType;
        this.img = img;
        this.discount = discount;
    }


    public String getTitle() {
        return title;
    }

    public String getAddress() {
        return address;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getTicketNo() {
        return ticketNo;
    }

    public String getPrice() {
        return price;
    }

    public String getEntryType() {
        return entryType;
    }

    public String getImg() {
        return img;
    }
}
