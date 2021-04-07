package com.hraps.pytorch.tec;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hraps.pytorch.R;

import org.pytorch.Device;
import org.pytorch.IValue;
import org.pytorch.Module;
import org.pytorch.PyTorchAndroid;
import org.pytorch.Tensor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DebugTextcnnActivity extends AppCompatActivity {
    String weightPath;
    String vocabPath;

    EditText inputView;
    Button startBtn;
    TextView resultView;

    Vocab vocab;
    Module module;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug_textcnn);

        inputView = findViewById(R.id.input);
        startBtn = findViewById(R.id.start);
        resultView = findViewById(R.id.result);

        weightPath = getIntent().getStringExtra("weightPath");
        vocabPath = getIntent().getStringExtra("classesPath");

        try {
            BufferedReader br;
            if(weightPath.length()==0){
                module = PyTorchAndroid.loadModuleFromAsset(getAssets(), "textcnn.torchscript.pt", Device.CPU);
                br = new BufferedReader(new InputStreamReader(getAssets().open("vocab.txt")));
            }else {
                module = Module.load(weightPath);
                br = new BufferedReader(new FileReader(vocabPath));
            }
            String line;
            List<String> words = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                words.add(line);
            }
            vocab = new Vocab(words);
        } catch (IOException e) {
            e.printStackTrace();
        }

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sentence = TecUtils.onlyLitter(inputView.getText().toString()).toLowerCase();
                long ids[] = vocab.getIds(sentence.split(" "),128);
                Tensor inputTensor = Tensor.fromBlob(ids,new long[]{1,128});
                Tensor outputTensor = module.forward(IValue.from(inputTensor)).toTensor();
                float result[] =  outputTensor.getDataAsFloatArray();
                String resStr = "Negative";
                if(result[1]>result[0]){
                    resStr = "Positive";
                }
                resultView.setText(resStr+"  (negative:"+result[0]+",positive:"+result[1]+")");
            }
        });
    }


}