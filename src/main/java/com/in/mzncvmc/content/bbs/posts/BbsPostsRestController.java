package com.in.mzncvmc.content.bbs.posts;

import com.in.mzncvmc.common.system.response.ApiResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import static com.in.mzncvmc.common.system.constants.CommonConstants.SLASH_API;
import static com.in.mzncvmc.common.system.constants.CommonConstants.SLASH_ID;

@Log4j2
@RestController
@RequestMapping(SLASH_API + "/bbs/bbsPosts")
public class BbsPostsRestController {
    private final BbsPostsService bbsPostsService;

    @Autowired
    public BbsPostsRestController(BbsPostsService bbsPostsService) {
        this.bbsPostsService = bbsPostsService;
    }

    // Search DTO
    public record BbsPostsSearchDto(String search) {}

    /**
     * C.해당 데이터 생성 (Create)
     *
     * @param !DataDto 생성할 데이터 DTO
     * @return ApiResponse<Long> 생성된 데이터 ID
     * @throws IllegalArgumentException 잘못된 입력 값
     */
    @PostMapping(SLASH_ID)
    public ApiResponse<Long> createData(@RequestBody BbsPostsDto dto) {
        log.debug("BbsPostsRestController.createData : " + dto);

        Long newDataId = bbsPostsService.insert(dto);

        return ApiResponse.success(newDataId, "New Data created successfully");
    }

    /**
     * R.해당 데이터 목록조회 (Read List)
     *
     * @author Park Sung Ho
     * @since 2025-09-09
     */
    @GetMapping
    public ApiResponse<Page<BbsPostsDto>> getPagedLists(
            @RequestParam(defaultValue = "${app.pagination.default-page:0}") int page,
            @RequestParam(defaultValue = "${app.pagination.default-size:10}") int size,
            @RequestParam(value = "categoryId", required = false) String categoryId,
            @RequestParam(required = false) String search) {

        Page<BbsPostsDto> result = bbsPostsService.getPagedLists(page, size, categoryId, search);
        return ApiResponse.success(result, "Data paged list retrieved successfully");
    }

    /**
     * R.해당 데이터 단일조회 (Read One)
     *
     * @param id 조회할 데이터 ID
     * @return ApiResponse<DataDto> 조회된 데이터 정보
     * @throws IllegalArgumentException 데이터 ID가 존재하지 않을 경우
     * @author Park Sung Ho
     * @since 2025-09-09
     */
    @GetMapping(SLASH_ID)
    public ApiResponse<BbsPostsDto> getData(@PathVariable Long id) {
        log.debug("BbsPostsRestController.getData : " + id);

        BbsPostsDto dto = bbsPostsService.findById(id);

        return ApiResponse.success(dto, "Data retrieved successfully");
    }

    /**
     * U.해당 데이터 수정 (Update)
     *
     * @param id 수정할 데이터 ID
     * @param !DataDto 수정할 데이터 DTO
     * @return ApiResponse<Long> 수정된 데이터 ID
     * @throws IllegalArgumentException ID 불일치 또는 데이터 미존재
     */
    @PutMapping(SLASH_ID)
    public ApiResponse<Long> updateData(@PathVariable Long id, @RequestBody BbsPostsDto dto) {
        log.debug("BbsPostsRestController.updateData : " + dto);

        if (!id.equals(dto.getPostId())) {
            throw new IllegalArgumentException("ID 불일치");
        }

        bbsPostsService.update(dto);

        return ApiResponse.success(id, "Data updated successfully");
    }

    /**
     * D.해당 데이터 삭제 (Delete)
     *
     * @param id 삭제할 데이터 ID
     * @return ApiResponse<Long> 삭제된 데이터 ID
     * @throws IllegalArgumentException 데이터 미존재
     */
    @DeleteMapping(SLASH_ID)
    public ApiResponse<Long> deleteData(@PathVariable Long id) {
        log.debug("BbsPostsRestController.deleteData : " + id);

        bbsPostsService.delete(id);

        return ApiResponse.success(id, "Data deleted successfully");
    }
}