package com.webframework2.work.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    
    @NotBlank(message = "상품 이름을 입력해주세요")
    private String name;

    @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
    private int price;
}
