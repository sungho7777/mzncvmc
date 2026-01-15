package com.in.mzncvmc.content.dcs.dcsLog;

import com.in.mzncvmc.common.system.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import static com.in.mzncvmc.common.system.constants.CommonConstants.SLASH_API;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping(SLASH_API + "/dcs/dcsLog")
public class DcsLogRestController {
    @Autowired
    private final DcsLogService dcsLogService;


    /**
     * C.해당 데이터 생성 (Create)
     *
     * @param !DataDto 생성할 데이터 DTO
     * @return ApiResponse<Long> 생성된 데이터 ID
     * @throws IllegalArgumentException 잘못된 입력 값
     */
    @PostMapping()
    public ApiResponse<Long> createData(@RequestBody DcsLogVeriftDto dcsLogVeriftDto) {

        dcsLogService.deleteByServerNoAndLogDate(dcsLogVeriftDto);

        return dcsLogService.dcsLogAnalyze(dcsLogVeriftDto);
    }

    /**
     * R.해당 데이터 목록조회 (Read List)
     *
     * @author Park Sung Ho
     * @since 2025-09-09
     */
    @GetMapping
    public ApiResponse<Page<DcsLogDto>> getPagedLists(
            @RequestParam(defaultValue = "${app.pagination.default-page:0}") int page,
            @RequestParam(defaultValue = "${app.pagination.default-size:10}") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status) {

        Page<DcsLogDto> result = dcsLogService.getPagedLists(page, size, search, status);
        return ApiResponse.success(result, "Data paged list retrieved successfully");
    }
}
