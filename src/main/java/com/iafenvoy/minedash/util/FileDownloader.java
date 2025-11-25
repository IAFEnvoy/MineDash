package com.iafenvoy.minedash.util;

import org.jetbrains.annotations.NotNull;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public final class FileDownloader {
    public static void downloadFile(@NotNull String url, @NotNull String path) throws IOException, IllegalArgumentException {
        if (url.isBlank()) throw new IllegalArgumentException("url is empty");
        if (path.isBlank()) throw new IllegalArgumentException("path is empty");
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            // Create connection
            URL fileUrl = URI.create(url).toURL();
            connection = (HttpURLConnection) fileUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(10000);
            // Check response code
            int code = connection.getResponseCode();
            if (code != HttpURLConnection.HTTP_OK)
                throw new IOException("Failed to download file, http status code: " + code);
            // Write File
            inputStream = connection.getInputStream();
            outputStream = new FileOutputStream(path);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) outputStream.write(buffer, 0, bytesRead);
        } finally {
            if (outputStream != null) outputStream.close();
            if (inputStream != null) inputStream.close();
            if (connection != null) connection.disconnect();
        }
    }
}
