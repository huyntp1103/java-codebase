package com.example.blogapi.config.migration;

import com.example.blogapi.post.model.Post;
import com.example.blogapi.user.model.User;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;
import org.hibernate.tool.schema.TargetType;

import java.util.EnumSet;

public class SchemaDiffGenerator {
    public static void main(String[] args) {
        var registry = new StandardServiceRegistryBuilder()
                .applySetting("hibernate.connection.url", "jdbc:postgresql://localhost:5432/blogdb")
                .applySetting("hibernate.connection.username", "postgres")
                .applySetting("hibernate.connection.password", "postgres")
                .applySetting("hibernate.connection.driver_class", "org.postgresql.Driver")
                .applySetting("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect")
                .build();

        var metadata = new MetadataSources(registry)
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Post.class)
                .buildMetadata();

        new SchemaUpdate()
                .setFormat(true)
                .setOutputFile("src/main/resources/db/migration/V2__add_published_column.sql")
                .execute(EnumSet.of(TargetType.SCRIPT), metadata);
    }
}