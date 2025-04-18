package service;

import generator.CodeGenerator;
import model.UrlMapping;
import repository.UrlMappingRepository;

import javax.swing.text.html.Option;
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
        String shortCode;

        do {
            shortCode = codeGenerator.generateCode();
        } while(urlMappingRepository.exists(shortCode));

        UrlMapping urlMapping = new UrlMapping(shortCode, originalUrl, expiresAt.orElse(null));
        urlMappingRepository.save(urlMapping);

        return baseUrl + "/" + shortCode;
    }

    public Optional<String> expandUrl(String shortCode) {
        Optional<UrlMapping> optionalUrlMapping = urlMappingRepository.findByShortCode(shortCode);

        if(optionalUrlMapping.isEmpty() || optionalUrlMapping.get().isExpired()) {
            return Optional.empty();
        }

        return Optional.of(optionalUrlMapping.get().getOriginalUrl());
    }
}
