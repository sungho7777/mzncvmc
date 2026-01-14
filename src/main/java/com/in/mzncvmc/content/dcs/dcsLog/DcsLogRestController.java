package com.in.mzncvmc.content.dcs.dcsLog;

import com.in.mzncvmc.common.system.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @PostMapping("/dcsLogAnalyze")
    public ApiResponse dcsLogAnalyze(@RequestBody DcsLogVeriftDto dcsLogVeriftDto) {



        return dcsLogService.dcsLogAnalyze(dcsLogVeriftDto);
    }

}
