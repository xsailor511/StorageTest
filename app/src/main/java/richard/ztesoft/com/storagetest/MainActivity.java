package richard.ztesoft.com.storagetest;

import android.os.Environment;
import android.os.StatFs;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private String TAG = "TestStorage";
    private File mDownloadDir;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String sdCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        Log.i(TAG,"sdCardPath = "+sdCardPath);//   sdCardPath = /storage/emulated/0  魅族 魅蓝note1
        mDownloadDir = new File(Environment.getExternalStorageDirectory(), "/RES");
        if(!mDownloadDir.exists()||!mDownloadDir.isDirectory()){
            mDownloadDir.mkdir();
        }
        //不能一次创建两层文件夹，只能一层一层创建
        File file = new File(sdCardPath+"/RES/.video");
        if(!file.exists()||!file.isDirectory()){
            file.mkdir();
        }

        readSystem();
        readSDCard();
        readData();


        Map<String, File> externalLocations = ExternalStorage.getAllStorageLocations();
        File sdCard = externalLocations.get(ExternalStorage.SD_CARD);
        File externalSdCard = externalLocations.get(ExternalStorage.EXTERNAL_SD_CARD);

        Log.i(TAG,"sdCard = "+sdCard.getAbsolutePath());
        Log.i(TAG,"externalSdCard = "+externalSdCard);

        //寻找一个存在的存储空间，判断大小，以便以后下载时可以有足够的空间
        if (sdCard==null){
            if (externalSdCard==null){
                Log.i(TAG,"no external storage available");
            }else{
                //byte
                long size = ExternalStorage.availableSize(externalSdCard.getAbsolutePath());
                //KB
                long sizeKB = size/1024;
                //MB
                long sizeMB = sizeKB/1024;
                //GB
                long sizeGB = sizeMB/1024;
                Log.i(TAG,"externalSdCard size in byte = "+size);
                Log.i(TAG,"externalSdCard size in KB = "+sizeKB);
                Log.i(TAG,"externalSdCard size in MB = "+sizeMB);
                Log.i(TAG,"externalSdCard size in GB = "+sizeGB);
                //Log.i(TAG,"size in byte = "+size);

            }
        }else{
            //byte
            long size = ExternalStorage.availableSize(sdCard.getAbsolutePath());
            //KB
            long sizeKB = size/1024;
            //MB
            long sizeMB = sizeKB/1024;
            //GB
            long sizeGB = sizeMB/1024;
            Log.i(TAG,"sdCard size in byte = "+size);
            Log.i(TAG,"sdCard size in KB = "+sizeKB);
            Log.i(TAG,"sdCard size in MB = "+sizeMB);
            Log.i(TAG,"sdCard size in GB = "+sizeGB);
            //Log.i(TAG,"size in byte = "+size);
        }

        StatFs sf = new StatFs(sdCard.getAbsolutePath());
        long blockSize = sf.getBlockSize();
        long blockCount = sf.getBlockCount();
        long availCount = sf.getAvailableBlocks();
        Log.d("sdcard0", "block大小:" + blockSize + ",block数目:" + blockCount
                + ",总大小:" + blockSize * blockCount / 1024 + "KB");
        Log.d("sdcard0", "可用的block数目：:" + availCount + ",剩余空间:" + availCount
                * blockSize / 1024 + "KB");

        /*
        * /storage/emulated/0  与 /mnt/sdcard 挂载的是同一个目录
        * */
    }


    /*
    * 系统分区
    * */
    public void readSystem() {
        File root = Environment.getRootDirectory();
        StatFs sf = new StatFs(root.getPath());
        long blockSize = sf.getBlockSize();
        long blockCount = sf.getBlockCount();
        long availCount = sf.getAvailableBlocks();
        Log.d("system", "block大小:" + blockSize + ",block数目:" + blockCount + ",总大小:"
                + blockSize * blockCount / 1024 + "KB");
        Log.d("system", "可用的block数目：:" + availCount + ",可用大小:" + availCount
                * blockSize / 1024 + "KB");
    }


    /*
     * data分区
     *
     * */
    public void readData() {
        File root = Environment.getDataDirectory();
        StatFs sf = new StatFs(root.getPath());
        long blockSize = sf.getBlockSize();
        long blockCount = sf.getBlockCount();
        long availCount = sf.getAvailableBlocks();
        Log.d("data", "block大小:" + blockSize + ",block数目:" + blockCount + ",总大小:"
                + blockSize * blockCount / 1024 + "KB");
        Log.d("data", "可用的block数目：:" + availCount + ",可用大小:" + availCount
                * blockSize / 1024 + "KB");
    }

    /*
    * sdcard 分区
    * */
    public void readSDCard() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File sdcardDir = Environment.getExternalStorageDirectory();
            StatFs sf = new StatFs(sdcardDir.getPath());
            long blockSize = sf.getBlockSize();
            long blockCount = sf.getBlockCount();
            long availCount = sf.getAvailableBlocks();
            Log.d("sdcard", "block大小:" + blockSize + ",block数目:" + blockCount
                    + ",总大小:" + blockSize * blockCount / 1024 + "KB");
            Log.d("sdcard", "可用的block数目：:" + availCount + ",剩余空间:" + availCount
                    * blockSize / 1024 + "KB");
        }
    }
}
