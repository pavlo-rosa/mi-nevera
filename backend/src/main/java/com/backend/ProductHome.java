package com.backend;

import com.googlecode.objectify.annotation.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pablo on 03/04/2016.
 */
@Entity
public class ProductHome extends Product{

    private int units;
    private List<ProductDate> infoUnits;


    public ProductHome() {
        infoUnits = new ArrayList<ProductDate>();
    }

    public ProductHome(Product product, ProductDate date) {
        this.setNameProduct(product.getNameProduct());
        this.getInfoUnits().add(date);
    }

    public int getUnits() {
            int cont=0;
            for (int i = 0; i < infoUnits.size(); i++){
                cont = cont + infoUnits.get(i).getNumber();
            }
            return cont;
        }

    public List<ProductDate> getInfoUnits() {
        return infoUnits;
    }

    public void setInfoUnits(List<ProductDate> infoDates) {
        this.infoUnits = infoDates;
    }


}
