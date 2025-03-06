package com.moldavets.microservices.job_parser_service.service.Impl;

import com.moldavets.microservices.job_parser_service.exception.LevelNotFoundException;
import com.moldavets.microservices.job_parser_service.exception.TechNotFoundException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JobScraperServiceImplTest {

    private static final String HTML_MAIN_PAGE = """
                <html>
                    <head>
                    </head>
                    <body>
                        <a href="/job-offer/just-testing1">
                        <a href="/job-offer/just-testing2">
                    </body>
                </html>
                """;

    private static final String HTML_OFFER_PAGE = """
                <html>
                    <head>
                    </head>
                    <body>
                        <h4>Java</h4>
                        <h4>Junit</h4>
                        <h4>Mockito</h4>
                    </body>
                </html>
                """;

    private static final String HTML_EMPTY_PAGE = """
                <html>
                    <head>
                    </head>
                    <body>
                    </body>
                </html>
                """;

    private MockWebServer mockWebServer;
    private JobScraperServiceImpl jobScraperService;

    @InjectMocks
    private JobScraperServiceImpl jobScraperServiceInj;

    @Spy
    private JobScraperServiceImpl jobScraperServiceSpy;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        jobScraperService = new JobScraperServiceImpl();
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @DisplayName("parse() should return map with correct skills when input contains correct tech and level")
    @Test
    void parse_shouldReturnMapWithCorrectSkills_WhenInputContainsCorrectTechAndLevel() {
        mockWebServer.enqueue(new MockResponse().setBody(HTML_OFFER_PAGE));

        Map<String,Integer> expected = Map.of("Java", 2, "Junit", 1, "Mockito", 1);

        Mockito.doReturn(List.of("/job-offer/just-test"))
                        .when(jobScraperServiceSpy)
                                .getJobLinks(Mockito.anyString());

        Mockito.doReturn(List.of("Java", "Junit", "Mockito", "Java"))
                        .when(jobScraperServiceSpy)
                                .getSkills(Mockito.anyString());

        Map<String, Integer> actual = jobScraperServiceSpy.parse("java", "junior");

        assertEquals(expected, actual);
        Mockito.verify(jobScraperServiceSpy, Mockito.times(1)).getJobLinks(Mockito.anyString());
        Mockito.verify(jobScraperServiceSpy, Mockito.times(1)).getSkills(Mockito.anyString());
    }

    @DisplayName("parse() should throw NullPointerException when input is null or empty")
    @ParameterizedTest
    @CsvSource(value = {
            "java, ",
            " , junior",
            " , ",
            "null, junior",
            "java, null",
            "null, null",
    }, nullValues = "null")
    void parse_shouldThrowNullPointerException_WhenInputIsNullOrEmpty(String tech, String level) {
        assertThrows(NullPointerException.class,
                () -> jobScraperService.parse(tech, level));
    }



    @DisplayName("getSkills() should return skills list when input url is correct")
    @Test
    void getSkills_shouldReturnSkillsList_whenInputUrlIsCorrect() {
        List<String> expected = List.of("Java", "Junit", "Mockito");
        mockWebServer.enqueue(new MockResponse().setBody(HTML_OFFER_PAGE));

        List<String> actual = jobScraperService.getSkills(mockWebServer.url("/").toString());

        assertEquals(expected, actual);
        assertEquals(3, actual.size());
    }

    @DisplayName("getSkills() should return empty list when html is empty")
    @Test
    void getSkills_shouldReturnEmptyList_whenHtmlIsEmpty() {
        mockWebServer.enqueue(new MockResponse().setBody(HTML_EMPTY_PAGE));
        List<String> actual = jobScraperService.getSkills(mockWebServer.url("/").toString());
        assertEquals(0, actual.size());
    }

    @DisplayName("getSkills() should throw NullPointerException input url empty or null")
    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    @CsvSource(value = "null", nullValues = "null")
    void getJobLinks_shouldThrowNullPointerException_whenInputUrlIsNull(String url) {
        assertThrows(NullPointerException.class,
                () -> jobScraperService.getSkills(url));
    }

    @DisplayName("getJobLinks() should return correct job link when input data is correct")
    @Test
    void getJobLinks_shouldReturnCorrectJobLink_whenInputDataIsCorrect() {
        mockWebServer.enqueue(new MockResponse().setBody(HTML_MAIN_PAGE));

        List<String> expected = new ArrayList<>();
        expected.add("/job-offer/just-testing1");
        expected.add("/job-offer/just-testing2");

        List<String> actual = jobScraperService.getJobLinks(mockWebServer.url("/").toString());

        assertEquals(expected, actual);
        assertEquals(2, actual.size());
    }

    @DisplayName("getJobLinks() should return empty job list when input page is empty")
    @Test
    void getJobLinks_shouldReturnEmptyList_whenInputPageIsEmpty() {
        mockWebServer.enqueue(new MockResponse().setBody(HTML_EMPTY_PAGE));

        List<String> expected = new ArrayList<>();
        List<String> actual = jobScraperService.getJobLinks(mockWebServer.url("/").toString());

        assertEquals(expected, actual);
        assertTrue(actual.isEmpty());
    }

    @DisplayName("getJobLinks() should throw IllegalArgumentException input contains bad url")
    @Test
    void getJobLinks_shouldReturnIllegalArgumentException_whenInputContainsBadUrl() {
        assertThrows(IllegalArgumentException.class,
                () -> jobScraperService.getJobLinks("test"));
    }

    @DisplayName("getJobLinks() should throw NullPointerException input url empty or null")
    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    @CsvSource(value = "null", nullValues = "null")
    void getJobLinks_shouldThrowNullPointerException_whenInputUrlIsNullOrEmpty(String url) {
        assertThrows(NullPointerException.class,
                () -> jobScraperService.getJobLinks(url));
    }


    @DisplayName("getUrlWithParams() should return the correct url when the input data is correct")
    @Test
    void getUrlWithParams_shouldReturnCorrectUrl_whenInputIsValid() {
        String expectedUrl = "https://justjoin.it/job-offers/all-locations/java?experience-level=junior";
        String actual = jobScraperService.getUrlWithParams("java", "junior");
        assertEquals(expectedUrl, actual);
    }

    @DisplayName("getUrlWithParams() should throw TechNotFoundException when the input tech is invalid")
    @Test
    void getUrlWithParams_shouldThrowTechNotFoundException_whenInputTechIsInvalid() {
        assertThrows(TechNotFoundException.class,
                () -> jobScraperService.getUrlWithParams("test", "junior"));
    }

    @DisplayName("getUrlWithParams() should throw LevelNotFoundException when the input level is invalid")
    @Test
    void getUrlWithParams_shouldThrowLevelNotFoundException_whenInputLevelIsInvalid() {
        assertThrows(LevelNotFoundException.class,
                () -> jobScraperService.getUrlWithParams("java", "test"));
    }


    @DisplayName("getUrlWithParams() should throw NullPointerException when the input empty or null")
    @ParameterizedTest
    @CsvSource(value = {
            "java, ",
            " , junior",
            " , ",
            "null, junior",
            "java, null",
            "null, null",
    }, nullValues = "null")
    void getUrlWithParams_shouldThrowNullPointerException_whenInputIsNull(String tech, String level) {
        assertThrows(NullPointerException.class,
                () -> jobScraperService.getUrlWithParams(tech, level));
    }
}