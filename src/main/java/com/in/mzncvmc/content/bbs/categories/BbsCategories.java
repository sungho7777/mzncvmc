package com.in.mzncvmc.content.bbs.categories;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bbs_categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BbsCategoris {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long category_id;



    // TODO ing...
}
