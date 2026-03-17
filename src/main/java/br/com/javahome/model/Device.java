package br.com.javahome.model;

public record Device (
        String id,
        String name,
        Type type,
        Boolean status
) {

    public enum Type{
        PRESENCE("app.messages.device.type.presence"),
        MAGNETIC("app.messages.device.type.magnetic"),
        DEFAULT("app.messages.device.type.default"),
        ;

        private final String property;

        Type(String property) {
            this.property = property;
        }

        public String property() {
            return property;
        }
    }
}
