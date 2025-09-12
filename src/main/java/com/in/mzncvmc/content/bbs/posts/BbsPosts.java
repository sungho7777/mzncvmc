package com.in.mzncvmc.content.bbs.posts;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bbs_posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BbsPosts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long post_id;



    // TODO ing...
}
