package org.jelyck.utils

import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class DataFetcher {

    private static final String base = "https://adventofcode.com/2023/day/"
    private static final HttpClient client = HttpClient.newHttpClient()


    static get(int day) {
        Path path = Paths.get("data/D" + day + ".txt")

        if (!Files.exists(path)) {
            fetchCreate(day, path)
        }

        return Files.readAllLines(path)
    }

    static fetchCreate(int day, Path path) {
        def uri = new URI(base + day + "/input")
        def file = new File(DataFetcher.class.getResource("/secret.properties").toURI())

        def prop = new Properties()

        file.withInputStream {
            prop.load(it)
        }

        def session = prop.getProperty("AOC_SESSION")
        def request = HttpRequest.newBuilder()
                .uri(uri)
                .headers("Cookie", session)
                .GET()
                .build();

        client.send(request, HttpResponse.BodyHandlers.ofFile(path))
    }
}
