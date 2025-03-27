package com.example.zeroton.dto;

import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@Getter
public class AiResponse {

    private List<Choice> choices;

    // constructors, getters and setters

    public static class Choice {

        private int index;
        private AiMessage aiMessage;

        public Choice(int index, AiMessage aiMessage) {
            this.index = index;
            this.aiMessage = aiMessage;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public AiMessage getMessage() {
            return aiMessage;
        }

        public void setMessage(AiMessage aiMessage) {
            this.aiMessage = aiMessage;
        }
        // constructors, getters and setters
    }
}