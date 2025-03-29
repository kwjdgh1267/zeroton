package com.example.zeroton.service;

import com.example.zeroton.dto.CustomUserDetails;
import com.example.zeroton.dto.JwtToken;
import com.example.zeroton.entity.Member;
import com.example.zeroton.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    @Transactional
    public String signUp(String id, String password, String name){
        Optional<Member> user = memberRepository.findById(id);
        if(user.isPresent()){
            return memberRepository.findById(id).get().getObjectId()+" already exist"; //상태코드 반환하도록 수정해야함.

        }
        Member member = new Member(id, password,name);
        memberRepository.save(member);
        System.out.println(memberRepository.findById(id));
        return "sign up";

    }

    @Transactional
    public JwtToken signIn(String id, String password){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(id, password);
        Authentication authentication;
        try{
            //loadUserByUsername 호출
            authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        }catch (BadCredentialsException e){
            e.printStackTrace();
            return null;
        }
        if(!authentication.isAuthenticated()){
            return null;
        }
        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);
        return jwtToken;
    }

}
