package IE.Servlets;

import IE.Exceptions.*;
import IE.Loghme;
import IE.models.Cart;
import IE.models.Food;
import IE.models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;


@WebServlet("/cart")
public class CartServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Loghme loghme = Loghme.getInstance();
        User user = loghme.getAppUser();
        try {
            Cart cart = user.getInProcessCart();
            request.setAttribute("cart", cart);
            request.getRequestDispatcher("/cart.jsp").forward(request, response);
        } catch (NoOrder | NoInProcessOrder e) {
            e.printStackTrace();
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/exception.jsp").forward(request, response);
        }
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Loghme loghme = Loghme.getInstance();
        String name =  request.getParameter("name");
        String restaurantName = request.getParameter("restaurantName");
        float price = Float.parseFloat(Objects.requireNonNull(request.getParameter("price")));
        Food food = new Food(name, restaurantName, price);
        try {
            loghme.addToCart(food);
        } catch (NoRestaurant | WrongFood | DifRestaurants e) {
            e.printStackTrace();
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/exception.jsp").forward(request, response);
        }
        User user = loghme.getAppUser();
        try {
            Cart cart = user.getInProcessCart();
            request.setAttribute("cart", cart);
            request.getRequestDispatcher("/cart.jsp").forward(request, response);
        } catch (NoOrder | NoInProcessOrder e) {
            e.printStackTrace();
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/exception.jsp").forward(request, response);
        }
    }
}
