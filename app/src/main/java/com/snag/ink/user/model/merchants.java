package com.snag.ink.user.model;


import androidx.annotation.Keep;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
@Keep
public class merchants {
    private String merchantname;
    private String merchantimage;
    private String merchantdescription;

    public merchants() {
    }

    public merchants(String merchantname, String merchantimage, String merchantdescription) {
        this.merchantname = merchantname;
        this.merchantimage = merchantimage;
        this.merchantdescription = merchantdescription;
    }

    public String getMerchantname() {
        return merchantname;
    }

    public void setMerchantname(String merchantname) {
        this.merchantname = merchantname;
    }

    public String getMerchantimage() {
        return merchantimage;
    }

    public void setMerchantimage(String merchantimage) {
        this.merchantimage = merchantimage;
    }

    public String getMerchantdescription() {
        return merchantdescription;
    }

    public void setMerchantdescription(String merchantdescription) {
        this.merchantdescription = merchantdescription;
    }
}
