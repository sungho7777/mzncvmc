package com.in.mzncvmc.content.userMfa;

import com.in.mzncvmc.common.system.response.ApiResponse;
import com.in.mzncvmc.content.users.UsersDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import static com.in.mzncvmc.common.system.constants.CommonConstants.SLASH_API;
import static com.in.mzncvmc.common.system.constants.CommonConstants.SLASH_ID;

@Log4j2
@RestController
@RequestMapping(SLASH_API + "/userMfa")
public class UserMfaRestController {
    private final UserMfaService userMfaService;

    @Autowired
    public UserMfaRestController(UserMfaService userMfaService) {
        this.userMfaService = userMfaService;
    }


    /**
     * R.해당 데이터 목록조회 (Read List)
     *
     * @author Park Sung Ho
     * @since 2025-09-09
     */
    @GetMapping
    public ApiResponse<Page<UserMfaDto>> getPagedLists(
            @RequestParam(defaultValue = "${app.pagination.default-page:0}") int page,
            @RequestParam(defaultValue = "${app.pagination.default-size:10}") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status) {

        Page<UserMfaDto> result = userMfaService.getPagedLists(page, size, search, status);
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
    public ApiResponse<UserMfaDto> getData(@PathVariable Long id) {
        log.debug("UserMfaRestcontroller.getData : " + id);

        UserMfaDto dto = userMfaService.findByUserId(id);

        return ApiResponse.success(dto, "Data retrieved successfully");
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
        log.debug("UserMfaRestcontroller.deleteData : " + id);

        userMfaService.resetUserMfa(id);

        return ApiResponse.success(id, "Data deleted successfully");
    }
}
