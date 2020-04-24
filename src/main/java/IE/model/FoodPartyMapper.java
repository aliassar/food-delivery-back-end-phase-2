package IE.model;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;

public class FoodPartyMapper extends Mapper<FoodParty, Integer, String> {
    private static FoodPartyMapper instance;
    public static final String COLUMNS = " restaurantId, name, description, restaurantName, price, popularity, image, Type, oldPrice, count ";
    public static final String TABLE_NAME = "foodparties";

    public static FoodPartyMapper getInstance() {
        if (instance == null)
            try {
                instance = new FoodPartyMapper();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        return instance;
    }

    private FoodPartyMapper() throws SQLException {
        Connection con = ConnectionPool.getConnection();
        Statement st = con.createStatement();
        st.executeUpdate(String.format(
                "CREATE TABLE IF NOT EXISTS  %s " +
                        "(" +
                        "id integer NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                        "restaurantId varchar(100), " +
                        "name varchar(100), " +
                        "description varchar(100), " +
                        "restaurantName varchar(100), " +
                        "price float, " +
                        "popularity float, " +
                        "image varchar(200), " +
                        "Type varchar(100) " +
                        "oldPrice float, " +
                        "count integer, " +
                        ");",
                TABLE_NAME));
        st.close();
        con.close();
    }

    @Override
    protected String getFindStatement(Integer id) {
        return "SELECT " + COLUMNS +
                " FROM " + TABLE_NAME +
                " WHERE id = " + id.toString() + ";";
    }

    @Override
    protected String getInsertStatement(FoodParty food) {
        return "INSERT INTO " + TABLE_NAME +
                "(" + COLUMNS + ")" + " VALUES " +
                "(" +
                '"' + food.getRestaurantId() + '"' + ", " +
                '"' + food.getName() + '"' + ", " +
                '"' + food.getDescription() + '"' + ", " +
                '"' + food.getRestaurantName() + '"' + ", " +
                food.getPrice() + ", " +
                food.getPopularity() + ", " +
                '"' + food.getImage().toString() + '"' + ", " +
                '"' + food.getType() + '"' + ", " +
                '"' + food.getOldPrice() + '"' + ", " +
                '"' + food.getCount() + '"' +
                ");";
    }

    @Override
    protected String getDeleteStatement(Integer id) {
        return "DELETE FROM " + TABLE_NAME +
                " WHERE id = " + id.toString() + ";";
    }

    @Override
    protected FoodParty convertResultSetToObject(ResultSet rs) throws SQLException, MalformedURLException {
        URL url = new URL(rs.getString(8));
        return new FoodParty(
                rs.getString(3),
                rs.getString(4),
                rs.getString(5),
                rs.getFloat(6),
                rs.getFloat(7),
                url,
                rs.getString(9),
                rs.getFloat(10),
                rs.getInt(11)
        );
    }

    @Override
    protected String getAllStatement() {
        return "SELECT * FROM " + TABLE_NAME + ";";
    }

    @Override
    protected String getFilterStatement(String restaurantId) {
        return "SELECT " + COLUMNS +
                " FROM " + TABLE_NAME +
                " WHERE restaurantId = " + '"' + restaurantId + '"' + ";";
    }

    @Override
    protected void getInsertCallBack(FoodParty food) {

    }

    public void DecreaseFoodCount(String RestaurantID, FoodParty food) throws SQLException, MalformedURLException {
        String statement = "SELECT " + COLUMNS +
                " FROM " + TABLE_NAME +
                " WHERE restaurantId = " +
                '"' + RestaurantID + '"' +
                " AND name = " +
                '"' + food.getName() + '"' +
                ";";
        try (Connection con = ConnectionPool.getConnection();
             PreparedStatement st = con.prepareStatement(statement);
        ) {
            ResultSet resultSet;
            try {
                resultSet = st.executeQuery();
                FoodParty foodParty = convertResultSetToObject(resultSet);
                foodParty.decreaseCount();
                statement = "UPDATE " + TABLE_NAME +
                        " SET count = " +
                        foodParty.getCount() +
                        " WHERE restaurantId = " +
                        '"' + RestaurantID + '"' +
                        " AND name = " +
                        '"' + food.getName() + '"' +
                        ";";
                try {
                    PreparedStatement newSt = con.prepareStatement(statement);
                    newSt.executeQuery();
                    st.close();
                    con.close();
                } catch (SQLException ex) {
                    System.out.println("error in Mapper.getLastId query.");
                    throw ex;
                }

            } catch (SQLException | MalformedURLException ex) {
                System.out.println("error in Mapper.getLastId query.");
                throw ex;
            }
        }
    }
}