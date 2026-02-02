package com.in.mzncvmc.common.system.menu;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "admin_menu")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Long menuId;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "menu_name", nullable = false, length = 100)
    private String menuName;

    @Column(name = "menu_url", length = 255)
    private String menuUrl;

    @Column(name = "menu_order")
    private Integer menuOrder;

    @Column(name = "menu_level")
    private Integer menuLevel;

    @Column(name = "use_yn", length = 1)
    private String useYn;

    @Column(name = "icon_class", length = 100)
    private String iconClass;
}
