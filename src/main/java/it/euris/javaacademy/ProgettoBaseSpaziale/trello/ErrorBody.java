package it.euris.javaacademy.ProgettoBaseSpaziale.trello;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ErrorBody {
    private String message;
    private String error;
}
