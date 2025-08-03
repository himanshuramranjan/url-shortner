package repository;

import model.UrlMapping;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUrlMappingRepository implements UrlMappingRepository {
    private final Map<String, UrlMapping> urlStorage = new ConcurrentHashMap<>();
    private final Map<String, UrlMapping> originalUrlStorage = new ConcurrentHashMap<>();
    @Override
    public void save(UrlMapping urlMapping) {
        urlStorage.put(urlMapping.getShortCode(), urlMapping);
        originalUrlStorage.put(urlMapping.getOriginalUrl(), urlMapping);
    }

    @Override
    public Optional<UrlMapping> findByShortCode(String shortCode) {
        return Optional.ofNullable(urlStorage.get(shortCode));
    }

    public Optional<UrlMapping> findByOriginalUrl(String originalUrl) {
        return Optional.ofNullable(originalUrlStorage.get(originalUrl));
    }

    @Override
    public boolean exists(String code) {
        return urlStorage.containsKey(code);
    }

    @Override
    public boolean remove(String shortCode) {
        UrlMapping mapping = urlStorage.remove(shortCode);
        if (mapping != null) {
            originalUrlStorage.remove(mapping.getOriginalUrl());
            return true;
        }
        return false;
    }
}
