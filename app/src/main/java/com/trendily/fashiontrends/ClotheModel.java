package com.trendily.fashiontrends;

import android.net.Uri;

public class ClotheModel {


    String CategoryPrice;
    String ImageURL;
    String MemberPhone;
    String  PostCode;
    String MemberCity;
    String CategoryDescription;
    String ShopName;

    public ClotheModel() {
    }

    public ClotheModel(String categoryPrice, String imageURL, String memberPhone, String postCode, String memberCity, String categoryDescription, String shopName) {
        CategoryPrice = categoryPrice;
        ImageURL = imageURL;
        MemberPhone = memberPhone;
        PostCode = postCode;
        MemberCity = memberCity;
        CategoryDescription = categoryDescription;
        ShopName = shopName;
    }

    public String getCategoryPrice() {
        return CategoryPrice;
    }

    public void setCategoryPrice(String categoryPrice) {
        CategoryPrice = categoryPrice;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public String getMemberCity() {
        return MemberCity;
    }

    public void setMemberCity(String memberCity) {
        MemberCity = memberCity;
    }

    public String getShopName() {
        return ShopName;
    }

    public void setShopName(String shopName) {
        ShopName = shopName;
    }

    public String getMemberPhone() {
        return MemberPhone;
    }

    public void setMemberPhone(String memberPhone) {
        MemberPhone = memberPhone;
    }

    public String getPostCode() {
        return PostCode;
    }

    public void setPostCode(String postCode) {
        PostCode = postCode;
    }

    public String getCategoryDescription() {
        return CategoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        CategoryDescription = categoryDescription;
    }
}
