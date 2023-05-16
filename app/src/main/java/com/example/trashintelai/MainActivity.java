package com.example.trashintelai;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.trashintelai.ml.ModelUnquant;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MainActivity extends AppCompatActivity {

    TextView result, confidence;
    ImageView imageView;
    Button picture,gallery;
    int imageSize = 224;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        result = findViewById(R.id.result);
        confidence = findViewById(R.id.confidence);
        imageView = findViewById(R.id.imageView);
        picture = findViewById(R.id.button);
        gallery=findViewById(R.id.button2);

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch camera if we have permission
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 1);
                } else {
                    //Request camera permission if we don't have it.
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                }
            }
        });


        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(cameraIntent, 3);
            }
        });
    }



    public void classifyImage(Bitmap image){
        try {
            ModelUnquant model = ModelUnquant.newInstance(getApplicationContext());
            //ModelNew model = ModelNew.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);

            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4*imageSize*imageSize*3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int [] intValues= new int[imageSize*imageSize];
            image.getPixels(intValues,0,image.getWidth(),0,0,image.getWidth(),image.getHeight());
            int pixel=0;
            for( int i=0 ; i<imageSize;i++){
                for(int j=0 ; j<imageSize;j++){
                    int val=intValues[pixel++];
                    byteBuffer.putFloat(((val>>16)& 0xFF)*(1.f/255.f));
                    byteBuffer.putFloat(((val>>8)& 0xFF)*(1.f/255.f));
                    byteBuffer.putFloat((val& 0xFF)*(1.f/255.f));
                }
            }
            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
           ModelUnquant.Outputs outputs = model.process(inputFeature0);
            //ModelNew.Outputs outputs = model.process(inputFeature0);

            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();


            float [] confidences =outputFeature0.getFloatArray();
            int maxPos=0;
            float maxConfidence=0;

            for(int i=0 ; i<confidences.length;i++){
                if(confidences[i] >maxConfidence){
                    maxConfidence=confidences[i];
                    maxPos=i;
                }
            }

            String[] classes = {"Clothes Waste","E-Waste","Glass Waste","Metallic Waste","Organic - Fresh Waste","Organic - Rotten Waste","Paper Waste","Plastic Waste"};
            //String[] classes = {"Dry Waste","Fresh Organic- Wet Waste","Rotten-Organic- Wet waste"};


            result.setText(classes[maxPos]);
            String s = "";
//            for(int i = 0; i < classes.length; i++){
//                s += String.format("%s: %.1f%%\n", classes[i], confidences[i] * 100);
//            }

            if(classes[maxPos]==classes[4] || classes[maxPos]==classes[5] ){
                confidence.setText("WET WASTE");
            }
            else{
                confidence.setText("DRY WASTE");
            }

            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( resultCode == RESULT_OK) {

            if(requestCode == 1){
                Bitmap image=(Bitmap) data.getExtras().get("data");
                int dimension = Math.min(image.getWidth(),image.getHeight() );
                image =ThumbnailUtils.extractThumbnail(image,dimension,dimension);
                imageView.setImageBitmap(image);

                image =Bitmap.createScaledBitmap(image , imageSize,imageSize,false);
                classifyImage(image);
            }
            else{
                Uri dat = data.getData();
                Bitmap image = null;
                try {
                    image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), dat);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageView.setImageBitmap(image);

                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                classifyImage(image);
            }



        }

    }
}