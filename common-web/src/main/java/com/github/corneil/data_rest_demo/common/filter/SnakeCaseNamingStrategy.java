package com.github.corneil.data_rest_demo.common.filter;

import lombok.extern.slf4j.XSlf4j;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

import static com.google.common.base.CaseFormat.*;

@XSlf4j
public class SnakeCaseNamingStrategy implements PhysicalNamingStrategy {
    @Override
    public Identifier toPhysicalCatalogName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        log.entry(identifier);
        if (identifier != null) {
            if (identifier.getText().contains("_")) {
                return log.exit(new Identifier(identifier.getText().toLowerCase(), false));
            }
            return log.exit(new Identifier(UPPER_CAMEL.to(LOWER_UNDERSCORE, identifier.getText()), false));
        }
        return log.exit(null);
    }

    @Override
    public Identifier toPhysicalSchemaName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        log.entry(identifier);
        if (identifier != null) {
            if (identifier.getText().contains("_")) {
                return log.exit(new Identifier(identifier.getText().toLowerCase(), false));
            }
            return log.exit(new Identifier(UPPER_CAMEL.to(LOWER_UNDERSCORE, identifier.getText()), false));
        }
        return log.exit(null);
    }

    @Override
    public Identifier toPhysicalSequenceName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        log.entry(identifier);
        if (identifier != null) {
            if (identifier.getText().contains("_")) {
                return log.exit(new Identifier(identifier.getText().toLowerCase(), false));
            }
            return log.exit(new Identifier(UPPER_CAMEL.to(LOWER_UNDERSCORE, identifier.getText()), false));
        }
        return log.exit(null);
    }

    public Identifier toPhysicalTableName(Identifier identifier, JdbcEnvironment context) {
        log.entry(identifier);
        if (identifier.getText().toUpperCase().equals(identifier.getText())) {
            return log.exit(new Identifier(identifier.getText().toLowerCase(), false));
        }
        if (identifier.getText().contains("_")) {
            return log.exit(new Identifier(identifier.getText().toLowerCase(), false));
        }
        return log.exit(new Identifier(UPPER_CAMEL.to(LOWER_UNDERSCORE, identifier.getText()), false));
    }

    public Identifier toPhysicalColumnName(Identifier identifier, JdbcEnvironment context) {
        log.entry(identifier);
        if (identifier.getText().toUpperCase().equals(identifier.getText())) {
            return log.exit(new Identifier(identifier.getText().toLowerCase(), false));
        }
        if (identifier.getText().contains("_")) {
            return log.exit(new Identifier(identifier.getText().toLowerCase(), false));
        }
        return log.exit(new Identifier(LOWER_CAMEL.to(LOWER_UNDERSCORE, identifier.getText()), false));
    }
}