package com.alba.lionproject1;

import android.database.Cursor;
import android.os.Bundle;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


/* 증거물 파일 생성 버튼 이벤트 코드입니다.
 사용안내와 예를 누르면 엑셀 파일이 생성되고 아니요를 누르면 창을 닫습니다.*/
public class AlertDialog_addxls extends AppCompatActivity {
    Button AddXlsYes;
    Button AddXlsNo;
    Cursor cursor;
    String[][] registerArray = new String[100][9];
    Button albabutton = ((MainActivity) MainActivity.context_main).findViewById(R.id.btn_ChooseParttime);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alertdialog_addxls);


        TextView xlsExplain = findViewById(R.id.txt_XlsExplain);
        String text = "엑셀 파일 경로는 메인화면의 파일경로 버튼을 눌러서 확인할 수 있어\n\n" +
                "※ 파일은 부당한 일이 생겼을 때 필요할 거야";
        xlsExplain.setText(text);

        // DB 데이터들을 배열에 저장하고 그 데이터들로 엑셀 파일을 만드는 버튼
        AddXlsYes = findViewById(R.id.btn_XlsYes);
        AddXlsYes.setOnClickListener(view -> {
            // DB 데이터를 배열에 저장하는 코드
            cursor = getregisterCursor();
            // row
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                // column
                for (int j = 0; j < 9; j++) {
                    registerArray[i][j] = cursor.getString(j);
                }
            }

            // 배열에 저장된 내용을 Run창에 보여주는 코드(개발자 확인용)
            // row
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                // column
                System.out.println(registerArray[i][0] + " " + registerArray[i][1] + " " +
                        registerArray[i][2] + "\t" + registerArray[i][3] + "\t" +
                        registerArray[i][4] + "\t" + registerArray[i][5] + "\t" +
                        registerArray[i][6] + "\t" + registerArray[i][7] + "\t" +
                        registerArray[i][8]);
            }

            // 엑셀 파일에 내용을 입력해서 엑셀 파일 만들기
            Workbook wb = new HSSFWorkbook();
            Cell cell;

            Sheet sheet;
            sheet = wb.createSheet("Name of sheet");

            CellStyle cs = wb.createCellStyle();
            cs.setWrapText(true);

            Row row = sheet.createRow(0);

            cell = row.createCell(0);
            cell.setCellStyle(cs);
            cs.setAlignment(HorizontalAlignment.CENTER);
            cell.setCellValue("DATE_FROM");

            cell = row.createCell(1);
            cell.setCellStyle(cs);
            cs.setAlignment(HorizontalAlignment.CENTER);
            cell.setCellValue("TITLE");

            cell = row.createCell(2);
            cell.setCellStyle(cs);
            cs.setAlignment(HorizontalAlignment.CENTER);
            cell.setCellValue("FROM");

            cell = row.createCell(3);
            cell.setCellStyle(cs);
            cs.setAlignment(HorizontalAlignment.CENTER);
            cell.setCellValue("TO");

            cell = row.createCell(4);
            cell.setCellStyle(cs);
            cs.setAlignment(HorizontalAlignment.CENTER);
            cell.setCellValue("MEMO");

            cell = row.createCell(5);
            cell.setCellStyle(cs);
            cs.setAlignment(HorizontalAlignment.CENTER);
            cell.setCellValue("IMG_AT");

            cell = row.createCell(6);
            cell.setCellStyle(cs);
            cs.setAlignment(HorizontalAlignment.CENTER);
            cell.setCellValue("IMG_AFTER");

            cell = row.createCell(7);
            cell.setCellStyle(cs);
            cs.setAlignment(HorizontalAlignment.CENTER);
            cell.setCellValue("DATE_TO");


            for (int i = 0; i < cursor.getCount(); i++) { // 데이터 엑셀에 입력
                row = sheet.createRow(i + 1);
                for (int j = 1; j < 9; j++) {
                    cell = row.createCell(j - 1);
                    if (String.valueOf(registerArray[i][2]).equals(albabutton.getText().toString())) {
                        cell.setCellValue(String.valueOf(registerArray[i][j]));
                        System.out.println("registerArray = " + registerArray[i][j]);
                    }
                    cell.setCellStyle(cs);
                    cs.setAlignment(HorizontalAlignment.CENTER);
                    cs.setVerticalAlignment(VerticalAlignment.CENTER);
                    sheet.setColumnWidth(j - 1, (5000));
                }
            }


            File file = new File(getExternalFilesDir(null), "plik.xls");
            FileOutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(file);
                wb.write(outputStream);
                Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "so ok", Toast.LENGTH_LONG).show();
                try {
                    assert outputStream != null;
                    outputStream.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // 아니요 버튼(나가기)
        AddXlsNo = findViewById(R.id.btn_XlsNo);
        AddXlsNo.setOnClickListener(view -> finish());
    }


    // DB 데이터 읽어오기
    private Cursor getregisterCursor() {
        registerWorkDBHelper db = registerWorkDBHelper.getInstance(this);
        return db.getReadableDatabase().query(
                registerWorkContract.registerWorkEntry.TABLE_NAME, null, null, null, null, null, registerWorkContract.registerWorkEntry._ID);
    }
}