package com.commercial.tuds.earnandpay.Models;

public class FAQ {

    private String faqQuestion;
    private String faqAnswer;

    public FAQ() {
    }

    public FAQ(String faqQuestion, String faqAnswer) {
        this.faqQuestion = faqQuestion;
        this.faqAnswer = faqAnswer;
    }

    public String getFaqQuestion() {
        return faqQuestion;
    }

    public void setFaqQuestion(String faqQuestion) {
        this.faqQuestion = faqQuestion;
    }

    public String getFaqAnswer() {
        return faqAnswer;
    }

    public void setFaqAnswer(String faqAnswer) {
        this.faqAnswer = faqAnswer;
    }

    @Override
    public String toString() {
        return "Faq{" +
                "faqQuestion='" + faqQuestion + '\'' +
                ", faqAnswer='" + faqAnswer + '\'' +
                '}';
    }
}
