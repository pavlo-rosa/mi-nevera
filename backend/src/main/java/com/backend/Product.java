package com.backend;


import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;


/**
 * Created by Pablo on 19/04/2016.
 */
@Entity
public class Product {

    @Id
    private String nameProduct;
    private Long barcode;
    private String image;
    private boolean habitual;
    private int stockQuantity;
    private String kindQuantity;

    public Product() {
    }

    public Long getBarcode() {
        return barcode;
    }

    public void setBarcode(Long barcode) {
        this.barcode = barcode;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        String subString = image.substring(0,4);
        String subString2 = image.substring(0,19);
        if (subString.equals("http") ||subString2.equals("NeedStringToBitMap#")){
            this.image = image;
        }else{
            this.image = "NeedStringToBitMap#"+image;
        }
    }

    public boolean isHabitual() {
        return habitual;
    }

    public void setHabitual(boolean habitual) {
        this.habitual = habitual;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getKindQuantity() {
        return kindQuantity;
    }

    public void setKindQuantity(String kindQuantity) {
        this.kindQuantity = kindQuantity;
    }
}
