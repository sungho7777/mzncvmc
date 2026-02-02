package com.in.mzncvmc.common.auth.oAuth;

import com.in.mzncvmc.common.auth.login.LoginService;
import com.in.mzncvmc.common.system.jwt.JwtUtil;
import com.in.mzncvmc.common.system.util.ClientUtil;
import com.in.mzncvmc.common.system.util.CookieUtil;
import com.in.mzncvmc.common.system.util.SessionUtil;
import com.in.mzncvmc.content.users.Users;
import com.in.mzncvmc.content.users.UsersService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.UUID;

import static com.in.mzncvmc.common.system.constants.CommonConstants.SLASH_API;

@Controller
@RequestMapping(SLASH_API + "/oauth")
@RequiredArgsConstructor
public class OAuthController {
    @Autowired
    private final OAuthService oAuthService;
    @Autowired
    private final UsersService usersService;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private final JwtUtil jwtUtil;
    @Autowired
    private CookieUtil cookieUtil;
    @Autowired
    private SessionUtil sessionUtil;
    @Autowired
    private ClientUtil clientUtil;
    @Autowired
    private LoginService loginService;

    @Value("${oauth.google.client-id}")
    private String googleClientId;

    @Value("${oauth.google.redirect-uri}")
    private String googleRedirectUri;

    @Value("${oauth.google.authorization-uri}")
    private String googleAuthorizationUri;

    @Value("${oauth.kakao.client-id}")
    private String kakaoClientId;

    @Value("${oauth.kakao.redirect-uri}")
    private String kakaoRedirectUri;

    @Value("${oauth.kakao.authorization-uri}")
    private String kakaoAuthorizationUri;

    @Value("${oauth.naver.client-id}")
    private String naverClientId;

    @Value("${oauth.naver.redirect-uri}")
    private String naverRedirectUri;

    @Value("${oauth.naver.authorization-uri}")
    private String naverAuthorizationUri;


    // Google 로그인 시작 (인가 엔드포인트로 리다이렉트)
    @GetMapping("/google")
    public String googleLogin() throws UnsupportedEncodingException {
        String redirectUri = URLEncoder.encode(googleRedirectUri, "UTF-8");
        String authorizationUrl = googleAuthorizationUri +
                "?client_id=" + googleClientId +
                "&redirect_uri=" + redirectUri +
                "&response_type=code" +
                "&scope=email profile";

        return "redirect:" + authorizationUrl;
    }

    // Google 콜백 (Authorization Code 수신 및 처리)
    // TODO 현재 방화벽 정책 때문에 회사서는 못 함.. 나중에 노트북에서 할 것.
    @GetMapping("/callback/google")
    public void googleCallback(@RequestParam(required=false) String code,
                                            @RequestParam(required=false) String error,
                                            HttpSession session,
                                            HttpServletRequest httpRequest,
                                            HttpServletResponse response) throws IOException {
        // 1. 구글 Access Token (절대 외부 노출 X)
        String googleAccessToken = oAuthService.getGoogleAccessToken(code);

        // 2. 구글 사용자 정보 조회
        OAuthUserInfo userInfo = oAuthService.getGoogleUserInfo(googleAccessToken);

        // 3. 사용자 처리
        Users users = usersService.processOAuthUser(userInfo);

        // 4. 우리 서버용 JWT 발급
        UserDetails userDetails =
                userDetailsService.loadUserByUsername(users.getUsername());

        String jwtAccessToken = jwtUtil.generateToken(userDetails);

        // 5. JWT만 쿠키/세션에 저장
        cookieUtil.insertAccessTokenCookie("accessToken", jwtAccessToken, response);
        sessionUtil.insertAccessTokenSession("accessToken", jwtAccessToken, httpRequest);
        sessionUtil.insertUserDetailsSession("loginUser", userDetails, httpRequest);

        response.sendRedirect("/main");
    }

    // Kakao 로그인 시작
    @GetMapping("/kakao")
    public String kakaoLogin() throws UnsupportedEncodingException {
        String redirectUri = URLEncoder.encode(kakaoRedirectUri, "UTF-8");
        String authorizationUrl = kakaoAuthorizationUri +
                "?client_id=" + kakaoClientId +
                "&redirect_uri=" + redirectUri +
                "&response_type=code";

        return "redirect:" + authorizationUrl;
    }

    // Kakao 콜백
    @GetMapping("/callback/kakao")
    public String kakaoCallback(@RequestParam String code,
                                HttpSession session,
                                HttpServletResponse response) {
        try {
            String accessToken = oAuthService.getKakaoAccessToken(code);
            OAuthUserInfo userInfo = oAuthService.getKakaoUserInfo(accessToken);
            Users user = usersService.processOAuthUser(userInfo);

            // TODO error
            //String jwtToken = jwtUtil.generateToken(user.getEmail(), user.getId(), user.getRole().name());

            //session.setAttribute("userId", user.getId()); // TODO error
            session.setAttribute("userEmail", user.getEmail());
            //session.setAttribute("userName", user.getName()); // TODO error
            session.setAttribute("accessToken", accessToken);

            //Cookie jwtCookie = new Cookie("jwt", jwtToken); // TODO error
            Cookie jwtCookie = new Cookie("jwt", "jwtToken");
            jwtCookie.setHttpOnly(true);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(24 * 60 * 60);
            response.addCookie(jwtCookie);

            return "redirect:/dashboard";

        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/login?error=oauth_failed";
        }
    }

    // Naver 로그인 시작
    @GetMapping("/naver")
    public String naverLogin(HttpSession session) throws UnsupportedEncodingException {
        String state = UUID.randomUUID().toString();
        session.setAttribute("oauth_state", state);

        String redirectUri = URLEncoder.encode(naverRedirectUri, "UTF-8");
        String authorizationUrl = naverAuthorizationUri +
                "?client_id=" + naverClientId +
                "&redirect_uri=" + redirectUri +
                "&response_type=code" +
                "&state=" + state;

        return "redirect:" + authorizationUrl;
    }

    // Naver 콜백
    @GetMapping("/callback/naver")
    public String naverCallback(@RequestParam String code,
                                @RequestParam String state,
                                HttpSession session,
                                HttpServletResponse response) {
        try {
            String sessionState = (String) session.getAttribute("oauth_state");
            if (!state.equals(sessionState)) {
                return "redirect:/login?error=invalid_state";
            }

            String accessToken = oAuthService.getNaverAccessToken(code, state);
            OAuthUserInfo userInfo = oAuthService.getNaverUserInfo(accessToken);
            Users user = usersService.processOAuthUser(userInfo);

            // TODO error
            //String jwtToken = jwtUtil.generateToken(user.getEmail(), user.getId(), user.getRole().name());

            //session.setAttribute("userId", user.getId()); // TODO error
            session.setAttribute("userEmail", user.getEmail());
            //session.setAttribute("userName", user.getName()); // TODO error
            session.setAttribute("accessToken", accessToken);

            //Cookie jwtCookie = new Cookie("jwt", jwtToken); // TODO error
            Cookie jwtCookie = new Cookie("jwt", "jwtToken");
            jwtCookie.setHttpOnly(true);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(24 * 60 * 60);
            response.addCookie(jwtCookie);

            return "redirect:/dashboard";

        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/login?error=oauth_failed";
        }
    }

    // 로그아웃
    @GetMapping("/logout")
    public String logout(HttpSession session, HttpServletResponse response) {
        session.invalidate();

        Cookie jwtCookie = new Cookie("jwt", null);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0);
        response.addCookie(jwtCookie);

        return "redirect:/login";
    }
}
