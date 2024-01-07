package com.example.shoestore.core.account.client.model.response;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public interface AddressClientResponse {

    Long getId();

    String getProviceCode();

    String getDistrictCode();

    String getCommuneCode();

    String getAddressDetail();

    Boolean getIsDeleted();

    Integer getStatus();

    Long getIdClient();

    default String getAddress() {

        StringBuilder address = new StringBuilder();

        String token = "44022259-5cfb-11ee-96dc-de6f804954c9";

        String provinceUrl = "https://online-gateway.ghn.vn/shiip/public-api/master-data/province";
        address.append(callAPI(provinceUrl, token, null, getProviceCode(), "ProvinceID", "ProvinceName"));
        address.append(" - ");

        String districtUrl = "https://online-gateway.ghn.vn/shiip/public-api/master-data/district";
        address.append(callAPI(districtUrl, token, Map.of("province_id", Integer.parseInt(getProviceCode())), getDistrictCode(), "DistrictID", "DistrictName"));
        address.append(" - ");

        String communeUrl = "https://online-gateway.ghn.vn/shiip/public-api/master-data/ward";
        address.append(callAPI(communeUrl, token, Map.of("district_id", Integer.parseInt(getDistrictCode())), getCommuneCode(), "WardCode", "WardName"));

        return address.toString();
    }

    @SneakyThrows
    static String callAPI(String apiUrl, String token, Map<String, Integer> body, String code, String key, String fieldName) {

        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");

        // Đặt token vào headers
        connection.setRequestProperty("token", token);

        // Ghi dữ liệu vào body nếu có
        if (body != null && !body.isEmpty()) {
            connection.setDoOutput(true);
            try (OutputStream os = connection.getOutputStream()) {
                // Chuyển đối body thành dạng JSON và ghi vào body của yêu cầu
                ObjectMapper objectMapper = new ObjectMapper();
                byte[] input = objectMapper.writeValueAsBytes(body);
                os.write(input, 0, input.length);
            }
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.toString());

            if (jsonNode != null && jsonNode.has("data") && jsonNode.get("data").isArray()) {

                JsonNode dataArray = jsonNode.get("data");

                for (JsonNode item : dataArray) {
                    String fieldValue = item.get(key).asText();
                    if (code.equals(fieldValue)) {
                        return item.get(fieldName).asText();
                    }
                }

            }

            return jsonNode.get(fieldName).asText();
        } finally {
            connection.disconnect();
        }
    }

}
