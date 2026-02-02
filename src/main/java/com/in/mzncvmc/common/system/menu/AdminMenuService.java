package com.in.mzncvmc.common.system.menu;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminMenuService {
    @Autowired
    private final AdminMenuRepository adminMenuRepository;

    /**
     * 전체 메뉴 트리 구조로 조회 (VO 반환)
     * Entity는 내부에서만 사용하고 외부에는 VO만 노출
     */
    public List<AdminMenuVO> getMenuTree() {
        // 1. DB에서 전체 메뉴 조회 (Entity)
        List<AdminMenu> allMenus = adminMenuRepository.findByUseYnOrderByMenuOrderAsc("Y");

        // 2. Entity를 VO로 변환하면서 트리 구조 생성
        return buildMenuTree(allMenus, null);
    }

    /**
     * 메뉴 트리 구조 생성 (VO로 변환하여 반환)
     */
    private List<AdminMenuVO> buildMenuTree(List<AdminMenu> allMenus, Long parentId) {
        return allMenus.stream()
                .filter(menu -> {
                    // parentId가 null이면 최상위 메뉴(parent_id가 null인 메뉴)
                    if (parentId == null) {
                        return menu.getParentId() == null;
                    }
                    // parentId와 일치하는 자식 메뉴
                    return parentId.equals(menu.getParentId());
                })
                .map(menu -> {
                    // Entity -> VO 변환
                    AdminMenuVO vo = convertToVO(menu);

                    // 재귀적으로 자식 메뉴 조회 및 설정
                    List<AdminMenuVO> children = buildMenuTree(allMenus, menu.getMenuId());
                    vo.setChildren(children);
                    vo.setHasChildren(!children.isEmpty());

                    return vo;
                })
                .collect(Collectors.toList());
    }

    /**
     * 전체 메뉴 목록 조회 (플랫 리스트)
     */
    public List<AdminMenuVO> getAllMenus() {
        return adminMenuRepository.findByUseYnOrderByMenuOrderAsc("Y")
                .stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    /**
     * 메뉴 ID로 조회
     */
    public AdminMenuVO getMenuById(Long menuId) {
        return adminMenuRepository.findById(menuId)
                .map(this::convertToVO)
                .orElse(null);
    }

    /**
     * 메뉴 저장
     */
    @Transactional
    public AdminMenuVO saveMenu(AdminMenuVO menuVO) {
        // VO -> Entity 변환
        AdminMenu menu = convertToEntity(menuVO);

        // 저장
        AdminMenu savedMenu = adminMenuRepository.save(menu);

        // Entity -> VO 변환하여 반환
        return convertToVO(savedMenu);
    }

    /**
     * 메뉴 삭제
     */
    @Transactional
    public void deleteMenu(Long menuId) {
        adminMenuRepository.deleteById(menuId);
    }

    /**
     * 부모 메뉴 목록 조회 (셀렉트박스용)
     */
    public List<AdminMenuVO> getParentMenuList() {
        // 레벨 1, 2만 부모 메뉴로 사용 가능
        List<AdminMenu> menus = adminMenuRepository.findByUseYnOrderByMenuOrderAsc("Y");
        return menus.stream()
                .filter(menu -> menu.getMenuLevel() != null && menu.getMenuLevel() <= 2)
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    /**
     * Entity to VO 변환
     */
    private AdminMenuVO convertToVO(AdminMenu entity) {
        if (entity == null) {
            return null;
        }

        return AdminMenuVO.builder()
                .menuId(entity.getMenuId())
                .parentId(entity.getParentId())
                .menuName(entity.getMenuName())
                .menuUrl(entity.getMenuUrl())
                .menuOrder(entity.getMenuOrder())
                .menuLevel(entity.getMenuLevel())
                .useYn(entity.getUseYn())
                .iconClass(entity.getIconClass())
                .children(new ArrayList<>())  // 빈 리스트로 초기화
                .hasChildren(false)           // 기본값 false
                .build();
    }

    /**
     * VO to Entity 변환
     */
    private AdminMenu convertToEntity(AdminMenuVO vo) {
        if (vo == null) {
            return null;
        }

        return AdminMenu.builder()
                .menuId(vo.getMenuId())
                .parentId(vo.getParentId())
                .menuName(vo.getMenuName())
                .menuUrl(vo.getMenuUrl())
                .menuOrder(vo.getMenuOrder())
                .menuLevel(vo.getMenuLevel())
                .useYn(vo.getUseYn())
                .iconClass(vo.getIconClass())
                .build();
    }
}
