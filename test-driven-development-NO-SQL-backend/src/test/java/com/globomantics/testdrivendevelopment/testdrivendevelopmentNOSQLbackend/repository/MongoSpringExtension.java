package com.globomantics.testdrivendevelopment.testdrivendevelopmentNOSQLbackend.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

public class MongoSpringExtension implements BeforeEachCallback, AfterEachCallback {
    private static Path JSON_PATH = Paths.get("src", "test", "resources", "data");

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        context.getTestMethod().ifPresent(method -> {
            //load test file from the annotation
            MongoDataFile mongoDataFile = method.getAnnotation(MongoDataFile.class);

            //load the MongoTemplate that we can use to import our data
            getMongoTemplate(context).ifPresent(mongoTemplate -> {
                try {
                    //use Jackson's ObjectMapper to load a list of objects from the JSON file
                    List objects = mapper.readValue(JSON_PATH.resolve(mongoDataFile.value()).toFile(),
                            mapper.getTypeFactory().constructCollectionType(List.class, mongoDataFile.classType()));

                    //save each object into MongoDB
                    objects.forEach(mongoTemplate::save);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        });
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        context.getTestMethod().ifPresent(method -> {
            //load the MongoDataFile annotation value from the test method
            MongoDataFile mongoDataFile = method.getAnnotation(MongoDataFile.class);

            //load the MongoTemplate that we can use to drop the test collection
            Optional<MongoTemplate> mongoTemplate = getMongoTemplate(context);
            mongoTemplate.ifPresent(mt -> mt.dropCollection(mongoDataFile.collectionName()));
        });
    }

    private Optional<MongoTemplate> getMongoTemplate(ExtensionContext context) {
        Optional<Class<?>> clazz = context.getTestClass();
        if (clazz.isPresent()) {
            Class<?> c = clazz.get();
            try {
                //find the getMongoTemplate method on the test class
                Method method = c.getMethod("getMongoTemplate", null);

                //invoke the getMongoTemplate on the test class
                Optional<Object> testInstance = context.getTestInstance();
                if (testInstance.isPresent()) {
                    return Optional.of((MongoTemplate) method.invoke(testInstance.get(), null));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return Optional.empty();
    }
}
