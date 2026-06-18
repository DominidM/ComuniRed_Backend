package com.comunired.historias.infrastructure.adapter.out.musica;

import com.comunired.historias.application.dto.out.MusicTrackResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class MusicService {

    private static final Logger log = LoggerFactory.getLogger(MusicService.class);
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public MusicService() {
        this.httpClient = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
        this.objectMapper = new ObjectMapper();
    }

    public List<MusicTrackResponse> buscar(String query) {
        try {
            String encoded = URLEncoder.encode(query, StandardCharsets.UTF_8);
            String url = "https://itunes.apple.com/search?term=" + encoded
                    + "&media=music&entity=song&limit=15";

            log.info("Buscando en iTunes: {}", url);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("User-Agent", "ComuniRed/1.0")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                log.error("iTunes respondió con status: {}", response.statusCode());
                return List.of();
            }

            JsonNode root = objectMapper.readTree(response.body());
            JsonNode results = root.get("results");
            if (results == null || !results.isArray()) {
                return List.of();
            }

            List<MusicTrackResponse> tracks = new ArrayList<>();
            for (JsonNode item : results) {
                JsonNode previewUrlNode = item.get("previewUrl");
                JsonNode trackNameNode = item.get("trackName");
                if (previewUrlNode == null || previewUrlNode.isNull()
                        || trackNameNode == null || trackNameNode.isNull()) {
                    continue;
                }

                String previewUrl = previewUrlNode.asText();
                String trackName = trackNameNode.asText();

                String coverUrl = "";
                JsonNode artworkNode = item.get("artworkUrl100");
                if (artworkNode != null && !artworkNode.isNull()) {
                    coverUrl = artworkNode.asText().replace("100x100", "300x300");
                }

                String trackId = item.has("trackId") && !item.get("trackId").isNull()
                        ? item.get("trackId").asText() : "";

                String artistName = item.has("artistName") && !item.get("artistName").isNull()
                        ? item.get("artistName").asText() : "Unknown Artist";

                tracks.add(new MusicTrackResponse(
                        trackId, trackName, artistName, coverUrl, previewUrl));
            }

            log.info("iTunes devolvió {} resultados para '{}'", tracks.size(), query);
            return tracks;
        } catch (Exception e) {
            log.error("Error buscando en iTunes: {}", e.getMessage(), e);
            return List.of();
        }
    }
}
