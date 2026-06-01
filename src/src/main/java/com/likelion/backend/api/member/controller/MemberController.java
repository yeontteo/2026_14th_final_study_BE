package com.likelion.backend.api.member.controller;

import com.likelion.backend.api.member.service.MemberService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "회원(Member)", description = "회원 관련 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping( "/api/v1/members")
public class MemberController {

    private final MemberService memberService;


}
