package com.livegather.android.exceptions;

/*
 * Copyright (c) 2010 Andy Aspell-Clark
 * 
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.util.Log;

/**
 * @author andy
 *
 */
public class FileUtilities {

        public static String getApplicationRootDir(String appName) {
                final File extDir = Environment.getExternalStorageDirectory();
                return extDir.getPath() + File.separator + appName + File.separator;
        }

        public static boolean StoreByteImage(Context mContext, byte[] imageData, int quality, String imgDirectory,
                        String filename) {

                //File sdImageMainDirectory = new File(imgDirectory);
                FileOutputStream fileOutputStream = null;
                try {

                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 5;

                        Bitmap myImage = BitmapFactory.decodeByteArray(imageData, 0, imageData.length, options);

                        String newFilename = imgDirectory + File.separator + filename + ".jpg";
                        fileOutputStream = new FileOutputStream(newFilename);

                        BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);

                        myImage.compress(CompressFormat.JPEG, quality, bos);

                        bos.flush();
                        bos.close();
                        fileOutputStream.flush();
                        fileOutputStream.close();

                } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }

                return true;
        }

        public boolean isSdCardPresent() {
                if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
                	Log.d("FileUtilities", "SD Card is installed and mounted");
                        return true;
                }
                return false;
        }



    	public static String getMd5Hash(String input) {
            try     {
                    MessageDigest md = MessageDigest.getInstance("MD5");
                    byte[] messageDigest = md.digest(input.getBytes());
                    BigInteger number = new BigInteger(1,messageDigest);
                    String md5 = number.toString(16);
               
                    while (md5.length() < 32)
                            md5 = "0" + md5;
               
                    return md5;
            } catch(NoSuchAlgorithmException e) {
                    Log.e("MD5", e.getMessage());
                    return null;
            }
    }
 }
