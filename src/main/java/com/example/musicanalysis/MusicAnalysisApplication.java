package com.example.musicanalysis;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.cassandra.repository.config.EnableReactiveCassandraRepositories;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootApplication
@EnableReactiveCassandraRepositories
public class MusicAnalysisApplication {

    public static void main(String[] args) {
        SpringApplication.run(MusicAnalysisApplication.class, args);
    }

    @Bean
    public CommandLineRunner loadDataFromCsv(MusicTrackRepository repository) {
        return args -> {
            try (Reader reader = Files.newBufferedReader(Paths.get("/Users/sfkunal/IdeaProjects/MusicAnalysis/src/main/java/com/example/musicanalysis/music.csv"))) {
                CSVFormat format = CSVFormat.DEFAULT.builder().setHeader("id", "song", "album", "duration").build();
                CSVParser csvParser = new CSVParser(reader, format);

                csvParser.forEach(record -> {
                    MusicTrack track = new MusicTrack();
                    track.setId(record.get("id"));
                    track.setSong(record.get("song"));
                    track.setAlbum(record.get("album"));
                    track.setDuration(record.get("duration"));

                    repository.save(track)
                            .subscribe(
                                    savedTrack -> System.out.println("Saved: " + savedTrack),
                                    error -> System.err.println("Error saving track: " + error.getMessage())
                            );
                });

                System.out.println("Data loaded successfully.");
            } catch (Exception e) {
                System.err.println("Failed to load data from CSV: " + e.getMessage());
            }
        };
    }
}
