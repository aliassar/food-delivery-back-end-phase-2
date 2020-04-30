package IE.controller;

import IE.exceptions.DifRestaurants;
import IE.exceptions.NoFoodRemained;
import IE.exceptions.NoRestaurant;
import IE.exceptions.WrongFood;
import IE.Loghme;
import IE.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class CartController {
    @RequestMapping(value = "/cart",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Cart GetCart() {
        Loghme loghme = Loghme.getInstance();
        User user = loghme.getAppUser();
        Cart cart = user.getInProcessCart();
        if (cart.getOrders().size() > 0) {
            float estimatedArrive = 10000;
            try {
                Restaurant chosenRestaurant = loghme.FindRestaurant(cart.getRestaurantID());
                estimatedArrive = loghme.EstimateArivingTime(chosenRestaurant.getLocation());
            } catch (NoRestaurant e) {
                try {
                    FoodPartyRestaurant chosenFoodPartyRestaurants = loghme.FindFoodPartyRestaurant(cart.getRestaurantID());
                    estimatedArrive = loghme.EstimateArivingTime(chosenFoodPartyRestaurants.getLocation());
                } catch (NoRestaurant error) {
                    error.printStackTrace();
                }
            }


        }
        return cart;
    }
    @RequestMapping(value = "/cart",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> AddToCart(@RequestParam(value = "orders") ArrayList<Food> orders)
    {
        Loghme loghme = Loghme.getInstance();
        boolean ErrorDetected = false;
        for (int i=0; i<orders.size(); i++){
            try {
                loghme.addToCart(orders.get(i), orders.get(i).getRestaurantName());

            }catch (NoRestaurant | WrongFood | DifRestaurants e) {
                e.printStackTrace();
                ErrorDetected = true;
            }
        }
        float estimatedArrive = 10000;
        if (ErrorDetected){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(estimatedArrive);
        }
        else {
            return ResponseEntity.status(HttpStatus.OK).body(estimatedArrive);
        }

    }

    @RequestMapping(value = "/cart/foodparty",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> AddToCartFoodParty(@RequestParam(value = "orders") ArrayList<FoodParty> orders)
    {
        Loghme loghme = Loghme.getInstance();
        boolean ErrorDetected = false;
        for (int i=0; i<orders.size(); i++){
            try {
                loghme.FoodPartyaddToCart(orders.get(i), orders.get(i).getRestaurantName());
                loghme.addToCart(orders.get(i), orders.get(i).getRestaurantName());

            }catch (NoRestaurant | WrongFood | DifRestaurants | NoFoodRemained e) {
                e.printStackTrace();
                ErrorDetected = true;
            }
        }
        float estimatedArrive = 10000;
        if (ErrorDetected){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(estimatedArrive);
        }
        else {
            return ResponseEntity.status(HttpStatus.OK).body(estimatedArrive);
        }

    }

//    @RequestMapping(value = "/cart",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> AddToCart(@RequestParam(value = "count") String count,
//                                       @RequestParam(value = "name") String name,
//                                       @RequestParam(value = "restaurantName") String restaurantName,
//                                       @RequestParam(value = "ID") String restaurantID,
//                                       @RequestParam(value = "type") String type,
//                                       @RequestParam(value = "oldPrice") String oldPrice,
//                                       @RequestParam(value = "price") float price) {
//        Loghme loghme = Loghme.getInstance();
//        try {
//            if(type!=null && type.equals("foodParty")){
//                FoodParty foodParty = new FoodParty(name, restaurantName, price,Float.parseFloat(oldPrice),Integer.parseInt(count));
//                loghme.FoodPartyaddToCart(foodParty,restaurantID);
//            }
//            else{
//                Food food = new Food(name, restaurantName, price);
//                loghme.addToCart(food, restaurantID);
//            }
//        } catch (NoRestaurant | WrongFood | DifRestaurants | NoFoodRemained e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        }
//        User user = loghme.getAppUser();
//        Cart cart = user.getInProcessCart();
//        float estimatedArrive = 10000;
//        try {
//            Restaurant chosenRestaurant = loghme.FindRestaurant(cart.getRestaurantID());
//            estimatedArrive = loghme.EstimateArivingTime(chosenRestaurant.getLocation());
//        } catch (NoRestaurant e) {
//            try {
//                FoodPartyRestaurant chosenFoodPartyRestaurants = loghme.FindFoodPartyRestaurant(cart.getRestaurantID());
//                estimatedArrive = loghme.EstimateArivingTime(chosenFoodPartyRestaurants.getLocation());
//            } catch (NoRestaurant error) {
//                error.printStackTrace();
//            }
//        }
//        return ResponseEntity.status(HttpStatus.OK).body(cart);
//
//
//    }
}

