package repository;

import model.UrlMapping;

import java.util.Optional;

public interface UrlMappingRepository {
    void save(UrlMapping urlMapping);
    Optional<UrlMapping> findByShortCode(String shortCode);
    boolean exists(String code);
}
