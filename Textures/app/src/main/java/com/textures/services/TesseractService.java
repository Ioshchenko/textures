package com.textures.services;


import android.graphics.Bitmap;

import com.googlecode.tesseract.android.ResultIterator;
import com.googlecode.tesseract.android.TessBaseAPI;

public class TesseractService {
    private static final String TESSARACT_LANGUAGES_DATA_PATH = "/sdcard/tessaract_languages/";

    private TessBaseAPI baseApi;

    public TesseractService() {
        this.baseApi = new TessBaseAPI();
    }

    public String proccesImage(Bitmap image, String language) {
        baseApi.init(TESSARACT_LANGUAGES_DATA_PATH, language);
        baseApi.setImage(image);
        String result = baseApi.getUTF8Text();
        baseApi.end();
        return result;

    }

    public void testResultIterator(Bitmap image, String language) {
        baseApi.init(TESSARACT_LANGUAGES_DATA_PATH, language);
        baseApi.setImage(image);
        ResultIterator resultIterator = baseApi.getResultIterator();
        int level = TessBaseAPI.PageIteratorLevel.RIL_WORD;

        if(resultIterator!=null){

            while(resultIterator.next(TessBaseAPI.PageIteratorLevel.RIL_WORD)){


            }
        }


    }
}
