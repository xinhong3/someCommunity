package com.example.demo.provider;

import com.alibaba.fastjson.JSON;
import com.example.demo.dto.AccessTokenDTO;
import com.example.demo.dto.GithubUser;
import com.squareup.okhttp.*;
//import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GithubProvider {
    public static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("text/x-markdown; charset=utf-8");
    private static final OkHttpClient client = new OkHttpClient();

    public String getAccessToken(AccessTokenDTO accessTokenDTO) {
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

        // create post body
        RequestBody requestBody = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));

        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(requestBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            String string = response.body().string();
            String[] split = string.split("&");
            String tokenStr = split[0];
            String token = tokenStr.split("=")[1];
            return token;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public GithubUser getUser(String accessToken) {
        Request request = new Request.Builder()
                .url("https://api.github.com/user")
                .header("Authorization", "token " + accessToken)
                .build();
//        Headers.Builder headerBuilder = new Headers.Builder();
//        headerBuilder.add("access_token", accessToken);
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            System.out.println(string);
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
            return githubUser;
        }
        catch (IOException e) {
            return null;
        }
    }
}
