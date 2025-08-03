package repository;

import model.UrlMapping;

import java.util.Optional;

public interface UrlMappingRepository {
    void save(UrlMapping urlMapping);
    Optional<UrlMapping> findByShortCode(String shortCode);
    Optional<UrlMapping> findByOriginalUrl(String originalUrl);
    boolean exists(String code);
    boolean remove(String shortCode);
}
