package com.moldavets.microservices.job_parser_service.service.Impl;

import com.moldavets.microservices.job_parser_service.exception.LevelNotFoundException;
import com.moldavets.microservices.job_parser_service.exception.LostConnectionException;
import com.moldavets.microservices.job_parser_service.exception.TechNotFoundException;
import com.moldavets.microservices.job_parser_service.service.JobScraperService;
import com.moldavets.microservices.job_parser_service.util.LevelEnum;
import com.moldavets.microservices.job_parser_service.util.TechEnum;
import lombok.NoArgsConstructor;
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

@Service
@NoArgsConstructor
public class JobScraperServiceImpl implements JobScraperService {
    

    public Map<String,Integer> parse(String tech, String level) {

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
        return rateMap;
    }

    private List<String> getSkills(String url) {

        List<String> jobSkills = new ArrayList<>();

        try {
            Document document = Jsoup.connect(url).get();
            Elements elements = document.select("h4");

            for (Element tempElement : elements) {
                jobSkills.add(tempElement.text());
            }
            
            return jobSkills;
        } catch (IOException e) {
            throw new LostConnectionException(e.getMessage());
        }
    }

    private String getUrlWithParams(String tech, String level) {

        StringBuilder urlBuilder = new StringBuilder();

        urlBuilder.append("https://justjoin.it")
                .append("/job-offers")
                .append("/all-locations");

        try {
            TechEnum.valueOf(tech.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new TechNotFoundException(e.getMessage());
        }

        if(level == null) {
            urlBuilder.append("/")
               .append(tech);
        } else {
            try {
                LevelEnum.valueOf(level.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new LevelNotFoundException(e.getMessage());
            }

            urlBuilder.append("/")
                    .append(tech)
                    .append("?experience-level=")
                    .append(level);
        }

        return urlBuilder.toString();
    }

    private static List<String> getJobLinks(String url) {

        List<String> jobLinks = new ArrayList<>();

        try {
            Elements elements = Jsoup.connect(url)
                    .get()
                    .select("a[href^='/job-offer/']");

            for (int i = 0; i <= elements.size()-1; i++) {
                jobLinks.add(elements.get(i).attr("href"));
            }
            return jobLinks;

        } catch (IOException e) {
            throw new LostConnectionException(e.getMessage());
        }
    }
}
