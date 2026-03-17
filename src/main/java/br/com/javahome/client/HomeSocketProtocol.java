package br.com.javahome.client;

import br.com.javahome.enums.Type;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public record HomeSocketProtocol(
        Type method,
        String path,
        Map<String, String> headers,
        String body
) {

    public static HomeSocketProtocol fromText(String text){
        var lines = text.split("\n");
        String[] firstLineTokens = lines[0].split(" ");
        Builder builder = HomeSocketProtocol.builder()
                .method(Type.valueOf(firstLineTokens[0].toUpperCase()))
                .path(firstLineTokens[1]);
        Map<String, String> headers = new HashMap<>();
        for (int i = 1; i < lines.length - 3; i++) {
            String[] headerSplit = lines[i].split(":");
            headers.putIfAbsent(headerSplit[0].trim(), headerSplit[1].trim());
        }
        return builder.headers(headers)
            .body(lines[lines.length - 1]).build();
    }

    public String writeAsString(){
        var headersStr = headers.entrySet().stream().map(entry -> entry.getKey() + ":" + entry.getValue())
                .collect(Collectors.joining("\n"));
        return """
                %s %s HOMESOCKET/1.0
                %s
                
                
                %s
                """.formatted(method.name(), path, headersStr, body);
    }

    private String getBodyOrEmpty(){
        return body != null ? body : "";
    }


    public static Builder builder(){
        return new Builder();
    }

    public Builder toBuilder(){
        return new Builder(method, path, headers, body);
    }

    public static class Builder {

        private Type method;
        private String path;
        private Map<String, String> headers;
        private String body;

        private Builder() {
        }

        public Builder(Type method, String path, Map<String, String> headers, String body) {
            this.method = method;
            this.path = path;
            this.headers = headers;
            this.body = body;
        }

        public Builder method(Type method) {
            this.method = method;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder headers(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public HomeSocketProtocol build(){
            return new HomeSocketProtocol(method, path, headers, body);
        }
    }
}
