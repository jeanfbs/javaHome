package br.com.javahome.model;

import java.util.List;

public record RoomData(
        String name,
        Double totalArea,
        Integer lightSwitchAmount,
        Double doorSize,
        Light light,
        List<Outlet> outlets,
        Temperature temperature,
        List<Device> security
) {

    public static Builder builder(){
        return new Builder();
    }

    public Builder toBuilder(){
        return new Builder(name, totalArea, lightSwitchAmount, doorSize, light, outlets, temperature, security);
    }

    public static class Builder{
        private String name;
        private Double totalArea;
        private Integer lightSwitchAmount;
        private Double doorSize;
        private Light light;
        private List<Outlet> outlets;
        private Temperature temperature;
        private List<Device> security;

        private Builder() {
        }

        public Builder(String name, Double totalArea, Integer lightSwitchAmount, Double doorSize,
                       Light light, List<Outlet> outlets, Temperature temperature, List<Device> security) {
            this.name = name;
            this.totalArea = totalArea;
            this.lightSwitchAmount = lightSwitchAmount;
            this.doorSize = doorSize;
            this.light = light;
            this.outlets = outlets;
            this.temperature = temperature;
            this.security = security;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder totalArea(Double totalArea) {
            this.totalArea = totalArea;
            return this;
        }

        public Builder lightSwitchAmount(Integer lightSwitchAmount) {
            this.lightSwitchAmount = lightSwitchAmount;
            return this;
        }

        public Builder doorSize(Double doorSize) {
            this.doorSize = doorSize;
            return this;
        }

        public Builder light(Light light) {
            this.light = light;
            return this;
        }

        public Builder outlets(List<Outlet> outlets) {
            this.outlets = outlets;
            return this;
        }

        public Builder temperature(Temperature temperature) {
            this.temperature = temperature;
            return this;
        }

        public Builder security(List<Device> security) {
            this.security = security;
            return this;
        }

        public RoomData build(){
            return new RoomData(name, totalArea, lightSwitchAmount, doorSize, light, outlets, temperature, security);
        }
    }
}
