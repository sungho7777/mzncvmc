package com.in.mzncvmc.common.handler;

import com.in.mzncvmc.content.users.UsersService;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class ServerShutdownHandler implements ServletContextListener {

    private final UsersService usersService;

    public ServerShutdownHandler(UsersService usersService) {
        this.usersService = usersService;
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        log.debug("Context closed : N");


        // TODO 접속자 정보 업데이트 잘 안되고 있음... 수정 할 것.
        usersService.updateAllUsersConnected("N");
    }

}
