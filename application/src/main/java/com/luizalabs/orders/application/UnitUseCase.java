package com.luizalabs.orders.application;

public interface UnitUseCase<INPUT> {
    void execute(INPUT input);
}
