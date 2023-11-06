package com.vention.fm.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vention.fm.exception.BadRequestException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Utils {
    private static Connection connection;
    private static ObjectMapper objectMapper;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                Properties properties = new Properties();
                InputStream inputStream = Utils.class.getClassLoader().getResourceAsStream("dbconnection.properties");
                properties.load(inputStream);

                String DRIVER = properties.getProperty("DRIVER");
                String URL = properties.getProperty("URL");
                String USERNAME = properties.getProperty("USERNAME");
                String PASSWORD = properties.getProperty("PASSWORD");
                Class.forName(DRIVER);
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            } catch (SQLException | ClassNotFoundException | IOException e) {
                throw new BadRequestException(e.getMessage());
            }
        }
        return connection;
    }

    public static ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        }
        return objectMapper;
    }
    public static String getLoaderURL() {
        try {
            Properties properties = new Properties();
            InputStream inputStream = Utils.class.getClassLoader().getResourceAsStream("loader.properties");
            properties.load(inputStream);
            return properties.getProperty("LOADER_URL");
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
    }
    public static void methodNotAllowed(HttpServletRequest req, HttpServletResponse resp){
        resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        try {
            resp.getWriter().print(req.getMethod() + " Method not allowed for this url");
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}
