package lk.ijse.dep.web.listner;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.SQLException;

@WebListener
public class ContextListner implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setUsername("root");
        basicDataSource.setPassword("1234");
        basicDataSource.setUrl("jdbc:mysql://localhost:3306/posDB");
        basicDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        basicDataSource.setInitialSize(5);
        basicDataSource.setMaxTotal(10);
        ServletContext servletContext = sce.getServletContext();
        servletContext.setAttribute("theConnectionPool", basicDataSource);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        BasicDataSource cp = (BasicDataSource) sce.getServletContext().getAttribute("theConnectionPool");
        try {
            cp.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
