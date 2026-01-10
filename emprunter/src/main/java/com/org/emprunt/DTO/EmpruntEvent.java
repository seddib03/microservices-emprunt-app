package com.org.emprunt.DTO;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmpruntEvent {
    private Long empruntId;
    private Long userId;
    private Long bookId;
    private LocalDateTime dateEmprunt;
}