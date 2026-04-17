package com.pucminas.car_rental_system.domain.entity;

/**
 * Interface polimórfica para proprietários de veículos.
 * Implementa o Open/Closed Principle (OCP) — SOLID.
 */
public interface CarOwner {
    Long getId();
    String getName();
}

