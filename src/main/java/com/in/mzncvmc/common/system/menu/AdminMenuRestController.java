package com.in.mzncvmc.common.system.menu;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.in.mzncvmc.common.system.constants.CommonConstants.SLASH_API;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping(SLASH_API + "/menu")
public class AdminMenuRestController {

    @Autowired
    private final AdminMenuService adminMenuService;

    /**
     * 메뉴 트리 데이터 조회 (Ajax)
     */
    @GetMapping("/tree")
    public ResponseEntity<List<AdminMenuVO>> getMenuTree() {
        List<AdminMenuVO> menuTree = adminMenuService.getMenuTree();
        return ResponseEntity.ok(menuTree);
    }

    /**
     * 전체 메뉴 목록 조회 (Ajax)
     */
    @GetMapping("/all")
    public ResponseEntity<List<AdminMenuVO>> getAllMenus() {
        List<AdminMenuVO> menus = adminMenuService.getAllMenus();
        return ResponseEntity.ok(menus);
    }

    /**
     * 메뉴 상세 조회 (Ajax)
     */
    @GetMapping("/{menuId}")
    public ResponseEntity<AdminMenuVO> getMenu(@PathVariable Long menuId) {
        AdminMenuVO menu = adminMenuService.getMenuById(menuId);
        return ResponseEntity.ok(menu);
    }

    /**
     * 부모 메뉴 목록 조회 (Ajax)
     */
    @GetMapping("/parent-list")
    public ResponseEntity<List<AdminMenuVO>> getParentMenuList() {
        List<AdminMenuVO> parentMenus = adminMenuService.getParentMenuList();
        return ResponseEntity.ok(parentMenus);
    }

    /**
     * 메뉴 저장 (Ajax)
     */
    @PostMapping("/save")
    public ResponseEntity<Map<String, Object>> saveMenu(@RequestBody AdminMenuVO menuVO) {
        Map<String, Object> result = new HashMap<>();
        try {
            AdminMenuVO savedMenu = adminMenuService.saveMenu(menuVO);
            result.put("success", true);
            result.put("message", "메뉴가 저장되었습니다.");
            result.put("data", savedMenu);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "메뉴 저장 중 오류가 발생했습니다: " + e.getMessage());
        }
        return ResponseEntity.ok(result);
    }

    /**
     * 메뉴 삭제 (Ajax)
     */
    @DeleteMapping("/{menuId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteMenu(@PathVariable Long menuId) {
        Map<String, Object> result = new HashMap<>();
        try {
            adminMenuService.deleteMenu(menuId);
            result.put("success", true);
            result.put("message", "메뉴가 삭제되었습니다.");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "메뉴 삭제 중 오류가 발생했습니다: " + e.getMessage());
        }
        return ResponseEntity.ok(result);
    }
}
