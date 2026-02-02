package com.in.mzncvmc.common.system.menu;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminMenuVO {
    private Long menuId;
    private Long parentId;
    private String menuName;
    private String menuUrl;
    private Integer menuOrder;
    private Integer menuLevel;
    private String useYn;
    private String iconClass;

    // 트리 구조 표현을 위한 필드
    @Builder.Default
    private List<AdminMenuVO> children = new ArrayList<>();

    // 화면 표시용 추가 필드
    private String parentMenuName;
    private Boolean hasChildren;
}
