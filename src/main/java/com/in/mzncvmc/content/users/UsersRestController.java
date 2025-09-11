package com.in.mzncvmc.content.users;

import com.in.mzncvmc.content.common.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import static com.in.mzncvmc.content.common.CommonConstants.*;

@RestController
@RequestMapping(SLASH_API + "/users")
public class UsersRestController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final UsersService usersService;

    @Autowired
    public UsersRestController(UsersService usersService) {
        this.usersService = usersService;
    }

    // Search DTO
    public record UsersSearchDto(String search, String status) {}

    /**
     * C.해당 데이터 생성 (Create)
     *
     * @param !DataDto 생성할 데이터 DTO
     * @return ApiResponse<Long> 생성된 데이터 ID
     * @throws IllegalArgumentException 잘못된 입력 값
     */
    @PostMapping(SLASH_ID)
    public ApiResponse<Long> createData(@RequestBody UsersDto dto) {
        logger.debug("UsersRestcontroller.createData : " + dto);

        Long newDataId = usersService.insert(dto);

        return ApiResponse.success(newDataId, "New Data created successfully");
    }

    /**
     * R.해당 데이터 목록조회 (Read List)
     *
     * @author Park Sung Ho
     * @since 2025-09-09
     */
    @GetMapping
    public ApiResponse<Page<UsersDto>> getPagedUsers(
            @RequestParam(defaultValue = "${app.pagination.default-page:0}") int page,
            @RequestParam(defaultValue = "${app.pagination.default-size:10}") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status) {

        Page<UsersDto> result = usersService.getPagedDatas(page, size, search, status);
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
    public ApiResponse<UsersDto> getData(@PathVariable Long id) {
        logger.debug("UsersRestcontroller.getData : " + id);

        UsersDto dto = usersService.findById(id);

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
    public ApiResponse<Long> updateData(@PathVariable Long id, @RequestBody UsersDto dto) {
        logger.debug("UsersRestcontroller.updateData : " + dto);

        if (!id.equals(dto.getUserId())) {
            throw new IllegalArgumentException("ID 불일치");
        }

        usersService.update(dto);

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
        logger.debug("UsersRestcontroller.deleteData : " + id);

        usersService.delete(id);

        return ApiResponse.success(id, "Data deleted successfully");
    }
}
