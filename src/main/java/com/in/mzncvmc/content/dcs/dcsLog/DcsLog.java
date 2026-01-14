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
    @Column(name = "server_no", nullable = false)
    private String serverNo;
    @Column(name = "log_date", nullable = false)
    private String logDate;
    @Column(name = "random_no", nullable = false)
    private String randomNo;
    @Column(name = "User_id", nullable = false)
    private String userId;
    @Column(name = "hsb", nullable = false)
    private String hsb;
    @Column(name = "screen_id", nullable = false)
    private String screenId;
    @Column(name = "url", nullable = false)
    private String url;
    @Column(name = "action", nullable = false)
    private String action;
    @Column(name = "ip", nullable = false)
    private String ip;
    @Column(name = "type", nullable = false)
    private String type;
}
