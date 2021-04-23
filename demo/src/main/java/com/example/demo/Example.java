package com.example.demo;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.web.bind.annotation.*;
import com.zaxxer.hikari.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@RestController
@EnableAutoConfiguration
public class Example {
    @RequestMapping("/")
    String home() {
        return "Hello World!";
    }

    static HikariDataSource ds;
    public static void main(String[] args) throws SQLException {
        SpringApplication.run(Example.class, args);

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mariadb://localhost:3306/db");
        config.setUsername("root");
        config.setPassword("my-secret-pw");
        config.setMaximumPoolSize(10);
        config.setConnectionTimeout(1000);

        ds = new HikariDataSource(config);
        //try (HikariDataSource ds = new HikariDataSource(config)) {
            System.out.println("Start con1");
            Connection con1 = ds.getConnection();
            System.out.println("Start con2");
            Connection con2 = ds.getConnection();
        //}
    }

    public static void connectionTest(HikariDataSource ds) throws Exception {
        try (Connection con = ds.getConnection()) {
            // -> 現在日時を取得するSQLを準備
            PreparedStatement ps = con.prepareStatement("select Now() as now;");
            // -> SQLを実行してデータを取得
            ResultSet rs = ps.executeQuery();
            // -> データのカーソルを１つ進める
            rs.next();
            // -> データを表示
            System.out.print("NOW ");
            System.out.println(rs.getTimestamp("now"));
            // -> 後処理
            rs.close();
            ps.close();
        } catch (Exception e) {
            throw e;
        }

    }

}