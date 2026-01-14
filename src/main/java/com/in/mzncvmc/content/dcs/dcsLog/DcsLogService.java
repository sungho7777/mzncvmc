package com.in.mzncvmc.content.dcs.dcsLog;

import com.in.mzncvmc.common.system.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

@Log4j2
@Service
@RequiredArgsConstructor
public class DcsLogService {



    @Transactional
    public ApiResponse dcsLogAnalyze(@RequestBody DcsLogVeriftDto dcsLogVeriftDto){
        String serverNo = dcsLogVeriftDto.getServerNo();
        String logDate = dcsLogVeriftDto.getLogDate();




        return ApiResponse.success(true, "Dcs LogAnalyze Successfully.");
    }
}
