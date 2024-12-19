package com.luizalabs.orders.application;

public interface UseCase<INPUT, OUTPUT> {
    OUTPUT execute(INPUT input);
}
