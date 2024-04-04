package com.erman.codechallenge.dtos.posts;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaginationRequestDto {

    @NotBlank
    private Integer pageNumber;

    @NotBlank
    private Integer pageSize;

    private String sortByField = "createdAt";

    private String sortByDirection = "desc";

}
