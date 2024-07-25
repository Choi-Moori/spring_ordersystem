package beyond.ordersystem.member.service;

//import beyond.ordersystem.common.WebConfig;
import beyond.ordersystem.common.dto.CommonResDto;
import beyond.ordersystem.member.domain.Member;
import beyond.ordersystem.member.dto.MemberCreateResDto;
import beyond.ordersystem.member.dto.MemberResDto;
import beyond.ordersystem.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
//    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MemberService(MemberRepository memberRepository /*PasswordEncoder passwordEncoder*/){
        this.memberRepository = memberRepository;
        //this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<CommonResDto> memberCreate(MemberCreateResDto dto){
        if(dto.getPassword().length()<8){
            throw new IllegalArgumentException("비밀번호가 너무 짧습니다.");
        }
        if(memberRepository.findByEmail(dto.getEmail()).isPresent())
            throw new IllegalArgumentException("중복된 이메일입니다.");

        Member member = dto.toEntity();
        memberRepository.save(member);
        CommonResDto memberResDto = new CommonResDto(HttpStatus.CREATED, "Member is successful created", member.getId());

        return new ResponseEntity<>(memberResDto, HttpStatus.CREATED);
    }

    public Page<MemberResDto> memberList(Pageable pageable){
        Page<Member> members = memberRepository.findAll(pageable);
        return members.map(Member::fromEntity);
    }
}
