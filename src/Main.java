import generator.Base62CodeGenerator;
import generator.CodeGenerator;
import repository.InMemoryUrlMappingRepository;
import repository.UrlMappingRepository;
import service.UrlShortenerService;

import java.time.LocalDateTime;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {

        UrlMappingRepository repository = new InMemoryUrlMappingRepository();
        CodeGenerator generator = new Base62CodeGenerator();
        UrlShortenerService service = new UrlShortenerService(repository, generator, "http://short");

        String longUrl = "https://example.com/my-long-url";
        String shortUrl = service.shortenUrl(longUrl, Optional.of(LocalDateTime.now().plusMinutes(5)));

        System.out.println("Shortened URL: " + shortUrl);

        String code = shortUrl.substring(shortUrl.lastIndexOf("/") + 1);

        System.out.println("Code :" + code);

        service.expandUrl(code).ifPresentOrElse(
                original -> System.out.println("Original URL: " + original),
                () -> System.out.println("URL not found or expired")
        );
    }
}