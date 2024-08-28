package com.matzip.api.matzip_api.domain.dosi.entity;

import com.matzip.api.matzip_api.domain.sgg.entity.Sgg;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "dosi")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoSi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "dosi", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Sgg> sggs = new ArrayList<>();
}
