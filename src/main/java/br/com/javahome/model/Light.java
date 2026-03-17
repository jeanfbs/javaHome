package br.com.javahome.model;

import java.time.LocalDateTime;
import java.util.List;

public record Light(
        String id,
        String name,
        Double brightness,
        Boolean state,
        LocalDateTime lastChanged,
        List<ConsumeValue> historyConsume
) {

    public static Builder builder(){
        return new Builder();
    }

    public Builder toBuilder(){
        return new Builder(id, name, brightness, state, lastChanged, historyConsume);
    }

    public static class Builder{

        private String id;
        private String name;
        private Double brightness;
        private Boolean state;
        private LocalDateTime lastChanged;
        private List<ConsumeValue> historyConsume;

        private Builder() {
        }

        public Builder(String id, String name, Double brightness, Boolean state, LocalDateTime lastChanged,
                       List<ConsumeValue> historyConsume) {
            this.id = id;
            this.name = name;
            this.brightness = brightness;
            this.state = state;
            this.lastChanged = lastChanged;
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

        public Builder brightness(Double brightness) {
            this.brightness = brightness;
            return this;
        }

        public Builder state(Boolean state) {
            this.state = state;
            return this;
        }

        public Builder lastChanged(LocalDateTime lastChanged) {
            this.lastChanged = lastChanged;
            return this;
        }

        public Builder historyConsume(List<ConsumeValue> historyConsume) {
            this.historyConsume = historyConsume;
            return this;
        }

        public Light build(){
            return new Light(id, name, brightness, state, lastChanged, historyConsume);
        }
    }
}
