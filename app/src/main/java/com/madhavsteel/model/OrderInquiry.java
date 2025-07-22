package com.madhavsteel.model;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderInquiry implements Parcelable {

    String orderInquiryId, orderId, userId, userName, userContact, sizeId, sizeTitle, catId, catTitle, quantity, measurement, productId, productTitle, productImage,
            productThumbImage, productMediumImage, price, tonnePrice, kgPrice, differentPrice, orderStatus, status, dateTimeAdded;

    public OrderInquiry () {}

    protected OrderInquiry(Parcel in) {
        orderInquiryId = in.readString();
        orderId = in.readString();
        userId = in.readString();
        userName = in.readString();
        userContact = in.readString();
        sizeId = in.readString();
        sizeTitle = in.readString();
        catId = in.readString();
        catTitle = in.readString();
        quantity = in.readString();
        measurement = in.readString();
        productId = in.readString();
        productTitle = in.readString();
        productImage = in.readString();
        productThumbImage = in.readString();
        productMediumImage = in.readString();
        price = in.readString();
        tonnePrice = in.readString();
        kgPrice = in.readString();
        differentPrice = in.readString();
        orderStatus = in.readString();
        status = in.readString();
        dateTimeAdded = in.readString();
    }

    public static final Creator<OrderInquiry> CREATOR = new Creator<OrderInquiry>() {
        @Override
        public OrderInquiry createFromParcel(Parcel in) {
            return new OrderInquiry(in);
        }

        @Override
        public OrderInquiry[] newArray(int size) {
            return new OrderInquiry[size];
        }
    };

    public String getOrderInquiryId() {
        return orderInquiryId;
    }

    public void setOrderInquiryId(String orderInquiryId) {
        this.orderInquiryId = orderInquiryId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserContact() {
        return userContact;
    }

    public void setUserContact(String userContact) {
        this.userContact = userContact;
    }

    public String getSizeId() {
        return sizeId;
    }

    public void setSizeId(String sizeId) {
        this.sizeId = sizeId;
    }

    public String getSizeTitle() {
        return sizeTitle;
    }

    public void setSizeTitle(String sizeTitle) {
        this.sizeTitle = sizeTitle;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getCatTitle() {
        return catTitle;
    }

    public void setCatTitle(String catTitle) {
        this.catTitle = catTitle;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductThumbImage() {
        return productThumbImage;
    }

    public void setProductThumbImage(String productThumbImage) {
        this.productThumbImage = productThumbImage;
    }

    public String getProductMediumImage() {
        return productMediumImage;
    }

    public void setProductMediumImage(String productMediumImage) {
        this.productMediumImage = productMediumImage;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTonnePrice() {
        return tonnePrice;
    }

    public void setTonnePrice(String tonnePrice) {
        this.tonnePrice = tonnePrice;
    }

    public String getKgPrice() {
        return kgPrice;
    }

    public void setKgPrice(String kgPrice) {
        this.kgPrice = kgPrice;
    }

    public String getDifferentPrice() {
        return differentPrice;
    }

    public void setDifferentPrice(String differentPrice) {
        this.differentPrice = differentPrice;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDateTimeAdded() {
        return dateTimeAdded;
    }

    public void setDateTimeAdded(String dateTimeAdded) {
        this.dateTimeAdded = dateTimeAdded;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(orderInquiryId);
        dest.writeString(orderId);
        dest.writeString(userId);
        dest.writeString(userName);
        dest.writeString(userContact);
        dest.writeString(sizeId);
        dest.writeString(sizeTitle);
        dest.writeString(catId);
        dest.writeString(catTitle);
        dest.writeString(quantity);
        dest.writeString(measurement);
        dest.writeString(productId);
        dest.writeString(productTitle);
        dest.writeString(productImage);
        dest.writeString(productThumbImage);
        dest.writeString(productMediumImage);
        dest.writeString(price);
        dest.writeString(tonnePrice);
        dest.writeString(kgPrice);
        dest.writeString(differentPrice);
        dest.writeString(orderStatus);
        dest.writeString(status);
        dest.writeString(dateTimeAdded);
    }
}
