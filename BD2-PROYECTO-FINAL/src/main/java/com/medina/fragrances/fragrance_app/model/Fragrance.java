package com.medina.fragrances.fragrance_app.model;

public class Fragrance {
    private final int id;
    private final String name;
    private final String brand;
    private final double price;
    private final String imageUrl;
    private final String description;
    private final String category;
    private final String discountLabel;

    public Fragrance(int id, String name, String brand, double price, String imageUrl, String description, String category, String discountLabel) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.imageUrl = imageUrl;
        this.description = description;
        this.category = category;
        this.discountLabel = discountLabel;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public double getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getDiscountLabel() {
        return discountLabel;
    }

    public String getPriceFormatted() {
        return String.format("S/. %.2f", price);
    }
}
