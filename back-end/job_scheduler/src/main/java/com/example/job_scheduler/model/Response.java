package com.example.job_scheduler.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

//Mesaj ve veri gönderebilmek için hazırlanmış response sınıfı
@Data
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    LocalDateTime time;
    HttpStatus httpStatus;
    String reason;
    String message;
    int id;
    Map<?, ?> data;
}

