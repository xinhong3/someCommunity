package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.example.demo.dto.AccessTokenDTO;
import com.example.demo.dto.GithubUser;
import com.example.demo.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthorizeController {
    @Value("${github.client.id}")
    private String client_id;
//    private final String client_id = "a7a3c25c8b9dcc5ff0d0";

    @Value("${github.client.secret}")
    private String client_secret;
//    private final String client_secret = "b332850161bba99050a35968b1928e28ce85ecd4";

    @Value("${github.redirect.uri}")
    private String redirect_uri;
//    private final String redirect_uri = "http://localhost:7387/callback";

    @Autowired
    private GithubProvider githubProvider;

    @GetMapping("/callback")
    public String callback(@RequestParam(name="code") String code,
                           @RequestParam(name="state") String state) {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(client_id);
        accessTokenDTO.setClient_secret(client_secret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirect_uri);
        accessTokenDTO.setState(state);

        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(accessToken);
        System.out.println(JSON.toJSONString(githubUser));

        return "index";
    }
}