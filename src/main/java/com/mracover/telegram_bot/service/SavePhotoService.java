package com.mracover.telegram_bot.service;

import com.mracover.telegram_bot.model.Image;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

@Component
@Slf4j
@PropertySource("/bot.properties")
public class SavePhotoService {
    @Value("${bot.token}")
    private  String token;
    @Value("${service.file_storage.uri}")
    private String fileStorageUri;
    @Value("${service.file_info.uri}")
    private String fileInfoUri;

    public Image downloadPhotoAndGetEntity(Message inputMsg) {
        Image image = new Image();
        int photoSizeCount = inputMsg.getPhoto().size();
        int photoIndex = photoSizeCount > 1 ? inputMsg.getPhoto().size() - 1 : 0;
        PhotoSize telegramPhoto = inputMsg.getPhoto().get(photoIndex);
        String fileId = telegramPhoto.getFileId();
        ResponseEntity<String> response = getFilePath(fileId);
        if (response.getStatusCode() == HttpStatus.OK) {
            String path = getFilePath(response);
            if (downloadFile(path) != null) {
                image.setImage(downloadFile(path));
                log.info("Файл успешно скачался");
            } else {
                return null;
            }
        }
        return image;
    }

    private byte[] downloadFile(String filePath) {
        String fullUri = fileStorageUri.replace("{token}", token)
                .replace("{filePath}", filePath);
        URL urlObj = null;
        log.info("файл начал сохраняться");
        try {
            urlObj = new URL(fullUri);
        } catch (MalformedURLException e) {
            log.error(e.getMessage());
        }

        try (InputStream is = urlObj.openStream()) {
            return is.readAllBytes();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    private ResponseEntity<String> getFilePath(String fileId) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> request = new HttpEntity<>(headers);

        return restTemplate.exchange(
                fileInfoUri,
                HttpMethod.GET,
                request,
                String.class,
                token, fileId
        );
    }

    private String getFilePath(ResponseEntity<String> response) {
        JSONObject jsonObject = new JSONObject(response.getBody());
        return String.valueOf(jsonObject
                .getJSONObject("result")
                .getString("file_path"));
    }
}
