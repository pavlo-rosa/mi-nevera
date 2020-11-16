package com.backend;

import com.backend.ProductBuy;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pablo on 20/03/2016.
 */
@Entity
public class GroupHome {


    @Id Long id;
    private String password;
    private List<User> usersList;
    private List<ProductBuy> shoppingList;
    private List<ProductHome> homeList;
    private List<Product> productsList;

    public GroupHome() {
        this.id  = null;
        this.usersList = new ArrayList<User>();
        this.shoppingList = new ArrayList<ProductBuy>();
        this.homeList = new ArrayList<ProductHome>();
        this.productsList = new ArrayList<Product>();
    }

    public void setAddUser(User user){
        this.usersList.add(user);
    }

    public List<ProductHome> getHomeList() {
        return homeList;
    }

    public void setHomeList(List<ProductHome> homeList) {
        this.homeList = homeList;
    }

    public List<ProductBuy> getShoppingList() {
        return shoppingList;
    }

    public void setShoppingList(List<ProductBuy> shoppingList) {
        this.shoppingList = shoppingList;
    }

    public List<User> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<User> usersList) {
        this.usersList = usersList;
    }

    public long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Product> getProductsList() {
        return productsList;
    }

    public void setProductsList(List<Product> productsList) {
        this.productsList = productsList;
    }
}
