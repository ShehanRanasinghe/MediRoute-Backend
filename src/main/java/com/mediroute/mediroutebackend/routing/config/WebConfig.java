// This configuration allows the frontend to call the backend API from a different local port during development.
// It sets the CORS policy so browser requests to the Spring application are accepted when the UI runs on localhost:3000.
// This is needed because the frontend and backend are separate services and browser security would otherwise block the requests.

package com.mediroute.mediroutebackend.routing.config; // Declares the package this class belongs to

import org.springframework.context.annotation.Configuration; // Imports @Configuration to mark this as a Spring configuration class
import org.springframework.web.servlet.config.annotation.CorsRegistry; // Imports CorsRegistry used to register CORS rules for specific URL patterns
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer; // Imports the WebMvcConfigurer interface whose methods Spring calls to customise MVC behaviour

@Configuration // Tells Spring that this class provides bean definitions and configuration; processed at startup
public class WebConfig implements WebMvcConfigurer { // Implements WebMvcConfigurer to hook into Spring MVC's configuration callbacks

    @Override // Signals that this method overrides the default (empty) implementation in WebMvcConfigurer
    public void addCorsMappings(CorsRegistry registry) { // Called by Spring at startup to register CORS rules; receives the registry to add mappings to
        registry.addMapping("/api/**") // Applies CORS rules to every URL path that starts with /api/ (covers ping, shortest-path, compare)
                .allowedOrigins("http://localhost:3000") // Permits requests originating from the Next.js development server on port 3000
                .allowedMethods("GET", "POST", "PUT", "DELETE"); // Allows these HTTP methods so the frontend can call GET (ping) and POST (routing) endpoints
    }
}
