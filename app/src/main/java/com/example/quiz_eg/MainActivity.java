package com.example.quiz_eg;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.quiz_eg.utils.Constants;
import com.example.quiz_eg.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class MainActivity extends AppCompatActivity {

	List<File> quizFiles = new ArrayList<>();
	ArrayAdapter<String> question_ArrayAdapter;

	@BindView(R.id.toolbar)
	Toolbar toolbar;
	@BindView(R.id.quiz_ListView)
	ListView question_ListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
			requestStoragePermission();
		} else
			setUpMainMenu();
	}

	@OnItemClick(R.id.quiz_ListView)
	public void question_listView_onItemClick(int pos) {
		String quizName = (String) question_ListView.getItemAtPosition(pos);

		for (File file : quizFiles)
			if (file.getName().equals(quizName)) {
				Intent intent = new Intent(this, QuizActivity.class);
				intent.putExtra(Constants.QUIZ_EXTRA, file);
				startActivity(intent);
			}
	}

	private void setUpMainMenu() {
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);

		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayShowHomeEnabled(true);

		getQuizFiles();
		if (quizFiles.size() == 0)
			noQuestionFileFound();

		question_ArrayAdapter = new ArrayAdapter<>(
				this.getApplicationContext(),
				R.layout.quiz_item,
				R.id.quiz_item,
				getQuizPaths()
		);

		question_ListView.setAdapter(question_ArrayAdapter);
	}

	private void getQuizFiles() {
		final String
				env = Constants.EXTERNAL_STORAGE_DIRECTORY,
				app_name = getString(R.string.app_name);

		for (String path : Constants.QUIZ_PATHS) {
			File file = new File(String.format("%s/%s/%s", env, path, app_name));
			quizFiles.addAll(FileUtils.get(file, Constants.QUIZ_FILE_EXTENSIONS));
		}
	}

	private String[] getQuizPaths() {
		String[] result = new String[quizFiles.size()];
		for (int i = 0; i < result.length; ++i) {
			result[i] = quizFiles.get(i).getName();
		}
		return result;
	}

	private void restart() {
		if (Build.VERSION.SDK_INT >= 11)
			recreate();
		else {
			Intent intent = getIntent();
			intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			finish();
			overridePendingTransition(0, 0);

			startActivity(intent);
			overridePendingTransition(0, 0);
		}
	}

	private void noQuestionFileFound() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder
				.setMessage(R.string.noQuizFileFoundMsg)
				.setCancelable(false)
				.setPositiveButton(R.string.exitLabel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// add items to the action bar
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// action bar clicks are handled here
		int id = item.getItemId();
		return id == R.id.action_settings || super.onOptionsItemSelected(item);
	}

	private void requestStoragePermission() {
		if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
			new AlertDialog.Builder(this)
					.setTitle(R.string.permissionReqTitle)
					.setMessage(R.string.permissionReqMsg)
					.setPositiveButton(R.string.okLabel, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							ActivityCompat.requestPermissions(
									MainActivity.this,
									Constants.PERMISSIONS_REQUIRED,
									Constants.STORAGE_PERMISSION_CODE
							);
							MainActivity.this.restart();
						}
					})
					.setNegativeButton(R.string.cancelLabel, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							finish();
						}
					})
					.create().show();
		} else
			ActivityCompat.requestPermissions(
					this,
					Constants.PERMISSIONS_REQUIRED,
					Constants.STORAGE_PERMISSION_CODE
			);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		//super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == Constants.STORAGE_PERMISSION_CODE) {
			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				Toast.makeText(this, getString(R.string.permissionGranted), Toast.LENGTH_SHORT).show();
				restart();
			} else
				requestStoragePermission();
		}
	}
}
