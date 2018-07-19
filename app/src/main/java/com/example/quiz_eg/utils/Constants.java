package com.example.quiz_eg.utils;

import android.Manifest;
import android.os.Environment;

public final class Constants {

	public static final int STORAGE_PERMISSION_CODE = 1;

	public static final String
			EXTERNAL_STORAGE_DIRECTORY = Environment.getExternalStorageDirectory().getPath(),
			QUIZ_EXTRA = "QUIZ";

	public static final String[]
			QUIZ_PATHS = {"", "Android/data", "Documents/data"},
			QUIZ_FILE_EXTENSIONS = {"xml"},
			PERMISSIONS_REQUIRED = {Manifest.permission.READ_EXTERNAL_STORAGE};

	private Constants() {
	}
}
