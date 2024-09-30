package com.basic.dto.input;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProductInputDto(@NotBlank(message = "Name is required") String name,
                              @NotNull(message = "Price is required") @Positive(message = "Price should be positive") Double price){}

