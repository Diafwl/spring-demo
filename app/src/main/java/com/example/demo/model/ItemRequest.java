package com.example.demo.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ItemRequest(
        @NotBlank(message = "name is required") String name,
        @Min(value = 0, message = "quantity must not be negative") int quantity) {
}
