package com.yousef_shora.omniaclient.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OfferDocument implements Serializable {
    public List<Offer> offers_array ;
    public OfferDocument(){
        offers_array = new ArrayList<>();
    }
}
