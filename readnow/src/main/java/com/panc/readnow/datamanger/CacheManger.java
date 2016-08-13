package com.panc.readnow.datamanger;

import android.os.Environment;

import com.panc.readnow.app.App;
import com.panc.readnow.util.ToastUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * 统一缓存文件管理类
 */
public class CacheManger
{
    
    public static String cacheDir;
    public boolean canSave ;
    private CacheManger()
    {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            if (cacheDir == null) {
                // /sdcard/包名/cache/
                // File.separator跨平台/,window / linux \
                cacheDir = Environment.getExternalStorageDirectory().getPath() + File.separator
                        + App.context.getPackageName() + File.separator + "cache";
                File fileDir = new File(cacheDir);
                if (!fileDir.exists()) {
                    // 如果不存在创建目录
                    fileDir.mkdirs();
                }
            }
            canSave = true;
        } else {
            canSave = false;
        }
    }
    
    private static CacheManger sCacheManger = new CacheManger();
    
    public static CacheManger getInstance()
    {
        return sCacheManger;
    }

    public boolean deleteFile(String filename) {
        if (!canSave) {
            ToastUtils.showToast("没有发现sd卡，暂时无法删除");
            return false;
        }
        File file = new File(cacheDir, filename);

        return file.delete();
    }
    // 保存缓存文件
    public boolean saveText(String json,String filename)
    {
        if (!canSave) {
            ToastUtils.showToast("没有发现sd卡，暂时无法收藏");
            return false;
        }
        // 1.保存的位置
        // 2. md5加密文件名,参数是url
        // sdcard/包名/cache/
        try
        {
            File saveFile = new File(cacheDir, filename);
            if (saveFile.exists()) {
                ToastUtils.showToast("本地已经收藏啦~");
                return false;
            }

            FileOutputStream fileOutputStream = new FileOutputStream(saveFile);
            // 2.保存的内容写入到文件中
            fileOutputStream.write(json.getBytes());

            fileOutputStream.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return true;

    }
    public String getText(String filename)
    {
        if (!canSave) {
            ToastUtils.showToast("sd卡异常");
            return null;
        }
        //记录内容
        StringBuffer stringBuffer = new StringBuffer();
        try
        {
            File readFile = new File(cacheDir, filename);

            FileInputStream fileInputStream = new FileInputStream(readFile);

            int len = -1;

            byte[] buffer = new byte[1024];

            while ((len = fileInputStream.read(buffer)) != -1) {
                stringBuffer.append(new String(buffer, 0, len));
            }

            fileInputStream.close();

            return stringBuffer.toString();

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "";
        }

    }
    /**
     * 生成一个md5文件名
     * 
     * @param url
     * @return
     */
    private String getMd5Name(String url)
    {
        StringBuffer stringBuffer = new StringBuffer();
        try
        {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(url.getBytes());
            byte[] digest = messageDigest.digest();
            // 把byte转成16进制
            for (int i = 0; i < digest.length; i++)
            {
                System.out.println("md5" + Integer.toHexString(digest[i] & 0xff));
                stringBuffer.append(Integer.toHexString(digest[i] & 0xff));
            }
            
            return stringBuffer.toString();
            
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
            return null;
        }
        
    }
    
    public String getData(String url)
    {
        //记录内容
        StringBuffer stringBuffer = new StringBuffer();
        try
        {
            File readFile = new File(cacheDir, getMd5Name(url));
            
            FileInputStream fileInputStream = new FileInputStream(readFile);

            int len = -1;

            byte[] buffer = new byte[1024];

            while ((len = fileInputStream.read(buffer)) != -1) {
                stringBuffer.append(new String(buffer, 0, len));
            }

            fileInputStream.close();

            return stringBuffer.toString();

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "";
        }
        
    }
    
}
