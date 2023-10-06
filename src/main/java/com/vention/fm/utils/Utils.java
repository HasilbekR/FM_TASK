package com.vention.fm.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vention.fm.exception.DataNotFoundException;
import org.modelmapper.ModelMapper;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;

public class Utils {
    private static Connection connection;
    private static ObjectMapper objectMapper;
    private static ModelMapper modelMapper;

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
                throw new RuntimeException(e);
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

    public static ModelMapper modelMapper() {
        if (modelMapper == null) {
            modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setSkipNullEnabled(true);
        }
        return modelMapper;
    }

    public static String url(String request) {
        try {
            Properties properties = new Properties();
            InputStream inputStream = Utils.class.getClassLoader().getResourceAsStream("loader.properties");
            properties.load(inputStream);
            if(Objects.equals(request, "/url")) return properties.getProperty("LOADER_URL");
           } catch (IOException e) {
            throw new RuntimeException(e);
        }
        throw new DataNotFoundException("Invalid request");
    }
}
