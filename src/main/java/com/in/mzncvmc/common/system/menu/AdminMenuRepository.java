package com.in.mzncvmc.common.system.menu;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminMenuRepository extends JpaRepository<AdminMenu, Long> {

    // 사용 중인 메뉴만 조회 (정렬 순서대로)
    List<AdminMenu> findByUseYnOrderByMenuOrderAsc(String useYn);

    // 특정 부모 ID의 자식 메뉴 조회
    //List<AdminMenu> findByParentAndUseYnOrderByMenuOrderAsc(Long parent, String useYn);

    // 최상위 메뉴 조회 (parent_id가 null인 메뉴)
    //List<AdminMenu> findByParentIsNullAndUseYnOrderByMenuOrderAsc(String useYn);

    // 메뉴 레벨별 조회
    //List<AdminMenu> findByMenuLevelAndUseYnOrderByMenuOrderAsc(Integer menuLevel, String useYn);

}