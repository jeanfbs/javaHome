package br.com.javahome.model;

import java.util.List;

public record Outlet(
        String id,
        String name,
        Boolean inUse,
        Type type,
        Double powerUsage,
        List<ConsumeValue> historyConsume
) {

    public enum Type{
        SINGLE("app.messages.outlet.type.single"),
        DOUBLE("app.messages.outlet.type.double")
        ;

        private final String property;

        Type(String property) {
            this.property = property;
        }

        public String property() {
            return property;
        }
    }

    public static Builder builder(){
        return new Builder();
    }

    public Builder toBuilder(){
        return new Builder(id, name, inUse, type, powerUsage, historyConsume);
    }

    public static class Builder{

        private String id;
        private String name;
        private Boolean inUse;
        private Type type;
        private Double powerUsage;
        private List<ConsumeValue> historyConsume;

        private Builder() {
        }

        public Builder(String id, String name, Boolean inUse, Type type, Double powerUsage, List<ConsumeValue> historyConsume) {
            this.id = id;
            this.name = name;
            this.inUse = inUse;
            this.type = type;
            this.powerUsage = powerUsage;
            this.historyConsume = historyConsume;
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder inUse(Boolean inUse) {
            this.inUse = inUse;
            return this;
        }

        public Builder type(Type type) {
            this.type = type;
            return this;
        }

        public Builder powerUsage(Double powerUsage) {
            this.powerUsage = powerUsage;
            return this;
        }

        public Builder historyConsume(List<ConsumeValue> historyConsume) {
            this.historyConsume = historyConsume;
            return this;
        }

        public Outlet build(){
            return new Outlet(id, name, inUse, type, powerUsage, historyConsume);
        }
    }
}
