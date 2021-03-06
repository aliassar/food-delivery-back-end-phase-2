package IE.model;

import IE.Exceptions.NoOrder;
import IE.password.Password;

import java.util.ArrayList;


public class User {
    private ArrayList<Cart> cartsOfUser = new ArrayList<>();
    private String fname;
    private String lname;
    private String phoneNumber = "0";
    private String email;
    private String password;
    private float wallet = 0;
    private Cart inProcessCart = new Cart();

    public User() {
    }

    public User(String fname, String lname, String email, String password) throws Exception {
        this.phoneNumber = "0";
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        try {
            this.password = Password.getSaltedHash(password);

        }catch (Exception e){
            throw e;
        }

        this.wallet = 0;
    }

    public User(String fname, String lname, String phoneNumber, String email, float wallet) {
        this.cartsOfUser = new ArrayList<>();
        inProcessCart = new Cart();
        this.fname = fname;
        this.lname = lname;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.wallet = wallet;
    }



    public User(ArrayList<Cart> cartsOfUser, String fname, String lname, String phoneNumber, String email, float wallet, String password) {
        this.cartsOfUser = cartsOfUser;
        this.fname = fname;
        this.lname = lname;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.wallet = wallet;
        this.password = password;
    }

    public void AddToWallet(float amount) {
        this.wallet += amount;

    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public float getWallet() {
        return wallet;
    }

    public void setWallet(float wallet) {
        this.wallet = wallet;
    }

    public Cart getInProcessCart() {
        return inProcessCart;
    }

    public Cart getLastCart() throws NoOrder {
        if (cartsOfUser.size() < 1) {
            throw new NoOrder("There are no processed order");
        } else {
            return cartsOfUser.get(cartsOfUser.size() - 1);
        }
    }

    public void clearInProcessCart() {
        inProcessCart = new Cart();
    }

    public ArrayList<Cart> getCartsOfUser() {
        return cartsOfUser;
    }

    public void newProcessedCart(Cart cart) {
        cartsOfUser.add(cart);
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}

