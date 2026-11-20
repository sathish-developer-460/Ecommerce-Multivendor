package com.sathish.ecommerce.multivendor.modal;

import java.util.List;

import lombok.Data;

@Data
public class Home {
    private List<HomeCategory> grid;

    private List<HomeCategory> ShopByCategories;

    private List<HomeCategory> electricCategories;

    private List<HomeCategory> dealCategories;

    private List<Deal> deals;
}
