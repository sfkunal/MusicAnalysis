package com.example.musicanalysis;

import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

import java.util.UUID;

public interface MusicTrackRepository extends ReactiveCassandraRepository<MusicTrack, UUID> {
}