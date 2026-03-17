package br.com.javahome.model;

public record HomeData(
        String name,
        Double totalArea,
        String address,
        String coordinates,
        Boolean status,
        Integer roomAmount
) { }
