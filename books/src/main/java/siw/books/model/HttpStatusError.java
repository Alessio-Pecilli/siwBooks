package  siw.books.model;

public enum HttpStatusError {
    OK(200L, "Success"),
    CREATED(201L, "Created successfully"),
    ACCEPTED(202L, "Request accepted"),
    NO_CONTENT(204L, "No content"),
    BAD_REQUEST(400L, "Bad request"),
    UNAUTHORIZED(401L, "Unauthorized access"),
    FORBIDDEN(403L, "Access forbidden"),
    NOT_FOUND(404L, "Resource not found"),
    METHOD_NOT_ALLOWED(405L, "Method not allowed"),
    CONFLICT(409L, "Conflict with current state"),
    UNSUPPORTED_MEDIA_TYPE(415L, "Unsupported media type"),
    UNPROCESSABLE_ENTITY(422L, "Unprocessable entity"),
    TOO_MANY_REQUESTS(429L, "Too many requests"),
    INTERNAL_SERVER_ERROR(500L, "Internal server error"),
    NOT_IMPLEMENTED(501L, "Not implemented"),
    BAD_GATEWAY(502L, "Bad gateway"),
    SERVICE_UNAVAILABLE(503L, "Service unavailable"),
    GATEWAY_TIMEOUT(504L, "Gateway timeout");
    private final Long code;
    private final String description;
    HttpStatusError(Long code, String description) {
        this.code = code;
        this.description = description;
    }
    public Long getCode() {
        return code;
    }
    public String getDescription() {
        return description;
    }
    // Metodo per ottenere la descrizione dato il codice
    public static String getDescriptionByCode(Long code) {
        for (HttpStatusError status : HttpStatusError.values()) {
            if (status.getCode().equals(code)) {
                return status.getDescription();
            }
        }
        return "Unknown HTTP status code";
    }
    // Metodo per ottenere l'enum dato il codice
    public static HttpStatusError getByCode(Long code) {
        for (HttpStatusError status : HttpStatusError.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid HTTP status code: " + code);
    }
}
