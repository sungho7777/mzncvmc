package com.in.mzncvmc.content.dcs.dcsLog;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DcsLogDto {
    private Long logId;
    private String serverNo;
    private String logDate;
    private String randomNo;
    private String userId;
    private String hsb;
    private String screenId;
    private String url;
    private String action;
    private String ip;
    private String type;
}
