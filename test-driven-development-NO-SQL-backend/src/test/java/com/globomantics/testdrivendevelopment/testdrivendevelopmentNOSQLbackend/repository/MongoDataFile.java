package com.globomantics.testdrivendevelopment.testdrivendevelopmentNOSQLbackend.repository;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MongoDataFile {
    /**
     * @return The name of the MongoDB JSON file
     */
    String value();

    /**
     * @return The class of objects stored in the MongoDB test file
     */
    Class classType();

    /**
     * @return The name of the MongoDB collection hosting the test objects
     */
    String collectionName();
}
