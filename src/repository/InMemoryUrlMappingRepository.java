package repository;

import model.UrlMapping;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUrlMappingRepository implements UrlMappingRepository {
    private final Map<String, UrlMapping> urlStorage = new ConcurrentHashMap<>();
    @Override
    public void save(UrlMapping urlMapping) {
        urlStorage.put(urlMapping.getShortCode(), urlMapping);
    }

    @Override
    public Optional<UrlMapping> findByShortCode(String shortCode) {
        return Optional.ofNullable(urlStorage.get(shortCode));
    }

    @Override
    public boolean exists(String code) {
        return urlStorage.containsKey(code);
    }
}
