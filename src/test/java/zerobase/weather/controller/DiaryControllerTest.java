package zerobase.weather.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weather.domain.Diary;
import zerobase.weather.service.DiaryService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DiaryController.class)
class DiaryControllerTest {
    @MockBean
    private DiaryService diaryService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createDiarySuccess() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(post("/create/diary?date=2023-03-15")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("날씨 좋아요"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void successReadDiary() throws Exception {
        //given
        List<Diary> diaryList =
                Arrays.asList(
                        Diary.builder()
                                .id(1)
                                .weather("sunny")
                                .icon("A01")
                                .temperature(36.05)
                                .text("Wow")
                                .date(LocalDate.parse("2023-03-15"))
                                .build(),
                        Diary.builder()
                                .id(2)
                                .weather("cloudy")
                                .icon("A02")
                                .temperature(35.05)
                                .text("oh")
                                .date(LocalDate.parse("2023-03-15"))
                                .build()
                );
        given(diaryService.readDiary(any()))
                .willReturn(diaryList);
        //when
        //then
        mockMvc.perform(get("/read/diary?date=2023-03-15"))
                .andDo(print())
                .andExpect(jsonPath("$[0].weather").value("sunny"))
                .andExpect(jsonPath("$[0].temperature").value(36.05))
                .andExpect(jsonPath("$[0].text").value("Wow"))
                .andExpect(jsonPath("$[0].date").value("2023-03-15"))
                .andExpect(jsonPath("$[1].weather").value("cloudy"))
                .andExpect(jsonPath("$[1].temperature").value(35.05))
                .andExpect(jsonPath("$[1].text").value("oh"))
                .andExpect(jsonPath("$[1].date").value("2023-03-15"));
    }

    @Test
    void successReadDiaries() throws Exception {
        //given
        List<Diary> diaryList =
                Arrays.asList(
                        Diary.builder()
                                .id(1)
                                .weather("sunny")
                                .icon("A01")
                                .temperature(36.05)
                                .text("Wow")
                                .date(LocalDate.parse("2023-03-02"))
                                .build(),
                        Diary.builder()
                                .id(2)
                                .weather("cloudy")
                                .icon("A02")
                                .temperature(35.05)
                                .text("oh")
                                .date(LocalDate.parse("2023-03-04"))
                                .build(),
                        Diary.builder()
                                .id(3)
                                .weather("rainy")
                                .icon("A03")
                                .temperature(25.05)
                                .text("화이트데이")
                                .date(LocalDate.parse("2023-03-14"))
                                .build()
                );
        given(diaryService.readDiaries(any(), any()))
                .willReturn(diaryList);
        //when
        //then
        mockMvc.perform(get("/read/diaries?startDate=2023-03-01&endDate=2023-03-15"))
                .andDo(print())
                .andExpect(jsonPath("$[0].weather").value("sunny"))
                .andExpect(jsonPath("$[0].temperature").value(36.05))
                .andExpect(jsonPath("$[0].text").value("Wow"))
                .andExpect(jsonPath("$[0].date").value("2023-03-02"))
                .andExpect(jsonPath("$[1].weather").value("cloudy"))
                .andExpect(jsonPath("$[1].temperature").value(35.05))
                .andExpect(jsonPath("$[1].text").value("oh"))
                .andExpect(jsonPath("$[1].date").value("2023-03-04"))
                .andExpect(jsonPath("$[2].weather").value("rainy"))
                .andExpect(jsonPath("$[2].temperature").value(25.05))
                .andExpect(jsonPath("$[2].text").value("화이트데이"))
                .andExpect(jsonPath("$[2].date").value("2023-03-14"));
    }

    @Test
    void updateDiarySuccess() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(put("/update/diary?date=2023-03-15")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("Hello zerobase"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void deleteDiarySuccess() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(delete("/delete/diary?date=2023-03-15"))
                .andExpect(status().isOk())
                .andDo(print());
    }
}