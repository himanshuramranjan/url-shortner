package service;

import generator.CodeGenerator;
import model.UrlMapping;
import repository.UrlMappingRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public class UrlShortenerService {
    private final UrlMappingRepository urlMappingRepository;
    private final CodeGenerator codeGenerator;
    private final String baseUrl;

    public UrlShortenerService(UrlMappingRepository urlMappingRepository, CodeGenerator codeGenerator, String baseUrl) {
        this.urlMappingRepository = urlMappingRepository;
        this.codeGenerator = codeGenerator;
        this.baseUrl = baseUrl;
    }

    public String shortenUrl(String originalUrl, Optional<LocalDateTime> expiresAt) {
        // 1. Check if URL already exists and is not expired
        Optional<UrlMapping> existingMapping = urlMappingRepository.findByOriginalUrl(originalUrl);
        if (existingMapping.isPresent()) {
            if (existingMapping.get().isExpired()) {
                urlMappingRepository.remove(existingMapping.get().getShortCode()); // Cleanup old expired mapping
            } else {
                return baseUrl + "/" + existingMapping.get().getShortCode();
            }
        }

        // 2. Generate a new unique short code
        String shortCode;
        do {
            shortCode = codeGenerator.generateCode();
        } while (urlMappingRepository.exists(shortCode));

        // 3. Save new mapping
        UrlMapping urlMapping = new UrlMapping(shortCode, originalUrl, expiresAt.orElse(null));
        urlMappingRepository.save(urlMapping);

        return baseUrl + "/" + shortCode;
    }

    public Optional<String> expandUrl(String shortCode) {
        Optional<UrlMapping> optionalUrlMapping = urlMappingRepository.findByShortCode(shortCode);

        if(optionalUrlMapping.isEmpty() || optionalUrlMapping.get().isExpired()) {
            urlMappingRepository.remove(shortCode);  // Cleanup old expired mapping
            return Optional.empty();
        }

        return Optional.of(optionalUrlMapping.get().getOriginalUrl());
    }
}
