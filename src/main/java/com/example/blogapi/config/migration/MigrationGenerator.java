package com.example.blogapi.config.migration;

import com.example.blogapi.user.model.User;
import com.example.blogapi.post.model.Post;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.tool.schema.TargetType;
import org.hibernate.tool.hbm2ddl.SchemaExport;

import java.util.EnumSet;

public class MigrationGenerator {
    public static void main(String[] args) {
        var registry = new StandardServiceRegistryBuilder()
                .applySetting("hibernate.connection.url", "jdbc:postgresql://localhost:5432/blogdb")
                .applySetting("hibernate.connection.username", "postgres")
                .applySetting("hibernate.connection.password", "postgres")
                .applySetting("hibernate.connection.driver_class", "org.postgresql.Driver")
                .build();

        var metadata = new MetadataSources(registry)
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Post.class)
                .buildMetadata();

        new SchemaExport()
                .setFormat(true)
                .setHaltOnError(true)
                .setOutputFile("src/main/resources/db/migration/V1__initial_schema.sql")
                .create(EnumSet.of(TargetType.SCRIPT), metadata);
    }
}