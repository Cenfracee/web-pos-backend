package lk.ijse.dep.web.api;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import lk.ijse.dep.web.model.Customer;
import lk.ijse.dep.web.model.Item;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static lk.ijse.dep.web.functions.Validation.validateCustomer;
import static lk.ijse.dep.web.functions.Validation.validateItem;

@WebServlet(name = "ItemServlet", urlPatterns = "/items")
public class ItemServlet extends HttpServlet {

    //Create
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BasicDataSource connectionPool = (BasicDataSource) getServletContext().getAttribute("theConnectionPool");
        try (Connection connection = connectionPool.getConnection()) {
            Item item;
            String code = request.getParameter("code");
            String description = request.getParameter("description");
            int qtyOnHand = Integer.parseInt(request.getParameter("qtyOnHand"));
            double unitPrice = Double.parseDouble(request.getParameter("unitPrice"));
            if (!validateItem(code, description, qtyOnHand, unitPrice)) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            } else {
                item = new Item(code, description, qtyOnHand, unitPrice);
            }
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Item SET description=?, qtyOnHand=?, unitPrice=? WHERE code=?");
            preparedStatement.setObject(1, description);
            preparedStatement.setObject(2, qtyOnHand);
            preparedStatement.setObject(3, unitPrice);
            preparedStatement.setObject(4, code);
            boolean success = preparedStatement.executeUpdate() > 0;
            if (success) {
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }


    //Read
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BasicDataSource connectionPool = (BasicDataSource) getServletContext().getAttribute("theConnectionPool");
        response.setContentType("application/json");
        String code = request.getParameter("code");
        try (Connection connection = connectionPool.getConnection()) {
            PrintWriter out = response.getWriter();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Item" + ((code != null) ? " WHERE code=?" : ""));
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Item> itemsList = new ArrayList<>();
            while (resultSet.next()) {
                code = resultSet.getString(1);
                String description = resultSet.getString(2);
                int qtyOnHand = resultSet.getInt(3);
                double unitPrice = resultSet.getDouble(4);
                itemsList.add(new Item(code, description, qtyOnHand,unitPrice));
            }

            Jsonb jsonb = JsonbBuilder.create();
            out.println(jsonb.toJson(itemsList));
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }


    }

    //Update
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BasicDataSource connectionPool = (BasicDataSource) getServletContext().getAttribute("theConnectionPool");
        try (Connection connection = connectionPool.getConnection()) {
            Item item;
            String code = request.getParameter("code");
            String description = request.getParameter("description");
            int qtyOnHand = Integer.parseInt(request.getParameter("qtyOnHand")) ;
            double unitPrice =Double.parseDouble(request.getParameter("unitPrice")) ;
            if (!validateItem(code, description, qtyOnHand,unitPrice)) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            } else {
                item = new Item(code, description, qtyOnHand,unitPrice);
            }

            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Item VALUES (?,?,?,?)");
            preparedStatement.setString(1, item.getCode());
            preparedStatement.setString(2, item.getDescription());
            preparedStatement.setInt(3, item.getQtyOnHand());
            preparedStatement.setDouble(4, item.getUnitPrice());
            if (preparedStatement.executeUpdate() > 0) {
                response.setStatus(HttpServletResponse.SC_CREATED);
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }

    //Delete
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");
        if (code.trim().isEmpty() || !code.matches("I\\d{3}")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        BasicDataSource connectionPool = (BasicDataSource) getServletContext().getAttribute("theConnectionPool");
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Item WHERE code=?");
            preparedStatement.setObject(1, code);
            boolean success = preparedStatement.executeUpdate() > 0;
            if (success) {
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);
    }
}
