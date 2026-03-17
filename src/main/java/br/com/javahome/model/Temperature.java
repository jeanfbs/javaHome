package br.com.javahome.model;

import java.util.List;

public record Temperature(
        String id,
        String name,
        Double currentTemperature,
        Double relativeHumidity,
        List<ConsumeValue> historyConsume
) {
    public static Builder builder(){
        return new Builder();
    }

    public Builder toBuilder(){
        return new Builder(id, name, currentTemperature, relativeHumidity, historyConsume);
    }

    public static class Builder{

        private String id;
        private String name;
        private Double currentTemperature;
        private Double relativeHumidity;
        private List<ConsumeValue> historyConsume;

        private Builder() {
        }

        public Builder(String id, String name, Double currentTemperature, Double relativeHumidity, List<ConsumeValue> historyConsume) {
            this.id = id;
            this.name = name;
            this.currentTemperature = currentTemperature;
            this.relativeHumidity = relativeHumidity;
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

        public Builder currentTemperature(Double currentTemperature) {
            this.currentTemperature = currentTemperature;
            return this;
        }

        public Builder relativeHumidity(Double relativeHumidity) {
            this.relativeHumidity = relativeHumidity;
            return this;
        }

        public Builder historyConsume(List<ConsumeValue> historyConsume) {
            this.historyConsume = historyConsume;
            return this;
        }

        public Temperature build(){
            return new Temperature(id, name, currentTemperature, relativeHumidity, historyConsume);
        }
    }
    
}
