package lk.ijse.dep.web.api;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import lk.ijse.dep.web.model.Customer;
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

@WebServlet(name = "CustomerServlet", urlPatterns = "/customers")
public class CustomerServlet extends HttpServlet {

    //Create
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BasicDataSource connectionPool = (BasicDataSource) getServletContext().getAttribute("theConnectionPool");
        try (Connection connection = connectionPool.getConnection()) {
            Customer customer;
            String id = request.getParameter("id");
            String name = request.getParameter("name");
            String address = request.getParameter("address");
            if (!validateCustomer(id, name, address)) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            } else {
                customer = new Customer(id, name, address);
            }
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Customer SET name=?, address=? WHERE id=?");
            preparedStatement.setObject(1, name);
            preparedStatement.setObject(2, address);
            preparedStatement.setObject(3, id);
            boolean success =preparedStatement.executeUpdate()>0;
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
        String id = request.getParameter("id");
        try (Connection connection = connectionPool.getConnection()) {
            PrintWriter out = response.getWriter();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Customer" + ((id != null) ? " WHERE id=?" : ""));
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Customer> customersList = new ArrayList<>();
            while (resultSet.next()) {
                id = resultSet.getString(1);
                String name = resultSet.getString(2);
                String address = resultSet.getString(3);
                customersList.add(new Customer(id, name, address));
            }

            Jsonb jsonb = JsonbBuilder.create();
            out.println(jsonb.toJson(customersList));
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
            Customer customer;
            String id = request.getParameter("id");
            String name = request.getParameter("name");
            String address = request.getParameter("address");
            if (!validateCustomer(id, name, address)) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            } else {
                customer = new Customer(id, name, address);
            }

            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Customer VALUES (?,?,?)");
            preparedStatement.setString(1, customer.getId());
            preparedStatement.setString(2, customer.getName());
            preparedStatement.setString(3, customer.getAddress());
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
        String id = request.getParameter("id");
        if (id.trim().isEmpty() || !id.matches("C\\d{3}")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        BasicDataSource connectionPool = (BasicDataSource) getServletContext().getAttribute("theConnectionPool");
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Customer WHERE id=?");
            preparedStatement.setObject(1, id);
            boolean success =preparedStatement.executeUpdate()>0;
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
