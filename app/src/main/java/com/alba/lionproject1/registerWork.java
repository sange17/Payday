package com.alba.lionproject1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import static com.alba.lionproject1.registerWorkContract.registerWorkEntry.TABLE_NAME;

public class registerWork extends AppCompatActivity implements View.OnClickListener {
    final String TAG = getClass().getSimpleName();
    int id;
    String NEXTDATE;
    Cursor cursor;
    ImageView imageView_at;
    ImageView imageView_after;
    Button cameraBtn_at;
    Button cameraBtn_after;
    String mCurrentPhotoPath;
    String calendardate, month, year;
    public static Context context;
    static final int REQUEST_TAKE_PHOTO = 1;
    int dividePath = 0;
    String[] registerArray = new String[9];
    String[][] totalArray = new String[20][8];
    String date = "";
    String title = "";
    String from = "";
    String to = "";
    String memo = "";
    File path_at = null;
    File path_after = null;
    double timeSubtraction = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_work);
        context = this;





        //전역변수
        final Myapplication myapplication = (Myapplication) getApplication();


        for(int i=0;i<20;i++)
        {
            for(int j=0;j<8;j++) {
                totalArray[i][j] = "";
            }
        }
        for (int i = 0; i < 9; i++) {
            registerArray[i] = "";
        }

        Intent intent = getIntent();
        calendardate = Objects.requireNonNull(intent.getExtras()).getString("date");
        month = intent.getExtras().getString("month");
        year = intent.getExtras().getString("year");
        id = Objects.requireNonNull(intent.getExtras()).getInt("id");


        // 날씨, 이름, 시간, 메모 캐스팅
        final TextView txt_date = (TextView) findViewById(R.id.txt_date);
        final TextView albaname = (TextView) findViewById(R.id.albaName);
        final TextView mFromEditText = (TextView) findViewById(R.id.txt_rwFrom);
        final TextView mToEditText = (TextView) findViewById(R.id.txt_rwTo);
        final TextInputEditText mMemoEditText = findViewById(R.id.txt_memo);
        final TextView txt_rwDateFrom = (TextView) findViewById(R.id.txt_rwDateFrom);
        final Button btn_rwDateTo = (Button) findViewById(R.id.btn_rwDateTo);
        final TextView txt_rwDateTo = (TextView) findViewById(R.id.txt_rwDateTo);

        imageView_at = findViewById(R.id.iv_picture_at_work);
        imageView_after = findViewById(R.id.iv_picture_out_work);
        cameraBtn_at = findViewById(R.id.btn_takepicture);
        cameraBtn_after = findViewById(R.id.btn_takepicture_afterWork);

        txt_date.setText(calendardate);// 날짜 텍스트 창에 날짜 넣기
        txt_rwDateFrom.setText(calendardate);
        Button albabutton = ((MainActivity) MainActivity.context_main).findViewById(R.id.btn_ChooseParttime);
        albaname.setText(albabutton.getText());         // 알바 이름 텍스트 창에 메인에 있는 알바1 버튼 이름 가져와서 넣기

        //rwDate , rwFrom initialize
        int count = 0;
        Cursor totalDBCursor = getTotalCursor();
        for (int i = 0; i < totalDBCursor.getCount(); i++) // n^2
        {
            if (i > 0 && !(totalDBCursor.getString(1).equals(albabutton.getText().toString()))) {
                i = i - 1;
            }
            totalDBCursor.moveToPosition(count);
            if (count < totalDBCursor.getCount()) {
                count = count + 1;
            } else {
                break;
            }
            System.out.println(totalDBCursor.getString(1) + "  ,  " + albabutton.getText().toString());
            if (totalDBCursor.getString(1).equals(albabutton.getText().toString())) {
                for (int j = 0; j < 8; j++) {
                    totalArray[i][j] = totalDBCursor.getString(j);
                    System.out.println("total : " + totalArray[i][j]);
                }
            }
        }


        mFromEditText.setText(totalArray[0][4]);
        mToEditText.setText(totalArray[0][5]);


        //calendardate에서 하루 추가한 값 받기
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);

        try {
            Date toDate = format.parse(calendardate);
            assert toDate != null;
            long date = toDate.getTime() + 86400000;
            NEXTDATE = format.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        // DB에 저장된 데이터를 배열에 저장하는 코드
        count = 0;
        cursor = getregisterCursor();
        for (int i = 0; i < cursor.getCount(); i++) {
            // row
            cursor.moveToPosition(i);
            // db에서 원하는 정보 가져오기
            if (txt_date.getText().toString().equals(cursor.getString(1)) && albaname.getText().toString().equals(cursor.getString(2))) {
                for (int j = 0; j < 9; j++) {
                    // column
                    registerArray[j] = cursor.getString(j);
                }
                count += 1;
                break;
            }
        }

        for (int i = 0; i < 9; i++) {
            System.out.println(registerArray[i]);
        }


        // DB 데이터가 저장된 배열로 화면에 텍스트 표시
        if (count != 0) {
            assert registerArray != null;
            mFromEditText.setText(registerArray[3]);
            mToEditText.setText(registerArray[4]);
            mMemoEditText.setText(registerArray[5]);
            txt_rwDateTo.setText(registerArray[8]);
            if (!registerArray[6].equals("")) {
                try {
                    // 비트맵 이미지로 가져온다
                    String atimgPath = registerArray[6].substring(7);
                    Bitmap atImage = BitmapFactory.decodeFile(atimgPath);


                    // 이미지를 상황에 맞게 회전시킨다(at)
                    ExifInterface exif_at = new ExifInterface(atimgPath);
                    int exifOrientation_at = exif_at.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                    int exifDegree_at = exifOrientationToDegrees(exifOrientation_at);
                    atImage = rotate(atImage, exifDegree_at);

                    // 변환된 이미지 사용
                    imageView_at.setImageBitmap(atImage);
                } catch (IOException ex) {
                    Toast.makeText(this, "사진 변환에 오류가 발생하였습니다: " + ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }

            if (!registerArray[7].equals("")) {
                try {
                    // 비트맵 이미지로 가져온다(after)
                    String afterimgPath = registerArray[7].substring(7);
                    Bitmap afterImage = BitmapFactory.decodeFile(afterimgPath);
                    // 이미지를 상황에 맞게 회전시킨다(after)
                    ExifInterface exif_after = new ExifInterface(afterimgPath);
                    int exifOrientation_after = exif_after.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                    int exifDegree_after = exifOrientationToDegrees(exifOrientation_after);
                    afterImage = rotate(afterImage, exifDegree_after);
                    // 변환된 이미지 사용(after)
                    imageView_after.setImageBitmap(afterImage);
                } catch (IOException ex) {
                    Toast.makeText(this, "사진 변환에 오류가 발생하였습니다: " + ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }

        //몇시 부터 몇시까지 일을 했는지 설정할 수 있게 해주는 버튼 btn_rwFrom 캐스팅과 클릭시 이벤트
        Button btn_From = (Button) findViewById(R.id.btn_rwFrom);
        btn_From.setOnClickListener(view -> {
            Intent intent12 = new Intent(registerWork.this, Timepicker.class);
            intent12.putExtra("view", "rwfrom");
            startActivityForResult(intent12, 1);
        });

        //몇시 부터 몇시까지 일을 했는지 설정할 수 있게 해주는 버튼 btn_btn_rwTo 캐스팅과 클릭 시 이벤트
        Button btn_To = (Button) findViewById(R.id.btn_rwTo);
        btn_To.setOnClickListener(view -> {
            Intent intent1 = new Intent(registerWork.this, Timepicker.class);
            intent1.putExtra("view", "rwto");
            startActivity(intent1);
        });


        //저장 버튼 btn_rwSave 캐스팅과 클릭시 이벤트
        Button btn_Save = (Button) findViewById(R.id.btn_rwSave);
        btn_Save.setOnClickListener((View.OnClickListener) view -> {

            timeSubtraction = TimeSubtraction(mFromEditText.getText().toString(), mToEditText.getText().toString());

            if ((txt_rwDateFrom.getText().toString().equals(txt_rwDateTo.getText().toString()) && timeSubtraction > 0)
                    || !txt_rwDateFrom.getText().toString().equals(txt_rwDateTo.getText().toString()) && timeSubtraction < 0) {
                // 날짜 텍스트, 알바이름
                date = txt_date.getText().toString();
                title = albaname.getText().toString();
                // 근무시작시간과 끝시간 텍스트
                from = mFromEditText.getText().toString();
                to = mToEditText.getText().toString();
                // 메모창 텍스트
                memo = Objects.requireNonNull(mMemoEditText.getText()).toString();

                if (date.equals("") || title.equals("") || from.equals("") || to.equals("") || (path_at == null && registerArray[6].equals("")) && (path_after == null && registerArray[7].equals(""))) {
                    Toast.makeText(getApplicationContext(), "항목들을 다 채웠는지 확인해봐", Toast.LENGTH_LONG).show();
                } else {
                    System.out.println();
                    //이전 mainActivity 종료
                    Activity mainActivity = (MainActivity) MainActivity.main_activity;
                    mainActivity.finish();

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(registerWorkContract.registerWorkEntry.COLUMN_NAME_DATE, date);
                    contentValues.put(registerWorkContract.registerWorkEntry.COLUMN_NAME_TITLE, title);
                    contentValues.put(registerWorkContract.registerWorkEntry.COLUMN_NAME_FROM, from);
                    contentValues.put(registerWorkContract.registerWorkEntry.COLUMN_NAME_TO, to);
                    contentValues.put(registerWorkContract.registerWorkEntry.COLUMN_NAME_MEMO, memo);

                    try {
                        myapplication.setPhoto_atPath(Uri.fromFile(path_at).toString());
                    } catch (Exception ex) {
                        myapplication.setPhoto_atPath(registerArray[6]);
                    }
                    try {
                        myapplication.setPhoto_afterPath(Uri.fromFile(path_after).toString());
                    } catch (Exception ex) {
                        myapplication.setPhoto_afterPath(registerArray[7]);
                    }

                    contentValues.put(registerWorkContract.registerWorkEntry.COLUMN_NAME_ATPHOTOPATH, myapplication.getPhoto_atPath());
                    contentValues.put(registerWorkContract.registerWorkEntry.COLUMN_NAME_AFTERPHOTOPATH, myapplication.getPhoto_afterPath());
                    contentValues.put(registerWorkContract.registerWorkEntry.COLUMN_NAME_TODATE, txt_rwDateTo.getText().toString());
                    myapplication.setPhoto_afterPath("");
                    myapplication.setPhoto_atPath("");

                    SQLiteDatabase db = registerWorkDBHelper.getInstance(getApplicationContext()).getWritableDatabase();


                    if (registerArray[1].equals(date)) {
                        db.update(TABLE_NAME, contentValues, registerWorkContract.registerWorkEntry._ID + " = " + registerArray[0], null);
                    } else {
                        db.insert(TABLE_NAME, "null", contentValues);
                    }

                    SaveEvent(calendardate, month, year);
                    Intent intent13 = new Intent(registerWork.this, MainActivity.class);
                    db.close();
                    startActivity(intent13);
                    finish();
                }
            } else {
                Toast.makeText(getApplicationContext(), "근무시작 시간 그리고 근무종료 시간과 날짜를 확인해봐", Toast.LENGTH_LONG).show();
            }
        });

        //취소 버튼  btn_rwCancel 캐스팅과 클릭시 이벤트
        Button btn_Cancel = (Button) findViewById(R.id.btn_rwCancel);
        btn_Cancel.setOnClickListener(view -> finish());


        //--------------------------------카메라 찰칵버튼 관련----------------------------------
        // 카메라 버튼에 리스터 추가
        cameraBtn_at.setOnClickListener((View.OnClickListener) this);
        cameraBtn_after.setOnClickListener((View.OnClickListener) this);

        // 6.0 마쉬멜로우 이상일 경우에는 권한 체크 후 권한 요청
        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "권한 설정 완료");
        } else {
            Log.d(TAG, "권한 설정 요청");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }


        btn_rwDateTo.setOnClickListener(view -> {
            Intent intent14 = new Intent(registerWork.this, getDatePopUp.class);
            intent14.putExtra("view", "rwdateto");
            intent14.putExtra("nextdate", NEXTDATE);
            intent14.putExtra("currentdate", calendardate);
            startActivity(intent14);
        });
    }

    private double TimeSubtraction(String from, String to) {
        try {
            String[] organizedHoursFrom = from.split(" ");
            String[] organizedHoursTo = to.split(" ");

            if (organizedHoursFrom[0].equals("오후")) {
                organizedHoursFrom[1] = Integer.toString(Integer.parseInt(organizedHoursFrom[1]) + 12);
            }

            if (organizedHoursTo[0].equals("오후")) {
                organizedHoursTo[1] = Integer.toString(Integer.parseInt(organizedHoursTo[1]) + 12);
            }

            if (Integer.parseInt(organizedHoursFrom[1]) < 10) {
                organizedHoursFrom[1] = "0" + organizedHoursFrom[1];
            }

            if (Integer.parseInt(organizedHoursTo[1]) < 10) {
                organizedHoursTo[1] = "0" + organizedHoursTo[1];
            }

            from = organizedHoursFrom[1] + ":" + organizedHoursFrom[3];
            to = organizedHoursTo[1] + ":" + organizedHoursTo[3];


            SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.KOREA);

            Date fromDate = format.parse(from);
            Date toDate = format.parse(to);


            assert fromDate != null;
            assert toDate != null;


            return (double) (toDate.getTime() - fromDate.getTime());
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    // 권한 요청
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult");
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
        } else{
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);

                localBuilder.setTitle("권한 설정")
                        .setMessage("권한 거절로 인해 일부기능이 제한됩니다.")
                        .setPositiveButton("권한 설정하러 가기", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                                try {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                            .setData(Uri.parse("package:" + getPackageName()));
                                    startActivity(intent);
                                } catch (ActivityNotFoundException e) {
                                    e.printStackTrace();
                                    Intent intent = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                                    startActivity(intent);
                                }
                            }
                        })
                        .setNegativeButton("취소하기", (paramAnonymousDialogInterface, paramAnonymousInt) -> {
                        })
                        .create()
                        .show();
            }
        }
    }

    // 버튼 onClick리스너 처리 부분
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_takepicture) {
            dividePath = 1;
            dispatchTakePictureIntent();


        }
        if (view.getId() == R.id.btn_takepicture_afterWork) {
            dividePath = 2;
            dispatchTakePictureIntent();

        }
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
                if (dividePath == 1) {
                    path_at = photoFile;
                } else if (dividePath == 2) {
                    path_after = photoFile;
                }
            } catch (IOException ex) {
                // Error occurred while creating the File
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.alba.lionproject1.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    // 이미지 파일 만들어주는 코드
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.KOREA).format(new Date());
        String imageFileName = "photoDate_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    // 카메라로 촬영한 사진을 가져오는 부분
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        try {
            if (requestCode == REQUEST_TAKE_PHOTO) {
                if (resultCode == RESULT_OK) {
                    File file = new File(mCurrentPhotoPath);
                    Bitmap bitmap = MediaStore.Images.Media
                            .getBitmap(getContentResolver(), Uri.fromFile(file));
                    if (bitmap != null) {
                        ExifInterface ei = new ExifInterface(mCurrentPhotoPath);
                        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                ExifInterface.ORIENTATION_UNDEFINED);

                        Bitmap rotatedBitmap;
                        switch (orientation) {

                            case ExifInterface.ORIENTATION_ROTATE_90:
                                rotatedBitmap = rotateImage(bitmap, 90);
                                break;

                            case ExifInterface.ORIENTATION_ROTATE_180:
                                rotatedBitmap = rotateImage(bitmap, 180);
                                break;

                            case ExifInterface.ORIENTATION_ROTATE_270:
                                rotatedBitmap = rotateImage(bitmap, 270);
                                break;

                            case ExifInterface.ORIENTATION_NORMAL:
                            default:
                                rotatedBitmap = bitmap;
                        }

                        // -------------------이미지 뷰에 수정된 사진을 띄우는 코드-----------------
                        if (dividePath == 1) {
                            imageView_at.setImageBitmap(rotatedBitmap);
                        } else if (dividePath == 2) {
                            imageView_after.setImageBitmap(rotatedBitmap);
                        }
                    }
                }
            }
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    private static void SaveEvent(String calendardate, String month, String year) {
        DBOpenHelper dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        dbOpenHelper.SaveEvent("event", "time", calendardate, month, year, database);
        dbOpenHelper.close();
    }

    // DB 데이터 읽어오기?
    private Cursor getregisterCursor() {
        registerWorkDBHelper db = registerWorkDBHelper.getInstance(this);
        return db.getReadableDatabase().query(
                registerWorkContract.registerWorkEntry.TABLE_NAME, null, null, null, null, null, registerWorkContract.registerWorkEntry._ID);
    }


    public Bitmap rotate(Bitmap bitmap, int degrees) {
        if (degrees != 0 && bitmap != null) {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) bitmap.getWidth() / 2,
                    (float) bitmap.getHeight() / 2);

            try {
                Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0,
                        bitmap.getWidth(), bitmap.getHeight(), m, true);
                if (bitmap != converted) {
                    bitmap.recycle();
                    bitmap = converted;
                }
            } catch (OutOfMemoryError ex) {
                // 메모리가 부족하여 회전을 시키지 못할 경우 그냥 원본을 반환합니다.
            }
        }
        return bitmap;
    }

    public int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }
    private Cursor getTotalCursor() {
        totalDB db = totalDB.getInstance(this);
        return db.getReadableDatabase().query(
                DataBases.CreatDB._TABLENAME0, null, null, null, null, null, DataBases.CreatDB._ID);

    }

}