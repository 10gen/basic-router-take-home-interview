package main.java.mdb.router;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Router {
    private final Map<RouteKey, RequestHandler> routes = new HashMap<>();
    private final Map<Pattern, RouteKey> patternToHandlerKey = new HashMap<>();

    private final Set<String> validMethods = Set.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS", "HEAD");

    /**
     * Adds a route to the router with a specified HTTP method, path, and handler.
     * The route can be a static path or a dynamic segments enclosed in curly braces, e.g., "/users/{userId}".
     * For dynamic paths the method creates a regular expression to match the path pattern and extract path parameters.
     *
     * @param method  The HTTP method for the route (e.g., "GET", "POST").
     * @param path    The path for the route, which can be static (e.g., "/users") or dynamic (e.g., "/users/{userId}").
     * @param handler The RequestHandler to be executed when the route is matched. This handler is responsible
     *                for processing the request and returning an appropriate Response.
     */
    public void addRoute(String method, String path, RequestHandler handler) {
        if (!isValidPath(path)) {
            throw new IllegalArgumentException("Invalid path: " + path);
        }

        RouteKey routeKey = new RouteKey(method.toUpperCase(), path);
        routes.put(routeKey, handler);

        if (isDynamicPath(path)) {
            String regex = path.replaceAll("\\{\\w+\\}", "([^/]+)");
            Pattern pattern = Pattern.compile("^" + regex + "$");
            patternToHandlerKey.put(pattern, routeKey);
        }
    }

    private boolean isDynamicPath(String path) {
        return path.contains("{") && path.contains("}");
    }

    /**
     * Validates the incoming path for addition
     * @param path The path of the request
     * @return A boolean indicating path is valid or not.
     */
    private boolean isValidPath(String path) {
        if (!path.startsWith("/")) {
            return false;
        }

        if (!path.matches("/[a-zA-Z0-9_/\\{\\}-]*")) {
            return false;
        }

        String[] segments = path.split("/");
        for (String segment : segments) {
            if (segment.contains("{") || segment.contains("}")) {
                if (!segment.matches("\\{\\w+\\}")) {
                    return false;
                }
            }
        }

        if (path.contains("//") || path.contains("{/") || path.contains("/}") || path.contains("{}")) {
            return false;
        }

        return true;
    }


    /**
     * Routes an HTTP request to the appropriate handler based on the method and path.
     * It first checks for static routes and, if none are matched, checks for dynamic routes.
     * If no route is found, returns a 404 Not Found response.
     *
     * @param method The HTTP method of the request.
     * @param path   The path of the request.
     * @param body  The body of the request.
     * @return A Response object representing the result of the route handling.
     */
    public Response route(String method, String path, String body) {

        Response validatorResponse = routeValidator(method, path);
        if (validatorResponse != null) return validatorResponse;

        RouteKey staticRouteKey = new RouteKey(method.toUpperCase(), path);

        // Checking for a static route first, if there is a direct match
        if (routes.containsKey(staticRouteKey)) {
            RequestHandler handler = routes.get(staticRouteKey);
            return handler.handle(new Request(method, path, body));
        }

        // If there is no static route then check for dynamic patterns
        for (Map.Entry<Pattern, RouteKey> entry : patternToHandlerKey.entrySet()) {
            Matcher matcher = entry.getKey().matcher(path);
            if (matcher.matches()) {
                RouteKey dynamicRouteKey = entry.getValue();
                if (dynamicRouteKey.getMethod().equals(method.toUpperCase())) {
                    RequestHandler handler = routes.get(dynamicRouteKey);
                    if (handler != null) {
                        Request request = new Request(method, path, body);
                        extractPathParameters(matcher, request, dynamicRouteKey.getPath());
                        return handler.handle(request);
                    }
                }
            }
        }
        return new Response(404, "Not Found");
    }


    /**
     * Validates the incoming requests method and path.
     * Checks if the method is valid and if the path is not empty.
     *
     * @param method The HTTP method to validate.
     * @param path  The path to validate.
     * @return A Response object representing an error if validation fails; null if validation passes.
     */
    private Response routeValidator(String method, String path){
        if (method == null || method.isEmpty()) {
            return new Response(400, "HTTP Method is required");
        }

        if (!validMethods.contains(method.toUpperCase())) {
            return new Response(405, "Invalid HTTP Method");
        }

        if (path == null || path.isEmpty()) {
            return new Response(400, "Path is required");
        }
        return null;
    }

    /**
     * Extract the path parameters from the matched pattern and adds them to the request.
     *
     * @param matcher The Matcher object that contains the pattern match results.
     * @param request The Request object to which extracted path parameters will be added.
     * @param path  The path used to extract parameter names.
     */
    private void extractPathParameters(Matcher matcher, Request request, String path) {
        Pattern paramPattern = Pattern.compile("\\{\\w+\\}");
        Matcher paramMatcher = paramPattern.matcher(path);

        int index = 1;
        while (paramMatcher.find()) {
            String paramName = paramMatcher.group().substring(1, paramMatcher.group().length() - 1); // Remove '{' and '}'
            String paramValue = matcher.group(index++);
            request.addPathParameter(paramName, paramValue);
        }
    }

    /**
     * This class is to create the composite key for the Route.
     * The key is defined as a class to be easily expandable if we need to further optimize by
     * adding additional parameters like methods as the part of the key.
     */
    private static class RouteKey {
        private final String method;

        private final String path;

        RouteKey(String method, String path) {
            this.method = method;
            this.path = path;
        }

        public String getMethod() {
            return method;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            RouteKey routeKey = (RouteKey) o;
            return method.equals(routeKey.method) && path.equals(routeKey.path);
        }

        @Override
        public int hashCode() {
            return Objects.hash(method, path);
        }

        public String getPath() {
            return path;
        }
    }

}
