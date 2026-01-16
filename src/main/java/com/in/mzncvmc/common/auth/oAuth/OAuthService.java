package com.in.mzncvmc.common.auth.oAuth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.in.mzncvmc.common.auth.oAuth.dto.*;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class OAuthService {

    @Value("${oauth.google.client-id}")
    private String googleClientId;

    @Value("${oauth.google.client-secret}")
    private String googleClientSecret;

    @Value("${oauth.google.redirect-uri}")
    private String googleRedirectUri;

    @Value("${oauth.google.token-uri}")
    private String googleTokenUri;

    @Value("${oauth.google.user-info-uri}")
    private String googleUserInfoUri;

    @Value("${oauth.kakao.client-id}")
    private String kakaoClientId;

    @Value("${oauth.kakao.client-secret}")
    private String kakaoClientSecret;

    @Value("${oauth.kakao.redirect-uri}")
    private String kakaoRedirectUri;

    @Value("${oauth.kakao.token-uri}")
    private String kakaoTokenUri;

    @Value("${oauth.kakao.user-info-uri}")
    private String kakaoUserInfoUri;

    @Value("${oauth.naver.client-id}")
    private String naverClientId;

    @Value("${oauth.naver.client-secret}")
    private String naverClientSecret;

    @Value("${oauth.naver.redirect-uri}")
    private String naverRedirectUri;

    @Value("${oauth.naver.token-uri}")
    private String naverTokenUri;

    @Value("${oauth.naver.user-info-uri}")
    private String naverUserInfoUri;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClients.createDefault();

    // Google Access Token 요청
    public String getGoogleAccessToken(String code) throws IOException {
        HttpPost httpPost = new HttpPost(googleTokenUri);

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("code", code));
        params.add(new BasicNameValuePair("client_id", googleClientId));
        params.add(new BasicNameValuePair("client_secret", googleClientSecret));
        params.add(new BasicNameValuePair("redirect_uri", googleRedirectUri));
        params.add(new BasicNameValuePair("grant_type", "authorization_code"));

        httpPost.setEntity(new UrlEncodedFormEntity(params));

        HttpResponse response = httpClient.execute(httpPost);
        String responseBody = EntityUtils.toString(response.getEntity());

        OAuthTokenResponse tokenResponse = objectMapper.readValue(responseBody, OAuthTokenResponse.class);
        return tokenResponse.getAccess_token();
    }

    // Google 사용자 정보 조회
    public OAuthUserInfo getGoogleUserInfo(String accessToken) throws IOException {
        HttpGet httpGet = new HttpGet(googleUserInfoUri);
        httpGet.setHeader("Authorization", "Bearer " + accessToken);

        HttpResponse response = httpClient.execute(httpGet);
        String responseBody = EntityUtils.toString(response.getEntity());

        GoogleUserInfo googleUser = objectMapper.readValue(responseBody, GoogleUserInfo.class);

        return OAuthUserInfo.builder()
                .providerId(googleUser.getId())
                .email(googleUser.getEmail())
                .name(googleUser.getName())
                .profileImage(googleUser.getPicture())
                .provider("GOOGLE")
                .build();
    }

    // Kakao Access Token 요청
    public String getKakaoAccessToken(String code) throws IOException {
        HttpPost httpPost = new HttpPost(kakaoTokenUri);

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("grant_type", "authorization_code"));
        params.add(new BasicNameValuePair("client_id", kakaoClientId));
        params.add(new BasicNameValuePair("client_secret", kakaoClientSecret));
        params.add(new BasicNameValuePair("redirect_uri", kakaoRedirectUri));
        params.add(new BasicNameValuePair("code", code));

        httpPost.setEntity(new UrlEncodedFormEntity(params));

        HttpResponse response = httpClient.execute(httpPost);
        String responseBody = EntityUtils.toString(response.getEntity());

        OAuthTokenResponse tokenResponse = objectMapper.readValue(responseBody, OAuthTokenResponse.class);
        return tokenResponse.getAccess_token();
    }

    // Kakao 사용자 정보 조회
    public OAuthUserInfo getKakaoUserInfo(String accessToken) throws IOException {
        HttpGet httpGet = new HttpGet(kakaoUserInfoUri);
        httpGet.setHeader("Authorization", "Bearer " + accessToken);

        HttpResponse response = httpClient.execute(httpGet);
        String responseBody = EntityUtils.toString(response.getEntity());

        KakaoUserInfo kakaoUser = objectMapper.readValue(responseBody, KakaoUserInfo.class);

        return OAuthUserInfo.builder()
                .providerId(String.valueOf(kakaoUser.getId()))
                .email(kakaoUser.getKakao_account().getEmail())
                .name(kakaoUser.getKakao_account().getProfile().getNickname())
                .profileImage(kakaoUser.getKakao_account().getProfile().getProfile_image_url())
                .provider("KAKAO")
                .build();
    }

    // Naver Access Token 요청
    public String getNaverAccessToken(String code, String state) throws IOException {
        HttpPost httpPost = new HttpPost(naverTokenUri);

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("grant_type", "authorization_code"));
        params.add(new BasicNameValuePair("client_id", naverClientId));
        params.add(new BasicNameValuePair("client_secret", naverClientSecret));
        params.add(new BasicNameValuePair("code", code));
        params.add(new BasicNameValuePair("state", state));

        httpPost.setEntity(new UrlEncodedFormEntity(params));

        HttpResponse response = httpClient.execute(httpPost);
        String responseBody = EntityUtils.toString(response.getEntity());

        OAuthTokenResponse tokenResponse = objectMapper.readValue(responseBody, OAuthTokenResponse.class);
        return tokenResponse.getAccess_token();
    }

    // Naver 사용자 정보 조회
    public OAuthUserInfo getNaverUserInfo(String accessToken) throws IOException {
        HttpGet httpGet = new HttpGet(naverUserInfoUri);
        httpGet.setHeader("Authorization", "Bearer " + accessToken);

        HttpResponse response = httpClient.execute(httpGet);
        String responseBody = EntityUtils.toString(response.getEntity());

        NaverUserInfo naverUser = objectMapper.readValue(responseBody, NaverUserInfo.class);
        NaverUserInfo.Response naverResponse = naverUser.getResponse();

        return OAuthUserInfo.builder()
                .providerId(naverResponse.getId())
                .email(naverResponse.getEmail())
                .name(naverResponse.getName())
                .profileImage(naverResponse.getProfile_image())
                .provider("NAVER")
                .build();
    }
}