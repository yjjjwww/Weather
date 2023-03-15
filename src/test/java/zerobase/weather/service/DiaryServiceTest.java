package zerobase.weather.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;
import zerobase.weather.domain.DateWeather;
import zerobase.weather.domain.Diary;
import zerobase.weather.error.InvalidDate;
import zerobase.weather.repository.DateWeatherRepository;
import zerobase.weather.repository.DiaryRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DiaryServiceTest {
    @Mock
    private DiaryRepository diaryRepository;

    @Mock
    private DateWeatherRepository dateWeatherRepository;

    @InjectMocks
    private DiaryService diaryService;

    @Test
    void createDiarySuccess() {
        //given
        LocalDate date = LocalDate.parse("2023-03-15");

        DateWeather dateWeather = DateWeather.builder()
                .date(date)
                .weather("sunny")
                .icon("A01")
                .temperature(20.00)
                .build();

        given(dateWeatherRepository.findAllByDate(date))
                .willReturn(Arrays.asList(
                        dateWeather
                ));

        ArgumentCaptor<Diary> captor = ArgumentCaptor.forClass(Diary.class);

        //when
        diaryService.createDiary(date, "hello");

        //then
        verify(diaryRepository, times(1)).save(captor.capture());
        assertEquals("sunny", captor.getValue().getWeather());
    }

    @Test
    void readDiarySuccess() {
        //given
        LocalDate date = LocalDate.parse("2023-03-15");

        given(diaryRepository.findAllByDate(date))
                .willReturn(Arrays.asList(
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
                                .build(),
                        Diary.builder()
                                .id(3)
                                .weather("rainy")
                                .icon("A03")
                                .temperature(25.05)
                                .text("화이트데이")
                                .date(LocalDate.parse("2023-03-15"))
                                .build()
                ));
        //when
        List<Diary> diaries = diaryService.readDiary(date);

        //then
        assertEquals(3, diaries.size());
        assertEquals("Wow", diaries.get(0).getText());
        assertEquals("oh", diaries.get(1).getText());
        assertEquals("화이트데이", diaries.get(2).getText());
        assertEquals("Wow", diaries.get(0).getText());
        assertEquals("sunny", diaries.get(0).getWeather());
        assertEquals("cloudy", diaries.get(1).getWeather());
        assertEquals("rainy", diaries.get(2).getWeather());
    }

    @Test
    void readDiaryFail() {
        //given
        LocalDate date = LocalDate.parse("4000-03-15");

        //when
        InvalidDate exception = assertThrows(InvalidDate.class,
                () -> diaryService.readDiary(date));

        //then
        assertEquals("너무 과거 혹은 미래의 날짜입니다.", exception.getMessage());
    }

    @Test
    void readDiariesSuccess() {
        //given
        LocalDate startDate = LocalDate.parse("2023-03-01");
        LocalDate endDate = LocalDate.parse("2023-03-15");

        given(diaryRepository.findAllByDateBetween(startDate, endDate))
                .willReturn(Arrays.asList(
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
                ));

        //when
        List<Diary> diaries = diaryService.readDiaries(startDate, endDate);

        //then
        assertEquals(3, diaries.size());
        assertEquals("Wow", diaries.get(0).getText());
        assertEquals("oh", diaries.get(1).getText());
        assertEquals("화이트데이", diaries.get(2).getText());
        assertEquals("Wow", diaries.get(0).getText());
        assertEquals("sunny", diaries.get(0).getWeather());
        assertEquals("cloudy", diaries.get(1).getWeather());
        assertEquals("rainy", diaries.get(2).getWeather());
        assertEquals(LocalDate.parse("2023-03-02"), diaries.get(0).getDate());
        assertEquals(LocalDate.parse("2023-03-04"), diaries.get(1).getDate());
        assertEquals(LocalDate.parse("2023-03-14"), diaries.get(2).getDate());
    }

    @Test
    void updateDiarySuccess() {
        //given
        LocalDate date = LocalDate.parse("2023-03-15");

        given(diaryRepository.getFirstByDate(date))
                .willReturn(
                        Diary.builder()
                                .id(2)
                                .weather("cloudy")
                                .icon("A02")
                                .temperature(35.05)
                                .text("oh")
                                .date(LocalDate.parse("2023-03-15"))
                                .build()
                );
        ArgumentCaptor<Diary> captor = ArgumentCaptor.forClass(Diary.class);

        //when
        diaryService.updateDiary(date, "hello");

        //then
        verify(diaryRepository, times(1)).save(captor.capture());
        assertEquals("hello", captor.getValue().getText());
    }

    @Test
    void deleteDiarySuccess() {
        //given
        LocalDate date = LocalDate.parse("2023-03-15");

        //when
        diaryService.deleteDiary(date);

        //then
        verify(diaryRepository, times(1)).deleteAllByDate(date);
    }
}