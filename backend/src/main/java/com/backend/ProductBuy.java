package com.backend;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;


/**
 * Created by Pablo on 08/03/2016.
 */
@Entity
public class ProductBuy {

    @Id
    private String nameProduct;
    private String image;
    private int units;
    private String kindQuantity;

    public ProductBuy() {
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
        this.image = image;
    }

    public String getKindQuantity() {
        return kindQuantity;
    }

    public void setKindQuantity(String kindQuantity) {
        this.kindQuantity = kindQuantity;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }
}
