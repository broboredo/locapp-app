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
    NewProductStepOneScreen("products/create/step_one"),

    // Errors
    SomethingWentWrongScreen("error");

    companion object {
        fun ProductDetails(productId: Int) = "products/$productId"
    }
}