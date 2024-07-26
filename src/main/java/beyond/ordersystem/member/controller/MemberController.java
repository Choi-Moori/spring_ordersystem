package beyond.ordersystem.member.controller;

import beyond.ordersystem.common.auth.JwtTokenProvider;
import beyond.ordersystem.common.dto.CommonResDto;
import beyond.ordersystem.member.domain.Member;
import beyond.ordersystem.member.dto.MemberCreateResDto;
import beyond.ordersystem.member.dto.MemberLoginDto;
import beyond.ordersystem.member.dto.MemberResDto;
import beyond.ordersystem.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public MemberController(MemberService memberService, JwtTokenProvider jwtTokenProvider){
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/create")
    public ResponseEntity<CommonResDto> memberCreate(@Valid @RequestBody MemberCreateResDto dto){
        return memberService.memberCreate(dto);
    }

    @PostMapping("/list")
    public ResponseEntity<?> memberList(Pageable pageable){
        Page<MemberResDto> dtos = memberService.memberList(pageable);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "members are found", dtos);
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }

    @PostMapping("/doLogin")
    @ResponseBody
    public ResponseEntity<?> doLogin(@RequestBody MemberLoginDto dto){
//        email, password가 일치하는지 검증
        Member member = memberService.login(dto);
//        일치할 경우 accessToken 생성
        String jwtToken = jwtTokenProvider.createToken(member.getEmail(), member.getRole().toString());

//        생성된 토큰을 CommonResDto에 담아 사용자에게 return
        Map<String, Object> loginInfo = new HashMap<>();
        loginInfo.put("id", member.getId());
        loginInfo.put("token", jwtToken);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "member login is successful", loginInfo);
        return new ResponseEntity<>(commonResDto,HttpStatus.OK);
    }
}
