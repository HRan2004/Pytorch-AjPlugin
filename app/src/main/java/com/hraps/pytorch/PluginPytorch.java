package com.hraps.pytorch;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;

import com.hraps.pytorch.od.OdResult;
import com.hraps.pytorch.od.DebugYoloActivity;
import com.hraps.pytorch.od.ObjectDetectionActivity;
import com.hraps.pytorch.od.PrePostProcessor;
import com.hraps.pytorch.tec.DebugTextcnnActivity;
import com.hraps.pytorch.tec.TecUtils;
import com.hraps.pytorch.tec.Vocab;

import org.autojs.plugin.sdk.Plugin;
import org.pytorch.Device;
import org.pytorch.IValue;
import org.pytorch.Module;
import org.pytorch.PyTorchAndroid;
import org.pytorch.Tensor;
import org.pytorch.torchvision.TensorImageUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class PluginPytorch extends Plugin {

    public PluginPytorch(Context context, Context selfContext, Object runtime, Object topLevelScope) {
        super(context, selfContext, runtime, topLevelScope);
    }

    public void debugOd(String weightPath,String classesPath) {
        getSelfContext().startActivity(new Intent(getSelfContext(), DebugYoloActivity.class)
                .putExtra("weightPath", weightPath)
                .putExtra("classesPath", classesPath)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }
    public void liveOd(String weightPath,String classesPath){
        getSelfContext().startActivity(new Intent(getSelfContext(), ObjectDetectionActivity.class)
                .putExtra("weightPath", weightPath)
                .putExtra("classesPath", classesPath)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public void debugTec(String weightPath,String vocabPath) {
        getSelfContext().startActivity(new Intent(getSelfContext(), DebugTextcnnActivity.class)
                .putExtra("weightPath", weightPath)
                .putExtra("vocabPath", vocabPath)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public float getIOU(Rect a,Rect b){
        return PrePostProcessor.IOU(a,b);
    }

    public ArrayList<OdResult> useNMS(ArrayList<OdResult> boxes, int limit, float threshold){
        return PrePostProcessor.nonMaxSuppression(boxes,limit,threshold);
    }

    public Module load(String path,int device) {
        Device d = Device.CPU;
        if(device==1){
            d = Device.VULKAN;
        }
        if (path.equals("yolov5s")) {
            return PyTorchAndroid.loadModuleFromAsset(getSelfContext().getAssets(), "yolov5s.torchscript.pt",d);
        }else if(path.equals("textcnn")){
            return PyTorchAndroid.loadModuleFromAsset(getSelfContext().getAssets(), "textcnn.torchscript.pt",d);
        }else {
            return Module.load(path,d);
        }
    }

    public Tensor fromBlob(long[] ids,long[] arr){
        return Tensor.fromBlob(ids,arr);
    }
    public Tensor fromBlob(float[] ids,long[] arr){
        return Tensor.fromBlob(ids,arr);
    }
    public Tensor fromBlob(int[] ids,long[] arr){
        return Tensor.fromBlob(ids,arr);
    }
    public Tensor fromBlob(byte[] ids,long[] arr){
        return Tensor.fromBlob(ids,arr);
    }
    public Tensor fromBlob(double[] ids,long[] arr){
        return Tensor.fromBlob(ids,arr);
    }

    public ArrayList<String> getCocoClasses(){
        ArrayList<String> classes = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(getSelfContext().getAssets().open("classes.txt")));
            String line;
            while ((line = br.readLine()) != null) {
                classes.add(line);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return classes;
    }

    public Vocab getTextcnnVocab(){
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(getSelfContext().getAssets().open("vocab.txt")));
            String line;
            List<String> words = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                words.add(line);
            }
            return new Vocab(words);
        } catch (IOException e) {
            return null;
        }
    }

    public Vocab vocab(List<String> words){
        return new Vocab(words);
    }

    public Vocab vocabPath(String path){
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line;
            List<String> words = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                words.add(line);
            }
            return new Vocab(words);
        } catch (IOException e) {
            return null;
        }
    }

    public Tensor forward(Module module,Tensor inputTensor){
        return module.forward(IValue.from(inputTensor)).toTensor();
    }

    public Tensor forwardTuple(Module module,Tensor inputTensor){
        IValue[] outputTuple = module.forward(IValue.from(inputTensor)).toTuple();
        final Tensor outputTensor = outputTuple[0].toTensor();
        return outputTensor;
    }

    public Tensor bitmapToTensor(Bitmap b,float[] mean,float[] std){
        return TensorImageUtils.bitmapToFloat32Tensor(b,mean,std);
    }

    public static ArrayList<OdResult> floatsToResults(float[] outputs, int mOutputRow, int mOutputColumn, float imgScaleX, float imgScaleY, float mThreshold) {
        ArrayList<OdResult> odResults = new ArrayList<>();
        for (int i = 0; i< mOutputRow; i++) {
            if (outputs[i* mOutputColumn +4] > mThreshold) {
                float x = outputs[i* mOutputColumn];
                float y = outputs[i* mOutputColumn +1];
                float w = outputs[i* mOutputColumn +2];
                float h = outputs[i* mOutputColumn +3];

                float left = imgScaleX * (x - w/2);
                float top = imgScaleY * (y - h/2);
                float right = imgScaleX * (x + w/2);
                float bottom = imgScaleY * (y + h/2);

                float max = outputs[i* mOutputColumn +5];
                int cls = 0;
                for (int j = 0; j < mOutputColumn -5; j++) {
                    if (outputs[i* mOutputColumn +5+j] > max) {
                        max = outputs[i* mOutputColumn +5+j];
                        cls = j;
                    }
                }

                Rect rect = new Rect((int)left,(int)top,(int)right,(int)bottom);
                OdResult odResult = new OdResult(cls, outputs[i*mOutputColumn+4], rect);
                odResults.add(odResult);
            }
        }
        return odResults;
    }

    public String simplifySentence(String str){
        return TecUtils.onlyLitter(str).toLowerCase();
    }

    @Override
    public String getAssetsScriptDir() {
        return "plugin-pytorch";
    }

}
