package com.example.musicplay;

import android.content.Context;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadWorker extends Worker {
    public DownloadWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        // Obtenha o URL da música a partir dos dados de entrada
        String musicUrl = getInputData().getString("music_url");

        if (musicUrl != null) {
            try {
                return downloadFile(musicUrl) ? Result.success() : Result.failure();
            } catch (IOException e) {
                e.printStackTrace();
                return Result.failure();
            }
        }
        return Result.failure();
    }

    private boolean downloadFile(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) {
            return false; // Retorna false se o download falhar
        }

        // Obtenha o diretório de armazenamento público
        File outputDir = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File outputFile = new File(outputDir, "downloaded_music_" + System.currentTimeMillis() + ".mp3"); // Nome do arquivo

        // Escreva os dados no arquivo
        try (InputStream inputStream = response.body().byteStream();
             FileOutputStream outputStream = new FileOutputStream(outputFile)) {

            byte[] buffer = new byte[4096]; // Buffer para leitura
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }

        return true; // Retorna true se o download e a gravação forem bem-sucedidos
    }
}