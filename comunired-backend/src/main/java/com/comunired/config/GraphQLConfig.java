package com.comunired.config;

import graphql.scalars.ExtendedScalars;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.time.format.DateTimeParseException;

@Configuration
public class GraphQLConfig {
    
    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {

        GraphQLScalarType instantScalar = GraphQLScalarType.newScalar()
            .name("DateTime")
            .description("Instant tipo DateTime en formato ISO-8601")
            .coercing(new Coercing<Instant, String>() {
                @Override
                public String serialize(Object dataFetcherResult) throws CoercingSerializeException {
                    if (dataFetcherResult instanceof Instant) {
                        return ((Instant) dataFetcherResult).toString();
                    }
                    throw new CoercingSerializeException("Expected an Instant object.");
                }

                @Override
                public Instant parseValue(Object input) throws CoercingParseValueException {
                    try {
                        if (input instanceof String) {
                            return Instant.parse((String) input);
                        }
                        throw new CoercingParseValueException("Expected a String");
                    } catch (DateTimeParseException e) {
                        throw new CoercingParseValueException("Invalid DateTime format", e);
                    }
                }

                @Override
                public Instant parseLiteral(Object input) throws CoercingParseLiteralException {
                    try {
                        if (input instanceof String) {
                            return Instant.parse((String) input);
                        }
                        throw new CoercingParseLiteralException("Expected a String literal");
                    } catch (DateTimeParseException e) {
                        throw new CoercingParseLiteralException("Invalid DateTime format", e);
                    }
                }
            })
            .build();

        return wiringBuilder -> wiringBuilder
            .scalar(ExtendedScalars.Date)
            .scalar(instantScalar)
            .scalar(uploadScalar());  // ← Ya lo tienes, ahora agrega el método abajo
    }

    /**
     * Scalar personalizado para Upload (MultipartFile)
     */
    private GraphQLScalarType uploadScalar() {
        return GraphQLScalarType.newScalar()
                .name("Upload")
                .description("A custom scalar that represents a file upload")
                .coercing(new Coercing<MultipartFile, Void>() {
                    
                    @Override
                    public Void serialize(Object dataFetcherResult) throws CoercingSerializeException {
                        throw new CoercingSerializeException("Upload scalar cannot be serialized");
                    }

                    @Override
                    public MultipartFile parseValue(Object input) throws CoercingParseValueException {
                        if (input instanceof MultipartFile) {
                            return (MultipartFile) input;
                        }
                        throw new CoercingParseValueException("Expected MultipartFile");
                    }

                    @Override
                    public MultipartFile parseLiteral(Object input) throws CoercingParseLiteralException {
                        throw new CoercingParseLiteralException("Upload scalar cannot be parsed from literal");
                    }
                })
                .build();
    }
}
