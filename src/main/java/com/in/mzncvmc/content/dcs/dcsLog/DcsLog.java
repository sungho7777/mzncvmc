package com.in.mzncvmc.content.dcs.dcsLog;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "dcs_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DcsLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id", unique = true, nullable = false)
    private Long logId;
    @Column(name = "server_no")
    private String serverNo;
    @Column(name = "log_date")
    private String logDate;
    @Column(name = "log_time")
    private String logTime;
    @Column(name = "random_no")
    private String randomNo;
    @Column(name = "User_id")
    private String userId;
    @Column(name = "hsb")
    private String hsb;
    @Column(name = "screen_id")
    private String screenId;
    @Column(name = "url")
    private String url;
    @Column(name = "action")
    private String action;
    @Column(name = "ip")
    private String ip;
    @Column(name = "type")
    private String type;
}
