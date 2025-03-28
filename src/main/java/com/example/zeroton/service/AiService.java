package com.example.zeroton.service;

import com.example.zeroton.dto.AiRequest;
import com.example.zeroton.dto.AiResponse;
import com.example.zeroton.dto.CustomUserDetails;
import com.example.zeroton.dto.AiMessage;
import com.example.zeroton.entity.Member;
import com.example.zeroton.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AiService {
    private final MemberRepository memberRepository;

    @Qualifier("openaiRestTemplate")
    @Autowired
    private RestTemplate restTemplate;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;


    public String chat(String prompt){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Member currentMember = memberRepository.findById(userDetails.getUsername()).get();

        String system = "json 내용을 정리해서 회의 내용을 요약하고, 각 사람마다 todo list를 만들어서 json으로 반환해줘. 다음과 같은 형식으로 " +
                "{\n" +
                "  \"summary\": \"회의 요약내용\",\n" +
                "  \"todo\": {\n" +
                "    \"name1\": \"todo1\",\n" +
                "    \"name2\": \"todo2\",\n" +
                "    \"name3\": \"todo3\"\n" +
                "  }\n" +
                "}";


        // create a request
        AiRequest request = new AiRequest(model);
        List<AiMessage> aiMessages = request.getMessages();
        aiMessages.add(new AiMessage("system", system));
        aiMessages.add(new AiMessage("user", prompt));


        // call the API
        AiResponse response = restTemplate.postForObject(apiUrl, request, AiResponse.class);

        if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
            return "No response";
        }
//        recordRepository.save(new Record(new Message("user", prompt),currentMember));//질문 저장
//        recordRepository.save(new Record(response.getChoices().get(0).getMessage(),currentMember));//답변 저장
        return response.getChoices().get(0).getMessage().getContent();
    }



}
