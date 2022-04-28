import db.DbConnection;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
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

@WebServlet(urlPatterns = "/customer")
public class CustomerServlet extends HttpServlet {




    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            Connection connection = DbConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM customer");

            ResultSet resultSet = preparedStatement.executeQuery();

            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            while (resultSet.next()){
                String id  = resultSet.getString(1);
                String name  =  resultSet.getString(2);
                String address =  resultSet.getString(3);
                double salary  =  resultSet.getDouble(4);


                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("id", id);
                objectBuilder.add("name",name);
                objectBuilder.add("address",address );
                objectBuilder.add("salary", salary);

                arrayBuilder.add(objectBuilder.build());




            }


            PrintWriter writer = resp.getWriter();
            writer.print(arrayBuilder.build());


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String customerId = req.getParameter("customerId");
        String customerName = req.getParameter("customerName");
        String customerAddress = req.getParameter("customerAddress");
        String customerSalary = req.getParameter("customerSalary");


        System.out.println(customerId);

        try {
            Connection connection = DbConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO customer VALUES(?,?,?,?)");
            preparedStatement.setObject(1, customerId);
            preparedStatement.setObject(2, customerName);
            preparedStatement.setObject(3, customerAddress);
            preparedStatement.setObject(4, Double.parseDouble(customerSalary));

            PrintWriter writer = resp.getWriter();


            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();


            if(preparedStatement.executeUpdate()>0){
                objectBuilder.add("data", "");
                objectBuilder.add("message", "Successfully added");
                objectBuilder.add("status", "200");
                writer.print(objectBuilder.build());

            }else{
                writer.write("Add not success");
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }








}
