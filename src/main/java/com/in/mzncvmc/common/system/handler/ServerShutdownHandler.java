package com.in.mzncvmc.common.system.handler;

import com.in.mzncvmc.content.users.UsersService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class ServerShutdownHandler implements DisposableBean {

    private final UsersService usersService;

    public ServerShutdownHandler(UsersService usersService) {
        this.usersService = usersService;
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("서버 종료 시점에 실행!");


        usersService.updateAllUsersConnected("N");
    }

}
