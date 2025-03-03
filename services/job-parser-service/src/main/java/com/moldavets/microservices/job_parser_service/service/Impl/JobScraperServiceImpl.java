package com.moldavets.microservices.job_parser_service.service.Impl;

import com.moldavets.microservices.job_parser_service.exception.LevelNotFoundException;
import com.moldavets.microservices.job_parser_service.exception.LostConnectionException;
import com.moldavets.microservices.job_parser_service.exception.TechNotFoundException;
import com.moldavets.microservices.job_parser_service.service.JobScraperService;
import com.moldavets.microservices.job_parser_service.util.LevelEnum;
import com.moldavets.microservices.job_parser_service.util.TechEnum;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@NoArgsConstructor
public class JobScraperServiceImpl implements JobScraperService {

    public Map<String,Integer> parse(String tech, String level) {

        if (tech == null || tech.trim().isEmpty() || level == null || level.trim().isEmpty()) {
            throw new NullPointerException("Tech or level for getSkills method cannot be null or empty");
        }

        Map<String,Integer> rateMap = new HashMap<>();

        for (String tempJob : getJobLinks(getUrlWithParams(tech, level))) {
            for(String tempSkill : getSkills("https://justjoin.it" + tempJob)) {

                if(rateMap.containsKey(tempSkill)) {
                    rateMap.put(tempSkill, rateMap.get(tempSkill) + 1);
                } else {
                    rateMap.put(tempSkill, 1);
                }
            }
        }
        log.info("IN JobScraperServiceImpl.parse(): result - {}", rateMap);
        return rateMap;
    }

    protected List<String> getSkills(String url) {

        if (url == null || url.trim().isEmpty()) {
            log.warn("IN JobScraperServiceImpl.getSkills(): url is null or empty");
            throw new NullPointerException("Url for getSkills method cannot be null or empty");
        }

        List<String> jobSkills = new ArrayList<>();

        try {
            Document document = Jsoup.connect(url).get();
            Elements elements = document.select("h4");

            for (Element tempElement : elements) {
                jobSkills.add(tempElement.text());
            }
            log.info("IN JobScraperServiceImpl.getSkills(): result - {}", jobSkills);
            return jobSkills;
        } catch (IOException e) {
            log.error("IN JobScraperServiceImpl.getSkills(): lost connection");
            throw new LostConnectionException(e.getMessage());
        }
    }

    protected String getUrlWithParams(String tech, String level) {

        if(tech == null || tech.trim().isEmpty() || level == null || level.trim().isEmpty()) {
            log.warn("IN JobScraperServiceImpl.getUrlWithParams(): tech or level is empty or null");
            throw new NullPointerException("Tech or level for getUrlWithParams method cannot be null or empty");
        }

        StringBuilder urlBuilder = new StringBuilder();

        urlBuilder.append("https://justjoin.it")
                .append("/job-offers")
                .append("/all-locations");

        try {
            TechEnum.valueOf(tech.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.warn("IN JobScraperServiceImpl.getUrlWithParams(): tech {} not found", tech);
            throw new TechNotFoundException(e.getMessage());
        }

        try {
            LevelEnum.valueOf(level.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.warn("IN JobScraperServiceImpl.getUrlWithParams(): level {} not found", level);
            throw new LevelNotFoundException(e.getMessage());
        }

        urlBuilder.append("/")
                .append(tech)
                .append("?experience-level=")
                .append(level);

        String result = urlBuilder.toString();
        log.info("IN JobScraperServiceImpl.getUrlWithParams(): result - " + result);
        return result;
    }

    protected List<String> getJobLinks(String url) {
        if (url == null || url.trim().isEmpty()) {
            log.warn("IN JobScraperServiceImpl.getJobLinks(): url is null or empty");
            throw new NullPointerException("Url for getJobLinks method cannot be null or empty");
        }

        List<String> jobLinks = new ArrayList<>();

        try {
            Elements elements = Jsoup.connect(url)
                    .get()
                    .select("a[href^='/job-offer/']");

            for (int i = 0; i <= elements.size() - 1; i++) {
                jobLinks.add(elements.get(i).attr("href"));
            }
            log.info("IN JobScraperServiceImpl.getJobLinks(): result - " + jobLinks);
            return jobLinks;

        } catch (IOException e) {
            log.error("IN JobScraperServiceImpl.getJobLinks(): lost connection");
            throw new LostConnectionException(e.getMessage());
        }
    }
}
