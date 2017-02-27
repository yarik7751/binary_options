package com.elatesoftware.grandcapital.api.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Ярослав Левшунов on 23.02.2017.
 */

public class QuestionsAnswer {

    @SerializedName("count")
    @Expose
    private Integer count;

    @SerializedName("num_pages")
    @Expose
    private Integer numPages;

    @SerializedName("page")
    @Expose
    private Integer page;

    @SerializedName("results")
    @Expose
    private ArrayList<Question> questions = new ArrayList<>();

    public static QuestionsAnswer answersInstance = null;
    public static QuestionsAnswer getInstance() {
        return answersInstance;
    }
    public static void setInstance(QuestionsAnswer answers) {
        answersInstance = answers;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getNumPages() {
        return numPages;
    }

    public void setNumPages(Integer numPages) {
        this.numPages = numPages;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }

    @Override
    public String toString() {
        return "count: " + count + ", numPages: " + numPages + ", page: " + page;
    }

    public class Question {
        @SerializedName("question")
        @Expose
        private String question;

        @SerializedName("answer")
        @Expose
        private String answer;

        @SerializedName("pk")
        @Expose
        private String pk;

        @Override
        public String toString() {
            return "\nquestion: " + question + "\nanswer: " + answer + "\n";
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public String getPk() {
            return pk;
        }

        public void setPk(String pk) {
            this.pk = pk;
        }
    }
}
