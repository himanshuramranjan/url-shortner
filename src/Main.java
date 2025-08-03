import generator.Base62CodeGenerator;
import generator.CodeGenerator;
import repository.InMemoryUrlMappingRepository;
import repository.UrlMappingRepository;
import service.UrlShortenerService;

import java.time.LocalDateTime;
import java.util.Optional;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        UrlMappingRepository repository = new InMemoryUrlMappingRepository();
        CodeGenerator generator = new Base62CodeGenerator();
        UrlShortenerService service = new UrlShortenerService(repository, generator, "http://short");

        String longUrl = "https://example.com/my-long-url";

        // === First shortening ===
        String shortUrl1 = service.shortenUrl(longUrl, Optional.of(LocalDateTime.now().plusSeconds(3)));
        System.out.println("First Shortened URL: " + shortUrl1);

        // === Shorten the same URL again (before expiry) ===
        String shortUrl2 = service.shortenUrl(longUrl, Optional.of(LocalDateTime.now().plusSeconds(3)));
        System.out.println("Second Shortened URL (should be same): " + shortUrl2);

        System.out.println(shortUrl1.equals(shortUrl2)
                ? "✅ Reused existing short code"
                : "❌ Generated a new code (unexpected)");

        // === Wait for expiration ===
        System.out.println("Waiting for URL to expire...");
        Thread.sleep(4000); // sleep > expiry duration

        // === Shorten again after expiry ===
        String shortUrl3 = service.shortenUrl(longUrl, Optional.of(LocalDateTime.now().plusMinutes(5)));
        System.out.println("Shortened URL after expiry: " + shortUrl3);

        System.out.println(!shortUrl1.equals(shortUrl3)
                ? "✅ New code generated after expiry"
                : "❌ Code was not regenerated (unexpected)");

        // === Expand latest code ===
        String latestCode = shortUrl3.substring(shortUrl3.lastIndexOf("/") + 1);

        service.expandUrl(latestCode).ifPresentOrElse(
                original -> System.out.println("Expanded to Original URL: " + original),
                () -> System.out.println("URL not found or expired")
        );
    }
}