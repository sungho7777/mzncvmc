package com.in.mzncvmc.content.dcs.dcsLog;


import com.in.mzncvmc.content.dcs.dcsLog.DcsLogRepository;
import com.in.mzncvmc.common.system.response.ApiResponse;
import com.in.mzncvmc.content.users.Users;
import com.in.mzncvmc.content.users.UsersDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.*;
import java.time.LocalTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log4j2
@Service
@RequiredArgsConstructor
public class DcsLogService {


    static int pNo = 0;
    private static String[] uniqueArr;

    // 값별 현재 순번 기록용
    Map<String, Integer> sequenceMap = new HashMap<>();
    // 최종 저장 데이터
    List<String[]> resultList = new ArrayList<>();
    
    
    @Autowired
    private final DcsLogRepository dcsLogRepository;


    /**
     * C.데이터 생성
     *
     * @param dto 해당 정보 DTO
     * @return 생성된 데이터 ID
     * @throws IllegalArgumentException 입력 값 검증 실패 시
     */
    @Transactional
    public ApiResponse dcsLogAnalyze(@RequestBody DcsLogVeriftDto dcsLogVeriftDto){
        String serverNo = dcsLogVeriftDto.getServerNo();
        String logDate = dcsLogVeriftDto.getLogDate();

        String[] logFileArr = {
                "C:\\dev\\workspaceStringIN\\mzncvmc\\file\\"+serverNo+"_"+logDate+"_server.log"
        };
        LocalTime nowLocalTime = LocalTime.now();
        String formattedNowTime = String.format("%02d%02d%02d", nowLocalTime.getHour(), nowLocalTime.getMinute(), nowLocalTime.getSecond());
        String saveFilePath = serverNo+"_WEBLOG_" + ( logDate + "_" + formattedNowTime)+".txt";


        int[] processYnArr;
        processYnArr = new int[]{
                0	// 0 Server Stop	: DWS WEB_APP_SERVER STOP
                , 0	// 1 Server Start	: DWS WEB_APP_SERVER START
                , 0 // DCS System

                , 0 // input R no
                , 0 // output R no

                , 0 // error
        };

        for (int i = 0; i < logFileArr.length; i++) {
            try(BufferedReader br = new BufferedReader(new FileReader(logFileArr[i]))){
                String line;
                while((line = br.readLine()) != null) {
                    // Server Stop
                    if(processYnArr[0] < 1) {
                        stopServerLogMsg(dcsLogVeriftDto, line, " DWS WEB_APP_SERVER STOP ");
                    }
                    // Server Start
                    if(processYnArr[1] < 1) {
                        startServerLogMsg(dcsLogVeriftDto, line, " DWS WEB_APP_SERVER START ");
                    }
                    // DCS System
                    if(processYnArr[2] < 1) {
                        dcsSystemLogMsg(dcsLogVeriftDto, line, " DCS System");
                    }
                    // input R no
                    if(processYnArr[3] < 1) {
                        dataInputRLogMsg(dcsLogVeriftDto, line, "<input:R", "ScreenProcessController");
                    }
                    // output R no
                    if(processYnArr[4] < 1) {
                        dataOutputRLogMsg(dcsLogVeriftDto, line, "<output:R", "ScreenProcessController");
                    }
                    // UT005023 : Exception handling request to
                    //	- 해당 워크플로를 트리거 하기 위해 rest API 를 싱행 하는 동안 오류가 발생

                    // ISPN000136: Error executing command GetKeyValueCommand
                    // ISPN000299: Unable to acquire lock after 60 seconds for key SessionCreationMetaDataKey
                    //	- EJB 사양에 따르면 동시 액세스르 허용하지 않습니다.
                    //	- 호출이 감기고 동시 호출은 시간 초과까지 잠금을 기다립니다.
                    //	-
                    if(processYnArr[5] < 1) {
                        errorExecutingCommandGetKeyValueCommandLogMsg(dcsLogVeriftDto, line, " ERROR ", "UT005023: Exception handling request to", "ISPN000299: Unable to acquire lock after 60 seconds for key SessionCreationMetaDataKey");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return ApiResponse.success(true, "Dcs LogAnalyze Successfully.");
    }

    /**
     * R.데이터 목록조회
     *
     * @param search 조회할 목록 데이터 search
     * @return DataList 조회된 목록 데이터
     *
     */
    @Transactional(readOnly = true)
    public Page<DcsLogDto> getPagedLists(int page, int size, String search, String status) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "userId"));

        Page<DcsLog> DataPage;

        DataPage = dcsLogRepository.findAll(pageable);

        return DataPage.map(this::entityToDto);
    }

    /**
     * D.데이터 삭제
     *
     * @param dcsLogVeriftDto
     */
    @Transactional
    public void deleteByServerNoAndLogDate(@RequestBody DcsLogVeriftDto dcsLogVeriftDto){
        String serverNo = dcsLogVeriftDto.getServerNo();
        String logDate = dcsLogVeriftDto.getLogDate();

        dcsLogRepository.deleteByServerNoAndLogDate(serverNo, logDate);
    }
    /**
     * ETC.엔티티 → DTO 변환용 private 메서드
     *
     * @param !Entity 데이터
     */
    private DcsLogDto entityToDto(DcsLog entity) {
        DcsLogDto dto = DcsLogDto.builder()
                .serverNo(entity.getServerNo())
                .logDate(entity.getLogDate())
                .logTime(entity.getLogTime())
                .randomNo(entity.getRandomNo())
                .userId(entity.getUserId())
                .hsb(entity.getHsb())
                .screenId(entity.getScreenId())
                .url(entity.getUrl())
                .action(entity.getAction())
                .ip(entity.getIp())
                .type(entity.getType())

                .build();

        return dto;
    }




    public void stopServerLogMsg(@RequestBody DcsLogVeriftDto dcsLogVeriftDto, String line, String logMsg) {
        String serverNo = dcsLogVeriftDto.getServerNo();
        String logDate = dcsLogVeriftDto.getLogDate();

        Pattern pattern = Pattern.compile(logMsg);
        Matcher matcher = pattern.matcher(line);

        String logText = "";
        String[] logTextArr = null;

        DcsLog dcsLog = new DcsLog();
        if(matcher.find()) {
            logText = (++pNo) + " " + line;
            logText = logText.replaceAll("  ", " ").replaceAll(" ", "|");
            logTextArr = logText.split("\\|");

            String num = logTextArr[0];
            String date = logTextArr[1];
            String time = logTextArr[2];

            // save
            dcsLog.setServerNo(serverNo);
            dcsLog.setLogDate(date);
            dcsLog.setLogTime(time);
            dcsLog.setRandomNo(serverNo);
            dcsLog.setUserId("DWS");
            dcsLog.setHsb("EMPTY");
            dcsLog.setScreenId("EMPTY");
            dcsLog.setUrl("EMPTY");
            dcsLog.setAction("EMPTY");
            dcsLog.setIp("EMPTY");
            dcsLog.setType("WEB_APP_SERVER_STOP");

            DcsLog saved = dcsLogRepository.save(dcsLog);
        }
    }
    public void startServerLogMsg(@RequestBody DcsLogVeriftDto dcsLogVeriftDto, String line, String logMsg) {
        String serverNo = dcsLogVeriftDto.getServerNo();
        String logDate = dcsLogVeriftDto.getLogDate();

        Pattern pattern = Pattern.compile(logMsg);
        Matcher matcher = pattern.matcher(line);

        String logText = "";
        String[] logTextArr = null;

        DcsLog dcsLog = new DcsLog();
        if(matcher.find()) {
            logText = (++pNo) + " " + line;
            logText = logText.replaceAll("  ", " ").replaceAll(" ", "|");
            logTextArr = logText.split("\\|");
            // save
            dcsLog.setServerNo(serverNo);
            dcsLog.setLogDate(logTextArr[1]);
            dcsLog.setLogTime(logTextArr[2]);
            dcsLog.setRandomNo(serverNo);
            dcsLog.setUserId("DWS");
            dcsLog.setHsb("EMPTY");
            dcsLog.setScreenId("EMPTY");
            dcsLog.setUrl("EMPTY");
            dcsLog.setAction("EMPTY");
            dcsLog.setIp("EMPTY");
            dcsLog.setType("WEB_APP_SERVER_START");

            DcsLog saved = dcsLogRepository.save(dcsLog);
        }
    }
    public void dcsSystemLogMsg(@RequestBody DcsLogVeriftDto dcsLogVeriftDto, String line, String logMsg) {
        String serverNo = dcsLogVeriftDto.getServerNo();
        String logDate = dcsLogVeriftDto.getLogDate();

        Pattern pattern = Pattern.compile(logMsg);
        Matcher matcher = pattern.matcher(line);

        //String result = "";
        String logText = "";
        String[] logTextArr = null;

        DcsLog dcsLog = new DcsLog();
        if(matcher.find()) {
            logText = line;
            logTextArr = logText.split("DCS System\\|");
            //result = logTextArr.length > 1 ? logTextArr[1] : "";

            logText = (++pNo) + " " + line;
            logText = logText.replaceAll("  ", " ").replaceAll(" ", "|");
            logText = logText.replaceAll("\\(default\\|task-", "(defaulttask-");
            logTextArr = logText.split("\\|");

            System.out.println();
            System.out.println(logText);
            // 1|2026-01-12|04:04:36,254|INFO|[my.com.klse.dcs.controller.user.Service]|[81]|(defaulttask-1)|DCS|System|R040436|BMDC.NIZAM|LrpkxzOVJl-UB..|0000|/user/service/login.dcs||172.16.4.101|login
            // save
            dcsLog.setServerNo(serverNo);
            dcsLog.setLogDate(logTextArr[1]);
            dcsLog.setLogTime(logTextArr[2]);
            dcsLog.setRandomNo(logTextArr[9]);
            dcsLog.setUserId(logTextArr[10]);
            dcsLog.setHsb(logTextArr[11]);
            dcsLog.setScreenId(logTextArr[12]);
            dcsLog.setUrl(logTextArr[13]);
            dcsLog.setAction(logTextArr[14]);
            dcsLog.setIp(logTextArr[15]);
            dcsLog.setType(logTextArr[16]);

            DcsLog saved = dcsLogRepository.save(dcsLog);
        }
    }


    private void dataInputRLogMsg(@RequestBody DcsLogVeriftDto dcsLogVeriftDto, String line, String logMsg1, String logMsg2) {
        String serverNo = dcsLogVeriftDto.getServerNo();
        String logDate = dcsLogVeriftDto.getLogDate();

        Pattern pattern1 = Pattern.compile(logMsg1);
        Pattern pattern2 = Pattern.compile(logMsg2);

        Matcher matcher1 = pattern1.matcher(line);
        Matcher matcher2 = pattern2.matcher(line);

        String result = "";
        String logText = "";
        String[] logTextArr = null;

        DcsLog dcsLog = new DcsLog();
        if(matcher1.find() && matcher2.find()) {
            logText = line;
            logTextArr = logText.split("\\<input\\:R");
            logTextArr = logTextArr[1].split("\\> base");

            result = logTextArr.length > 1 ? "R"+logTextArr[0] : "";

            logText = (++pNo) + " " + line;
            logText = logText.replaceAll("  ", " ").replaceAll(" ", "|");
            logTextArr = logText.split("\\|");

            System.out.println();
            System.out.println(logText);
            // save
            dcsLog.setServerNo(serverNo);
            dcsLog.setLogDate(logTextArr[1]);
            dcsLog.setLogTime(logTextArr[2]);
            dcsLog.setRandomNo(result);
            dcsLog.setUserId("EMPTY");
            dcsLog.setHsb("EMPTY");
            dcsLog.setScreenId("EMPTY");
            dcsLog.setUrl("EMPTY");
            dcsLog.setAction("EMPTY");
            dcsLog.setIp("EMPTY");
            dcsLog.setType("INPUT_DATA");

            DcsLog saved = dcsLogRepository.save(dcsLog);
        }
    }
    private void dataOutputRLogMsg(@RequestBody DcsLogVeriftDto dcsLogVeriftDto, String line, String logMsg1, String logMsg2) {
        String serverNo = dcsLogVeriftDto.getServerNo();
        String logDate = dcsLogVeriftDto.getLogDate();

        Pattern pattern1 = Pattern.compile(logMsg1);
        Pattern pattern2 = Pattern.compile(logMsg2);

        Matcher matcher1 = pattern1.matcher(line);
        Matcher matcher2 = pattern2.matcher(line);

        String result = "";
        String logText = "";
        String[] logTextArr = null;

        DcsLog dcsLog = new DcsLog();
        if(matcher1.find() && matcher2.find()) {
            logText = line;
            logTextArr = logText.split("\\<output\\:R");
            logTextArr = logTextArr[1].split("\\> outputHeader");

            result = logTextArr.length > 1 ? "R"+logTextArr[0] : "";

            logText = (++pNo) + " " + line;
            logText = logText.replaceAll("  ", " ").replaceAll(" ", "|");
            logTextArr = logText.split("\\|");

            System.out.println();
            System.out.println(logText);
            // save
            dcsLog.setServerNo(serverNo);
            dcsLog.setLogDate(logTextArr[1]);
            dcsLog.setLogTime(logTextArr[2]);
            dcsLog.setRandomNo(result);
            dcsLog.setUserId("EMPTY");
            dcsLog.setHsb("EMPTY");
            dcsLog.setScreenId("EMPTY");
            dcsLog.setUrl("EMPTY");
            dcsLog.setAction("EMPTY");
            dcsLog.setIp("EMPTY");
            dcsLog.setType("OUTPUT_DATA");

            DcsLog saved = dcsLogRepository.save(dcsLog);
        }
    }

    private void errorExecutingCommandGetKeyValueCommandLogMsg(@RequestBody DcsLogVeriftDto dcsLogVeriftDto, String line, String logMsg1, String logMsg2, String logMsg3) {
        String serverNo = dcsLogVeriftDto.getServerNo();
        String logDate = dcsLogVeriftDto.getLogDate();

        Pattern pattern1 = Pattern.compile(logMsg1);
        Pattern pattern2 = Pattern.compile(logMsg2);
        Pattern pattern3 = Pattern.compile(logMsg3);

        Matcher matcher1 = pattern1.matcher(line);
        Matcher matcher2 = pattern2.matcher(line);
        Matcher matcher3 = pattern3.matcher(line);

        String result = "";
        String logText = "";
        String[] logTextArr = null;
        DcsLog dcsLog = new DcsLog();
        if(matcher1.find() && matcher2.find() && matcher3.find()) {
            logText = line;
            logTextArr = logText.split("SessionCreationMetaDataKey");
            logTextArr = logTextArr[1].replaceAll("\\(", "").split("\\) and requestor GlobalTransaction");

            result = logTextArr.length > 1 ? logTextArr[0] : "";

            logText = (++pNo) + " " + line;
            logText = logText.replaceAll("  ", " ").replaceAll(" ", "|");
            logTextArr = logText.split("\\|");

            // RandomNo
            int seq = sequenceMap.getOrDefault(result, 0) + 1;
            sequenceMap.put(result, seq);
            // (값, 순번) 저장
            resultList.add(new String[]{result, String.valueOf(seq)});
            
            System.out.println();
            System.out.println(logText);
            // save
            dcsLog.setServerNo(serverNo);
            dcsLog.setLogDate(logTextArr[1]);
            dcsLog.setLogTime(logTextArr[2]);
            dcsLog.setRandomNo(String.valueOf(seq));
            dcsLog.setUserId("EMPTY");
            dcsLog.setHsb(result);
            dcsLog.setScreenId("EMPTY");
            dcsLog.setUrl("EMPTY");
            dcsLog.setAction("EMPTY");
            dcsLog.setIp("EMPTY");
            dcsLog.setType("ERR_LOCK");

            DcsLog saved = dcsLogRepository.save(dcsLog);
        }
    }
}
