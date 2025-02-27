package com.moldavets.microservices.api_gateway.entity;

import com.moldavets.microservices.api_gateway.util.StatusEnum;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@MappedSuperclass
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @CreatedDate
    @Column(name = "created")
    Date created;

    @LastModifiedDate
    @Column(name = "updated")
    Date updated;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    StatusEnum status;

}
