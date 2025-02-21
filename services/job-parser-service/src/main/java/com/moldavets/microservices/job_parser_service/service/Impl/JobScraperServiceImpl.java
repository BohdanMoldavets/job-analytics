package com.moldavets.microservices.job_parser_service.service.Impl;

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

    //todo skip if cannot open URL

    private static final String JUST_JOIN_IT_PATH = "https://justjoin.it";

    public Map<String,Integer> parse(String tech, String level) {

        String URL = getUrlWithParams(tech, level);

        List<String> jobLinks = getJobLinks(URL);
        Map<String,Integer> rateMap = new HashMap<>();

        for (String tempJob : jobLinks) {
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

    private List<String> getSkills(String URL) {

        List<String> jobSkills = new ArrayList<>();

        try {
            Document document = Jsoup.connect(URL).get();
            Elements elements = document.select("h4");

            for (Element tempElement : elements) {
                jobSkills.add(tempElement.text());
            }

            return jobSkills;
        } catch (IOException e) {
            //todo custom exception
            throw new RuntimeException(e);
        }
    }

    private String getUrlWithParams(String tech, String level) {

        StringBuilder URL = new StringBuilder();

        URL.append(JUST_JOIN_IT_PATH)
                .append("/job-offers")
                .append("/all-locations");

        try {
            TechEnum techEnum = TechEnum.valueOf(tech.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid tech: " + tech);
            //todo exception
        }

        if(level == null) {
            URL.append("/")
               .append(tech);
        } else {

            try {
                LevelEnum levelEnum = LevelEnum.valueOf(level.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid level: " + level);
                //todo exception
            }

            URL.append("/")
                    .append(tech)
                    .append("?experience-level=")
                    .append(level);
        }

        return URL.toString();
    }

    private static List<String> getJobLinks(String URL) {

        List<String> jobLinks = new ArrayList<>();

        try {
            Elements elements = Jsoup.connect(URL)
                    .get()
                    .select("a[href^='/job-offer/']");

            for (int i = 0; i <= elements.size()-1; i++) {
                jobLinks.add(elements.get(i).attr("href"));
            }
            return jobLinks;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
