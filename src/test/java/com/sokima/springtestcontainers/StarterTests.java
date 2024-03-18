package com.sokima.springtestcontainers;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;

class StarterTests {

    static GenericContainer<?> APPLICATION = createGenericContainer();

    private static GenericContainer<?> createGenericContainer() {
        return new GenericContainer<>(
                new ImageFromDockerfile()
                        .withFileFromPath(".", Path.of(".")))
                .withExposedPorts(8080)
                .withCreateContainerCmdModifier(
                        e -> e.withPortBindings(new PortBinding(
                                Ports.Binding.bindPort(8080),
                                new ExposedPort(8080)
                        ))
                );
    }

    @BeforeAll
    static void setUp() {
        APPLICATION.start();
    }

    @AfterAll
    static void tearDown() {
        APPLICATION.close();
    }

    @RepeatedTest(3)
    void contextLoads() throws Exception {
        final URL url = new URL("http://localhost:8080/greet");
        final HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        final InputStream inputStream = urlConnection.getInputStream();
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        final String s = bufferedReader.readLine();
        System.out.println(s);
        Assertions.assertTrue(s.startsWith("greeting"));
    }
}
