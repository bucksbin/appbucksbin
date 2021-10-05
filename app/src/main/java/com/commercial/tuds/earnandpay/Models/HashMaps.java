package com.commercial.tuds.earnandpay.Models;

import java.util.HashMap;

public class HashMaps {

    static HashMap<String, Integer> topupOperatorCodes = new HashMap();
    static HashMap<String, Integer> specialRechargeOperatorCodes = new HashMap();
    static HashMap<String, Integer> postpaidOperatorCodes = new HashMap();
    static HashMap<String, Integer> dthOperatorCodes = new HashMap();
    static HashMap<String, Integer> gasProviderCodes = new HashMap();
    static HashMap<String, Integer> electricityProviderCodes = new HashMap();

    public static HashMap<String, Integer> getTopupOperatorCodes() {
        topupOperatorCodes.put("Airtel",1);
        topupOperatorCodes.put("Idea",3);
        topupOperatorCodes.put("Bsnl",4);
        topupOperatorCodes.put("Tata Docomo",7);
        topupOperatorCodes.put("Tata Indicom",9);
        topupOperatorCodes.put("Vodafone",10);
        topupOperatorCodes.put("Loop Mobile",14);
        topupOperatorCodes.put("MTNL DL",17);
        topupOperatorCodes.put("MTNL Mumbai",19);
        topupOperatorCodes.put("Tata Walky",21);
        topupOperatorCodes.put("Reliance Jio",93);
        return topupOperatorCodes;
    }

    public static HashMap<String, Integer> getSpecialRechargeOperatorCodes() {
        specialRechargeOperatorCodes.put("Airtel",1);
        specialRechargeOperatorCodes.put("Idea",3);
        specialRechargeOperatorCodes.put("Bsnl",5);
        specialRechargeOperatorCodes.put("Tata Docomo",8);
        specialRechargeOperatorCodes.put("Tata Indicom",9);
        specialRechargeOperatorCodes.put("Vodafone",10);
        specialRechargeOperatorCodes.put("Loop Mobile",14);
        specialRechargeOperatorCodes.put("MTNL DL",18);
        specialRechargeOperatorCodes.put("MTNL Mumbai",20);
        specialRechargeOperatorCodes.put("Tata Walky",21);
        specialRechargeOperatorCodes.put("Reliance Jio",93);
        return specialRechargeOperatorCodes;
    }
    public static HashMap<String, Integer> getPostpaidOperatorCodes() {
        postpaidOperatorCodes.put("Airtel",31);
        postpaidOperatorCodes.put("Idea",33);
        postpaidOperatorCodes.put("Bsnl",32);
        postpaidOperatorCodes.put("Tata Docomo",37);
        postpaidOperatorCodes.put("Tata Indicom",38);
        postpaidOperatorCodes.put("Vodafone",30);
        postpaidOperatorCodes.put("Loop Mobile",0);
        postpaidOperatorCodes.put("MTNL DL",0);
        postpaidOperatorCodes.put("MTNL Mumbai",0);
        postpaidOperatorCodes.put("Tata Walky",0);
        postpaidOperatorCodes.put("Reliance Jio",34);
        return postpaidOperatorCodes;
    }
    public static HashMap<String, Integer> getGasProviderCodes() {
        gasProviderCodes.put("Adani Gas - Gujrat",48);
        gasProviderCodes.put("Gujarat Gas",49);
        gasProviderCodes.put("Indraprastha Gas",50);
        return gasProviderCodes;
    }
    public static HashMap<String, Integer> getDthOperatorCodes() {
        dthOperatorCodes.put("Videocon DTH",28);
        dthOperatorCodes.put("Sun DTH",26);
        dthOperatorCodes.put("Big Tv DTH",24);
        dthOperatorCodes.put("Tata Sky DTH",27);
        dthOperatorCodes.put("Airtel DTH",23);
        dthOperatorCodes.put("Dish DTH",25);
        return dthOperatorCodes;
    }
    public static HashMap<String, Integer> getElectricityProviderCodes() {
        electricityProviderCodes.put("Ajmer Vidyut Vitran Nigam - RAJASTHAN",52);
        electricityProviderCodes.put("APDCL (RAPDR) - ASSAM",53);
        electricityProviderCodes.put("BESCOM - BENGALURU",54);
        electricityProviderCodes.put("BEST Undertaking - MUMBAI",55);
        electricityProviderCodes.put("BSES Rajdhani - DELHI",56);
        electricityProviderCodes.put("BSES Yamuna - DELHI",57);
        electricityProviderCodes.put("CESC - WEST BENGAL",58);
        electricityProviderCodes.put("DHBVN - HARYANA",60);
        electricityProviderCodes.put("DNHPDCL - DADRA and NAGAR HAVELI",61);
        electricityProviderCodes.put("India Power - BIHAR",62);
        electricityProviderCodes.put("Jaipur Vidyut Vitran Nigam - RAJASTHAN",63);
        electricityProviderCodes.put("Jamshedpur Utilities and Services (JUSCO)",64);
        electricityProviderCodes.put("Jodhpur Vidyut Vitran Nigam - RAJASTHAN",65);
        electricityProviderCodes.put("Madhya Kshetra Vitaran - MADHYA PRADESH",66);
        electricityProviderCodes.put("Noida Power - NOIDA",68);
        electricityProviderCodes.put("Tata Power - DELHI",74);
        electricityProviderCodes.put("TSECL - TRIPURA",76);
        electricityProviderCodes.put("Paschim Kshetra Vitaran - MADHYA PRADESH",70);
        return electricityProviderCodes;
    }
}
