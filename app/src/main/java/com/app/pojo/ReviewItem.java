package com.app.pojo;

/**
 * Created by ram on 14/06/16.
 */
public class ReviewItem {
    String rId;
    String rName;
    String rDatetime;
    String rStarCount;
    String eThumbnail;
    String rDesc;

    public ReviewItem(String rId, String rName, String rDesc, String rDatetime, String rStarCount, String eThumbnail) {
        this.rId = rId;
        this.rName = rName;
        this.rDatetime = rDatetime;
        this.rStarCount = rStarCount;
        this.eThumbnail = eThumbnail;
        this.rDesc = rDesc;
    }

    public String getrDesc() {
        return rDesc;
    }


    public String getrId() {
        return rId;
    }

    public String getrName() {
        return rName;
    }

    public String getrDatetime() {
        return rDatetime;
    }

    public String getrStarCount() {
        return rStarCount;
    }

    public String geteThumbnail() {
        return eThumbnail;
    }
}
