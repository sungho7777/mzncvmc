package com.in.mzncvmc.content.users;

import com.in.mzncvmc.content.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        logger.debug("UserssRestcontroller.createData : " + dto);

        Long newUserId = usersService.insert(dto);

        return ApiResponse.success(newUserId, "Users created successfully");
    }

    /**
     * R.해당 데이터 목록조회 (Read List)
     *
     * @author Park Sung Ho
     * @since 2025-09-09
     */
    // /api/users?page=0&size=10&search=xxx&status=ACTIVE
    @GetMapping
    public ApiResponse<Page<UsersDto>> getPagedUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status) {

        Page<UsersDto> result = usersService.getPagedUsers(page, size, search, status);
        return ApiResponse.success(result, "Users paged list retrieved successfully");
    }

    //@PostMapping
    public List<UsersDto> getList__(@RequestBody UsersSearchDto searchDto) {
        String search = searchDto.search();
        String status = searchDto.status();

        logger.debug("UsersRestcontroller.getList : search='{}', status='{}'", search, status);



        return null;//usersService.findUsers(search, status);
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

        UsersDto user = usersService.findById(id);

        return ApiResponse.success(user, "User retrieved successfully");
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
        logger.debug("UsersRestcontroller.updateData : " + id);
        logger.debug("UsersRestcontroller.updateData : " + dto);
        if (!id.equals(dto.getUserId())) {
            throw new IllegalArgumentException("ID 불일치");
        }

        usersService.update(dto);

        return ApiResponse.success(id, "Users updated successfully");
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

        return ApiResponse.success(id, "Users deleted successfully");
    }
}
