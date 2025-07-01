package com.example.oraclejdbcapi.controller;

import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.*;

@RestController
@RequestMapping("/query")
public class QueryController {

    @GetMapping
    public Object executeQuery(@RequestParam String sql) {
        String url = System.getenv("ORACLE_JDBC_URL");
        String user = System.getenv("ORACLE_JDBC_USER");
        String password = System.getenv("ORACLE_JDBC_PASSWORD");

        try (Connection conn = DriverManager.getConnection(url, user, password);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            List<Map<String, Object>> results = new ArrayList<>();
            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();

            while (rs.next()) {
                Map<String, Object> row = new LinkedHashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(meta.getColumnName(i), rs.getObject(i));
                }
                results.add(row);
            }

            return results;

        } catch (SQLException e) {
            return Map.of("error", "SQL実行中にエラーが発生しました。", "message", e.getMessage());
        }
    }
}
