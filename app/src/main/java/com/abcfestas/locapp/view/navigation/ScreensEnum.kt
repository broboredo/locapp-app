package com.abcfestas.locapp.view.navigation

enum class ScreensEnum(val route: String) {
    HomeScreen("home"),
    RentScreen("rentals"),
    // Customer
    CustomerScreen("customers"),
    CustomerFormScreen("customers/create"),
    CustomerDetailsScreen("customers/{customerId}"),
    // Product
    ProductScreen("products"),
    ProductDetailsScreen("products/{productId}"),
    ProductFormScreen("products/create"),

    // Errors
    SomethingWentWrongScreen("error");
}