package com.app.engineerapp.DataBaseUtil;

import java.sql.Connection;
import java.sql.DriverManager;

public class dbConnection {
    public static Connection getConnexion()  {
        Connection connection=null;
        try{
            Class.forName("org.postgresql.Driver");
             // connection= DriverManager.getConnection("jdbc:postgresql://localhost:5432/EtatDeLieuAgriculture?user=postgres&password=root");
              connection= DriverManager.getConnection("jdbc:postgresql://localhost:5432/agriculture?user=postgres&password=root");
            //connection= DriverManager.getConnection("jdbc:postgresql://localhost:5432/etatdelieuagriculture?user=postgres&password=toor");
            return connection;
        }catch (Exception e){
            System.out.println("can't connect database");
            System.exit(1);
        }
        return connection;
    }
}
