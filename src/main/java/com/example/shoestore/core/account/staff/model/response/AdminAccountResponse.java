package com.example.shoestore.core.account.staff.model.response;

import com.example.shoestore.infrastructure.utils.DataUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface AdminAccountResponse {

    Long getId();

    String getUsername();

    String getFullname();

    Boolean getGender();

    LocalDate getDateOfBirth();

    String getEmail();

    byte[] getAvatar();

    String getPhoneNumber();

    LocalDateTime getCreateTime();

    LocalDateTime getUpdateTime();

    Integer getStatus();
    Integer getTotalPoints();

    Boolean getIsDeleted();

    String getProviceCode();

    String getDistrictCode();

    String getCommuneCode();

    String getAddressDetail();

    default String getAddress() {

        StringBuilder address = new StringBuilder();

        if(DataUtils.isNotNull(getCommuneCode())){
            String wardUrl = "https://provinces.open-api.vn/api/w/" + getCommuneCode() + "?depth=1";
            address.append(callAPI(wardUrl, "name"));
            address.append(" - ");
        }

        if(DataUtils.isNotNull(getDistrictCode())){
            String districtUrl = "https://provinces.open-api.vn/api/d/" + getDistrictCode() + "?depth=1";
            address.append(callAPI(districtUrl, "name"));
            address.append(" - ");
        }

        if(DataUtils.isNotNull(getProviceCode())){
            String provinceUrl = "https://provinces.open-api.vn/api/p/" + getProviceCode() + "?depth=1";
            address.append(callAPI(provinceUrl, "name"));
        }

        return address.toString();
    }

    @SneakyThrows
    static String callAPI(String apiUrl, String fieldName) {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }

        reader.close();
        connection.disconnect();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response.toString());

        return jsonNode.get(fieldName).asText();
    }

}
