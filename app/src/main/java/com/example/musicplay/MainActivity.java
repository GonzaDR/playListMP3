package com.example.musicplay;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private List<Music> musicList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.list_view);
        musicList = getMusicList(); // Método para obter a lista de músicas

        MusicAdapter adapter = new MusicAdapter(this, musicList);
        listView.setAdapter(adapter);
    }

    private List<Music> getMusicList() {
        List<Music> list = new ArrayList<>();
        // Adicione suas músicas aqui
        list.add(new Music("Crush", "Andree Amos", "https://www.rafaelamorim.com.br/mobile2/musicas/AXIS1237_01_Crush_Full.mp3", "02:47"));
        list.add(new Music("When Duty Calls", "Nathan Forsbach", "https://www.rafaelamorim.com.br/mobile2/musicas/AXIS1188_11_When%20Duty%20Calls_Full.mp3", "02:10"));
        list.add(new Music("Higher Self", "John Cimino", "https://www.rafaelamorim.com.br/mobile2/musicas/AXIS1199_01_Higher%20Self_Full.mp3", "02:20"));
        return list;
    }

    private void startDownload(String url) {
        // Crie os dados de entrada para o Worker
        Data inputData = new Data.Builder()
                .putString("music_url", url)
                .build();

        // Crie e inicie o WorkManager
        OneTimeWorkRequest downloadRequest = new OneTimeWorkRequest.Builder(DownloadWorker.class)
                .setInputData(inputData)
                .build();

        WorkManager.getInstance(this).enqueue(downloadRequest);
    }

    private class MusicAdapter extends ArrayAdapter<Music> {
        public MusicAdapter(MainActivity context, List<Music> musicList) {
            super(context, 0, musicList);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            Music music = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_music, parent, false);
            }

            TextView title = convertView.findViewById(R.id.music_title);
            TextView author = convertView.findViewById(R.id.music_author);
            Button downloadButton = convertView.findViewById(R.id.download_button);

            title.setText(music.getTitle());
            author.setText(music.getAuthor());

            downloadButton.setOnClickListener(v -> {
                startDownload(music.getUrl()); // Aqui chamamos o método
            });

            return convertView;
        }
    }
}