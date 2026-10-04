package org.acme.infra;

import java.util.List;

import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

/**
 * Serves the React entry point for direct browser navigation to client-side
 * routes while leaving REST, Quarkus tools, and static assets to their handlers.
 */
@ApplicationScoped
public class SpaRoutingFilter {

    private static final List<String> SERVER_PATH_PREFIXES = List.of(
            "/vehicles", "/users", "/payment", "/webhook",
            "/q", "/openapi", "/swagger-ui", "/health"
    );

    void registerSpaFallback(@Observes Router router) {
        router.route("/*").order(9999).handler(this::routeClientSidePages);
    }

    private void routeClientSidePages(RoutingContext context) {
        var request = context.request();
        var path = context.normalizedPath();
        var accept = request.getHeader("Accept");

        if (!("GET".equals(request.method().name()) || "HEAD".equals(request.method().name()))
                || path.equals("/")
                || accept == null || !accept.contains("text/html")
                || isServerPath(path)
                || hasFileExtension(path)) {
            context.next();
            return;
        }

        context.reroute("/");
    }

    private boolean isServerPath(String path) {
        return SERVER_PATH_PREFIXES.stream().anyMatch(prefix ->
                path.equals(prefix) || path.startsWith(prefix + "/"));
    }

    private boolean hasFileExtension(String path) {
        int lastSlash = path.lastIndexOf('/');
        int lastDot = path.lastIndexOf('.');
        return lastDot > lastSlash;
    }
}
