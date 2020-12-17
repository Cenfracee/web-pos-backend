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

@WebServlet(name = "CustomerServlet",urlPatterns ="/customer" )
public class CustomerServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BasicDataSource connectionPool = (BasicDataSource) getServletContext().getAttribute("theConnectionPool");
        response.setContentType("application/json");

        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            PrintWriter out = response.getWriter();
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM Customer");
            ResultSet resultSet = pstm.executeQuery();
            List<Customer> customersList = new ArrayList<>();
            while (resultSet.next()) {
                String id = resultSet.getString(1);
                String name = resultSet.getString(2);
                String address = resultSet.getString(3);
                customersList.add(new Customer(id, name, address));
            }
            Jsonb jsonb = JsonbBuilder.create();
            out.println(jsonb.toJson(customersList));
            connection.close();
            response.setContentType("text/html");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }



    }
}
