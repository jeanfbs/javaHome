package br.com.javahome.model;

public record House(
        String residenceName,
        Double totalArea,
        String address,
        String location,
        Integer roomAmount,
        Boolean status
) {

    public static Builder builder(){
        return new Builder();
    }

    public Builder toBuilder(){
        return new Builder(residenceName, totalArea, address, location, roomAmount,status);
    }

    public static class Builder{

        private String residenceName;
        private Double totalArea;
        private String address;
        private String location;
        private Integer roomAmount;
        private Boolean status;

        private Builder() {
        }

        public Builder(String residenceName, Double totalArea, String address, String location,
                       Integer roomAmount, Boolean status) {
            this.residenceName = residenceName;
            this.totalArea = totalArea;
            this.address = address;
            this.location = location;
            this.roomAmount = roomAmount;
            this.status = status;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public Builder residenceName(String residenceName) {
            this.residenceName = residenceName;
            return this;
        }

        public Builder totalArea(Double totalArea) {
            this.totalArea = totalArea;
            return this;
        }

        public Builder location(String location) {
            this.location = location;
            return this;
        }

        public Builder roomAmount(Integer roomAmount) {
            this.roomAmount = roomAmount;
            return this;
        }

        public Builder status(Boolean status) {
            this.status = status;
            return this;
        }

        public House build(){
            return new House(residenceName, totalArea, address, location, roomAmount,status);
        }
    }

}
