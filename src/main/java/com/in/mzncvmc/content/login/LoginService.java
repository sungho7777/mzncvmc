package com.in.mzncvmc.content.login;

import com.in.mzncvmc.content.users.Users;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    public Users login(String username, String password) {
        if (("admin".equals(username) || "admin@gmail.com".equals(username)) && "1".equals(password)) {

            Users users = new Users();
            users.setUsername(username);
            users.setFullName("Park Sung Ho");

            return users;
        }

        return null;
    }
}
