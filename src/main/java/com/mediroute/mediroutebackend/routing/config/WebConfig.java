// WHAT: Spring MVC configuration class that enables Cross-Origin Resource Sharing (CORS) for all
//       /api/** endpoints so the Next.js frontend can call the Spring Boot backend from a browser.

// WHY: Browsers enforce the Same-Origin Policy — by default they block JavaScript fetch/XHR requests
//      from one origin (localhost:3000, the Next.js dev server) to a different origin (localhost:8080,
//      the Spring Boot API). Without this CORS configuration the frontend routing page would receive
//      a network error on every API call and no route results would be displayed.

// HOW: Implementing WebMvcConfigurer and overriding addCorsMappings() lets Spring MVC intercept
//      preflight OPTIONS requests and inject the correct Access-Control-Allow-* headers automatically.
//      The mapping covers all paths under /api/**, allows requests from localhost:3000, and permits
//      the four HTTP methods used by the routing endpoints (GET for ping, POST for route queries).

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
