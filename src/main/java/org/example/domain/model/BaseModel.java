package org.example.domain.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;
@Data
public abstract class BaseModel {
    {
        id = UUID.randomUUID();
        createdDate = LocalDateTime.now();
        updatedDate = LocalDateTime.now();
    }
    protected UUID id;
    protected LocalDateTime createdDate;
    protected LocalDateTime updatedDate;
}
